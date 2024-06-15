package fr.rakambda.fallingtree.fabric;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FallingTreeUtils{
	@NotNull
	public static ResourceLocation id(@NotNull String name){
		return ResourceLocation.fromNamespaceAndPath(FallingTree.MOD_ID, name);
	}
	
	@NotNull
	public static ResourceLocation idExternal(@NotNull String fullName){
		return ResourceLocation.parse(fullName);
	}
}
