package fr.raksrinana.fallingtree.fabric.config;

import com.google.gson.annotations.Expose;
import lombok.Data;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class TreeConfiguration{
	@Expose
	private List<String> allowedLogs = new ArrayList<>();
	@Expose
	private List<String> deniedLogs = new ArrayList<>();
	@Expose
	private List<String> allowedLeaves = new ArrayList<>();
	@Expose
	private List<String> allowedNonDecayLeaves = new ArrayList<>();
	@Expose
	private List<String> deniedLeaves = new ArrayList<>();
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
	private boolean includePersistentLeavesInRequiredCount = true;
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
	private List<String> allowedAdjacentBlocks = new ArrayList<>();
	@Expose
	private AdjacentStopMode adjacentStopMode = AdjacentStopMode.STOP_ALL;
	
	public Collection<Block> getAllowedNonDecayLeaveBlocks(){
		return ConfigCache.getInstance().getNonDecayLeavesAllowed(this::getAllowedNonDecayLeaves);
	}
	
	public Collection<Block> getDeniedLeaveBlocks(){
		return ConfigCache.getInstance().getLeavesDenied(this::getDeniedLeaves);
	}
	
	public Collection<Block> getDeniedLogBlocks(){
		return ConfigCache.getInstance().getLogsDenied(this::getDeniedLogs);
	}
	
	public Collection<Block> getAllowedLeaveBlocks(){
		return ConfigCache.getInstance().getLeavesAllowed(this::getAllowedLeaves);
	}
	
	public Collection<Block> getAllowedLogBlocks(){
		return ConfigCache.getInstance().getLogsAllowed(this::getAllowedLogs);
	}
	
	public Collection<Block> getAllowedAdjacentBlockBlocks(){
		return ConfigCache.getInstance().getAdjacentBlocksAllowed(this::getAllowedAdjacentBlocks);
	}
}
