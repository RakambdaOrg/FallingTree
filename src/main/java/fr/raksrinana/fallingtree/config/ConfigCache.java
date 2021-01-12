package fr.raksrinana.fallingtree.config;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsBlocks;
import static fr.raksrinana.fallingtree.utils.FallingTreeUtils.getAsItems;

public class ConfigCache{
	private static ConfigCache INSTANCE;
	private Set<Item> toolsBlacklist;
	private Set<Item> toolsWhitelist;
	private Set<Block> leavesBlacklist;
	private Set<Block> logsBlacklist;
	private Set<Block> leavesWhitelist;
	private Set<Block> logsWhitelist;
	
	public void invalidate(){
		toolsBlacklist = null;
		toolsWhitelist = null;
		leavesBlacklist = null;
		leavesWhitelist = null;
		logsBlacklist = null;
		logsWhitelist = null;
	}
	
	public Collection<Item> getToolsWhitelisted(Supplier<Collection<String>> collectionSupplier){
		if(Objects.isNull(toolsWhitelist)){
			toolsWhitelist = getAsItems(collectionSupplier.get());
		}
		return toolsWhitelist;
	}
	
	public Collection<Item> getToolsBlacklisted(Supplier<Collection<String>> collectionSupplier){
		if(Objects.isNull(toolsBlacklist)){
			toolsBlacklist = getAsItems(collectionSupplier.get());
		}
		return toolsBlacklist;
	}
	
	public Collection<Block> getBlacklistedLeaves(Supplier<Collection<String>> collectionSupplier){
		if(Objects.isNull(leavesBlacklist)){
			leavesBlacklist = getAsBlocks(collectionSupplier.get());
		}
		return leavesBlacklist;
	}
	
	public Collection<Block> getBlacklistedLogs(Supplier<Collection<String>> collectionSupplier){
		if(Objects.isNull(logsBlacklist)){
			logsBlacklist = getAsBlocks(collectionSupplier.get());
		}
		return logsBlacklist;
	}
	
	public Collection<Block> getWhitelistedLeaves(Supplier<Collection<String>> collectionSupplier){
		if(Objects.isNull(leavesWhitelist)){
			leavesWhitelist = getAsBlocks(collectionSupplier.get());
		}
		return leavesWhitelist;
	}
	
	public Collection<Block> getWhitelistedLogs(Supplier<Collection<String>> collectionSupplier){
		if(Objects.isNull(logsWhitelist)){
			logsWhitelist = getAsBlocks(collectionSupplier.get());
		}
		return logsWhitelist;
	}
	
	public static ConfigCache getInstance(){
		if(Objects.isNull(INSTANCE)){
			INSTANCE = new ConfigCache();
		}
		return INSTANCE;
	}
}
