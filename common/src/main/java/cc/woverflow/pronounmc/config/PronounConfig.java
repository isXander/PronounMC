package cc.woverflow.pronounmc.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "pronounmc")
public class PronounConfig implements ConfigData {
    @ConfigEntry.Category("Visibility")
    public boolean showInChat = true;

    @ConfigEntry.Category("Visibility")
    public boolean showOnNametag = true;

    @ConfigEntry.Category("Visibility")
    public boolean showInTooltip = true;
}
