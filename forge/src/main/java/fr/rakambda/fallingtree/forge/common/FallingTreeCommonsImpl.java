package fr.rakambda.fallingtree.forge.common;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.leaf.LeafBreakingHandler;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.common.wrapper.DirectionCompat;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IComponent;
import fr.rakambda.fallingtree.common.wrapper.IEnchantment;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import fr.rakambda.fallingtree.forge.client.event.PlayerLeaveListener;
import fr.rakambda.fallingtree.forge.common.wrapper.BlockWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.ComponentWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.EnchantmentWrapper;
import fr.rakambda.fallingtree.forge.common.wrapper.ItemWrapper;
import fr.rakambda.fallingtree.forge.event.BlockBreakListener;
import fr.rakambda.fallingtree.forge.event.FallingTreeBlockBreakEvent;
import fr.rakambda.fallingtree.forge.event.FallingTreeEnchantments;
import fr.rakambda.fallingtree.forge.event.LeafBreakingListener;
import fr.rakambda.fallingtree.forge.event.ServerCommandRegistrationListener;
import fr.rakambda.fallingtree.forge.network.ForgePacketHandler;
import fr.rakambda.fallingtree.forge.network.PlayerJoinListener;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Stream.empty;

public class FallingTreeCommonsImpl extends FallingTreeCommon<Direction>{
	@Getter
	private final LeafBreakingHandler leafBreakingHandler;
	private final ForgePacketHandler packetHandler;
	@Getter
	private Collection<IEnchantment> chopperEnchantments;
	
	public FallingTreeCommonsImpl(){
		leafBreakingHandler = new LeafBreakingHandler(this);
		chopperEnchantments = new ArrayList<>();
		packetHandler = new ForgePacketHandler(this);
	}
	
	@Override
	@NotNull
	public IComponent translate(@NotNull String key, Object... objects){
		return new ComponentWrapper(MutableComponent.create(new TranslatableContents(key, objects)));
	}
	
	@Override
	@NotNull
	public ServerPacketHandler getServerPacketHandler(){
		return packetHandler;
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
				var tag = TagKey.create(Registries.BLOCK, resourceLocation);
				return getRegistryTagContent(ForgeRegistries.BLOCKS, tag).map(BlockWrapper::new);
			}
			return getRegistryElement(ForgeRegistries.BLOCKS, resourceLocation).stream().map(BlockWrapper::new);
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
				var tag = TagKey.create(Registries.ITEM, resourceLocation);
				return getRegistryTagContent(ForgeRegistries.ITEMS, tag).map(ItemWrapper::new);
			}
			return getRegistryElement(ForgeRegistries.ITEMS, resourceLocation).stream().map(ItemWrapper::new);
		}
		catch(Exception e){
			return empty();
		}
	}
	
	@Override
	public boolean isLeafBlock(@NotNull IBlock block){
		var isAllowedBlock = registryTagContains(ForgeRegistries.BLOCKS, BlockTags.LEAVES, (Block) block.getRaw())
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
		return getRegistryTagContent(ForgeRegistries.BLOCKS, BlockTags.LOGS)
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
		return registryTagContains(ForgeRegistries.BLOCKS, BlockTags.WART_BLOCKS, (Block) block.getRaw())
		       || Blocks.SHROOMLIGHT.equals(block.getRaw());
	}
	
	@Override
	public boolean isMangroveRoots(@NotNull IBlock block){
		return Blocks.MANGROVE_ROOTS.equals(block.getRaw());
	}
	
	@Override
	public boolean checkCanBreakBlock(@NotNull ILevel level, @NotNull IBlockPos blockPos, @NotNull IBlockState blockState, @NotNull IPlayer player){
		return !MinecraftForge.EVENT_BUS.post(new FallingTreeBlockBreakEvent((Level) level.getRaw(), (BlockPos) blockPos.getRaw(), (BlockState) blockState.getRaw(), (Player) player.getRaw()));
	}
	
	@Override
	protected void performDefaultEnchantRegister(){
		FallingTreeEnchantments.registerDefault();
	}
	
	@Override
	protected void performSpecificEnchantRegister(){
		FallingTreeEnchantments.registerSpecific();
	}
	
	@Override
	protected void performCommitEnchantRegister(){
		FallingTreeEnchantments.commit(FMLJavaModLoadingContext.get().getModEventBus());
		
		Stream.of(FallingTreeEnchantments.CHOPPER_ENCHANTMENT, FallingTreeEnchantments.CHOPPER_INSTANTANEOUS_ENCHANTMENT, FallingTreeEnchantments.CHOPPER_SHIFT_DOWN_ENCHANTMENT)
				.filter(Objects::nonNull)
				.map(EnchantmentWrapper::new)
				.forEach(chopperEnchantments::add);
	}
	
	@NotNull
	private <T> Optional<T> getRegistryElement(IForgeRegistry<T> registryKey, ResourceLocation identifier){
		return registryKey.getHolder(identifier).map(Holder::value);
	}
	
	@NotNull
	private <T> Stream<T> getRegistryTagContent(@NotNull IForgeRegistry<T> registry, @NotNull TagKey<T> tag){
		return registry.tags().getTag(tag).stream();
	}
	
	private <T> boolean registryTagContains(@NotNull IForgeRegistry<T> registry, @NotNull TagKey<T> tag, @NotNull T element){
		return getRegistryTagContent(registry, tag).anyMatch(element::equals);
	}
	
	public void registerForge(@NotNull IEventBus eventBus){
		getServerPacketHandler().registerServer();
		
		eventBus.register(new BlockBreakListener(this));
		eventBus.register(new LeafBreakingListener(this));
		eventBus.register(new PlayerJoinListener(this));
		eventBus.register(new PlayerLeaveListener(this));
		eventBus.register(new ServerCommandRegistrationListener(this));
	}
}
