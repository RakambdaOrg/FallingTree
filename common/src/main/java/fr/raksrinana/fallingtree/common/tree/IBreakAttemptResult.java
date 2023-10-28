package fr.raksrinana.fallingtree.common.tree;

import fr.raksrinana.fallingtree.common.wrapper.IBlockPos;
import fr.raksrinana.fallingtree.common.wrapper.ILevel;
import fr.raksrinana.fallingtree.common.wrapper.IPlayer;

/**
 * The result of a {@link TreeHandler#breakTree(ILevel, IPlayer, IBlockPos)}, whether it succeeded or not.
 * Failures are generally instances of {@link AbortedResult}, where are succeeded attempts are instances of
 * {@link BreakTreeResult}.
 */
public sealed interface IBreakAttemptResult permits BreakTreeResult, AbortedResult {
    boolean shouldCancel();
}
