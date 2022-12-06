package fr.rakambda.fallingtree.common.command;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ToggleCommand{
	public static final String FALLINGTREE_DISABLE_TAG = "fallingtree-disabled";
	
	private final FallingTreeCommon<?> mod;
	
	public int apply(@NotNull IPlayer player){
		if(player.getTags().contains(FALLINGTREE_DISABLE_TAG)){
			player.removeTag(FALLINGTREE_DISABLE_TAG);
			mod.notifyPlayer(player, mod.translate("fallingtree.command.toggle.enabled"));
		}
		else {
			player.addTag(FALLINGTREE_DISABLE_TAG);
			mod.notifyPlayer(player, mod.translate("fallingtree.command.toggle.disabled"));
		}
		return 1;
	}
}
