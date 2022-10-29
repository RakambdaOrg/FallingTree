package fr.rakambda.fallingtree.common.tree.builder;

import fr.rakambda.fallingtree.common.wrapper.IComponent;
import org.jetbrains.annotations.NotNull;

public abstract class AbortSearchException extends RuntimeException{
	public AbortSearchException(@NotNull String reason){
		super(reason);
	}
	
	@NotNull
	public abstract IComponent getComponent();
}
