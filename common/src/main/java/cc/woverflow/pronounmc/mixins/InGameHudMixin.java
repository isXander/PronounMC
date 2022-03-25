package cc.woverflow.pronounmc.mixins;

import cc.woverflow.pronounmc.PronounMC;
import cc.woverflow.pronounmc.utils.Pronouns;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.UUID;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @ModifyVariable(method = "addChatMessage", at = @At("HEAD"), argsOnly = true)
    private Text modifyChatMessage(Text text, MessageType type, Text dontUseThis, UUID sender) {
        if (type != MessageType.CHAT) return text;

        Pronouns pronouns = PronounMC.getPronounManager().getOrFindPronouns(sender);
        if (pronouns == null || pronouns == Pronouns.UNSPECIFIED) return text;

        return new LiteralText("").append(pronouns.getText().formatted(Formatting.DARK_GRAY)).append(" ").append(text);
    }
}
