package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.mrcraftcod.fallingtree.common.wrapper.IBlockState;
import fr.mrcraftcod.fallingtree.common.wrapper.IItem;
import fr.mrcraftcod.fallingtree.common.wrapper.IItemStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ItemWrapper implements IItem{
	@NotNull
	@Getter
	private final Item raw;
	
	@Override
	public boolean isAir(){
		return Items.AIR.equals(raw);
	}
	
	@Override
	public float getDestroySpeed(@NotNull IItemStack itemStack, @NotNull IBlockState blockState){
		return raw.getDestroySpeed((ItemStack) itemStack.getRaw(), (BlockState) blockState.getRaw());
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof IItem item)){
			return false;
		}
		return raw.equals(item.getRaw());
	}
	
	@Override
	public int hashCode(){
		return raw.hashCode();
	}
}
