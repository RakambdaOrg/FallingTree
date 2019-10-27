package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
public class Config{
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final CommonConfig COMMON;
	
	static {
		Pair<CommonConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON = commonPair.getLeft();
		COMMON_SPEC = commonPair.getRight();
	}
}
