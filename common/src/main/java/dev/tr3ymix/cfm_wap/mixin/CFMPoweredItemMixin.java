package dev.tr3ymix.cfm_wap.mixin;

import com.mrcrayfish.furniture.refurbished.Components;
import com.mrcrayfish.furniture.refurbished.client.FontIcons;
import com.mrcrayfish.furniture.refurbished.item.PoweredItem;
import dev.tr3ymix.cfm_wap.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoweredItem.class)
public class CFMPoweredItemMixin {

    @Shadow @Final @Mutable public static Component POWER_TOOLTIP;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replacePowerTooltip(CallbackInfo ci){
        POWER_TOOLTIP = Component.empty()
                .append(Components.getIcon(FontIcons.INFO))
                .append(CommonComponents.SPACE)
                .append(Utils.translation("gui", "requires_power",
                            Components.GUI_ELECTRICITY_GENERATOR.plainCopy().withStyle(ChatFormatting.YELLOW),
                            Utils.translation("gui", "circuit_breaker")
                                    .plainCopy().withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY)
                        );
    }
}
