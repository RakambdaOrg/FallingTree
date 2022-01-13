package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.mrcraftcod.fallingtree.common.wrapper.IEnchantment;
import fr.mrcraftcod.fallingtree.common.wrapper.IItem;
import fr.mrcraftcod.fallingtree.common.wrapper.IItemStack;
import fr.mrcraftcod.fallingtree.common.wrapper.IPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class ItemStackWrapper implements IItemStack{
	@NotNull
	@Getter
	private final ItemStack raw;
	
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
		return EnchantmentHelper.getItemEnchantmentLevel((Enchantment) enchantment.getRaw(), raw);
	}
}
