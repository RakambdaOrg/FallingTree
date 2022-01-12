package fr.mrcraftcod.fallingtree.common.tree.builder;

import fr.mrcraftcod.fallingtree.common.wrapper.IComponent;
import org.jetbrains.annotations.NotNull;

public abstract class AbortSearchException extends RuntimeException{
	public AbortSearchException(@NotNull String reason){
		super(reason);
	}
	
	@NotNull
	public abstract IComponent getComponent();
}
