package fr.raksrinana.fallingtree.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.fabric.cloth.ClothConfigHook;
import lombok.extern.slf4j.Slf4j;
import me.shedaniel.autoconfig.gui.DefaultGuiProviders;
import me.shedaniel.autoconfig.gui.DefaultGuiTransformers;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ModMenuImpl implements ModMenuApi{
	@SuppressWarnings("unused")
	private static final GuiRegistry defaultGuiRegistry = DefaultGuiTransformers.apply(DefaultGuiProviders.apply(new GuiRegistry()));
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory(){
		if(FabricLoader.getInstance().isModLoaded("cloth-config")){
			return (screen) -> {
				try{
					return Class.forName("fr.raksrinana.fallingtree.fabric.cloth.ClothConfigHook")
							.asSubclass(ClothConfigHook.class)
							.getConstructor(FallingTreeCommon.class)
							.newInstance(FallingTree.getMod())
							.load()
							.apply(screen);
				}
				catch(ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e){
					log.error("Failed to hook into ClothConfig", e);
				}
				return null;
			};
		}
		return screen -> null;
	}
}
