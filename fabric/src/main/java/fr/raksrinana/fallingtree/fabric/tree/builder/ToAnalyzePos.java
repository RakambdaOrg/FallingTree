package fr.raksrinana.fallingtree.fabric.tree.builder;

import fr.raksrinana.fallingtree.fabric.tree.TreePart;
import fr.raksrinana.fallingtree.fabric.tree.builder.position.IPositionFetcher;
import fr.raksrinana.fallingtree.fabric.utils.TreePartType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import java.util.Objects;

public class ToAnalyzePos implements Comparable<ToAnalyzePos>{
	private final IPositionFetcher positionFetcher;
	private final BlockPos parentPos;
	private final Block parentBlock;
	private final BlockPos checkPos;
	private final Block checkBlock;
	private final TreePartType treePartType;
	private final int sequence;
	
	public ToAnalyzePos(IPositionFetcher positionFetcher, BlockPos parentPos, Block parentBlock, BlockPos checkPos, Block checkBlock, TreePartType treePartType, int sequence){
		this.positionFetcher = positionFetcher;
		this.parentPos = parentPos;
		this.parentBlock = parentBlock;
		this.checkPos = checkPos;
		this.checkBlock = checkBlock;
		this.treePartType = treePartType;
		this.sequence = sequence;
	}
	
	@Override
	public int compareTo(ToAnalyzePos o){
		return 0;
	}
	
	public TreePart toTreePart(){
		return new TreePart(getCheckPos(), getTreePartType(), sequence);
	}
	
	public Block getCheckBlock(){
		return checkBlock;
	}
	
	public BlockPos getParentPos(){
		return parentPos;
	}
	
	public Block getParentBlock(){
		return parentBlock;
	}
	
	public BlockPos getCheckPos(){
		return checkPos;
	}
	
	public IPositionFetcher getPositionFetcher(){
		return positionFetcher;
	}
	
	public TreePartType getTreePartType(){
		return treePartType;
	}
	
	public int getSequence(){
		return sequence;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(!(o instanceof ToAnalyzePos)){
			return false;
		}
		ToAnalyzePos that = (ToAnalyzePos) o;
		return Objects.equals(getCheckPos(), that.getCheckPos());
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(getCheckPos());
	}
}
