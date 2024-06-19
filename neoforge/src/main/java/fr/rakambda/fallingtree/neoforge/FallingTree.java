package fr.rakambda.fallingtree.neoforge;

import fr.rakambda.fallingtree.neoforge.common.FallingTreeCommonsImpl;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

@Log4j2
@Mod(FallingTree.MOD_ID)
public class FallingTree{
	public static final String MOD_ID = "fallingtree";
	@Getter
	private static FallingTreeCommonsImpl mod;
	
	public FallingTree(@NotNull IEventBus modEventBus, @NotNull ModContainer modContainer){
		mod = new FallingTreeCommonsImpl(modEventBus);
		mod.registerForge(NeoForge.EVENT_BUS);
	}
}
