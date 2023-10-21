package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;

/**
 * The result of a {@link TreeHandler#attemptTreeBreaking(ILevel, IPlayer, IBlockPos)}, whether it succeeded or not.
 * Failures are generally instances of {@link BreakAbortionCause}, where are succeeded attempts are instances of
 * {@link BreakTreeResult}.
 */
public interface IBreakAttemptResult{
	boolean shouldCancel();
}
