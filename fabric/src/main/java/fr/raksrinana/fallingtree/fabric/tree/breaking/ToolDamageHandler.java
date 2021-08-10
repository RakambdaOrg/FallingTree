package fr.raksrinana.fallingtree.fabric.tree.breaking;

import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;

public class ToolDamageHandler{
	private final ItemStack tool;
	private final double damageMultiplicand;
	private final boolean preserve;
	private final int maxDurabilityTaken;
	@Getter
	private final int maxBreakCount;
	
	public ToolDamageHandler(ItemStack tool, double damageMultiplicand, boolean preserve, int breakableCount){
		this.tool = tool;
		this.damageMultiplicand = damageMultiplicand;
		this.preserve = preserve;
		
		maxBreakCount = damageMultiplicand == 0 ? breakableCount : (int) Math.floor(getToolDurability() / damageMultiplicand);
		maxDurabilityTaken = getDamage(maxBreakCount);
	}
	
	private int getDamage(long count){
		if(damageMultiplicand == 0){
			return 1;
		}
		var rawDamage = count * damageMultiplicand;
		
		return (int) switch(config.getTools().getDamageRounding()){
			case ROUND_DOWN -> Math.floor(rawDamage);
			case ROUND_UP -> Math.ceil(rawDamage);
			case ROUNDING -> Math.round(rawDamage);
		};
	}
	
	public boolean shouldPreserveTool(){
		if(!preserve){
			return false;
		}
		return getToolDurability() <= maxDurabilityTaken;
	}
	
	public int getActualDamage(int brokenCount){
		return brokenCount == maxBreakCount ? maxDurabilityTaken : getDamage(brokenCount);
	}
	
	private int getToolDurability(){
		return tool.getMaxDamage() - tool.getDamageValue();
	}
}
