package cc.woverflow.pronounmc.mixins;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HoverEvent.EntityContent.class)
public interface EntityContentAccessor {
    @Accessor("name") @Mutable
    public void setName(Text name);
}
