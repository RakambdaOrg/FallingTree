package fr.raksrinana.fallingtree.fabric.tree.breaking;

import fr.raksrinana.fallingtree.fabric.tree.Tree;
import net.minecraft.world.entity.player.Player;

public interface ITreeBreakingHandler{
	boolean breakTree(Player player, Tree tree);
}
