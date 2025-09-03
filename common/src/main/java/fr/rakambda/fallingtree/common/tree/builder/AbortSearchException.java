package fr.rakambda.fallingtree.common.tree.builder;

import fr.rakambda.fallingtree.common.wrapper.IComponent;
import org.jspecify.annotations.NonNull;

public abstract class AbortSearchException extends RuntimeException{
	public AbortSearchException(@NonNull String reason){
		super(reason);
	}
	
	@NonNull
	public abstract IComponent getComponent();
}
