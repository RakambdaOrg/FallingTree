package fr.rakambda.fallingtree.fabric.event;

import com.mojang.brigadier.CommandDispatcher;
import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.command.ToggleCommand;
import fr.rakambda.fallingtree.fabric.common.wrapper.PlayerWrapper;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import static net.minecraft.commands.Commands.literal;

@RequiredArgsConstructor
public class ServerCommandRegistrationListener implements CommandRegistrationCallback{
	private final FallingTreeCommon<?> mod;
	
	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment){
		var toggleCommand = new ToggleCommand(mod);
		
		dispatcher.register(
				literal("fallingtree")
						.then(literal("toggle")
								.executes(ctx -> toggleCommand.apply(new PlayerWrapper(ctx.getSource().getPlayerOrException()))))
		);
	}
}
