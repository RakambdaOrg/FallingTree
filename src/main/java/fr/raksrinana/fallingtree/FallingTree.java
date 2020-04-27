package fr.raksrinana.fallingtree;

import net.minecraftforge.fml.common.Mod;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Mod(modid = FallingTree.MOD_ID, version = FallingTree.VERSION)
public class FallingTree{
	public static final String MOD_ID = "falling_tree";
	public static final String MOD_NAME = "Falling Tree";
	public static final String VERSION = "2.1.0";
	public static final Logger LOGGER = LogManager.getLogManager().getLogger(MOD_ID);
}
