package cc.woverflow.pronounmc.forge;

import cc.woverflow.pronounmc.PronounMC;
import cc.woverflow.pronounmc.config.PronounConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("pronounmc")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEntrypoint {
    public ForgeEntrypoint() {
        PronounMC.onInitialize();

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
                new ConfigGuiHandler.ConfigGuiFactory(
                        (mc, screen) -> AutoConfig.getConfigScreen(PronounConfig.class, screen).get()
                )
        );
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        PronounMC.getMessageManager().sendQueuedMessages();
    }
}
