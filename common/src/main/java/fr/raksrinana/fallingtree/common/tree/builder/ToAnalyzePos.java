package fr.raksrinana.fallingtree.common.tree.builder;

import fr.raksrinana.fallingtree.common.tree.TreePart;
import fr.raksrinana.fallingtree.common.tree.TreePartType;
import fr.raksrinana.fallingtree.common.tree.builder.position.IPositionFetcher;
import fr.raksrinana.fallingtree.common.wrapper.IBlock;
import fr.raksrinana.fallingtree.common.wrapper.IBlockPos;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public record ToAnalyzePos(@NotNull IPositionFetcher positionFetcher,
                           @NotNull IBlockPos parentPos,
                           @NotNull IBlock parentBlock,
                           @NotNull IBlockPos checkPos,
                           @NotNull IBlock checkBlock,
                           @NotNull TreePartType treePartType,
                           int sequence)
		implements Comparable<ToAnalyzePos>{
	
	@Override
	public int compareTo(@NotNull ToAnalyzePos o){
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
		return checkPos().hashCode();
	}
}
