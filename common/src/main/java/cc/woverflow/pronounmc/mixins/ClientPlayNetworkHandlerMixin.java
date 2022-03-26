package cc.woverflow.pronounmc.mixins;

import cc.woverflow.pronounmc.PronounMC;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onPlayerList", at = @At("TAIL"))
    private void onPlayerList(PlayerListS2CPacket packet, CallbackInfo ci) {
        PronounMC.getPronounManager().cachePronounsForServer();
    }
}
