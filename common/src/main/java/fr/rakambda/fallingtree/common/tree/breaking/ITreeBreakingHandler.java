package fr.rakambda.fallingtree.common.tree.breaking;

import fr.rakambda.fallingtree.common.tree.IBreakAttemptResult;
import fr.rakambda.fallingtree.common.tree.Tree;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import org.jspecify.annotations.NonNull;

public interface ITreeBreakingHandler{
	@NonNull
	IBreakAttemptResult breakTree(boolean isCancellable, @NonNull IPlayer player, @NonNull Tree tree) throws BreakTreeTooBigException, BreakTreeTooSmallException;
}
