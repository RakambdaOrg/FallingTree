package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.mrcraftcod.fallingtree.common.wrapper.IItem;
import fr.mrcraftcod.fallingtree.common.wrapper.IItemStack;
import fr.mrcraftcod.fallingtree.common.wrapper.IPlayer;
import fr.raksrinana.fallingtree.fabric.FallingTree;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

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
	public int getChopperEnchantLevel(){
		return EnchantmentHelper.getItemEnchantmentLevel(FallingTree.CHOPPER_ENCHANTMENT, raw);
	}
}
