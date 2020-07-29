package fr.raksrinana.fallingtree;

import fr.raksrinana.fallingtree.config.Config;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import javax.annotation.Nonnull;

@Mod(FallingTree.MOD_ID)
public class FallingTree{
	public static final String MOD_ID = "falling_tree";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public FallingTree(){
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
	}
	
	@Nonnull
	public static String getVersion(){
		return ModList.get().getModContainerById(MOD_ID).map(ModContainer::getModInfo).map(IModInfo::getVersion).map(ArtifactVersion::toString).orElse("NONE");
	}
	
	public static boolean isDevBuild(){
		return "NONE".equals(getVersion());
	}
}
