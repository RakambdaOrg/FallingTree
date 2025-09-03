package fr.rakambda.fallingtree.common.config.proxy;

import fr.rakambda.fallingtree.common.config.IConfiguration;
import fr.rakambda.fallingtree.common.config.IEnchantmentConfiguration;
import fr.rakambda.fallingtree.common.config.IPlayerConfiguration;
import fr.rakambda.fallingtree.common.config.IResettable;
import fr.rakambda.fallingtree.common.config.enums.NotificationMode;
import fr.rakambda.fallingtree.common.config.enums.SneakMode;
import org.jspecify.annotations.NonNull;

public class ProxyConfiguration implements IConfiguration, IResettable{
	private final IConfiguration delegate;
	private final ToolProxyConfiguration toolDelegate;
	private final TreeProxyConfiguration treeDelegate;
	
	public ProxyConfiguration(IConfiguration delegate){
		this.delegate = delegate;
		toolDelegate = new ToolProxyConfiguration(delegate.getTools());
		treeDelegate = new TreeProxyConfiguration(delegate.getTrees());
	}
	
	@Override
	public void reset(){
		toolDelegate.reset();
		treeDelegate.reset();
	}
	
	@NonNull
	@Override
	public TreeProxyConfiguration getTrees(){
		return treeDelegate;
	}
	
	@NonNull
	@Override
	public ToolProxyConfiguration getTools(){
		return toolDelegate;
	}
	
	@NonNull
	@Override
	public IPlayerConfiguration getPlayer(){
		return delegate.getPlayer();
	}
	
	@NonNull
	@Override
	public IEnchantmentConfiguration getEnchantment(){
		return delegate.getEnchantment();
	}
	
	@Override
	@NonNull
	public SneakMode getSneakMode(){
		return delegate.getSneakMode();
	}
	
	@Override
	public boolean isBreakInCreative(){
		return delegate.isBreakInCreative();
	}
	
	@Override
	public boolean isLootInCreative(){
		return delegate.isLootInCreative();
	}
	
	@Override
	@NonNull
	public NotificationMode getNotificationMode(){
		return delegate.getNotificationMode();
	}
}
