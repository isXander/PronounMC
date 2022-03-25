package cc.woverflow.pronounmc.forge;

import cc.woverflow.pronounmc.PronounMC;
import net.minecraftforge.fml.common.Mod;

@Mod("pronounmc")
public class ForgeEntrypoint {
    public ForgeEntrypoint() {
        PronounMC.onInitialize();
    }
}
