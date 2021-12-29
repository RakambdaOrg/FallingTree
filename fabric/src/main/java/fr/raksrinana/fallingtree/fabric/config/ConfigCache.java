package fr.raksrinana.fallingtree.fabric.config;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.getAsBlocks;
import static fr.raksrinana.fallingtree.fabric.utils.FallingTreeUtils.getAsItems;
import static java.util.Objects.isNull;

public class ConfigCache{
	private static ConfigCache INSTANCE;
	private Set<Item> toolsDenied;
	private Set<Item> toolsAllowed;
	private Set<Block> leavesDenied;
	private Set<Block> logsDenied;
	private Set<Block> leavesAllowed;
	private Set<Block> leavesNonDecayAllowed;
	private Set<Block> logsAllowed;
	private Set<Block> adjacentBlocksAllowed;
	private Set<Block> adjacentBlocksBase;
	private Set<Block> defaultLogs;
	
	public void invalidate(){
		toolsDenied = null;
		toolsAllowed = null;
		leavesDenied = null;
		leavesAllowed = null;
		leavesNonDecayAllowed = null;
		logsDenied = null;
		logsAllowed = null;
		adjacentBlocksAllowed = null;
		adjacentBlocksBase = null;
		defaultLogs = null;
	}
	
	public Collection<Item> getToolsAllowed(Supplier<Collection<String>> collectionSupplier){
		if(isNull(toolsAllowed)){
			toolsAllowed = getAsItems(collectionSupplier.get());
		}
		return toolsAllowed;
	}
	
	public Collection<Item> getToolsDenied(Supplier<Collection<String>> collectionSupplier){
		if(isNull(toolsDenied)){
			toolsDenied = getAsItems(collectionSupplier.get());
		}
		return toolsDenied;
	}
	
	public Collection<Block> getLeavesDenied(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesDenied)){
			leavesDenied = getAsBlocks(collectionSupplier.get());
		}
		return leavesDenied;
	}
	
	public Collection<Block> getLogsDenied(Supplier<Collection<String>> collectionSupplier){
		if(isNull(logsDenied)){
			logsDenied = getAsBlocks(collectionSupplier.get());
		}
		return logsDenied;
	}
	
	public Collection<Block> getLeavesAllowed(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesAllowed)){
			leavesAllowed = getAsBlocks(collectionSupplier.get());
		}
		return leavesAllowed;
	}
	
	public Collection<Block> getAdjacentBlocksAllowed(Supplier<Collection<String>> collectionSupplier){
		if(isNull(adjacentBlocksAllowed)){
			adjacentBlocksAllowed = getAsBlocks(collectionSupplier.get());
		}
		return adjacentBlocksAllowed;
	}
	
	public Collection<Block> getDefaultLogs(){
		if(isNull(defaultLogs)){
			defaultLogs = BlockTags.LOGS.getValues().stream()
					.filter(block -> !Optional.of(Registry.BLOCK.getKey(block))
							.map(ResourceLocation::getPath)
							.map(name -> name.startsWith("stripped"))
							.orElse(false))
					.collect(Collectors.toSet());
		}
		return defaultLogs;
	}
	
	public Collection<Block> getAdjacentBlocksBase(){
		if(isNull(adjacentBlocksBase)){
			adjacentBlocksBase = new HashSet<>();
			adjacentBlocksBase.add(Blocks.AIR);
			adjacentBlocksBase.addAll(BlockTags.LEAVES.getValues());
			adjacentBlocksBase.addAll(getDefaultLogs());
			adjacentBlocksBase.addAll(getLogsAllowed(Configuration.getInstance().getTrees()::getAllowedLogs));
			adjacentBlocksBase.addAll(getLeavesAllowed(Configuration.getInstance().getTrees()::getAllowedLeaves));
			adjacentBlocksBase.addAll(getNonDecayLeavesAllowed(Configuration.getInstance().getTrees()::getAllowedNonDecayLeaves));
			adjacentBlocksBase.removeAll(getLogsDenied(Configuration.getInstance().getTrees()::getDeniedLogs));
			adjacentBlocksBase.removeAll(getLeavesDenied(Configuration.getInstance().getTrees()::getDeniedLeaves));
		}
		return adjacentBlocksBase;
	}
	
	public Collection<Block> getLogsAllowed(Supplier<Collection<String>> collectionSupplier){
		if(isNull(logsAllowed)){
			logsAllowed = getAsBlocks(collectionSupplier.get());
		}
		return logsAllowed;
	}
	
	public Collection<Block> getNonDecayLeavesAllowed(Supplier<Collection<String>> collectionSupplier){
		if(isNull(leavesNonDecayAllowed)){
			leavesNonDecayAllowed = getAsBlocks(collectionSupplier.get());
		}
		return leavesNonDecayAllowed;
	}
	
	public static ConfigCache getInstance(){
		if(isNull(INSTANCE)){
			INSTANCE = new ConfigCache();
		}
		return INSTANCE;
	}
}
