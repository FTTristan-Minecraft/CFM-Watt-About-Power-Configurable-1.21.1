package dev.tr3ymix.cfm_wap.mixin;

import dev.tr3ymix.cfm_wap.Config;
import dev.tr3ymix.cfm_wap.blockentity.CircuitBreakerBlockEntity;
import dev.tr3ymix.cfm_wap.blockentity.CircuitBreakerCache;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onConstruct(CallbackInfo ci){

        LightningBolt bolt = (LightningBolt)(Object)this;
        Level level = bolt.level();

        if(!level.isClientSide() && level instanceof ServerLevel serverLevel){

            BlockPos pos = bolt.blockPosition();

            int radius = Config.SERVER.electricity.lightningStrikeOutageRadius.get();

            for(BlockPos blockPos : CircuitBreakerCache.get(serverLevel)){
                if(blockPos.distSqr(pos) <= radius * radius){
                    BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);

                    if(blockEntity instanceof CircuitBreakerBlockEntity breaker){
                        breaker.setEnabled(false);
                        breaker.setChanged();
                    }
                }
            }
        }
    }
}
