package fr.raksrinana.fallingtree.fabric.tree.breaking;

import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import static fr.raksrinana.fallingtree.fabric.FallingTree.config;

public class ToolDamageHandler{
	private final ItemStack tool;
	private final double damageMultiplicand;
	private final int maxDurabilityTaken;
	@Getter
	private final int maxBreakCount;
	
	public ToolDamageHandler(ItemStack tool, double damageMultiplicand, boolean preserve, int breakableCount){
		this.tool = tool;
		this.damageMultiplicand = damageMultiplicand;
		
		var breakCount = damageMultiplicand == 0 ? config.getTrees().getMaxSize() : (int) Math.floor(getToolDurability() / damageMultiplicand);
		if(preserve && breakCount <= breakableCount){
			breakCount--;
		}
		
		maxBreakCount = breakCount;
		maxDurabilityTaken = getDamage(maxBreakCount);
	}
	
	private int getDamage(long count){
		if(Double.compare(damageMultiplicand, 0) <= 0){
			return 1;
		}
		var rawDamage = count * damageMultiplicand;
		
		return (int) switch(config.getTools().getDamageRounding()){
			case ROUND_DOWN -> Math.floor(rawDamage);
			case ROUND_UP -> Math.ceil(rawDamage);
			case ROUNDING -> Math.round(rawDamage);
			case PROBABILISTIC -> getProbabilisticDamage(rawDamage);
		};
	}

	private int getProbabilisticDamage(double rawDamage) {
		var damage = Math.floor(rawDamage);
		var finalDamage = (int) damage;
		var probability = rawDamage - damage;
		if (Math.random() < probability) {
			finalDamage++;
		}
		return finalDamage;
	}
	
	public int getActualDamage(int brokenCount){
		return brokenCount == maxBreakCount ? maxDurabilityTaken : Math.min(maxDurabilityTaken, getDamage(brokenCount));
	}
	
	private int getToolDurability(){
		return tool.getMaxDamage() - tool.getDamageValue();
	}
}
