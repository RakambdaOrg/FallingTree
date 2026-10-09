package fr.rakambda.fallingtree.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import org.jspecify.annotations.NonNull;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simulates a protection/claim mod: each loader's test mod registers a regular (normal priority, default phase) block break listener that vetoes breaking the positions
 * denied here.
 * <p>
 * FallingTree must let such mods veto the break of the hit block before cutting the tree, and must ask them for every other log it breaks.
 */
public final class ProtectionSimulator{
	private static final Set<BlockPos> DENIED_POSITIONS = ConcurrentHashMap.newKeySet();

	private ProtectionSimulator(){
	}

	public static void deny(@NonNull GameTestHelper helper, @NonNull BlockPos relativePos){
		DENIED_POSITIONS.add(helper.absolutePos(relativePos).immutable());
	}

	public static boolean isDenied(@NonNull BlockPos absolutePos){
		return DENIED_POSITIONS.contains(absolutePos);
	}

	public static void reset(){
		DENIED_POSITIONS.clear();
	}
}
