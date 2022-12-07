package fr.rakambda.fallingtree.common.tree.builder;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import fr.rakambda.fallingtree.common.wrapper.IComponent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class AdjacentAbortSearchException extends AbortSearchException{
	@Getter
	private final IComponent component;
	
	public AdjacentAbortSearchException(@NotNull IBlock block, @NotNull FallingTreeCommon<?> common){
		super("Found block " + block.getRaw() + " that isn't allowed in the adjacent blocks");
		component = common.translate("chat.fallingtree.search_aborted.adjacent", block);
	}
}
