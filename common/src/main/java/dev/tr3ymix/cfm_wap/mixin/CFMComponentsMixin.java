package dev.tr3ymix.cfm_wap.mixin;

import com.mrcrayfish.furniture.refurbished.Components;
import dev.tr3ymix.cfm_wap.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Components.class)
public class CFMComponentsMixin {

    @Final @Shadow @Mutable public static Component GUI_CONNECT_TO_POWER;

    @Unique
    private static final Component GUI_CIRCUIT_BREAKER = Utils.translation("gui", "circuit_breaker");

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replaceComponents(CallbackInfo ci){
        GUI_CONNECT_TO_POWER = Utils.translation("gui", "connect_to_power",
                Components.GUI_ELECTRICITY_GENERATOR.plainCopy().withStyle(ChatFormatting.YELLOW),
                GUI_CIRCUIT_BREAKER.plainCopy().withStyle(ChatFormatting.YELLOW),
                com.mrcrayfish.furniture.refurbished.util.Utils.translation("item", "wrench").plainCopy().withStyle(ChatFormatting.YELLOW)
                );
    }
}
