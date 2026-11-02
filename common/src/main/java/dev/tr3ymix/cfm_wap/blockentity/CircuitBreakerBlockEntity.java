package dev.tr3ymix.cfm_wap.blockentity;

import com.mrcrayfish.furniture.refurbished.blockentity.ElectricitySourceLootBlockEntity;
import com.mrcrayfish.furniture.refurbished.blockentity.IPowerSwitch;
import com.mrcrayfish.furniture.refurbished.core.ModSounds;
import com.mrcrayfish.furniture.refurbished.electricity.IElectricityNode;
import com.mrcrayfish.furniture.refurbished.electricity.NodeSearchResult;
import com.mrcrayfish.furniture.refurbished.inventory.BuildableContainerData;
import dev.tr3ymix.cfm_wap.Config;
import dev.tr3ymix.cfm_wap.block.CircuitBreakerBlock;
import dev.tr3ymix.cfm_wap.energy.CommonEnergyStorage;
import dev.tr3ymix.cfm_wap.energy.FractionalEnergyDraw;
import dev.tr3ymix.cfm_wap.inventory.CircuitBreakerMenu;
import dev.tr3ymix.cfm_wap.registry.ModBlockEntities;
import dev.tr3ymix.cfm_wap.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CircuitBreakerBlockEntity extends ElectricitySourceLootBlockEntity implements
        IProcessingCircuitBreakerBlock, IPowerSwitch {

    protected final Vec3 centerPosition;
    protected boolean enabled;
    protected int nodeCount;

    protected final ContainerData data = new BuildableContainerData(builder -> {
        builder.add(0, this::getEnergy, (value)->{});
        builder.add(1, this::getMaxEnergy, (value)->{});
        builder.add(2, () -> this.enabled ? 1 : 0, (value)-> {});
        builder.add(3, () -> this.overloaded ? 1 : 0, (value)-> {});
        builder.add(4, () -> this.isNodePowered() ? 1 : 0, (value)-> {});
        builder.add(5, () -> this.nodeCount, (value)-> {});
    });;

    public final CommonEnergyStorage ENERGY_STORAGE = new CommonEnergyStorage(1000, 50);

    int ENERGY_RATE = 10;

    private final FractionalEnergyDraw energyDraw = new FractionalEnergyDraw();

    // The last network search, reused until it gets too old (see searchNodeNetwork).
    private NodeSearchResult cachedSearch;
    private boolean cachedSearchCancelledAtLimit;
    private long cachedSearchTime;

    public CircuitBreakerBlockEntity(BlockPos pos, BlockState state){
        this(ModBlockEntities.CIRCUIT_BREAKER.get(), pos, state);
    }

    public CircuitBreakerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 1);
        this.centerPosition = pos.getCenter();
    }
    @Override
    public int getNodeMaximumConnections() {
        return Config.SERVER.electricity.maximumLinksPerCircuitBreaker.get();
    }

    @Override
    public int getMaxPowerableNodes() {
        return Config.SERVER.electricity.maximumNodesInCircuitBreakerNetwork.get();
    }


    @Override
    protected @NotNull Component getDefaultName() {
        return Utils.translation("container", "circuit_breaker");
    }

    //create menu
    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory) {
        if(!this.enabled){
            this.scanNodeNetwork(false);
        }
        return new CircuitBreakerMenu(i, inventory, this, this.data);
    }

    @Override
    public boolean isMatchingContainerMenu(AbstractContainerMenu menu) {
        if(menu instanceof CircuitBreakerMenu circuitBreaker){
            return circuitBreaker.getContainer() == this;
        }
        return false;
    }

    @Override
    public boolean isNodePowered() {
        BlockState state = this.getBlockState();
        return state.hasProperty(CircuitBreakerBlock.POWERED) ? state.getValue(CircuitBreakerBlock.POWERED) : false;
    }

    @Override
    public void setNodePowered(boolean powered) {
        BlockState state = this.getBlockState();
        if(state.hasProperty(CircuitBreakerBlock.POWERED)) {
            assert this.level != null;
            this.level.setBlock(this.worldPosition, state.setValue(CircuitBreakerBlock.POWERED, powered), 3);
        }
    }

    @Override
    public void togglePower() {
        this.enabled = !this.enabled;
        if(this.enabled) {
            NodeSearchResult result = this.scanNodeNetwork(false);
            if(!result.overloaded()){
                if(this.overloaded) {
                    this.overloaded = false;
                }
            }else{
                this.enabled = false;
            }
        }
        this.setChanged();
    }

    @Override
    public void onNodeOverloaded() {
        this.enabled = false;
        this.setChanged();
    }

    /**
     * Called every tick while the breaker is online. Searching a network of thousands of nodes is expensive, so the
     * previous result is reused for a few ticks. Nodes which have been removed since force a new search right away.
     */
    @Override
    public NodeSearchResult searchNodeNetwork(boolean cancelAtLimit) {
        Level level = this.level;
        NodeSearchResult cached = this.cachedSearch;
        if(level != null && cached != null && this.cachedSearchCancelledAtLimit == cancelAtLimit) {
            long age = level.getGameTime() - this.cachedSearchTime;
            if(age >= 0 && age < Config.SERVER.electricity.nodeSearchIntervalTicks.get() && areAllValid(cached.nodes())) {
                return cached;
            }
        }
        return this.scanNodeNetwork(cancelAtLimit);
    }

    private static boolean areAllValid(List<IElectricityNode> nodes) {
        for(int i = 0; i < nodes.size(); i++) {
            if(!nodes.get(i).isNodeValid()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Always searches the network, used when an up to date result matters (toggling, opening the screen).
     */
    private NodeSearchResult scanNodeNetwork(boolean cancelAtLimit) {
        int maximum = this.getMaxPowerableNodes();
        List<IElectricityNode> nodes =
                IElectricityNode.searchNodes(this, maximum,
                        cancelAtLimit, (node) -> !node.isSourceNode() && node.canPowerTraverseNode(),
                        (node) -> !node.isSourceNode());
        boolean overloaded = nodes.size() > maximum;
        NodeSearchResult result = new NodeSearchResult(overloaded, nodes);
        this.nodeCount = nodes.size();
        this.cachedSearch = result;
        this.cachedSearchCancelledAtLimit = cancelAtLimit;
        this.cachedSearchTime = this.level != null ? this.level.getGameTime() : 0L;
        return result;
    }

    @Override
    public int getEnergy() {
        return ENERGY_STORAGE.getEnergy();
    }

    @Override
    public int getMaxEnergy() {
        return ENERGY_STORAGE.getCapacity();
    }

    @Override
    public int takeEnergyDraw() {
        return this.energyDraw.next(Config.SERVER.electricity.energyDrawPerTick.get());
    }

    @Override
    public void addEnergy(int amount) {
        this.ENERGY_STORAGE.setEnergy(amount);
        this.setChanged();
    }

    @Override
    public void removeEnergy(int amount) {
        this.ENERGY_STORAGE.extractEnergy(amount, false);
    }

    @Override
    public boolean requiresEnergy() {
        return true;
    }

    @Override
    public int retrieveEnergy(boolean simulate) {
        if(!simulate){
            return this.ENERGY_STORAGE.insertEnergy(ENERGY_RATE, false);
        }
        return 0;
    }

    @Override
    public int updateAndGetTotalProcessingTime() {
        return this.getTotalProcessingTime();
    }

    @Override
    public int getTotalProcessingTime() {
        return 1;
    }

    @Override
    public int getProcessingTime() {
        return 0;
    }

    @Override
    public void setProcessingTime(int time) {
        if(this.isNodePowered()) {
            if(time == 0){
                this.setNodePowered(false);
            }
        }else if (time == 1){
            this.setNodePowered(true);
        }
    }

    @Override
    public void onCompleteProcess() {}

    @Override
    public boolean canProcess() {
        return this.enabled && !this.isNodeOverloaded();
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        if(!level.isClientSide){
            CircuitBreakerCache.add(level, this.getBlockPos());
        }

    }

    @Override
    public void setRemoved() {
        assert this.level != null;
        if(!this.level.isClientSide){
            CircuitBreakerCache.remove(this.level, this.getBlockPos());
        }
        super.setRemoved();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if(tag.contains("Enabled", 1)) {
            this.enabled = tag.getBoolean("Enabled");
        }
        if(tag.contains("Energy", 3)) {
            this.ENERGY_STORAGE.setEnergy(tag.getInt("Energy"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putBoolean("Enabled", this.enabled);
        tag.putInt("Energy", this.ENERGY_STORAGE.getEnergy());
    }

    @Override
    public void earlyNodeTick(Level level) {
        if(!level.isClientSide){
            this.processTick();
        }
        super.earlyNodeTick(level);
    }

    @Override
    public void onOpen(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, centerPosition.x, centerPosition.y, centerPosition.z, ModSounds.BLOCK_STOVE_OPEN.get(),
                SoundSource.BLOCKS, 1.0F, 0.9F + 0.1F * level.random.nextFloat());
        setDoorState(state, true);
    }

    @Override
    public void onClose(Level level, BlockPos pos, BlockState state) {

        level.playSound(null, centerPosition.x, centerPosition.y, centerPosition.z, ModSounds.BLOCK_MICROWAVE_CLOSE.get(),
                SoundSource.BLOCKS, 1.0F, 0.9F + 0.1F * level.random.nextFloat());
        setDoorState(state, false);
    }

    private void setDoorState(BlockState state, boolean open){
        Level level = this.getLevel();
        if(level != null){
             level.setBlock(this.getBlockPos(), state.setValue(CircuitBreakerBlock.OPEN, open), 3);
         }
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
