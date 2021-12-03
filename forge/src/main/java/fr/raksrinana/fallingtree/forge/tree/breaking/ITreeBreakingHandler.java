package fr.raksrinana.fallingtree.forge.tree.breaking;

import fr.raksrinana.fallingtree.forge.tree.Tree;
import net.minecraftforge.event.world.BlockEvent;

public interface ITreeBreakingHandler{
	void breakTree(BlockEvent.BreakEvent event, Tree tree) throws BreakTreeTooBigException;
}
