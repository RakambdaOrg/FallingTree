package fr.raksrinana.fallingtree.fabric.common;

import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.leaf.LeafBreakingHandler;
import fr.raksrinana.fallingtree.common.wrapper.*;
import fr.raksrinana.fallingtree.fabric.common.wrapper.BlockWrapper;
import fr.raksrinana.fallingtree.fabric.common.wrapper.ComponentWrapper;
import fr.raksrinana.fallingtree.fabric.common.wrapper.EnchantmentWrapper;
import fr.raksrinana.fallingtree.fabric.common.wrapper.ItemWrapper;
import fr.raksrinana.fallingtree.fabric.enchant.ChopperEnchantment;
import fr.raksrinana.fallingtree.fabric.event.BlockBreakListener;
import fr.raksrinana.fallingtree.fabric.event.LeafBreakingListener;
import lombok.Getter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static fr.raksrinana.fallingtree.fabric.FallingTree.MOD_ID;
import static java.util.stream.Stream.empty;

public class FallingTreeCommonsImpl extends FallingTreeCommon<Direction>{
	@Getter
	private final LeafBreakingHandler leafBreakingHandler;
	@Getter
	private IEnchantment chopperEnchantment;
	
	public FallingTreeCommonsImpl(){
		leafBreakingHandler = new LeafBreakingHandler(this);
	}
	
	@Override
	@NotNull
	public IComponent translate(@NotNull String key, Object... objects){
		return new ComponentWrapper(new TranslatableComponent(key, objects));
	}
	
	@Override
	@NotNull
	public Stream<IBlock> getBlock(@NotNull String name){
		try{
			var isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			var identifier = new ResourceLocation(name);
			if(isTag){
				return TagFactory.BLOCK.create(identifier).getValues().stream().map(BlockWrapper::new);
			}
			return Stream.of(Registry.BLOCK.get(identifier)).map(BlockWrapper::new);
		}
		catch(Exception e){
			return empty();
		}
	}
	
	@Override
	@NotNull
	public Stream<IItem> getItem(@NotNull String name){
		try{
			var isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			var identifier = new ResourceLocation(name);
			if(isTag){
				return TagFactory.ITEM.create(identifier).getValues().stream().map(ItemWrapper::new);
			}
			return Stream.of(Registry.ITEM.get(identifier)).map(ItemWrapper::new);
		}
		catch(Exception e){
			return empty();
		}
	}
	
	@Override
	public boolean isLeafBlock(@NotNull IBlock block){
		var isAllowedBlock = BlockTags.LEAVES.contains((Block) block.getRaw())
		                     || getConfiguration().getTrees().getAllowedLeaveBlocks(this).stream().anyMatch(leaf -> leaf.equals(block));
		if(isAllowedBlock){
			var isDeniedBlock = getConfiguration().getTrees().getDeniedLeaveBlocks(this).stream().anyMatch(leaf -> leaf.equals(block));
			return !isDeniedBlock;
		}
		return false;
	}
	
	@Override
	public boolean isLogBlock(@NotNull IBlock block){
		var isAllowedBlock = getConfiguration().getTrees().getDefaultLogsBlocks(this).stream().anyMatch(log -> log.equals(block))
		                     || getConfiguration().getTrees().getAllowedLogBlocks(this).stream().anyMatch(log -> log.equals(block));
		if(isAllowedBlock){
			var isDeniedBlock = getConfiguration().getTrees().getDeniedLogBlocks(this).stream().anyMatch(log -> log.equals(block));
			return !isDeniedBlock;
		}
		return false;
	}
	
	@Override
	@NotNull
	public Set<IBlock> getAllNonStrippedLogsBlocks(){
		return BlockTags.LOGS.getValues().stream()
				.filter(block -> !Optional.of(Registry.BLOCK.getKey(block))
						.map(ResourceLocation::getPath)
						.map(name -> name.startsWith("stripped"))
						.orElse(false))
				.map(BlockWrapper::new)
				.collect(Collectors.toSet());
	}
	
	@Override
	@NotNull
	public DirectionCompat asDirectionCompat(@NotNull Direction dir){
		return DirectionCompat.valueOf(dir.name());
	}
	
	@Override
	@NotNull
	public Direction asDirection(@NotNull DirectionCompat dir){
		return Direction.valueOf(dir.name());
	}
	
	@Override
	public boolean isNetherWartOrShroomlight(@NotNull IBlock block){
		return BlockTags.WART_BLOCKS.contains((Block) block.getRaw()) || Blocks.SHROOMLIGHT.equals(block.getRaw());
	}
	
	@Override
	public boolean checkCanBreakBlock(@NotNull ILevel level, @NotNull IBlockPos blockPos, @NotNull IBlockState blockState, @NotNull IPlayer player){
		return true;
	}
	
	@Override
	protected void performEnchantRegister(){
		chopperEnchantment = new EnchantmentWrapper(Registry.register(
				Registry.ENCHANTMENT,
				new ResourceLocation(MOD_ID, "chopper"),
				new ChopperEnchantment()
		));
	}
	
	public void register(){
		ServerTickEvents.END_SERVER_TICK.register(new LeafBreakingListener(this));
		PlayerBlockBreakEvents.BEFORE.register(new BlockBreakListener(this));
	}
}
