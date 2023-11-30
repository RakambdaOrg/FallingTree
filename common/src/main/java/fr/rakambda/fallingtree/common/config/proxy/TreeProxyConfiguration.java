package fr.rakambda.fallingtree.common.config.proxy;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import fr.rakambda.fallingtree.common.config.IResettable;
import fr.rakambda.fallingtree.common.config.ITreeConfiguration;
import fr.rakambda.fallingtree.common.config.enums.AdjacentStopMode;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.config.enums.BreakOrder;
import fr.rakambda.fallingtree.common.config.enums.DetectionMode;
import fr.rakambda.fallingtree.common.config.enums.MaxSizeAction;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
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
	@NotNull
	public Collection<IBlock> getAllowedNonDecayLeaveBlocks(@NotNull FallingTreeCommon<?> mod){
		return delegate.getAllowedNonDecayLeaveBlocks(mod);
	}
	
	@Override
	@NotNull
	public Collection<IBlock> getDeniedLeaveBlocks(@NotNull FallingTreeCommon<?> mod){
		return delegate.getDeniedLeaveBlocks(mod);
	}
	
	@Override
	@NotNull
	public Collection<IBlock> getDeniedLogBlocks(@NotNull FallingTreeCommon<?> mod){
		return delegate.getDeniedLogBlocks(mod);
	}
	
	@Override
	@NotNull
	public Collection<IBlock> getAllowedLeaveBlocks(@NotNull FallingTreeCommon<?> mod){
		return delegate.getAllowedLeaveBlocks(mod);
	}
	
	@Override
	@NotNull
	public Collection<IBlock> getAllowedLogBlocks(@NotNull FallingTreeCommon<?> mod){
		return delegate.getAllowedLogBlocks(mod);
	}
	
	@Override
	@NotNull
	public Collection<IBlock> getAllowedAdjacentBlockBlocks(@NotNull FallingTreeCommon<?> mod){
		return delegate.getAllowedAdjacentBlockBlocks(mod);
	}
	
	@Override
	@NotNull
	public Collection<IBlock> getDefaultLogsBlocks(@NotNull FallingTreeCommon<?> mod){
		return delegate.getDefaultLogsBlocks(mod);
	}
	
	@Override
	@NotNull
	public Collection<IBlock> getAllAllowedAdjacentBlockBlocks(@NotNull FallingTreeCommon<?> mod){
		return delegate.getAllAllowedAdjacentBlockBlocks(mod);
	}
	
	@Override
	@NotNull
	public BreakMode getBreakMode(){
		return Optional.ofNullable(breakMode).orElseGet(delegate::getBreakMode);
	}
	
	@Override
	@NotNull
	public DetectionMode getDetectionMode(){
		return delegate.getDetectionMode();
	}
	
	@Override
	public int getMaxScanSize(){
		return delegate.getMaxScanSize();
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
	@NotNull
	public MaxSizeAction getMaxSizeAction(){
		return delegate.getMaxSizeAction();
	}
	
	@Override
	@NotNull
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
	public boolean isInstantlyBreakWarts(){
		return delegate.isInstantlyBreakWarts();
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
	@NotNull
	public AdjacentStopMode getAdjacentStopMode(){
		return delegate.getAdjacentStopMode();
	}
}
