package fr.rakambda.fallingtree.common;

import fr.rakambda.fallingtree.common.command.ToggleCommand;
import fr.rakambda.fallingtree.common.config.IConfiguration;
import fr.rakambda.fallingtree.common.config.IToolConfiguration;
import fr.rakambda.fallingtree.common.config.proxy.ProxyConfiguration;
import fr.rakambda.fallingtree.common.config.real.Configuration;
import fr.rakambda.fallingtree.common.leaf.LeafBreakingHandler;
import fr.rakambda.fallingtree.common.network.PacketUtils;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.common.tree.TreeHandler;
import fr.rakambda.fallingtree.common.tree.TreePartType;
import fr.rakambda.fallingtree.common.tree.builder.TreeBuilder;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Getter
public abstract class FallingTreeCommon<D extends Enum<D>> {
    private final Configuration ownConfiguration;
    private final ProxyConfiguration proxyConfiguration;
    private final TreeBuilder treeBuilder;
    private final TreeHandler treeHandler;
    private final PacketUtils packetUtils;

    public FallingTreeCommon() {
        ownConfiguration = Configuration.read();
        proxyConfiguration = new ProxyConfiguration(ownConfiguration);
        treeBuilder = new TreeBuilder(this);
        treeHandler = new TreeHandler(this);
        packetUtils = new PacketUtils(this);
    }

    public IConfiguration getConfiguration() {
        return getProxyConfiguration();
    }

    @NonNull
    public abstract IComponent translate(@NonNull String key, Object... objects);

    public void notifyPlayer(@NonNull IPlayer player, @NonNull IComponent component) {
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
    public boolean checkForceToolUsage(@NonNull IPlayer player, @NonNull ILevel level, @NonNull IBlockPos blockPos) {
        if (!getConfiguration().getTools().isForceToolUsage()) {
            return true;
        }
        var originBlock = level.getBlockState(blockPos).getBlock();
        if (!isLogBlock(originBlock)) {
            return true;
        }
        return isValidTool(player.getMainHandItem());
    }

    public boolean isPlayerInRightState(@NonNull IPlayer player) {
        if (player.isCreative() && !getConfiguration().isBreakInCreative()) {
            return false;
        }
        if (!getConfiguration().getSneakMode().test(player.isCrouching())) {
            return false;
        }
        if (playerHasToggledOff(player)) {
            return false;
        }
        if (!playerHasRequiredTags(player)) {
            return false;
        }
        return canPlayerBreakTree(player);
    }

    private boolean playerHasToggledOff(@NonNull IPlayer player) {
        return player.getTags().contains(ToggleCommand.FALLINGTREE_DISABLE_TAG);
    }

    private boolean playerHasRequiredTags(@NonNull IPlayer player) {
        var tags = getConfiguration().getPlayer().getAllowedTagsNormalized();
        if (tags.isEmpty()) {
            return true;
        }

        var playerTags = player.getTags();
        return tags.stream().anyMatch(playerTags::contains);
    }

    public boolean canPlayerBreakTree(@NonNull IPlayer player) {
        var heldItemStack = player.getMainHandItem();

        if (!isValidTool(heldItemStack)) {
            return false;
        }

        if (getConfiguration().getEnchantment().isRequireEnchantment()
                && !heldItemStack.hasChopperEnchant()) {
            return false;
        }

        return true;
    }

    public boolean isValidTool(@NonNull IItemStack heldItemStack) {
        var toolConfiguration = getConfiguration().getTools();
        var heldItem = heldItemStack.getItem();

        var isAllowedTool = toolConfiguration.isIgnoreTools()
                || heldItem.isAxe()
                || toolConfiguration.getAllowedItems(this).stream().anyMatch(tool -> tool.equals(heldItem))
                || heldItemStack.canPerformAxeAction();
        if (!isAllowedTool) {
            return false;
        }

        var isDeniedTool = toolConfiguration.getDeniedItems(this).stream().anyMatch(tool -> tool.equals(heldItem));
        return !isDeniedTool;
    }

    @NonNull
    public TreeHandler getTreeHandler() {
        return treeHandler;
    }

    @NonNull
    public TreeBuilder getTreeBuilder() {
        return treeBuilder;
    }

    @NonNull
    public abstract LeafBreakingHandler getLeafBreakingHandler();

    @NonNull
    public abstract ServerPacketHandler getServerPacketHandler();

    @NonNull
    public Set<IBlock> getAsBlocks(@NonNull Collection<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(val -> !val.isEmpty())
                .flatMap(this::getBlock)
                .filter(Objects::nonNull)
                .filter(block -> !block.isAir())
                .collect(toSet());
    }

    @NonNull
    public abstract Stream<IBlock> getBlock(@NonNull String name);

    @NonNull
    public Set<IItem> getAsItems(Collection<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(val -> !val.isEmpty())
                .flatMap(this::getItem)
                .filter(Objects::nonNull)
                .filter(item -> !item.isAir())
                .collect(toSet());
    }

    @NonNull
    public abstract Stream<IItem> getItem(@NonNull String name);

    public abstract boolean isLeafBlock(@NonNull IBlock block);

    public abstract boolean isLogBlock(@NonNull IBlock block);

    @NonNull
    public abstract Set<IBlock> getAllNonStrippedLogsBlocks();

    @NonNull
    public abstract DirectionCompat asDirectionCompat(@NonNull D dir);

    @NonNull
    public abstract D asDirection(@NonNull DirectionCompat dir);

    public boolean isLeafNeedBreakBlock(@NonNull IBlock block) {
        return getConfiguration().getTrees()
                .getAllowedNonDecayLeaveBlocks(this)
                .stream()
                .anyMatch(log -> log.equals(block));
    }

    public abstract boolean isNetherWartOrShroomlight(@NonNull IBlock block);

    public abstract boolean isMangroveRoots(@NonNull IBlock block);

    @NonNull
    public TreePartType getTreePart(@NonNull IBlock checkBlock) {
        if (isLogBlock(checkBlock)) {
            return TreePartType.LOG;
        }
        if (isNetherWartOrShroomlight(checkBlock)) {
            return TreePartType.NETHER_WART;
        }
        if (isMangroveRoots(checkBlock)) {
            return TreePartType.MANGROVE_ROOTS;
        }
        if (isLeafNeedBreakBlock(checkBlock)) {
            return TreePartType.LEAF_NEED_BREAK;
        }
        if (isLeafBlock(checkBlock)) {
            return TreePartType.LEAF;
        }
        return TreePartType.OTHER;
    }

    public abstract boolean checkCanBreakBlock(@NonNull ILevel level, @NonNull IBlockPos blockPos, @NonNull IBlockState blockState, @NonNull IPlayer player);

    @NonNull
    public abstract IItemStack getEmptyItemStack();
	
	public boolean isOwnEvent(@NonNull IBlockBreakEvent event){
		return false;
	}
	
	public abstract void onConfigUpdate();
}
