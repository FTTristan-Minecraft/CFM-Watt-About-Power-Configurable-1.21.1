package dev.tr3ymix.cfm_wap.registry;

import com.mrcrayfish.furniture.refurbished.block.MetalType;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.tr3ymix.cfm_wap.CFM_WAP;
import dev.tr3ymix.cfm_wap.block.CircuitBreakerBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(CFM_WAP.MOD_ID, Registries.BLOCK);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(CFM_WAP.MOD_ID, Registries.ITEM);

    public static RegistrySupplier<Block> LIGHT_CIRCUIT_BREAKER =
            register("light_circuit_breaker", () -> new CircuitBreakerBlock(MetalType.LIGHT, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(3.0F).sound(SoundType.LANTERN)
                    .requiresCorrectToolForDrops()));

    public static RegistrySupplier<Block> DARK_CIRCUIT_BREAKER =
            register("dark_circuit_breaker", () -> new CircuitBreakerBlock(MetalType.DARK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(3.0F).sound(SoundType.LANTERN)
                    .requiresCorrectToolForDrops()));

    private static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block) {
        RegistrySupplier<T> registeredBlock = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(registeredBlock.get(), new Item.Properties())); //register item
        return registeredBlock;
    }

    public static void register() {
        BLOCKS.register();
        ITEMS.register();
    }
}
