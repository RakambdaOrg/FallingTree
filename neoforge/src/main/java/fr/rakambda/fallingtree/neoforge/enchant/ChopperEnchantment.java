package fr.rakambda.fallingtree.neoforge.enchant;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.enchant.IFallingTreeEnchantment;
import fr.rakambda.fallingtree.neoforge.common.wrapper.ItemStackWrapper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public class ChopperEnchantment extends Enchantment implements IFallingTreeEnchantment{
	private final FallingTreeCommon<?> mod;
	private final BreakMode breakMode;
	
	public ChopperEnchantment(@NotNull FallingTreeCommon<?> mod, @Nullable BreakMode breakMode){
		super(Enchantment.definition(ItemTags.MINING_ENCHANTABLE, 1, 1, constantCost(1), constantCost(10), 1, EquipmentSlot.MAINHAND));
		this.mod = mod;
		this.breakMode = breakMode;
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
