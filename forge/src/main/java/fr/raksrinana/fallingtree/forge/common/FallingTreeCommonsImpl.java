package fr.raksrinana.fallingtree.forge.common;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.mrcraftcod.fallingtree.common.leaf.LeafBreakingHandler;
import fr.mrcraftcod.fallingtree.common.wrapper.*;
import fr.raksrinana.fallingtree.forge.common.wrapper.EnchantmentWrapper;
import fr.raksrinana.fallingtree.forge.event.BlockBreakListener;
import fr.raksrinana.fallingtree.forge.event.FallingTreeBlockBreakEvent;
import fr.raksrinana.fallingtree.forge.common.wrapper.BlockWrapper;
import fr.raksrinana.fallingtree.forge.common.wrapper.ComponentWrapper;
import fr.raksrinana.fallingtree.forge.common.wrapper.ItemWrapper;
import fr.raksrinana.fallingtree.forge.event.FallingTreeEnchantments;
import fr.raksrinana.fallingtree.forge.event.LeafBreakingListener;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Stream.empty;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

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
			var resourceLocation = new ResourceLocation(name);
			if(isTag){
				return Optional.ofNullable(BlockTags.getAllTags().getTag(resourceLocation))
						.stream()
						.map(Tag::getValues)
						.flatMap(Collection::stream)
						.map(BlockWrapper::new);
			}
			return Stream.of(ForgeRegistries.BLOCKS.getValue(resourceLocation)).filter(Objects::nonNull).map(BlockWrapper::new);
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
			var resourceLocation = new ResourceLocation(name);
			if(isTag){
				return Optional.ofNullable(ItemTags.getAllTags().getTag(resourceLocation))
						.stream()
						.map(Tag::getValues)
						.flatMap(Collection::stream)
						.map(ItemWrapper::new);
			}
			return Stream.of(ITEMS.getValue(resourceLocation)).filter(Objects::nonNull).map(ItemWrapper::new);
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
				.filter(block -> !Optional.ofNullable(ForgeRegistries.BLOCKS.getKey(block))
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
		return !MinecraftForge.EVENT_BUS.post(new FallingTreeBlockBreakEvent((Level) level.getRaw(), (BlockPos) blockPos.getRaw(), (BlockState) blockState.getRaw(), (Player) player.getRaw()));
	}
	
	@Override
	protected void performEnchantRegister(){
		FallingTreeEnchantments.register(FMLJavaModLoadingContext.get().getModEventBus());
		chopperEnchantment = new EnchantmentWrapper(FallingTreeEnchantments.CHOPPER_ENCHANTMENT);
	}
	
	public void registerForge(@NotNull IEventBus eventBus){
		eventBus.register(new BlockBreakListener(this));
		eventBus.register(new LeafBreakingListener(this));
	}
}
