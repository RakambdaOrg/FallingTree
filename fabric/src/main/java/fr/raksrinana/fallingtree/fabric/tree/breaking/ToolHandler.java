package fr.raksrinana.fallingtree.fabric.tree.breaking;

import net.minecraft.world.item.ItemStack;

public class ToolHandler{
	private final ItemStack tool;
	private final int damageMultiplicand;
	private final boolean preserve;
	private final int durabilityTaken;
	private final int breakCount;
	
	public ToolHandler(ItemStack tool, int damageMultiplicand, boolean preserve, int breakableCount){
		this.tool = tool;
		this.damageMultiplicand = damageMultiplicand;
		this.preserve = preserve;
		
		this.durabilityTaken = 0;
		this.breakCount = 0;
	}
	
	public boolean shouldPreserveTool(){
		if(!preserve){
			return false;
		}
		return getToolDurability() <= durabilityTaken;
	}
	
	public long getMaxBreakable(){
		return breakCount;
	}
	
	private int getToolDurability(){
		return tool.getMaxDamage() - tool.getDamageValue();
	}
	
	public boolean canBreakCount(int count){
		if(){
			var rawWeightedUsesLeft = damageMultiplicand == 0 ? (toolUsesLeft - 1) : ((1d * toolUsesLeft) / damageMultiplicand);
		}
		return false;
	}
}
