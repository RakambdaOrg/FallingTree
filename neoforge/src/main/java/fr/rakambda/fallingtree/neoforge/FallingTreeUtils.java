package fr.rakambda.fallingtree.neoforge;

import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.NonNull;

public class FallingTreeUtils{
	@NonNull
	public static ResourceLocation id(@NonNull String name){
		return ResourceLocation.fromNamespaceAndPath(FallingTree.MOD_ID, name);
	}
	
	@NonNull
	public static ResourceLocation idExternal(@NonNull String fullName){
		return ResourceLocation.parse(fullName);
	}
}
