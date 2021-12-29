package fr.raksrinana.fallingtree.forge;

import fr.raksrinana.fallingtree.forge.config.cloth.ClothConfigHook;
import fr.raksrinana.fallingtree.forge.enchant.FallingTreeEnchantments;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.InvocationTargetException;

@Mod(FallingTree.MOD_ID)
public class FallingTree{
	public static final String MOD_ID = "fallingtree";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	
	public FallingTree(){
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
		
		var eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		FallingTreeEnchantments.register(eventBus);
	}
}
