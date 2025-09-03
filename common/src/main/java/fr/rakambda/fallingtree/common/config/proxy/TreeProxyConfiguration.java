package fr.rakambda.fallingtree.common.config.proxy;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.IResettable;
import fr.rakambda.fallingtree.common.config.ITreeConfiguration;
import fr.rakambda.fallingtree.common.config.enums.AdjacentStopMode;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.config.enums.BreakOrder;
import fr.rakambda.fallingtree.common.config.enums.DetectionMode;
import fr.rakambda.fallingtree.common.config.enums.MaxSizeAction;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TreeProxyConfiguration implements ITreeConfiguration, IResettable{
	private final ITreeConfiguration delegate;
	
	@Setter
	private BreakMode breakMode;
	
	@Override
	public void reset(){
		breakMode = null;
	}
	
	@Override
	@NonNull
	public Collection<IBlock> getAllowedNonDecayLeaveBlocks(@NonNull FallingTreeCommon<?> mod){
		return delegate.getAllowedNonDecayLeaveBlocks(mod);
	}
	
	@Override
	@NonNull
	public Collection<IBlock> getDeniedLeaveBlocks(@NonNull FallingTreeCommon<?> mod){
		return delegate.getDeniedLeaveBlocks(mod);
	}
	
	@Override
	@NonNull
	public Collection<IBlock> getDeniedLogBlocks(@NonNull FallingTreeCommon<?> mod){
		return delegate.getDeniedLogBlocks(mod);
	}
	
	@Override
	@NonNull
	public Collection<IBlock> getAllowedLeaveBlocks(@NonNull FallingTreeCommon<?> mod){
		return delegate.getAllowedLeaveBlocks(mod);
	}
	
	@Override
	@NonNull
	public Collection<IBlock> getAllowedLogBlocks(@NonNull FallingTreeCommon<?> mod){
		return delegate.getAllowedLogBlocks(mod);
	}
	
	@Override
	@NonNull
	public Collection<IBlock> getAllowedAdjacentBlockBlocks(@NonNull FallingTreeCommon<?> mod){
		return delegate.getAllowedAdjacentBlockBlocks(mod);
	}
	
	@Override
	@NonNull
	public Collection<IBlock> getDefaultLogsBlocks(@NonNull FallingTreeCommon<?> mod){
		return delegate.getDefaultLogsBlocks(mod);
	}
	
	@Override
	@NonNull
	public Collection<IBlock> getAllAllowedAdjacentBlockBlocks(@NonNull FallingTreeCommon<?> mod){
		return delegate.getAllAllowedAdjacentBlockBlocks(mod);
	}
	
	@Override
	@NonNull
	public BreakMode getBreakMode(){
		return Optional.ofNullable(breakMode).orElseGet(delegate::getBreakMode);
	}
	
	@Override
	@NonNull
	public DetectionMode getDetectionMode(){
		return delegate.getDetectionMode();
	}
	
	@Override
	@NonNull
	public Map<String, DetectionMode> getDetectionModeBiomeOverride(){
		return delegate.getDetectionModeBiomeOverride();
	}
	
	@Override
	public int getMaxScanSize(){
		return delegate.getMaxScanSize();
	}
	
	@Override
	public int getMinSize(){
		return delegate.getMinSize();
	}
	
	@Override
	public int getMaxSize(){
		return delegate.getMaxSize();
	}
	
	@Override
	public int getMaxLeafDistanceFromLog(){
		return delegate.getMaxLeafDistanceFromLog();
	}
	
	@Override
	@NonNull
	public MaxSizeAction getMaxSizeAction(){
		return delegate.getMaxSizeAction();
	}
	
	@Override
	@NonNull
	public BreakOrder getBreakOrder(){
		return delegate.getBreakOrder();
	}
	
	@Override
	public int getMinimumLeavesAroundRequired(){
		return delegate.getMinimumLeavesAroundRequired();
	}
	
	@Override
	public boolean isIncludePersistentLeavesInRequiredCount(){
		return delegate.isIncludePersistentLeavesInRequiredCount();
	}
	
	@Override
	public boolean isTreeBreaking(){
		return delegate.isTreeBreaking();
	}
	
	@Override
	public boolean isLeavesBreaking(){
		return delegate.isLeavesBreaking();
	}
	
	@Override
	public int getLeavesBreakingForceRadius(){
		return delegate.getLeavesBreakingForceRadius();
	}
	
	@Override
	public boolean isAllowMixedLogs(){
		return delegate.isAllowMixedLogs();
	}
	
	@Override
	public boolean isBreakNetherTreeWarts(){
		return delegate.isBreakNetherTreeWarts();
	}
	
	@Override
	public boolean isBreakMangroveRoots(){
		return delegate.isBreakMangroveRoots();
	}
	
	@Override
	public int getSearchAreaRadius(){
		return delegate.getSearchAreaRadius();
	}
	
	@Override
	@NonNull
	public AdjacentStopMode getAdjacentStopMode(){
		return delegate.getAdjacentStopMode();
	}
	
	@Override
	public boolean isSpawnItemsAtBreakPoint(){
		return delegate.isSpawnItemsAtBreakPoint();
	}
	
	@Override
	public float getTrunkLootPercentage(){
		return delegate.getTrunkLootPercentage();
	}
}
