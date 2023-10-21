package fr.rakambda.fallingtree.common.tree.exception;

public class TreeBreakingException extends Exception{
	@Deprecated
	public TreeBreakingException(Throwable throwable){
		super(throwable);
	}
	public TreeBreakingException(String message){
		super(message);
	}
}
