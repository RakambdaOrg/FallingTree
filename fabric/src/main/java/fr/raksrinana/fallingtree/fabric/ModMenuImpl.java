package fr.raksrinana.fallingtree.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.fabric.cloth.ClothConfigHook;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.FabricLoader;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ModMenuImpl implements ModMenuApi{
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
