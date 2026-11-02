package dev.tr3ymix.cfm_wap.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class Utils {
    public Utils(){

    }

    public static ResourceLocation resource(String name){
        return ResourceLocation.fromNamespaceAndPath("cfm_wap", name);
    }

    public static MutableComponent translation(String category, String path, Object... args){
        return Component.translatable(String.format("%s.%s.%s", category, "cfm_wap", path), args);
    }
}
