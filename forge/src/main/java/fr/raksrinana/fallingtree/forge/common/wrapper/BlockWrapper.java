package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.mrcraftcod.fallingtree.common.wrapper.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class BlockWrapper implements IBlock{
	@NotNull
	@Getter
	private final Block raw;
	
	@Override
	public boolean isAir(){
		return Blocks.AIR.equals(raw);
	}
	
	@Override
	public void playerDestroy(@NotNull ILevel level, @NotNull IPlayer player, @NotNull IBlockPos blockPos, @NotNull IBlockState blockState, @Nullable IBlockEntity blockEntity, @NotNull IItemStack itemStack){
		var entity = blockEntity == null ? null : (BlockEntity) blockEntity.getRaw();
		raw.playerDestroy((Level) level.getRaw(), (Player) player.getRaw(), (BlockPos) blockPos.getRaw(), (BlockState) blockState.getRaw(), entity, (ItemStack) itemStack.getRaw());
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof IBlock block)){
			return false;
		}
		return raw.equals(block.getRaw());
	}
	
	@Override
	public int hashCode(){
		return raw.hashCode();
	}
}
