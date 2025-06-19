package fr.rakambda.fallingtree.forge.event;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.command.ToggleCommand;
import fr.rakambda.fallingtree.forge.common.wrapper.PlayerWrapper;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.event.RegisterCommandsEvent;
import static net.minecraft.commands.Commands.literal;

@RequiredArgsConstructor
public class ServerCommandRegistrationListener{
	private final FallingTreeCommon<?> mod;
	
	public void onRegisterCommands(RegisterCommandsEvent event){
		var toggleCommand = new ToggleCommand(mod);
		
		event.getDispatcher().register(
				literal("fallingtree")
						.then(literal("toggle")
								.executes(ctx -> toggleCommand.apply(new PlayerWrapper(ctx.getSource().getPlayerOrException()))))
		);
	}
}
