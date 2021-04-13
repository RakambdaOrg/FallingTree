package fr.raksrinana.fallingtree.forge.tree.builder;

public class AbortSearchException extends RuntimeException{
	public AbortSearchException(String reason){
		super(reason);
	}
}
