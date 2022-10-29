package fr.rakambda.fallingtree.common;

import fr.rakambda.fallingtree.common.leaf.LeafBreakingHandler;
import fr.rakambda.fallingtree.common.tree.TreeHandler;
import fr.rakambda.fallingtree.common.tree.TreePartType;
import fr.rakambda.fallingtree.common.tree.builder.TreeBuilder;
import fr.rakambda.fallingtree.common.wrapper.IBlock;
import fr.rakambda.fallingtree.common.wrapper.IBlockState;
import fr.rakambda.fallingtree.common.wrapper.IComponent;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.config.IConfiguration;
import fr.rakambda.fallingtree.common.config.IToolConfiguration;
import fr.rakambda.fallingtree.common.config.proxy.ProxyConfiguration;
import fr.rakambda.fallingtree.common.config.real.Configuration;
import fr.rakambda.fallingtree.common.network.PacketUtils;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.common.wrapper.DirectionCompat;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.IEnchantment;
import fr.rakambda.fallingtree.common.wrapper.IItemStack;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
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
	private final Configuration ownConfiguration;
	private final ProxyConfiguration proxyConfiguration;
	private final TreeBuilder treeBuilder;
	private final TreeHandler treeHandler;
	private final PacketUtils packetUtils;
	
	public FallingTreeCommon(){
		ownConfiguration = Configuration.read();
		proxyConfiguration = new ProxyConfiguration(ownConfiguration);
		treeBuilder = new TreeBuilder(this);
		treeHandler = new TreeHandler(this);
		packetUtils = new PacketUtils(this);
	}
	
	public IConfiguration getConfiguration(){
		return getProxyConfiguration();
	}
	
	@NotNull
	public abstract IComponent translate(@NotNull String key, Object... objects);
	
	public void notifyPlayer(@NotNull IPlayer player, @NotNull IComponent component){
		player.sendMessage(component, getConfiguration().getNotificationMode());
	}
	
	/**
	 * Checks if a player is allowed to break a block.
	 * <br>
	 * These conditions must be met in order, otherwise player is denied :
	 * <ul>
	 *     <li>If {@link IToolConfiguration#isForceToolUsage()} is set to false, player is allowed</li>
	 *     <li>If block is not a whitelisted one, player is allowed</li>
	 *     <li>If tool is valid, player is allowed</li>
	 * </ul>
	 *
	 * @return true if the player is allowed to break that block, false otherwise.
	 */
	public boolean checkForceToolUsage(@NotNull IPlayer player, @NotNull ILevel level, @NotNull IBlockPos blockPos){
		if(!getConfiguration().getTools().isForceToolUsage()){
			return true;
		}
		var originBlock = level.getBlockState(blockPos).getBlock();
		if(!isLogBlock(originBlock)){
			return true;
		}
		return isValidTool(player.getMainHandItem());
	}
	
	public boolean isPlayerInRightState(@NotNull IPlayer player){
		if(player.isCreative() && !getConfiguration().isBreakInCreative()){
			return false;
		}
		if(!getConfiguration().getSneakMode().test(player.isCrouching())){
			return false;
		}
		if(!playerHasRequiredTags(player)){
			return false;
		}
		return canPlayerBreakTree(player);
	}
	
	private boolean playerHasRequiredTags(@NotNull IPlayer player){
		var tags = getConfiguration().getPlayer().getAllowedTagsNormalized();
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
	
	@NotNull
	public abstract ServerPacketHandler getServerPacketHandler();
	
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
			return TreePartType.MANGROVE_ROOTS;
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
		if(getConfiguration().getEnchantment().isRegisterEnchant()){
			performDefaultEnchantRegister();
		}
		if(getConfiguration().getEnchantment().isRegisterSpecificEnchant()){
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
