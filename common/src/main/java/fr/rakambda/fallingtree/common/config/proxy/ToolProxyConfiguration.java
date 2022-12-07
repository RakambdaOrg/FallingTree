package fr.rakambda.fallingtree.common.config.proxy;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import fr.rakambda.fallingtree.common.config.IResettable;
import fr.rakambda.fallingtree.common.config.IToolConfiguration;
import fr.rakambda.fallingtree.common.config.enums.DamageRounding;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class ToolProxyConfiguration implements IToolConfiguration, IResettable{
	private final IToolConfiguration delegate;
	
	@Setter
	private Double speedMultiplicand;
	@Setter
	private Boolean forceToolUsage;
	
	@Override
	public void reset(){
		setSpeedMultiplicand(null);
		setForceToolUsage(null);
	}
	
	@Override
	@NotNull
	public Collection<IItem> getDeniedItems(@NotNull FallingTreeCommon<?> mod){
		return delegate.getDeniedItems(mod);
	}
	
	@Override
	@NotNull
	public Collection<IItem> getAllowedItems(@NotNull FallingTreeCommon<?> mod){
		return delegate.getAllowedItems(mod);
	}
	
	@Override
	public boolean isPreserve(){
		return delegate.isPreserve();
	}
	
	@Override
	public boolean isIgnoreTools(){
		return delegate.isIgnoreTools();
	}
	
	@Override
	public double getDamageMultiplicand(){
		return delegate.getDamageMultiplicand();
	}
	
	@Override
	public DamageRounding getDamageRounding(){
		return delegate.getDamageRounding();
	}
	
	@Override
	public double getSpeedMultiplicand(){
		return Optional.ofNullable(speedMultiplicand).orElseGet(delegate::getSpeedMultiplicand);
	}
	
	@Override
	public boolean isForceToolUsage(){
		return Optional.ofNullable(forceToolUsage).orElseGet(delegate::isForceToolUsage);
	}
}
