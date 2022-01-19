package fr.raksrinana.fallingtree.common.tree.breaking;

import fr.raksrinana.fallingtree.common.tree.Tree;
import fr.raksrinana.fallingtree.common.wrapper.IPlayer;
import org.jetbrains.annotations.NotNull;

public interface ITreeBreakingHandler{
	boolean breakTree(@NotNull IPlayer player, @NotNull Tree tree) throws BreakTreeTooBigException;
}
