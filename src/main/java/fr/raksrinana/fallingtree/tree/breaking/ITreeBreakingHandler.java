package fr.raksrinana.fallingtree.tree.breaking;

import fr.raksrinana.fallingtree.tree.Tree;
import net.minecraftforge.event.world.BlockEvent;

public interface ITreeBreakingHandler{
	void breakTree(BlockEvent.BreakEvent event, Tree tree);
}
