package fr.raksrinana.fallingtree.forge;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.forge.cloth.ClothConfigHook;
import fr.raksrinana.fallingtree.forge.common.FallingTreeCommonsImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@Mod(FallingTree.MOD_ID)
public class FallingTree{
	public static final String MOD_ID = "fallingtree";
	@Getter
	private static final FallingTreeCommonsImpl mod = new FallingTreeCommonsImpl();
	
	public FallingTree(){
		if(ModList.get().isLoaded("cloth_config")){
			try{
				Class.forName("fr.raksrinana.fallingtree.forge.cloth.ClothConfigHook")
						.asSubclass(ClothConfigHook.class)
						.getConstructor(FallingTreeCommon.class)
						.newInstance(mod)
						.load();
			}
			catch(ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e){
				log.error("Failed to hook into ClothConfig", e);
			}
		}
		
		mod.registerEnchant();
		mod.registerForge(MinecraftForge.EVENT_BUS);
	}
}
