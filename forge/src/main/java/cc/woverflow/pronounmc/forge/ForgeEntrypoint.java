package cc.woverflow.pronounmc.forge;

import cc.woverflow.pronounmc.PronounMC;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("pronounmc")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEntrypoint {
    public ForgeEntrypoint() {
        PronounMC.onInitialize();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        PronounMC.getMessageManager().sendMessages();
    }
}
