package cc.woverflow.pronounmc;

import cc.woverflow.pronounmc.utils.PronounManager;

public class PronounMC {
    private static PronounManager pronounManager;

    public static void onInitialize() {
        pronounManager = new PronounManager();
    }

    public static PronounManager getPronounManager() {
        return pronounManager;
    }
}
