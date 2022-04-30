package cc.woverflow.pronounmc.mixins;

import cc.woverflow.pronounmc.PronounMC;
import cc.woverflow.pronounmc.utils.Pronouns;
import net.minecraft.entity.EntityType;
import net.minecraft.text.HoverEvent.EntityContent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityContent.class)
public class EntityContentMixin {
    @Inject(method = "asTooltip", at = @At("HEAD"))
    private void modifyTooltip(CallbackInfoReturnable<List<Text>> cir) {
        if (!((EntityContent)(Object)this).entityType.equals(EntityType.PLAYER)) {
            return;
        }
        if (PronounMC.getConfig().showInTooltip) {
            Pronouns pronouns = PronounMC.getPronounManager().getOrFindPronouns(((EntityContent)(Object)this).uuid);
            if (pronouns == null || pronouns == Pronouns.UNSPECIFIED) {
                return;
            }

            ((EntityContentAccessor) ((EntityContent)(Object)this)).setName(new LiteralText("").append(((EntityContent)(Object)this).name).append(" (").append(pronouns.getText()).append(")"));
        }
    }
}
