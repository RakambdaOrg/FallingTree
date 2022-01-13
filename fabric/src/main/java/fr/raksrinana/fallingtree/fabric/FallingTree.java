package fr.raksrinana.fallingtree.fabric;

import fr.raksrinana.fallingtree.fabric.common.FallingTreeCommonsImpl;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;

public class FallingTree implements ModInitializer{
	public static final String MOD_ID = "fallingtree";
	@Getter
	private static final FallingTreeCommonsImpl mod = new FallingTreeCommonsImpl();
	
	@Override
	public void onInitialize(){
		mod.registerEnchant();
		mod.register();
	}
}
