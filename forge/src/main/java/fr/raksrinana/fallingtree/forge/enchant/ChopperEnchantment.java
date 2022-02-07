package fr.raksrinana.fallingtree.forge.enchant;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.forge.common.wrapper.ItemStackWrapper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class ChopperEnchantment extends Enchantment{
	private final FallingTreeCommon<?> mod;
	
	public ChopperEnchantment(FallingTreeCommon<?> mod){
		super(Rarity.COMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
		this.mod = mod;
	}
	
	@Override
	public int getMinCost(int i){
		return 1;
	}
	
	@Override
	public int getMaxCost(int i){
		return 10;
	}
	
	@Override
	public boolean isTradeable(){
		return !mod.getConfiguration().getEnchantment().isHideEnchant();
	}
	
	@Override
	public boolean isDiscoverable(){
		return !mod.getConfiguration().getEnchantment().isHideEnchant();
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack){
		return mod.isValidTool(new ItemStackWrapper(stack), false);
	}
}
