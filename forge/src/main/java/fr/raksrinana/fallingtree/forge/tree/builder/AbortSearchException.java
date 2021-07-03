package fr.raksrinana.fallingtree.forge.tree.builder;

import net.minecraft.util.text.ITextComponent;

public abstract class AbortSearchException extends RuntimeException{
	public AbortSearchException(String reason){
		super(reason);
	}
	
	public abstract ITextComponent getComponent();
}
