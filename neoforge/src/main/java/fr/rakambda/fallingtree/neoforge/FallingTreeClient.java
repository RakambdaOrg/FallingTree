package fr.rakambda.fallingtree.neoforge;

import lombok.extern.log4j.Log4j2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jspecify.annotations.NonNull;

@Log4j2
@Mod(value = FallingTree.MOD_ID, dist = Dist.CLIENT)
public class FallingTreeClient {
	public FallingTreeClient(@NonNull IEventBus modEventBus, @NonNull ModContainer modContainer){
		// if(ModList.get().isLoaded("cloth_config")){
		// 	try{
		// 		Class.forName("fr.rakambda.fallingtree.neoforge.client.cloth.ClothConfigHook")
		// 				.asSubclass(ClothConfigHook.class)
		// 				.getConstructor(FallingTreeCommon.class)
		// 				.newInstance(FallingTree.getMod())
		// 				.load(modContainer);
		// 	}
		// 	catch(ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e){
		// 		log.error("Failed to hook into ClothConfig", e);
		// 	}
		// }
	}
}
