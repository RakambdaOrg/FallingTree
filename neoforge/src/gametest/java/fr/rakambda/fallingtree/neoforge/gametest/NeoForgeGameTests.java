package fr.rakambda.fallingtree.neoforge.gametest;

import fr.rakambda.fallingtree.gametest.FallingTreeGameTests;
import fr.rakambda.fallingtree.gametest.ProtectionSimulator;
import fr.rakambda.fallingtree.neoforge.FallingTree;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

@Mod(FallingTreeGameTests.NAMESPACE)
public class NeoForgeGameTests{
	public NeoForgeGameTests(@NonNull IEventBus modEventBus){
		var testFunctions = DeferredRegister.create(Registries.TEST_FUNCTION, FallingTreeGameTests.NAMESPACE);
		FallingTreeGameTests.testFunctions(FallingTree::getMod).forEach((name, function) -> testFunctions.register(name, () -> function));
		testFunctions.register(modEventBus);

		// Like a claim mod, with the default priority
		NeoForge.EVENT_BUS.addListener(BreakBlockEvent.class, event -> {
			if(ProtectionSimulator.isDenied(event.getPos())){
				event.setCanceled(true);
			}
		});
	}
}
