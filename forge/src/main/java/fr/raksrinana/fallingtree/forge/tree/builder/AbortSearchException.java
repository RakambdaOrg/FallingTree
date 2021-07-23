package fr.raksrinana.fallingtree.forge.tree.builder;

import net.minecraft.network.chat.Component;

public abstract class AbortSearchException extends RuntimeException{
	public AbortSearchException(String reason){
		super(reason);
	}
	
	public abstract Component getComponent();
}
