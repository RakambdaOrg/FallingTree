package fr.raksrinana.fallingtree.forge.config;

import fr.raksrinana.fallingtree.forge.utils.FallingTreeUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
	private Set<Block> defaultLogs;
	
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
		defaultLogs = null;
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
	
	public Collection<Block> getDefaultLogs(){
		if(isNull(defaultLogs)){
			defaultLogs = BlockTags.LOGS.getValues().stream()
					.filter(block -> !Optional.ofNullable(ForgeRegistries.BLOCKS.getKey(block))
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
			adjacentBlocksBase.addAll(getWhitelistedLogs(Configuration.getInstance().getTrees()::getWhitelistedLogs));
			adjacentBlocksBase.addAll(getWhitelistedLeaves(Configuration.getInstance().getTrees()::getWhitelistedLeaves));
			adjacentBlocksBase.addAll(getWhitelistedNonDecayLeaves(Configuration.getInstance().getTrees()::getWhitelistedNonDecayLeaves));
			adjacentBlocksBase.removeAll(getBlacklistedLogs(Configuration.getInstance().getTrees()::getBlacklistedLogs));
			adjacentBlocksBase.removeAll(getBlacklistedLeaves(Configuration.getInstance().getTrees()::getBlacklistedLeaves));
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
