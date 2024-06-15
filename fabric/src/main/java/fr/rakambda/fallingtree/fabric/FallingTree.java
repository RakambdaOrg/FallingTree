package fr.rakambda.fallingtree.fabric;

import fr.rakambda.fallingtree.fabric.common.FallingTreeCommonsImpl;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;

public class FallingTree implements ModInitializer{
	public static final String MOD_ID = "fallingtree";
	@Getter
	private static final FallingTreeCommonsImpl mod = new FallingTreeCommonsImpl();
	
	@Override
	public void onInitialize(){
		mod.register();
	}
}
