package fr.rakambda.fallingtree.forge.gametest;

import fr.rakambda.fallingtree.forge.FallingTree;
import fr.rakambda.fallingtree.gametest.FallingTreeGameTests;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

@Mod(FallingTreeGameTests.NAMESPACE)
public class ForgeGameTests{
	public ForgeGameTests(FMLJavaModLoadingContext context){
		var testFunctions = DeferredRegister.create(Registries.TEST_FUNCTION, FallingTreeGameTests.NAMESPACE);
		FallingTreeGameTests.testFunctions(FallingTree::getMod).forEach((name, function) -> testFunctions.register(name, () -> function));
		testFunctions.register(context.getModBusGroup());
	}
}
