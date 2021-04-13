package fr.raksrinana.fallingtree.fabric.tree.builder;

public class AbortSearchException extends RuntimeException{
	public AbortSearchException(String reason){
		super(reason);
	}
}
