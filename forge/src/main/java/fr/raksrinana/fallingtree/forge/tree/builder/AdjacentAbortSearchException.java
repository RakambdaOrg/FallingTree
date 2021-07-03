package fr.raksrinana.fallingtree.forge.tree.builder;

import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AdjacentAbortSearchException extends AbortSearchException{
	private final Block block;
	
	public AdjacentAbortSearchException(Block block){
		super("Found block " + block + " that isn't whitelisted in the adjacent blocks");
		this.block = block;
	}
	
	@Override
	public ITextComponent getComponent(){
		return new TranslationTextComponent("chat.fallingtree.search_aborted.adjacent", block);
	}
}
