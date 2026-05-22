package fr.rakambda.fallingtree.common.tree.builder;

import fr.rakambda.fallingtree.common.tree.TreePart;
import fr.rakambda.fallingtree.common.tree.TreePartType;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import org.jspecify.annotations.NonNull;
import java.util.LinkedHashSet;
import java.util.Set;
import static java.util.Objects.isNull;

public class MutableTree extends ImmutableTree{
	public MutableTree(@NonNull ILevel level, @NonNull IBlockPos hitPos){
		this(level, hitPos, new LinkedHashSet<>());
	}
	
	public MutableTree(@NonNull ILevel level, @NonNull IBlockPos hitPos, @NonNull Set<TreePart> parts){
		super(level, hitPos, parts);
	}
	
	public void addPart(@NonNull TreePart treePart){
		getParts().add(treePart);
		getPartCounts().compute(treePart.treePartType(), (key, value) -> {
			if(isNull(value)){
				return 1;
			}
			return value + 1;
		});
	}
	
	public void removePartsHigherThan(int y, @NonNull TreePartType partType){
		getParts().removeIf(part -> {
			if(part.treePartType() != partType || part.blockPos().getY() <= y){
				return false;
			}
			getPartCounts().computeIfPresent(partType, (type, count) -> Math.max(0, count - 1));
			onPartRemoved(part);
			return true;
		});
	}
	
	public int getSize(){
		return getPartCounts().values().stream().mapToInt(i -> i).sum();
	}
	
	private void onPartRemoved(@NonNull TreePart removedTreePart){
		getTopMostLogCache().invalidateIfEqual(removedTreePart);
		getBottomMostLogCache().invalidateIfEqual(removedTreePart);
		getStartLogCache().invalidateIfEqual(removedTreePart);
	}
	
	@NonNull
	public ImmutableTree asImmutableTree(){
		return new ImmutableTree(getLevel(), getHitPos(), getParts(), getPartCounts(), getTopMostLogCache(), getBottomMostLogCache(), getStartLogCache());
	}
}
