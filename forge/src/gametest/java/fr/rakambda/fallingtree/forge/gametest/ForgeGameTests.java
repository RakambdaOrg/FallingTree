package fr.rakambda.fallingtree.forge.gametest;

import fr.rakambda.fallingtree.forge.FallingTree;
import fr.rakambda.fallingtree.gametest.FallingTreeGameTests;
import fr.rakambda.fallingtree.gametest.ProtectionSimulator;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

@Mod(FallingTreeGameTests.NAMESPACE)
public class ForgeGameTests{
	public ForgeGameTests(FMLJavaModLoadingContext context){
		var testFunctions = DeferredRegister.create(Registries.TEST_FUNCTION, FallingTreeGameTests.NAMESPACE);
		FallingTreeGameTests.testFunctions(FallingTree::getMod).forEach((name, function) -> testFunctions.register(name, () -> function));
		testFunctions.register(context.getModBusGroup());

		// Like a claim mod, with the default priority
		BlockEvent.BreakEvent.BUS.addListener(event -> {
			if(ProtectionSimulator.isDenied(event.getPos())){
				event.setResult(Result.DENY);
				return true;
			}
			return false;
		});
	}
}
