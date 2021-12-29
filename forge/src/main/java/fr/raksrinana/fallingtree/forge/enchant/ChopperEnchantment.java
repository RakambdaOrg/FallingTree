package fr.raksrinana.fallingtree.forge.enchant;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ChopperEnchantment extends Enchantment{
	public ChopperEnchantment(){
		super(Rarity.COMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	public boolean canEnchant(ItemStack itemStack){
		return true;
	}
}
