package fr.rakambda.fallingtree.fabric.gametest;

import fr.rakambda.fallingtree.fabric.FallingTree;
import fr.rakambda.fallingtree.gametest.FallingTreeGameTests;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class FabricGameTests implements ModInitializer{
	@Override
	public void onInitialize(){
		FallingTreeGameTests.testFunctions(FallingTree::getMod).forEach((name, function) ->
				Registry.register(BuiltInRegistries.TEST_FUNCTION, Identifier.fromNamespaceAndPath(FallingTreeGameTests.NAMESPACE, name), function));
	}
}
