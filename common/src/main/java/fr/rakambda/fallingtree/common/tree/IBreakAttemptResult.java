package fr.rakambda.fallingtree.common.tree;

/**
 * The result of a {@link TreeHandler#breakTree()}, whether it succeeded or not.
 * Failures are instances of {@link AbortedResult}, where succeeded attempts are instances of {@link SuccessResult}.
 */
public sealed interface IBreakAttemptResult permits SuccessResult, AbortedResult{
	/**
	 * @return true if the break of the block hit by the player should be cancelled.
	 */
	boolean shouldCancel();
}
