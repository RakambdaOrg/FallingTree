package fr.mrcraftcod.fallingtree.common.tree.breaking;

import fr.mrcraftcod.fallingtree.common.tree.Tree;
import fr.mrcraftcod.fallingtree.common.wrapper.IPlayer;
import org.jetbrains.annotations.NotNull;

public interface ITreeBreakingHandler{
	boolean breakTree(@NotNull IPlayer player, @NotNull Tree tree) throws BreakTreeTooBigException;
}
