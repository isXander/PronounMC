package cc.woverflow.pronounmc;

import cc.woverflow.pronounmc.utils.MessageManager;
import cc.woverflow.pronounmc.utils.PronounManager;

public class PronounMC {
    private static PronounManager pronounManager;
    private static MessageManager messageManager;

    public static void onInitialize() {
        pronounManager = new PronounManager();
        messageManager = new MessageManager();
    }

    public static PronounManager getPronounManager() {
        return pronounManager;
    }

    public static MessageManager getMessageManager() {
        return messageManager;
    }
}
