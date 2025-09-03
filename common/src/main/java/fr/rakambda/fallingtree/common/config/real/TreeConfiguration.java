package fr.rakambda.fallingtree.common.config.real;

import com.google.gson.annotations.Expose;
import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.IResettable;
import fr.rakambda.fallingtree.common.config.ITreeConfiguration;
import fr.rakambda.fallingtree.common.config.enums.AdjacentStopMode;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.config.enums.BreakOrder;
import fr.rakambda.fallingtree.common.config.enums.DetectionMode;
import fr.rakambda.fallingtree.common.config.enums.MaxSizeAction;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import lombok.Data;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.Objects.isNull;

@Data
public class TreeConfiguration implements ITreeConfiguration, IResettable{
	@Expose
	@NonNull
	private List<String> allowedLogs = new ArrayList<>();
	@Expose
	@NonNull
	private List<String> deniedLogs = new ArrayList<>();
	@Expose
	@NonNull
	private List<String> allowedLeaves = new ArrayList<>();
	@Expose
	@NonNull
	private List<String> allowedNonDecayLeaves = new ArrayList<>();
	@Expose
	@NonNull
	private List<String> deniedLeaves = new ArrayList<>();
	@Expose
	@NonNull
	private BreakMode breakMode = BreakMode.INSTANTANEOUS;
	@Expose
	@NonNull
	private DetectionMode detectionMode = DetectionMode.WHOLE_TREE;
	@Expose
	@NonNull
	private Map<String, DetectionMode> detectionModeBiomeOverride = new HashMap<>();
	@Expose
	private int maxScanSize = 500;
	@Expose
	private int minSize = 0;
	@Expose
	private int maxSize = 100;
	@Expose
	private int maxLeafDistanceFromLog = 15;
	@Expose
	@NonNull
	private MaxSizeAction maxSizeAction = MaxSizeAction.ABORT;
	@Expose
	@NonNull
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
	private boolean breakMangroveRoots = true;
	@Expose
	private int searchAreaRadius = -1;
	@Expose
	@NonNull
	private List<String> allowedAdjacentBlocks = new ArrayList<>();
	@Expose
	@NonNull
	private AdjacentStopMode adjacentStopMode = AdjacentStopMode.STOP_ALL;
	@Expose
	private boolean spawnItemsAtBreakPoint = false;
	@Expose
	private float trunkLootPercentage = 1f;
	
	//Cache
	private Set<IBlock> deniedLeavesCache;
	private Set<IBlock> deniedLogsCache;
	private Set<IBlock> allowedLeavesCache;
	private Set<IBlock> allowedNonDecayLeavesCache;
	private Set<IBlock> allowedLogsCache;
	private Set<IBlock> allowedAdjacentBlocksCache;
	private Set<IBlock> adjacentBlocksBaseCache;
	private Set<IBlock> defaultLogsBlocksCache;
	
	@NonNull
	public Collection<IBlock> getAllowedNonDecayLeaveBlocks(@NonNull FallingTreeCommon<?> common){
		if(isNull(allowedNonDecayLeavesCache)){
			allowedNonDecayLeavesCache = common.getAsBlocks(getAllowedNonDecayLeaves());
		}
		return allowedNonDecayLeavesCache;
	}
	
	@NonNull
	public Collection<IBlock> getDeniedLeaveBlocks(@NonNull FallingTreeCommon<?> common){
		if(isNull(deniedLeavesCache)){
			deniedLeavesCache = common.getAsBlocks(getDeniedLeaves());
		}
		return deniedLeavesCache;
	}
	
	@NonNull
	public Collection<IBlock> getDeniedLogBlocks(@NonNull FallingTreeCommon<?> common){
		if(isNull(deniedLogsCache)){
			deniedLogsCache = common.getAsBlocks(getDeniedLogs());
		}
		return deniedLogsCache;
	}
	
	@NonNull
	public Collection<IBlock> getAllowedLeaveBlocks(@NonNull FallingTreeCommon<?> common){
		if(isNull(allowedLeavesCache)){
			allowedLeavesCache = common.getAsBlocks(getAllowedLeaves());
		}
		return allowedLeavesCache;
	}
	
	@NonNull
	public Collection<IBlock> getAllowedLogBlocks(@NonNull FallingTreeCommon<?> common){
		if(isNull(allowedLogsCache)){
			allowedLogsCache = common.getAsBlocks(getAllowedLogs());
		}
		return allowedLogsCache;
	}
	
	@NonNull
	public Collection<IBlock> getAllowedAdjacentBlockBlocks(@NonNull FallingTreeCommon<?> common){
		if(isNull(allowedAdjacentBlocksCache)){
			allowedAdjacentBlocksCache = common.getAsBlocks(getAllowedAdjacentBlocks());
		}
		return allowedAdjacentBlocksCache;
	}
	
	@NonNull
	public Collection<IBlock> getDefaultLogsBlocks(@NonNull FallingTreeCommon<?> common){
		if(isNull(defaultLogsBlocksCache)){
			defaultLogsBlocksCache = common.getAllNonStrippedLogsBlocks();
		}
		return defaultLogsBlocksCache;
	}
	
	@NonNull
	public Collection<IBlock> getAllAllowedAdjacentBlockBlocks(@NonNull FallingTreeCommon<?> common){
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
	
	public void reset(){
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
