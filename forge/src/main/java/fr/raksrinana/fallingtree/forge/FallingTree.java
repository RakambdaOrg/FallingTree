package fr.raksrinana.fallingtree.forge;

import fr.raksrinana.fallingtree.forge.config.Config;
import fr.raksrinana.fallingtree.forge.config.cloth.ClothConfigHook;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod(FallingTree.MOD_ID)
public class FallingTree{
	public static final String MOD_ID = "fallingtree";
	
	public FallingTree() throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
		
		if(ModList.get().isLoaded("cloth-config")){
			Class.forName("fr.raksrinana.fallingtree.forge.config.cloth.ClothConfigHook")
					.asSubclass(ClothConfigHook.class)
					.newInstance()
					.load();
		}
	}
}
