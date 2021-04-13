package fr.raksrinana.fallingtree.fabric.tree.breaking;

import fr.raksrinana.fallingtree.fabric.tree.Tree;
import net.minecraft.entity.player.PlayerEntity;

public interface ITreeBreakingHandler{
	boolean breakTree(PlayerEntity player, Tree tree);
}
