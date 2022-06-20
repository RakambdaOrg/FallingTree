package fr.raksrinana.fallingtree.common;

import fr.raksrinana.fallingtree.common.config.Configuration;
import fr.raksrinana.fallingtree.common.leaf.LeafBreakingHandler;
import fr.raksrinana.fallingtree.common.network.PacketHandler;
import fr.raksrinana.fallingtree.common.network.PacketUtils;
import fr.raksrinana.fallingtree.common.tree.TreeHandler;
import fr.raksrinana.fallingtree.common.tree.TreePartType;
import fr.raksrinana.fallingtree.common.tree.builder.TreeBuilder;
import fr.raksrinana.fallingtree.common.wrapper.DirectionCompat;
import fr.raksrinana.fallingtree.common.wrapper.IBlock;
import fr.raksrinana.fallingtree.common.wrapper.IBlockPos;
import fr.raksrinana.fallingtree.common.wrapper.IBlockState;
import fr.raksrinana.fallingtree.common.wrapper.IComponent;
import fr.raksrinana.fallingtree.common.wrapper.IEnchantment;
import fr.raksrinana.fallingtree.common.wrapper.IItem;
import fr.raksrinana.fallingtree.common.wrapper.IItemStack;
import fr.raksrinana.fallingtree.common.wrapper.ILevel;
import fr.raksrinana.fallingtree.common.wrapper.IPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Getter
public abstract class FallingTreeCommon<D extends Enum<D>>{
	private final Configuration configuration;
	private final TreeBuilder treeBuilder;
	private final TreeHandler treeHandler;
	private final PacketUtils packetUtils;
	
	public FallingTreeCommon(){
		configuration = Configuration.read();
		treeBuilder = new TreeBuilder(this);
		treeHandler = new TreeHandler(this);
		packetUtils = new PacketUtils(this);
	}
	
	@NotNull
	public abstract IComponent translate(@NotNull String key, Object... objects);
	
	public void notifyPlayer(@NotNull IPlayer player, @NotNull IComponent component){
		player.sendMessage(component, getConfiguration().getNotificationMode());
	}
	
	public boolean isPlayerInRightState(@NotNull IPlayer player){
		if(player.isCreative() && !getConfiguration().isBreakInCreative()){
			return false;
		}
		if(!configuration.getSneakMode().test(player.isCrouching())){
			return false;
		}
		if(!playerHasRequiredTags(player)){
			return false;
		}
		return canPlayerBreakTree(player);
	}
	
	private boolean playerHasRequiredTags(@NotNull IPlayer player){
		var tags = configuration.getPlayer().getAllowedTagsNormalized();
		if(tags.isEmpty()){
			return true;
		}
		
		var playerTags = player.getTags();
		return tags.stream().anyMatch(playerTags::contains);
	}
	
	public boolean canPlayerBreakTree(@NotNull IPlayer player){
		var heldItemStack = player.getMainHandItem();
		
		if(!isValidTool(heldItemStack)){
			return false;
		}
		
		if(getConfiguration().getEnchantment().isAtLeastOneEnchantRegistered()
		   && !heldItemStack.hasOneOfEnchantAtLeast(getChopperEnchantments(), 1)){
			return false;
		}
		
		return true;
	}
	
	public boolean isValidTool(@NotNull IItemStack heldItemStack){
		var toolConfiguration = getConfiguration().getTools();
		var heldItem = heldItemStack.getItem();
		
		var isAllowedTool = toolConfiguration.isIgnoreTools()
		                    || heldItem.isAxe()
		                    || toolConfiguration.getAllowedItems(this).stream().anyMatch(tool -> tool.equals(heldItem))
							|| heldItemStack.canPerformAxeAction();
		if(!isAllowedTool){
			return false;
		}
		
		var isDeniedTool = toolConfiguration.getDeniedItems(this).stream().anyMatch(tool -> tool.equals(heldItem));
		return !isDeniedTool;
	}
	
	@NotNull
	public TreeHandler getTreeHandler(){
		return treeHandler;
	}
	
	@NotNull
	public TreeBuilder getTreeBuilder(){
		return treeBuilder;
	}
	
	@NotNull
	public abstract LeafBreakingHandler getLeafBreakingHandler();
	
	public abstract PacketHandler getPacketHandler();
	
	@NotNull
	public Set<IBlock> getAsBlocks(@NotNull Collection<String> names){
		return names.stream()
				.filter(Objects::nonNull)
				.filter(val -> !val.isEmpty())
				.flatMap(this::getBlock)
				.filter(Objects::nonNull)
				.filter(block -> !block.isAir())
				.collect(toSet());
	}
	
	@NotNull
	public abstract Stream<IBlock> getBlock(@NotNull String name);
	
	@NotNull
	public Set<IItem> getAsItems(Collection<String> names){
		return names.stream()
				.filter(Objects::nonNull)
				.filter(val -> !val.isEmpty())
				.flatMap(this::getItem)
				.filter(Objects::nonNull)
				.filter(item -> !item.isAir())
				.collect(toSet());
	}
	
	@NotNull
	public abstract Stream<IItem> getItem(@NotNull String name);
	
	public abstract boolean isLeafBlock(@NotNull IBlock block);
	
	public abstract boolean isLogBlock(@NotNull IBlock block);
	
	@NotNull
	public abstract Set<IBlock> getAllNonStrippedLogsBlocks();
	
	@NotNull
	public abstract DirectionCompat asDirectionCompat(@NotNull D dir);
	
	@NotNull
	public abstract D asDirection(@NotNull DirectionCompat dir);
	
	public boolean isLeafNeedBreakBlock(@NotNull IBlock block){
		return getConfiguration().getTrees()
				.getAllowedNonDecayLeaveBlocks(this)
				.stream()
				.anyMatch(log -> log.equals(block));
	}
	
	public abstract boolean isNetherWartOrShroomlight(@NotNull IBlock block);
	
	public abstract boolean isMangroveRoots(@NotNull IBlock block);
	
	@NotNull
	public TreePartType getTreePart(@NotNull IBlock checkBlock){
		if(isLogBlock(checkBlock)){
			return TreePartType.LOG;
		}
		if(isNetherWartOrShroomlight(checkBlock)){
			return TreePartType.NETHER_WART;
		}
		if(isMangroveRoots(checkBlock)){
			return TreePartType.NETHER_WART;
		}
		if(isLeafNeedBreakBlock(checkBlock)){
			return TreePartType.LEAF_NEED_BREAK;
		}
		if(isLeafBlock(checkBlock)){
			return TreePartType.LEAF;
		}
		return TreePartType.OTHER;
	}
	
	public abstract boolean checkCanBreakBlock(@NotNull ILevel level, @NotNull IBlockPos blockPos, @NotNull IBlockState blockState, @NotNull IPlayer player);
	
	public void registerEnchant(){
		if(configuration.getEnchantment().isRegisterEnchant()){
			performDefaultEnchantRegister();
		}
		if(configuration.getEnchantment().isRegisterSpecificEnchant()){
			performSpecificEnchantRegister();
		}
		performCommitEnchantRegister();
	}
	
	protected abstract void performDefaultEnchantRegister();
	
	protected abstract void performSpecificEnchantRegister();
	
	protected abstract void performCommitEnchantRegister();
	
	@NotNull
	public abstract Collection<IEnchantment> getChopperEnchantments();
}
