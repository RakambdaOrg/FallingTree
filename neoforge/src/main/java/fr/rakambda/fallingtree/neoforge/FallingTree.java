package fr.rakambda.fallingtree.neoforge;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.neoforge.client.cloth.ClothConfigHook;
import fr.rakambda.fallingtree.neoforge.common.FallingTreeCommonsImpl;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;

@Log4j2
@Mod(FallingTree.MOD_ID)
public class FallingTree{
	public static final String MOD_ID = "fallingtree";
	@Getter
	private static FallingTreeCommonsImpl mod;
	
	public FallingTree(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer){
		mod = new FallingTreeCommonsImpl(modEventBus);
		
		if(ModList.get().isLoaded("cloth_config")){
			try{
				Class.forName("fr.rakambda.fallingtree.neoforge.client.cloth.ClothConfigHook")
						.asSubclass(ClothConfigHook.class)
						.getConstructor(FallingTreeCommon.class)
						.newInstance(mod)
						.load(modContainer);
			}
			catch(ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e){
				log.error("Failed to hook into ClothConfig", e);
			}
		}
		
		mod.registerEnchant();
		mod.registerForge(NeoForge.EVENT_BUS);
	}
}
