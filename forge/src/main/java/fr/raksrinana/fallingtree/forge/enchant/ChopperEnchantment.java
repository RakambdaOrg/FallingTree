package fr.raksrinana.fallingtree.forge.enchant;

import fr.raksrinana.fallingtree.forge.FallingTree;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ChopperEnchantment extends Enchantment{
	public ChopperEnchantment(){
		super(Rarity.COMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
		setRegistryName(new ResourceLocation(FallingTree.MOD_ID, "chopper"));
	}
	
	public boolean canEnchant(ItemStack itemStack){
		return true;
	}
}
