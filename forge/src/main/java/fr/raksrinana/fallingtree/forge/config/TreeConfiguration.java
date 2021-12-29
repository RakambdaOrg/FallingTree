package fr.raksrinana.fallingtree.forge.config;

import com.google.gson.annotations.Expose;
import lombok.Data;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class TreeConfiguration{
	@Expose
	private List<String> whitelistedLogs = new ArrayList<>();
	@Expose
	private List<String> blacklistedLogs = new ArrayList<>();
	@Expose
	private List<String> whitelistedLeaves = new ArrayList<>();
	@Expose
	private List<String> whitelistedNonDecayLeaves = new ArrayList<>();
	@Expose
	private List<String> blacklistedLeaves = new ArrayList<>();
	@Expose
	private BreakMode breakMode = BreakMode.INSTANTANEOUS;
	@Expose
	private DetectionMode detectionMode = DetectionMode.WHOLE_TREE;
	@Expose
	private int maxScanSize = 500;
	@Expose
	private int maxSize = 100;
	@Expose
	private MaxSizeAction maxSizeAction = MaxSizeAction.ABORT;
	@Expose
	private BreakOrder breakOrder = BreakOrder.FURTHEST_FIRST;
	@Expose
	private int minimumLeavesAroundRequired = 1;
	@Expose
	private boolean treeBreaking = true;
	@Expose
	private boolean leavesBreaking = true;
	@Expose
	private int leavesBreakingForceRadius = 0;
	@Expose
	private boolean allowMixedLogs = false;
	@Expose
	private boolean breakNetherTreeWarts = true;
	@Expose
	private boolean instantlyBreakWarts = false;
	@Expose
	private int searchAreaRadius = -1;
	@Expose
	private List<String> whitelistedAdjacentBlocks = new ArrayList<>();
	@Expose
	private AdjacentStopMode adjacentStopMode = AdjacentStopMode.STOP_ALL;
	
	public Collection<Block> getWhitelistedNonDecayLeaveBlocks(){
		return ConfigCache.getInstance().getWhitelistedNonDecayLeaves(this::getWhitelistedNonDecayLeaves);
	}
	
	public Collection<Block> getBlacklistedLeaveBlocks(){
		return ConfigCache.getInstance().getBlacklistedLeaves(this::getBlacklistedLeaves);
	}
	
	public Collection<Block> getBlacklistedLogBlocks(){
		return ConfigCache.getInstance().getBlacklistedLogs(this::getBlacklistedLogs);
	}
	
	public Collection<Block> getWhitelistedLeaveBlocks(){
		return ConfigCache.getInstance().getWhitelistedLeaves(this::getWhitelistedLeaves);
	}
	
	public Collection<Block> getWhitelistedLogBlocks(){
		return ConfigCache.getInstance().getWhitelistedLogs(this::getWhitelistedLogs);
	}
	
	public Collection<Block> getWhitelistedAdjacentBlockBlocks(){
		return ConfigCache.getInstance().getWhitelistedAdjacentBlocks(this::getWhitelistedAdjacentBlocks);
	}
}
