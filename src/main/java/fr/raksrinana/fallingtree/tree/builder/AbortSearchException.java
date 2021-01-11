package fr.raksrinana.fallingtree.tree.builder;

import net.minecraft.block.Block;

public class AbortSearchException extends RuntimeException{
	public AbortSearchException(Block block){
		super("Found block " + block + " that isn't whitelisted");
	}
}
