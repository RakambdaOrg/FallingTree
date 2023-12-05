package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IEnchantment;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import fr.rakambda.fallingtree.common.wrapper.IItemStack;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import fr.rakambda.fallingtree.forge.compat.TetraCompat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@ToString
public class ItemStackWrapper implements IItemStack{
	
	@NotNull
	@Getter
	private final ItemStack raw;
	
	@Override
	public boolean isEmpty(){
		return raw.isEmpty();
	}
	
	@Override
	public boolean isDamageable(){
		return raw.isDamageableItem();
	}
	
	@Override
	public int getDamage(){
		return raw.getDamageValue();
	}
	
	@Override
	public int getMaxDamage(){
		return raw.getMaxDamage();
	}
	
	@Override
	public void damage(int amount, @NotNull IPlayer player){
		raw.hurtAndBreak(amount, (Player) player.getRaw(), entity -> {});
		TetraCompat.tickHoningProgression(this, player);
	}

	
	@Override
	@NotNull
	public IItem getItem(){
		return new ItemWrapper(raw.getItem());
	}
	
	@Override
	public int getEnchantLevel(@Nullable IEnchantment enchantment){
		if(enchantment == null){
			return 0;
		}
		return raw.getEnchantmentLevel((Enchantment) enchantment.getRaw());
	}
	
	@Override
	public boolean hasOneOfEnchantAtLeast(@NotNull Collection<IEnchantment> enchantments, int minLevel){
		var itemEnchantments = EnchantmentHelper.getEnchantments(raw);
		for(var enchantment : enchantments){
			var key = (Enchantment) enchantment.getRaw();
			if(itemEnchantments.containsKey(key)){
				if(itemEnchantments.get(key) >= minLevel){
					return true;
				}
			}
		}
		return false;
	}
	
	@NotNull
	public Optional<IEnchantment> getAnyEnchant(@NotNull Collection<IEnchantment> enchantments){
		var itemEnchantments = EnchantmentHelper.getEnchantments(raw);
		for(var enchantment : enchantments){
			var key = (Enchantment) enchantment.getRaw();
			if(itemEnchantments.containsKey(key)){
				return Optional.of(enchantment);
			}
		}
		return Optional.empty();
	}
	
	@Override
	public boolean canPerformAxeAction(){
		return raw.canPerformAction(ToolActions.AXE_DIG);
	}
}
