package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import javax.annotation.Nullable;

@Config(modid = FallingTree.MOD_ID)
public class CommonConfig{
	@Config.Name("reverse_sneaking")
	@Config.Comment("When set to true, a tree will only be chopped down if the player is sneaking")
	public static boolean reverseSneaking = false;
	
	@Nullable
	public static Block getBlock(String name){
		try{
			return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
		}
		catch(Exception e){
			return null;
		}
	}
	
	@Nullable
	public static Item getItem(String name){
		try{
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
		}
		catch(Exception e){
			return null;
		}
	}
	
	public boolean isReverseSneaking(){
		return reverseSneaking;
	}
	
	@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
	private static class Handler{
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
			if(event.getModID().equals(FallingTree.MOD_ID)){
				ConfigManager.sync(FallingTree.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
