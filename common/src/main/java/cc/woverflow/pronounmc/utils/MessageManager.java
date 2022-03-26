package cc.woverflow.pronounmc.utils;

import cc.woverflow.pronounmc.PronounMC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageManager {
    private final Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();

    public void queueMessage(Text message, UUID sender) {
        messageQueue.add(new Message(message, sender));
    }

    public void sendMessages() {
        while (messageQueue.peek() != null) {
            Message message = messageQueue.poll();
            MinecraftClient.getInstance().inGameHud.addChatMessage(null, message.message, message.sender);
        }
    }

    public void queuePronounMessage(Text message, UUID sender) {
        Multithreading.runAsync(() -> {
            Pronouns pronouns = PronounMC.getPronounManager().getOrFindPronouns(sender);
            if (pronouns == null || pronouns == Pronouns.UNSPECIFIED) {
                queueMessage(message, sender);
                return;
            }

            queueMessage(new LiteralText("").append(pronouns.getText().formatted(Formatting.DARK_GRAY)).append(" ").append(message), sender);
        });
    }

    public boolean isMessageSentByPlayer(UUID sender) {
        if (MinecraftClient.getInstance().world == null)
            return false;

        return MinecraftClient.getInstance().world.getPlayerByUuid(sender) != null;
    }

    private record Message(Text message, UUID sender) {}
}
