package fr.raksrinana.fallingtree.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import fr.raksrinana.fallingtree.FallingTree;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
public class Config{
	public static final ForgeConfigSpec SERVER_SPEC;
	public static final ServerConfig SERVER;
	
	static {
		Pair<ServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
		SERVER = serverPair.getLeft();
		SERVER_SPEC = serverPair.getRight();
	}
}
