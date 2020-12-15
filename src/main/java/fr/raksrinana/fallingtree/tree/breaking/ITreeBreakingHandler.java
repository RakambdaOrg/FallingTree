package fr.raksrinana.fallingtree.tree.breaking;

import fr.raksrinana.fallingtree.tree.Tree;
import net.minecraft.entity.player.PlayerEntity;

public interface ITreeBreakingHandler{
	boolean breakTree(PlayerEntity player, Tree tree);
}
