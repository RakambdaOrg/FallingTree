package fr.rakambda.fallingtree.forge.common;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.leaf.LeafBreakingHandler;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.common.utils.BoundedList;
import fr.rakambda.fallingtree.common.wrapper.DirectionCompat;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import fr.rakambda.fallingtree.common.wrapper.IBlockBreakEvent;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IComponent;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import fr.rakambda.fallingtree.common.wrapper.IItemStack;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import fr.rakambda.fallingtree.forge.client.event.PlayerLeaveListener;
import fr.rakambda.fallingtree.forge.common.wrapper.BlockWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.ComponentWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.ItemStackWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.ItemWrapper;
import fr.rakambda.fallingtree.forge.event.BlockBreakListener;
import fr.rakambda.fallingtree.forge.event.LeafBreakingListener;
import fr.rakambda.fallingtree.forge.event.ServerCommandRegistrationListener;
import fr.rakambda.fallingtree.forge.network.ForgePacketHandler;
import fr.rakambda.fallingtree.forge.network.PlayerJoinListener;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jspecify.annotations.NonNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static fr.rakambda.fallingtree.forge.FallingTreeUtils.id;
import static fr.rakambda.fallingtree.forge.FallingTreeUtils.idExternal;
import static java.util.stream.Stream.empty;

public class FallingTreeCommonsImpl extends FallingTreeCommon<Direction>{
	@Getter
	private final LeafBreakingHandler leafBreakingHandler;
	private final ForgePacketHandler packetHandler;
	@Getter
	private final TagKey<Enchantment> chopperEnchantmentTag;
	@Getter
	private final Map<BreakMode, TagKey<Enchantment>> breakModeChopperEnchantmentTag;
	private final List<BlockEvent.BreakEvent> breakEvents;
	
	private final Map<IBlock, Boolean> isLogBlockCache;
	private final Map<IBlock, Boolean> isLeafBlockCache;
	private final Map<IBlock, Boolean> isWartBlockCache;
	
	public FallingTreeCommonsImpl(){
		leafBreakingHandler = new LeafBreakingHandler(this);
		packetHandler = new ForgePacketHandler();
		
		isLogBlockCache = new HashMap<>();
		isLeafBlockCache = new HashMap<>();
		isWartBlockCache = new HashMap<>();
		
		chopperEnchantmentTag = TagKey.create(Registries.ENCHANTMENT, id("chopper_all"));
		
		breakModeChopperEnchantmentTag = new HashMap<>();
		breakModeChopperEnchantmentTag.put(BreakMode.FALL_ALL_BLOCK, TagKey.create(Registries.ENCHANTMENT, id("chopper_fall_all_block")));
		breakModeChopperEnchantmentTag.put(BreakMode.FALL_BLOCK, TagKey.create(Registries.ENCHANTMENT, id("chopper_fall_block")));
		breakModeChopperEnchantmentTag.put(BreakMode.FALL_ITEM, TagKey.create(Registries.ENCHANTMENT, id("chopper_fall_item")));
		breakModeChopperEnchantmentTag.put(BreakMode.INSTANTANEOUS, TagKey.create(Registries.ENCHANTMENT, id("chopper_instantaneous")));
		breakModeChopperEnchantmentTag.put(BreakMode.SHIFT_DOWN, TagKey.create(Registries.ENCHANTMENT, id("chopper_shift_down")));
		
		breakEvents = new BoundedList<>(50);
	}
	
	@Override
	@NonNull
	public IComponent translate(@NonNull String key, Object... objects){
		Object[] vars = Arrays.stream(objects)
				.map(o -> {
					if(o instanceof IComponent component){
						return component.getRaw();
					}
					return o;
				})
				.toArray();
		return new ComponentWrapper(Component.translatable(key, vars));
	}
	
	@Override
	@NonNull
	public ServerPacketHandler getServerPacketHandler(){
		return packetHandler;
	}
	
	@Override
	@NonNull
	public Stream<IBlock> getBlock(@NonNull String name){
		try{
			var isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			var resourceLocation = idExternal(name);
			if(isTag){
				var tag = TagKey.create(Registries.BLOCK, resourceLocation);
				return getRegistryTagContent(BuiltInRegistries.BLOCK, tag).map(BlockWrapper::new);
			}
			return getRegistryElement(BuiltInRegistries.BLOCK, resourceLocation).stream().map(BlockWrapper::new);
		}
		catch(Exception e){
			return empty();
		}
	}
	
	@Override
	@NonNull
	public Stream<IItem> getItem(@NonNull String name){
		try{
			var isTag = name.startsWith("#");
			if(isTag){
				name = name.substring(1);
			}
			var resourceLocation = idExternal(name);
			if(isTag){
				var tag = TagKey.create(Registries.ITEM, resourceLocation);
				return getRegistryTagContent(BuiltInRegistries.ITEM, tag).map(ItemWrapper::new);
			}
			return getRegistryElement(BuiltInRegistries.ITEM, resourceLocation).stream().map(ItemWrapper::new);
		}
		catch(Exception e){
			return empty();
		}
	}
	
	@Override
	public boolean isLeafBlock(@NonNull IBlock block){
		return isLeafBlockCache.computeIfAbsent(block, Key -> {
			var isAllowedBlock = registryTagContains(BuiltInRegistries.BLOCK, BlockTags.LEAVES, (Block) block.getRaw())
					|| getConfiguration().getTrees().getAllowedLeaveBlocks(this).stream().anyMatch(leaf -> leaf.equals(block));
			if(isAllowedBlock){
				var isDeniedBlock = getConfiguration().getTrees().getDeniedLeaveBlocks(this).stream().anyMatch(leaf -> leaf.equals(block));
				return !isDeniedBlock;
			}
			return false;
		});
	}
	
	@Override
	public boolean isLogBlock(@NonNull IBlock block){
		return isLogBlockCache.computeIfAbsent(block, Key -> {
			var isAllowedBlock = getConfiguration().getTrees().getDefaultLogsBlocks(this).stream().anyMatch(log -> log.equals(block))
					|| getConfiguration().getTrees().getAllowedLogBlocks(this).stream().anyMatch(log -> log.equals(block));
			if(isAllowedBlock){
				var isDeniedBlock = getConfiguration().getTrees().getDeniedLogBlocks(this).stream().anyMatch(log -> log.equals(block));
				return !isDeniedBlock;
			}
			return false;
		});
	}
	
	@Override
	@NonNull
	public Set<IBlock> getAllNonStrippedLogsBlocks(){
		return getRegistryTagContent(BuiltInRegistries.BLOCK, BlockTags.LOGS)
				.filter(block -> !Optional.of(BuiltInRegistries.BLOCK.getKey(block))
						.map(Identifier::getPath)
						.map(name -> name.startsWith("stripped"))
						.orElse(false)
				)
				.map(BlockWrapper::new)
				.collect(Collectors.toSet());
	}
	
	@Override
	@NonNull
	public DirectionCompat asDirectionCompat(@NonNull Direction dir){
		return DirectionCompat.valueOf(dir.name());
	}
	
	@Override
	@NonNull
	public Direction asDirection(@NonNull DirectionCompat dir){
		return Direction.valueOf(dir.name());
	}
	
	@Override
	public boolean isNetherWartOrShroomlight(@NonNull IBlock block){
		return isWartBlockCache.computeIfAbsent(block,
				Key -> registryTagContains(BuiltInRegistries.BLOCK, BlockTags.WART_BLOCKS, (Block) block.getRaw()) || Blocks.SHROOMLIGHT.equals(block.getRaw()));
	}
	
	@Override
	public boolean isMangroveRoots(@NonNull IBlock block){
		return Blocks.MANGROVE_ROOTS.equals(block.getRaw());
	}
	
	@Override
	public boolean checkCanBreakBlock(@NonNull ILevel level, @NonNull IBlockPos blockPos, @NonNull IBlockState blockState, @NonNull IPlayer player){
		var event = new BlockEvent.BreakEvent((Level) level.getRaw(), (BlockPos) blockPos.getRaw(), (BlockState) blockState.getRaw(), (Player) player.getRaw(), Result.DEFAULT);
		breakEvents.add(event);
		return !BlockEvent.BreakEvent.BUS.post(event);
	}
	
	@Override
	public boolean isOwnEvent(@NonNull IBlockBreakEvent event){
		var result = breakEvents.contains((BlockEvent.BreakEvent) event.getRaw());
		if(result){
			breakEvents.remove((BlockEvent.BreakEvent) event.getRaw());
		}
		return result;
	}
	
	@Override
	@NonNull
	public IItemStack getEmptyItemStack(){
		return new ItemStackWrapper(ItemStack.EMPTY);
	}
	
	@Override
	public void onConfigUpdate(){
		isLogBlockCache.clear();
		isLeafBlockCache.clear();
		isWartBlockCache.clear();
	}
	
	@NonNull
	private <T> Optional<T> getRegistryElement(Registry<T> registryKey, Identifier identifier){
		return registryKey.get(identifier).map(Holder::value);
	}
	
	@NonNull
	private <T> Stream<T> getRegistryTagContent(@NonNull Registry<T> registry, @NonNull TagKey<T> tag){
		return registry.get(tag).stream()
				.flatMap(a -> a.stream().map(Holder::value));
	}
	
	private <T> boolean registryTagContains(@NonNull Registry<T> registry, @NonNull TagKey<T> tag, @NonNull T element){
		return getRegistryTagContent(registry, tag).anyMatch(element::equals);
	}
	
	public void registerForge(){
		getServerPacketHandler().registerServer();
		
		var blockBreakListener = new BlockBreakListener(this);
		BlockEvent.BreakEvent.BUS.addListener(blockBreakListener::onBlockBreakEvent);
		PlayerEvent.BreakSpeed.BUS.addListener(blockBreakListener::onBreakSpeed);
		
		var leafBreakingListener = new LeafBreakingListener(this);
		TickEvent.ServerTickEvent.Post.BUS.addListener(leafBreakingListener::onServerTick);
		BlockEvent.NeighborNotifyEvent.BUS.addListener(leafBreakingListener::onNeighborNotifyEvent);
		LevelEvent.Unload.BUS.addListener(leafBreakingListener::onWorldUnload);
		
		var playerJoinListener = new PlayerJoinListener(this);
		PlayerEvent.PlayerLoggedInEvent.BUS.addListener(playerJoinListener::onPlayerLoggedInEvent);
		
		var playerLeaveListener = new PlayerLeaveListener(this);
		if(FMLEnvironment.dist == Dist.CLIENT){
			ClientPlayerNetworkEvent.LoggingOut.BUS.addListener(playerLeaveListener::onPlayerLoggedOutEvent);
		}
		
		var serverCommandRegistrationListener = new ServerCommandRegistrationListener(this);
		RegisterCommandsEvent.BUS.addListener(serverCommandRegistrationListener::onRegisterCommands);
	}
}
