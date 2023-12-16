package fr.rakambda.fallingtree.neoforge;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.neoforge.client.cloth.ClothConfigHook;
import fr.rakambda.fallingtree.neoforge.common.FallingTreeCommonsImpl;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import java.lang.reflect.InvocationTargetException;

@Log4j2
@Mod(FallingTree.MOD_ID)
public class FallingTree{
	public static final String MOD_ID = "fallingtree";
	@Getter
	private static final FallingTreeCommonsImpl mod = new FallingTreeCommonsImpl();
	
	public FallingTree(){
		if(ModList.get().isLoaded("cloth_config")){
			try{
				Class.forName("fr.rakambda.fallingtree.neoforge.client.cloth.ClothConfigHook")
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
		mod.registerNeoforge(NeoForge.EVENT_BUS);
	}
}
