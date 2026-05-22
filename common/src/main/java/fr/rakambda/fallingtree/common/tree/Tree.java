package fr.rakambda.fallingtree.common.tree;

import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import org.jspecify.annotations.NonNull;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface Tree{
	
	int getBreakableCount();
	
	@NonNull Optional<TreePart> getLastSequencePart();
	
	@NonNull Optional<TreePart> getLastSequenceLogPart();
	
	@NonNull Collection<TreePart> getBreakableLogs();
	
	@NonNull Collection<TreePart> getBreakableParts();
	
	int getLogCount();
	
	@NonNull Optional<IBlockPos> getTopMostLog();
	
	@NonNull Optional<IBlockPos> getBottomMostLog();
	
	@NonNull Optional<IBlockPos> getTopMostPart();
	
	@NonNull Collection<TreePart> getNetherWarts();
	
	@NonNull Collection<TreePart> getMangroveRoots();
	
	@NonNull Optional<TreePart> getStart();
	
	@NonNull Stream<TreePart> getPartsStream();
	
	ILevel getLevel();
	
	IBlockPos getHitPos();
}

