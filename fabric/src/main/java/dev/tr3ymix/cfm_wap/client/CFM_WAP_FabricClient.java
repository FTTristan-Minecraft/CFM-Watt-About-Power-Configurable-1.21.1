package dev.tr3ymix.cfm_wap.client;

import com.mrcrayfish.furniture.refurbished.client.registration.ScreenRegister;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.function.TriFunction;

public final class CFM_WAP_FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        CFM_WAP_Client.registerScreens(new ScreenRegister() {
            @Override
            public <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void apply(MenuType<T> type, TriFunction<T, Inventory, Component, U> factory) {
                MenuScreens.register(type, factory::apply);
            }
        });

        CFM_WAP_Client.registerBlockEntityRenderers(BlockEntityRenderers::register);

    }
}
