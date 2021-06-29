package fr.raksrinana.fallingtree.forge.config;

import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import static java.util.Objects.isNull;

public class ConfigCache{
	private static ConfigCache INSTANCE;
	private Set<Item> toolsBlacklist;
	private Set<Item> toolsWhitelist;
	private Set<Block> leavesBlacklist;
	private Set<Block> logsBlacklist;
	private Set<Block> leavesWhitelist;
	private Set<Block> leavesNonDecayWhitelist;
	private Set<Block> logsWhitelist;
	private Set<Block> adjacentBlocksWhitelist;
	private Set<Block> adjacentBlocksBase;
	
	public void invalidate(){
		toolsBlacklist = null;
		toolsWhitelist = null;
		leavesBlacklist = null;
		leavesWhitelist = null;
		leavesNonDecayWhitelist = null;
		logsBlacklist = null;
		logsWhitelist = null;
		adjacentBlocksWhitelist = null;
		adjacentBlocksBase = null;
	}
	
	public Collection<Item> getToolsWhitelisted(Supplier<Collection<String>> collectionSupplier){
		if(isNull(toolsWhitelist)){
			toolsWhitelist = FallingTreeUtils.getAsItems(collectionSupplier.get());
		}
		return toolsWhitelist;
	}
	
	public Collection<Item> getToolsBlacklisted(Supplier<Collection<String>> collectionSupplier){
		if(isNull(toolsBlacklist)){
			toolsBlacklist = FallingTreeUtils.getAsItems(collectionSupplier.get());
		}
		return toolsBlacklist;
	}
	
	public Collection<Block> getBlacklistedLeaves(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesBlacklist)){
			leavesBlacklist = FallingTreeUtils.getAsBlocks(collectionSupplier.get());
		}
		return leavesBlacklist;
	}
	
	public Collection<Block> getBlacklistedLogs(Supplier<Collection<String>> collectionSupplier){
		if(isNull(logsBlacklist)){
			logsBlacklist = FallingTreeUtils.getAsBlocks(collectionSupplier.get());
		}
		return logsBlacklist;
	}
	
	public Collection<Block> getWhitelistedLeaves(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesWhitelist)){
			leavesWhitelist = FallingTreeUtils.getAsBlocks(collectionSupplier.get());
		}
		return leavesWhitelist;
	}
	
	public Collection<Block> getWhitelistedAdjacentBlocks(Supplier<Collection<String>> collectionSupplier){
		if(isNull(adjacentBlocksWhitelist)){
			adjacentBlocksWhitelist = FallingTreeUtils.getAsBlocks(collectionSupplier.get());
		}
		return adjacentBlocksWhitelist;
	}
	
	public Collection<Block> getAdjacentBlocksBase(){
		if(isNull(adjacentBlocksBase)){
			adjacentBlocksBase = new HashSet<>();
			adjacentBlocksBase.add(Blocks.AIR);
			adjacentBlocksBase.addAll(BlockTags.LEAVES.getValues());
			adjacentBlocksBase.addAll(BlockTags.LOGS.getValues());
			adjacentBlocksBase.addAll(getWhitelistedLogs(Config.COMMON.getTrees()::getWhitelistedLogs));
			adjacentBlocksBase.addAll(getWhitelistedLeaves(Config.COMMON.getTrees()::getWhitelistedLeaves));
			adjacentBlocksBase.addAll(getWhitelistedNonDecayLeaves(Config.COMMON.getTrees()::getWhitelistedNonDecayLeaves));
			adjacentBlocksBase.removeAll(getBlacklistedLogs(Config.COMMON.getTrees()::getBlacklistedLogs));
			adjacentBlocksBase.removeAll(getBlacklistedLeaves(Config.COMMON.getTrees()::getBlacklistedLeaves));
		}
		return adjacentBlocksBase;
	}
	
	public Collection<Block> getWhitelistedLogs(Supplier<Collection<String>> collectionSupplier){
		if(isNull(logsWhitelist)){
			logsWhitelist = FallingTreeUtils.getAsBlocks(collectionSupplier.get());
		}
		return logsWhitelist;
	}
	
	public Collection<Block> getWhitelistedNonDecayLeaves(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesNonDecayWhitelist)){
			leavesNonDecayWhitelist = FallingTreeUtils.getAsBlocks(collectionSupplier.get());
		}
		return leavesNonDecayWhitelist;
	}
	
	public static ConfigCache getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new ConfigCache();
		}
		return INSTANCE;
	}
}
