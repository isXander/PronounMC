package cc.woverflow.pronounmc.mixins;

import cc.woverflow.pronounmc.PronounMC;
import cc.woverflow.pronounmc.utils.Multithreading;
import cc.woverflow.pronounmc.utils.Pronouns;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.UUID;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @ModifyVariable(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaky()Z"), argsOnly = true)
    private Text modifyNametag(Text text, T entity) {
        if (PronounMC.getConfig().showOnNametag && entity instanceof PlayerEntity player) {
            UUID uuid = player.getGameProfile().getId();
            Pronouns pronouns = PronounMC.getPronounManager().getCachedPronouns(uuid);

            if (pronouns == null) {
                if (!PronounMC.getPronounManager().isCurrentlyFetching(uuid)) {
                    PronounMC.getLogger().warn("Uncached pronouns for player: " + player.getName().getString());
                    Multithreading.runAsync(() -> PronounMC.getPronounManager().getOrFindPronouns(uuid));
                }
                return text;
            }

            if (pronouns == Pronouns.UNSPECIFIED)
                return text;

            return new LiteralText("").append(text).append(" ").append(pronouns.getText().formatted(Formatting.DARK_GRAY));
        }
        return text;
    }
}
