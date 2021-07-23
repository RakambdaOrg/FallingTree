package fr.raksrinana.fallingtree.forge.tree.builder;

import fr.raksrinana.fallingtree.forge.tree.TreePart;
import fr.raksrinana.fallingtree.forge.tree.builder.position.IPositionFetcher;
import fr.raksrinana.fallingtree.forge.utils.TreePartType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import java.util.Objects;

public record ToAnalyzePos(IPositionFetcher positionFetcher,
                           BlockPos parentPos,
                           Block parentBlock,
                           BlockPos checkPos,
                           Block checkBlock,
                           TreePartType treePartType,
                           int sequence)
		implements Comparable<ToAnalyzePos>{
	@Override
	public int compareTo(ToAnalyzePos o){
		return 0;
	}
	
	public TreePart toTreePart(){
		return new TreePart(checkPos(), treePartType(), sequence());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(!(o instanceof ToAnalyzePos that)){
			return false;
		}
		return Objects.equals(checkPos(), that.checkPos());
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(checkPos());
	}
}

