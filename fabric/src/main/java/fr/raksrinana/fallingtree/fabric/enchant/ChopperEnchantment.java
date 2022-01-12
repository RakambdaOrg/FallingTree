package fr.raksrinana.fallingtree.fabric.enchant;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class ChopperEnchantment extends Enchantment{
	public ChopperEnchantment(){
		super(Rarity.COMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	@Override
	public boolean canEnchant(@NotNull ItemStack itemStack){
		return true;
	}
}
