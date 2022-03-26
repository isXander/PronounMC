package cc.woverflow.pronounmc;

import cc.woverflow.pronounmc.utils.MessageManager;
import cc.woverflow.pronounmc.utils.PronounManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PronounMC {
    private static PronounManager pronounManager;
    private static MessageManager messageManager;
    private static Logger logger = LoggerFactory.getLogger("PronounMC");

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

    public static Logger getLogger() {
        return logger;
    }
}
