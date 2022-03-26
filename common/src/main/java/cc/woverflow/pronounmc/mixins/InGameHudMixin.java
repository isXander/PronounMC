package cc.woverflow.pronounmc.mixins;

import cc.woverflow.pronounmc.PronounMC;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "addChatMessage", at = @At("HEAD"), cancellable = true)
    private void onAddChatMessage(MessageType type, Text text, UUID sender, CallbackInfo ci) {
        if (type == null) return;

        if (type == MessageType.CHAT) {
            if (PronounMC.getMessageManager().isMessageSentByPlayer(sender)) {
                PronounMC.getMessageManager().queuePronounMessage(text, sender);
                ci.cancel();
            }
        }
    }

    @ModifyVariable(method = "addChatMessage", at = @At("HEAD"), argsOnly = true)
    private MessageType modifyMessageType(MessageType type) {
        return type == null ? MessageType.CHAT : type;
    }
}
