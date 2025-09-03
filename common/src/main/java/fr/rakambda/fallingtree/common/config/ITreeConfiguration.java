package fr.rakambda.fallingtree.common.config;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.AdjacentStopMode;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.config.enums.BreakOrder;
import fr.rakambda.fallingtree.common.config.enums.DetectionMode;
import fr.rakambda.fallingtree.common.config.enums.MaxSizeAction;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import org.jspecify.annotations.NonNull;
import java.util.Collection;
import java.util.Map;

public interface ITreeConfiguration{
	@NonNull
	Collection<IBlock> getAllowedNonDecayLeaveBlocks(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	Collection<IBlock> getDeniedLeaveBlocks(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	Collection<IBlock> getDeniedLogBlocks(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	Collection<IBlock> getAllowedLeaveBlocks(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	Collection<IBlock> getAllowedLogBlocks(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	Collection<IBlock> getAllowedAdjacentBlockBlocks(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	Collection<IBlock> getDefaultLogsBlocks(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	Collection<IBlock> getAllAllowedAdjacentBlockBlocks(@NonNull FallingTreeCommon<?> common);
	
	@NonNull
	BreakMode getBreakMode();
	
	@NonNull
	DetectionMode getDetectionMode();
	
	@NonNull
	Map<String, DetectionMode> getDetectionModeBiomeOverride();
	
	int getMaxScanSize();
	
	int getMinSize();
	
	int getMaxSize();
	
	int getMaxLeafDistanceFromLog();
	
	@NonNull
	MaxSizeAction getMaxSizeAction();
	
	@NonNull
	BreakOrder getBreakOrder();
	
	int getMinimumLeavesAroundRequired();
	
	boolean isIncludePersistentLeavesInRequiredCount();
	
	boolean isTreeBreaking();
	
	boolean isLeavesBreaking();
	
	int getLeavesBreakingForceRadius();
	
	boolean isAllowMixedLogs();
	
	boolean isBreakNetherTreeWarts();
	
	boolean isBreakMangroveRoots();
	
	int getSearchAreaRadius();
	
	@NonNull
	AdjacentStopMode getAdjacentStopMode();
	
	boolean isSpawnItemsAtBreakPoint();
	
	float getTrunkLootPercentage();
}
