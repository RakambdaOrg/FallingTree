package fr.raksrinana.fallingtree.tree.builder;

public class AbortSearchException extends RuntimeException{
	public AbortSearchException(String reason){
		super(reason);
	}
}
