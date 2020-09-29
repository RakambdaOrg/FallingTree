package fr.raksrinana.fallingtree.utils;

import fr.raksrinana.fallingtree.tree.TreePart;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import java.util.Objects;

public class ToAnalyzePos implements Comparable<ToAnalyzePos>{
	private final BlockPos parentPos;
	private final Block parentBlock;
	private final BlockPos checkPos;
	private final Block checkBlock;
	private final TreePartType treePartType;
	private final int sequence;
	
	public ToAnalyzePos(BlockPos parentPos, Block parentBlock, BlockPos checkPos, Block checkBlock, TreePartType treePartType, int sequence){
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
