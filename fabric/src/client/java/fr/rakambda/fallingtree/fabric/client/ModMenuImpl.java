package fr.rakambda.fallingtree.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.fabric.FallingTree;
import fr.rakambda.fallingtree.fabric.client.cloth.ClothConfigHook;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.InvocationTargetException;

public class ModMenuImpl implements ModMenuApi{
	private static final Logger log = LogManager.getLogger(ModMenuImpl.class);
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory(){
		if(FabricLoader.getInstance().isModLoaded("cloth-config")){
			return (screen) -> {
				try{
					return Class.forName("fr.rakambda.fallingtree.fabric.client.cloth.ClothConfigHook")
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
