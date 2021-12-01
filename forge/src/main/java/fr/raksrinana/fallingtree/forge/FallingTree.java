package fr.raksrinana.fallingtree.forge;

import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.config.cloth.ClothConfigHook;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.InvocationTargetException;

@Mod(FallingTree.MOD_ID)
public class FallingTree{
	public static final String MOD_ID = "fallingtree";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	public static final String IGNORESERVERONLY = "OHNOES\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31\uD83D\uDE31";
	
	public FallingTree(){
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> IGNORESERVERONLY, (a, b) -> true));
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
		
		if(ModList.get().isLoaded("cloth_config")){
			try{
				Class.forName("fr.raksrinana.fallingtree.forge.config.cloth.ClothConfigHook")
						.asSubclass(ClothConfigHook.class)
						.getConstructor()
						.newInstance()
						.load();
			}
			catch(ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e){
				logger.error("Failed to hook into ClothConfig", e);
			}
		}
	}
}
