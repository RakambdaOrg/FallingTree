package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import org.jetbrains.annotations.NotNull;

/**
 * Record that denotes that an attempt to break a tree with the given mode has
 * succeeded. Failures are instead denoted as {@link BreakAbortionCause}.
 *
 * @param shouldCancel Whether the event which triggered the query should be cancelled as a result of this result.
 * @param breakMode The mode with which the block was broken.
 */
public record BreakTreeResult(
		boolean shouldCancel,
		@NotNull BreakMode breakMode
) implements IBreakAttemptResult{
	@Override
	public boolean shouldCancel(){
		return this.shouldCancel;
	}
}
