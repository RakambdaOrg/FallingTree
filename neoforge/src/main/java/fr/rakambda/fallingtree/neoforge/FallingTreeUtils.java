package fr.rakambda.fallingtree.neoforge;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class FallingTreeUtils{
	@NonNull
	public static Identifier id(@NonNull String name){
		return Identifier.fromNamespaceAndPath(FallingTree.MOD_ID, name);
	}
	
	@NonNull
	public static Identifier idExternal(@NonNull String fullName){
		return Identifier.parse(fullName);
	}
}
