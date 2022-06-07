package fr.raksrinana.fallingtree.common.config;

import com.google.gson.annotations.Expose;
import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.config.enums.*;
import fr.raksrinana.fallingtree.common.wrapper.IBlock;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import static java.util.Objects.isNull;

@Data
public class TreeConfiguration{
	@Expose
	@NotNull
	private List<String> allowedLogs = new ArrayList<>();
	@Expose
	@NotNull
	private List<String> deniedLogs = new ArrayList<>();
	@Expose
	@NotNull
	private List<String> allowedLeaves = new ArrayList<>();
	@Expose
	@NotNull
	private List<String> allowedNonDecayLeaves = new ArrayList<>();
	@Expose
	@NotNull
	private List<String> deniedLeaves = new ArrayList<>();
	@Expose
	@NotNull
	private BreakMode breakMode = BreakMode.INSTANTANEOUS;
	@Expose
	@NotNull
	private DetectionMode detectionMode = DetectionMode.WHOLE_TREE;
	@Expose
	private int maxScanSize = 500;
	@Expose
	private int maxSize = 100;
	@Expose
	@NotNull
	private MaxSizeAction maxSizeAction = MaxSizeAction.ABORT;
	@Expose
	@NotNull
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
	private boolean breakMangroveRoots = true;
	@Expose
	private int searchAreaRadius = -1;
	@Expose
	@NotNull
	private List<String> allowedAdjacentBlocks = new ArrayList<>();
	@Expose
	@NotNull
	private AdjacentStopMode adjacentStopMode = AdjacentStopMode.STOP_ALL;
	
	//Cache
	private Set<IBlock> deniedLeavesCache;
	private Set<IBlock> deniedLogsCache;
	private Set<IBlock> allowedLeavesCache;
	private Set<IBlock> allowedNonDecayLeavesCache;
	private Set<IBlock> allowedLogsCache;
	private Set<IBlock> allowedAdjacentBlocksCache;
	private Set<IBlock> adjacentBlocksBaseCache;
	private Set<IBlock> defaultLogsBlocksCache;
	
	@NotNull
	public Collection<IBlock> getAllowedNonDecayLeaveBlocks(@NotNull FallingTreeCommon<?> common){
		if(isNull(allowedNonDecayLeavesCache)){
			allowedNonDecayLeavesCache = common.getAsBlocks(getAllowedNonDecayLeaves());
		}
		return allowedNonDecayLeavesCache;
	}
	
	@NotNull
	public Collection<IBlock> getDeniedLeaveBlocks(@NotNull FallingTreeCommon<?> common){
		if(isNull(deniedLeavesCache)){
			deniedLeavesCache = common.getAsBlocks(getDeniedLeaves());
		}
		return deniedLeavesCache;
	}
	
	@NotNull
	public Collection<IBlock> getDeniedLogBlocks(@NotNull FallingTreeCommon<?> common){
		if(isNull(deniedLogsCache)){
			deniedLogsCache = common.getAsBlocks(getDeniedLogs());
		}
		return deniedLogsCache;
	}
	
	@NotNull
	public Collection<IBlock> getAllowedLeaveBlocks(@NotNull FallingTreeCommon<?> common){
		if(isNull(allowedLeavesCache)){
			allowedLeavesCache = common.getAsBlocks(getAllowedLeaves());
		}
		return allowedLeavesCache;
	}
	
	@NotNull
	public Collection<IBlock> getAllowedLogBlocks(@NotNull FallingTreeCommon<?> common){
		if(isNull(allowedLogsCache)){
			allowedLogsCache = common.getAsBlocks(getAllowedLogs());
		}
		return allowedLogsCache;
	}
	
	@NotNull
	public Collection<IBlock> getAllowedAdjacentBlockBlocks(@NotNull FallingTreeCommon<?> common){
		if(isNull(allowedAdjacentBlocksCache)){
			allowedAdjacentBlocksCache = common.getAsBlocks(getAllowedAdjacentBlocks());
		}
		return allowedAdjacentBlocksCache;
	}
	
	@NotNull
	public Collection<IBlock> getDefaultLogsBlocks(@NotNull FallingTreeCommon<?> common){
		if(isNull(defaultLogsBlocksCache)){
			defaultLogsBlocksCache = common.getAllNonStrippedLogsBlocks();
		}
		return defaultLogsBlocksCache;
	}
	
	@NotNull
	public Collection<IBlock> getAllAllowedAdjacentBlockBlocks(@NotNull FallingTreeCommon<?> common){
		if(isNull(allowedAdjacentBlocksCache)){
			allowedAdjacentBlocksCache = new HashSet<>();
			common.getBlock("minecraft:air").forEach(allowedAdjacentBlocksCache::add);
			common.getBlock("#minecraft:leaves").forEach(allowedAdjacentBlocksCache::add);
			allowedAdjacentBlocksCache.addAll(getDefaultLogsBlocks(common));
			allowedAdjacentBlocksCache.addAll(getAllowedLogBlocks(common));
			allowedAdjacentBlocksCache.addAll(getAllowedLeaveBlocks(common));
			allowedAdjacentBlocksCache.addAll(getAllowedNonDecayLeaveBlocks(common));
			allowedAdjacentBlocksCache.removeAll(getDeniedLogBlocks(common));
			allowedAdjacentBlocksCache.removeAll(getDeniedLeaveBlocks(common));
		}
		return allowedAdjacentBlocksCache;
	}
	
	public void invalidate(){
		deniedLeavesCache = null;
		deniedLogsCache = null;
		allowedLeavesCache = null;
		allowedNonDecayLeavesCache = null;
		allowedLogsCache = null;
		allowedAdjacentBlocksCache = null;
		adjacentBlocksBaseCache = null;
		defaultLogsBlocksCache = null;
	}
}
