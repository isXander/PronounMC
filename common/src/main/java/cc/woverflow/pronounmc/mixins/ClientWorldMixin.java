package cc.woverflow.pronounmc.mixins;

import cc.woverflow.pronounmc.PronounMC;
import cc.woverflow.pronounmc.utils.Multithreading;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "addEntityPrivate", at = @At("RETURN"))
    private void onAddEntity(int id, Entity entity, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {
            Multithreading.runAsync(() -> {
                PronounMC.getPronounManager().getOrFindPronouns(player.getGameProfile().getId());
            });
        }
    }
}
