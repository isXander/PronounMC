package cc.woverflow.pronounmc.fabric;

import cc.woverflow.pronounmc.PronounMC;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FabricEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PronounMC.onInitialize();

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            PronounMC.getMessageManager().sendMessages();
        });
    }
}
