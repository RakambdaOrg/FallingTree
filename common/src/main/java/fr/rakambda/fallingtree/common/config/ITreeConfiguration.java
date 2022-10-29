package fr.rakambda.fallingtree.common.config;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.AdjacentStopMode;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.config.enums.BreakOrder;
import fr.rakambda.fallingtree.common.config.enums.DetectionMode;
import fr.rakambda.fallingtree.common.config.enums.MaxSizeAction;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public interface ITreeConfiguration{
	@NotNull
	Collection<IBlock> getAllowedNonDecayLeaveBlocks(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IBlock> getDeniedLeaveBlocks(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IBlock> getDeniedLogBlocks(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IBlock> getAllowedLeaveBlocks(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IBlock> getAllowedLogBlocks(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IBlock> getAllowedAdjacentBlockBlocks(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IBlock> getDefaultLogsBlocks(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	Collection<IBlock> getAllAllowedAdjacentBlockBlocks(@NotNull FallingTreeCommon<?> common);
	
	@NotNull
	BreakMode getBreakMode();
	
	@NotNull
	DetectionMode getDetectionMode();
	
	int getMaxScanSize();
	
	int getMaxSize();
	
	@NotNull
	MaxSizeAction getMaxSizeAction();
	
	@NotNull
	BreakOrder getBreakOrder();
	
	int getMinimumLeavesAroundRequired();
	
	boolean isIncludePersistentLeavesInRequiredCount();
	
	boolean isTreeBreaking();
	
	boolean isLeavesBreaking();
	
	int getLeavesBreakingForceRadius();
	
	boolean isAllowMixedLogs();
	
	boolean isBreakNetherTreeWarts();
	
	boolean isInstantlyBreakWarts();
	
	boolean isBreakMangroveRoots();
	
	int getSearchAreaRadius();
	
	@NotNull
	AdjacentStopMode getAdjacentStopMode();
}
