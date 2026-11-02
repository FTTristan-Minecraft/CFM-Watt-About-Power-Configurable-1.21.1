package dev.tr3ymix.cfm_wap.inventory;

import com.mrcrayfish.furniture.refurbished.blockentity.IPowerSwitch;
import com.mrcrayfish.furniture.refurbished.inventory.IPowerSwitchMenu;
import com.mrcrayfish.furniture.refurbished.inventory.SimpleContainerMenu;
import dev.tr3ymix.cfm_wap.registry.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CircuitBreakerMenu extends SimpleContainerMenu implements IPowerSwitchMenu {
    private final ContainerData data;

    public CircuitBreakerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(0), new SimpleContainerData(6));
    }

    public CircuitBreakerMenu(int id, Inventory inventory, Container container, ContainerData data) {
        super(ModMenuTypes.CIRCUIT_BREAKER.get(), id, container);

        checkContainerSize(container, 0);
        checkContainerDataCount(data, 6);
        container.startOpen(inventory.player);

        this.data = data;
        this.addDataSlots(data);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        //noinspection DataFlowIssue
        return null;
    }

    public int getEnergy() {
        return this.data.get(0);
    }

    public int getMaxEnergy() { return this.data.get(1);}

    @Override
    public boolean isEnabled() { return this.data.get(2) != 0;}

    public boolean isOverloaded() { return this.data.get(3) != 0;}

    public boolean isPowered() { return this.data.get(4) != 0;}

    public int getNodeCount() { return this.data.get(5);}


    @Override
    public void toggle() {
        if(this.container instanceof IPowerSwitch powerSwitch){
            powerSwitch.togglePower();
        }
    }
}
