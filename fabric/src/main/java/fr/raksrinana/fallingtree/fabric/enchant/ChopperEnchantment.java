package fr.raksrinana.fallingtree.fabric.enchant;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.config.enums.BreakMode;
import fr.raksrinana.fallingtree.common.enchant.IFallingTreeEnchantment;
import fr.raksrinana.fallingtree.fabric.common.wrapper.ItemStackWrapper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public class ChopperEnchantment extends Enchantment implements IFallingTreeEnchantment{
	private final FallingTreeCommon<?> mod;
	private final BreakMode breakMode;
	
	public ChopperEnchantment(@NotNull FallingTreeCommon<?> mod, @Nullable BreakMode breakMode){
		super(Rarity.COMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
		this.mod = mod;
		this.breakMode = breakMode;
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
	public boolean canEnchant(@NotNull ItemStack stack){
		return mod.isValidTool(new ItemStackWrapper(stack));
	}
	
	@Override
	protected boolean checkCompatibility(@NotNull Enchantment enchantment){
		return super.checkCompatibility(enchantment) && !(enchantment instanceof IFallingTreeEnchantment);
	}
	
	@Override
	@NotNull
	public Optional<BreakMode> getBreakMode(){
		return Optional.ofNullable(breakMode);
	}
}
