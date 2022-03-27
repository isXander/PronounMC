package cc.woverflow.pronounmc.mixins;

import cc.woverflow.pronounmc.PronounMC;
import cc.woverflow.pronounmc.config.PronounConfig;
import cc.woverflow.pronounmc.utils.Pronouns;
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
        PronounConfig config = PronounMC.getConfig();
        if (type == null || !config.showInChat) return;

        if (type == MessageType.CHAT) {
            if (PronounMC.getMessageManager().isMessageSentByPlayer(sender)) {
                Pronouns cachedPronouns = PronounMC.getPronounManager().getCachedPronouns(sender);
                if (cachedPronouns != null) {
                    // avoid delaying message to next tick if already cached
                    ((InGameHud) (Object) this).addChatMessage(null, PronounMC.getMessageManager().getPronounMessage(text, cachedPronouns), sender);
                } else {
                    // otherwise, queue the message to be sent once calculated. (this should never happen because it's calculated on player join)
                    PronounMC.getLogger().warn("Pronouns not cached for player " + sender.toString() + "! Queuing message to be sent after fetching.");
                    PronounMC.getMessageManager().queuePronounMessage(text, sender);
                }
                // cancel because we are resending the message
                ci.cancel();
            } else {
                System.out.println("Message not sent by player!");
            }
        }
    }

    /**
     * mod sends modified messages as MessageType null, so it
     * doesn't recalculate the pronouns getting into a recursive loop
     */
    @ModifyVariable(method = "addChatMessage", at = @At("HEAD"), argsOnly = true)
    private MessageType modifyMessageType(MessageType type) {
        return type == null ? MessageType.CHAT : type;
    }
}
