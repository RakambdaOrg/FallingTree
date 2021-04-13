package fr.raksrinana.fallingtree.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.raksrinana.fallingtree.fabric.config.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigManager;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;
import me.shedaniel.autoconfig.gui.DefaultGuiProviders;
import me.shedaniel.autoconfig.gui.DefaultGuiTransformers;
import me.shedaniel.autoconfig.gui.registry.ComposedGuiRegistryAccess;
import me.shedaniel.autoconfig.gui.registry.DefaultGuiRegistryAccess;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;

@SuppressWarnings("unused")
public class ModMenuImpl implements ModMenuApi{
	private static final GuiRegistry defaultGuiRegistry = DefaultGuiTransformers.apply(DefaultGuiProviders.apply(new GuiRegistry()));
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory(){
		return screen -> new ConfigScreenProvider<>(
				(ConfigManager<Configuration>) AutoConfig.getConfigHolder(Configuration.class), getGuiRegistryAccess(), screen)
				.get();
	}
	
	private static GuiRegistryAccess getGuiRegistryAccess(){
		return new ComposedGuiRegistryAccess(defaultGuiRegistry, AutoConfig.getGuiRegistry(Configuration.class), new DefaultGuiRegistryAccess());
	}
}
