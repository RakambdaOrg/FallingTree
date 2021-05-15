package fr.raksrinana.fallingtree.fabric.config;

import fr.raksrinana.fallingtree.fabric.FallingTree;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.getAsBlocks;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.getAsItems;
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
			toolsWhitelist = getAsItems(collectionSupplier.get());
		}
		return toolsWhitelist;
	}
	
	public Collection<Item> getToolsBlacklisted(Supplier<Collection<String>> collectionSupplier){
		if(isNull(toolsBlacklist)){
			toolsBlacklist = getAsItems(collectionSupplier.get());
		}
		return toolsBlacklist;
	}
	
	public Collection<Block> getBlacklistedLeaves(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesBlacklist)){
			leavesBlacklist = getAsBlocks(collectionSupplier.get());
		}
		return leavesBlacklist;
	}
	
	public Collection<Block> getBlacklistedLogs(Supplier<Collection<String>> collectionSupplier){
		if(isNull(logsBlacklist)){
			logsBlacklist = getAsBlocks(collectionSupplier.get());
		}
		return logsBlacklist;
	}
	
	public Collection<Block> getWhitelistedLeaves(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesWhitelist)){
			leavesWhitelist = getAsBlocks(collectionSupplier.get());
		}
		return leavesWhitelist;
	}
	
	public Collection<Block> getWhitelistedAdjacentBlocks(Supplier<Collection<String>> collectionSupplier){
		if(isNull(adjacentBlocksWhitelist)){
			adjacentBlocksWhitelist = getAsBlocks(collectionSupplier.get());
		}
		return adjacentBlocksWhitelist;
	}
	
	public Collection<Block> getAdjacentBlocksBase(){
		if(isNull(adjacentBlocksBase)){
			adjacentBlocksBase = new HashSet<>();
			adjacentBlocksBase.add(Blocks.AIR);
			adjacentBlocksBase.addAll(BlockTags.LEAVES.getValues());
			adjacentBlocksBase.addAll(BlockTags.LOGS.getValues());
			adjacentBlocksBase.addAll(getWhitelistedLogs(FallingTree.config.getTreesConfiguration()::getWhitelistedLogsStr));
			adjacentBlocksBase.addAll(getWhitelistedLeaves(FallingTree.config.getTreesConfiguration()::getWhitelistedLeavesStr));
			adjacentBlocksBase.addAll(getWhitelistedNonDecayLeaves(FallingTree.config.getTreesConfiguration()::getWhitelistedNonDecayLeavesStr));
			adjacentBlocksBase.removeAll(getBlacklistedLogs(FallingTree.config.getTreesConfiguration()::getBlacklistedLogsStr));
			adjacentBlocksBase.removeAll(getBlacklistedLeaves(FallingTree.config.getTreesConfiguration()::getBlacklistedLeavesStr));
		}
		return adjacentBlocksBase;
	}
	
	public Collection<Block> getWhitelistedLogs(Supplier<Collection<String>> collectionSupplier){
		if(isNull(logsWhitelist)){
			logsWhitelist = getAsBlocks(collectionSupplier.get());
		}
		return logsWhitelist;
	}
	
	public Collection<Block> getWhitelistedNonDecayLeaves(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesNonDecayWhitelist)){
			leavesNonDecayWhitelist = getAsBlocks(collectionSupplier.get());
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
