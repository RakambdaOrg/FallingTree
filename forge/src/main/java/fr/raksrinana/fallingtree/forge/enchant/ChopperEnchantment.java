package fr.raksrinana.fallingtree.forge.enchant;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class ChopperEnchantment extends Enchantment{
	private final FallingTreeCommon<?> mod;
	
	public ChopperEnchantment(FallingTreeCommon<?> mod){
		super(Rarity.COMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
		this.mod = mod;
	}
	
	@Override
	public boolean canEnchant(@NotNull ItemStack itemStack){
		return true;
	}
	
	@Override
	public boolean isDiscoverable(){
		return !mod.getConfiguration().isOnlyEnchantBook();
	}
}
