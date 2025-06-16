package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBlock;
import fr.rakambda.fallingtree.common.wrapper.IBlockEntity;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IComponent;
import fr.rakambda.fallingtree.common.wrapper.IItemStack;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
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
@ToString
public class BlockWrapper implements IBlock{
	@NotNull
	@Getter
	private final Block raw;
	
	@Override
	public boolean isAir(){
		return Blocks.AIR.equals(raw);
	}
	
	@Override
	public void playerDestroy(@NotNull ILevel level, @NotNull IPlayer player, @NotNull IBlockPos blockPos, @NotNull IBlockState blockState, @Nullable IBlockEntity blockEntity, @NotNull IItemStack itemStack, boolean dropResources){
		var entity = blockEntity == null ? null : (BlockEntity) blockEntity.getRaw();
		var realPlayer = (Player) player;
		
		// See Block.playerDestroy
		realPlayer.awardStat(Stats.BLOCK_MINED.get(raw));
		realPlayer.causeFoodExhaustion(0.005F);
		if(dropResources){
			Block.dropResources((BlockState) blockState.getRaw(), (Level) level.getRaw(), (BlockPos) blockPos.getRaw(), entity, realPlayer, (ItemStack) itemStack.getRaw());
		}
	}
	
	@NotNull
	@Override
	public IComponent getAsComponent(){
		return new ComponentWrapper(raw.getName());
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
