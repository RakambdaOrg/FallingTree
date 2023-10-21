package fr.rakambda.fallingtree.common.tree;

public enum BreakAbortionCause implements IBreakAttemptResult {
	NOT_SERVER(false),
	NOT_ENABLED(false),
	REQUIRED_TOOL_ABSENT(true),
	INVALID_PLAYER_STATE(false),
	NO_SUCH_TREE(false),
	TREE_TOO_BIG_SCAN(false),
	TREE_TOO_BIG_BREAK(false);
	
	private final boolean cancel;
	
	BreakAbortionCause(boolean cancel) {
		this.cancel = cancel;
	}
	
	@Override
	public boolean shouldCancel(){
		return this.cancel;
	}
}
