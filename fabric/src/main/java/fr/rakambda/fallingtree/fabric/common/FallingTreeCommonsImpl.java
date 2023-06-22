package fr.rakambda.fallingtree.fabric.common;

import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.BreakMode;
import fr.rakambda.fallingtree.common.leaf.LeafBreakingHandler;
import fr.rakambda.fallingtree.common.network.ServerPacketHandler;
import fr.rakambda.fallingtree.common.wrapper.*;
import fr.rakambda.fallingtree.fabric.FallingTree;
import fr.rakambda.fallingtree.fabric.common.wrapper.*;
import fr.rakambda.fallingtree.fabric.enchant.ChopperEnchantment;
import fr.rakambda.fallingtree.fabric.event.BlockBreakListener;
import fr.rakambda.fallingtree.fabric.event.LeafBreakingListener;
import fr.rakambda.fallingtree.fabric.event.PlayerJoinListener;
import fr.rakambda.fallingtree.fabric.event.ServerCommandRegistrationListener;
import fr.rakambda.fallingtree.fabric.network.FabricServerPacketHandler;
import lombok.Getter;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.empty;

public class FallingTreeCommonsImpl extends FallingTreeCommon<Direction> {
    @Getter
    private final LeafBreakingHandler leafBreakingHandler;
    @Getter
    private final ServerPacketHandler serverPacketHandler;
    @Getter
    private Collection<IEnchantment> chopperEnchantments;

    public FallingTreeCommonsImpl() {
        leafBreakingHandler = new LeafBreakingHandler(this);
        chopperEnchantments = new ArrayList<>();
        serverPacketHandler = new FabricServerPacketHandler(this);
    }

    @Override
    @NotNull
    public IComponent translate(@NotNull String key, Object... objects) {
        return new ComponentWrapper(Component.translatable(key, null, objects));
    }

    @Override
    @NotNull
    public Stream<IBlock> getBlock(@NotNull String name) {
        try {
            var isTag = name.startsWith("#");
            if (isTag) {
                name = name.substring(1);
            }
            var identifier = new ResourceLocation(name);
            if (isTag) {
                var tag = TagKey.create(Registries.BLOCK, identifier);
                return getRegistryTagContent(BuiltInRegistries.BLOCK, tag).map(BlockWrapper::new);
            }
            return getRegistryElement(BuiltInRegistries.BLOCK, identifier).stream().map(BlockWrapper::new);
        } catch (Exception e) {
            return empty();
        }
    }

    @Override
    @NotNull
    public Stream<IItem> getItem(@NotNull String name) {
        try {
            var isTag = name.startsWith("#");
            if (isTag) {
                name = name.substring(1);
            }
            var identifier = new ResourceLocation(name);
            if (isTag) {
                var tag = TagKey.create(Registries.ITEM, identifier);
                return getRegistryTagContent(BuiltInRegistries.ITEM, tag).map(ItemWrapper::new);
            }
            return getRegistryElement(BuiltInRegistries.ITEM, identifier).stream().map(ItemWrapper::new);
        } catch (Exception e) {
            return empty();
        }
    }

    @Override
    public boolean isLeafBlock(@NotNull IBlock block) {
        var isAllowedBlock = registryTagContains(BuiltInRegistries.BLOCK, BlockTags.LEAVES, (Block) block.getRaw())
                || getConfiguration().getTrees().getAllowedLeaveBlocks(this).stream().anyMatch(leaf -> leaf.equals(block));
        if (isAllowedBlock) {
            var isDeniedBlock = getConfiguration().getTrees().getDeniedLeaveBlocks(this).stream().anyMatch(leaf -> leaf.equals(block));
            return !isDeniedBlock;
        }
        return false;
    }

    @Override
    public boolean isLogBlock(@NotNull IBlock block) {
        var isAllowedBlock = getConfiguration().getTrees().getDefaultLogsBlocks(this).stream().anyMatch(log -> log.equals(block))
                || getConfiguration().getTrees().getAllowedLogBlocks(this).stream().anyMatch(log -> log.equals(block));
        if (isAllowedBlock) {
            var isDeniedBlock = getConfiguration().getTrees().getDeniedLogBlocks(this).stream().anyMatch(log -> log.equals(block));
            return !isDeniedBlock;
        }
        return false;
    }

    @Override
    @NotNull
    public Set<IBlock> getAllNonStrippedLogsBlocks() {
        return getRegistryTagContent(BuiltInRegistries.BLOCK, BlockTags.LOGS)
                .filter(block -> !Optional.of(BuiltInRegistries.BLOCK.getKey(block))
                        .map(ResourceLocation::getPath)
                        .map(name -> name.startsWith("stripped"))
                        .orElse(false))
                .map(BlockWrapper::new)
                .collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public DirectionCompat asDirectionCompat(@NotNull Direction dir) {
        return DirectionCompat.valueOf(dir.name());
    }

    @Override
    @NotNull
    public Direction asDirection(@NotNull DirectionCompat dir) {
        return Direction.valueOf(dir.name());
    }

    @Override
    public boolean isNetherWartOrShroomlight(@NotNull IBlock block) {
        return registryTagContains(BuiltInRegistries.BLOCK, BlockTags.WART_BLOCKS, (Block) block.getRaw())
                || Blocks.SHROOMLIGHT.equals(block.getRaw());
    }

    @Override
    public boolean isMangroveRoots(@NotNull IBlock block) {
        return Blocks.MANGROVE_ROOTS.equals(block.getRaw());
    }

    @Override
    public boolean checkCanBreakBlock(@NotNull ILevel level, @NotNull IBlockPos blockPos, @NotNull IBlockState blockState, @NotNull IPlayer player) {
        return true;
    }

    @Override
    protected void performDefaultEnchantRegister() {
        chopperEnchantments.add(new EnchantmentWrapper(Registry.register(
                BuiltInRegistries.ENCHANTMENT,
                new ResourceLocation(FallingTree.MOD_ID, "chopper"),
                new ChopperEnchantment(this, null)
        )));
    }

    @Override
    protected void performSpecificEnchantRegister() {
        chopperEnchantments.add(new EnchantmentWrapper(Registry.register(
                BuiltInRegistries.ENCHANTMENT,
                new ResourceLocation(FallingTree.MOD_ID, "chopper_instantaneous"),
                new ChopperEnchantment(this, BreakMode.INSTANTANEOUS)
        )));
        chopperEnchantments.add(new EnchantmentWrapper(Registry.register(
                BuiltInRegistries.ENCHANTMENT,
                new ResourceLocation(FallingTree.MOD_ID, "chopper_shift_down"),
                new ChopperEnchantment(this, BreakMode.SHIFT_DOWN)
        )));
    }

    @Override
    protected void performCommitEnchantRegister() {
    }

    @Override
    @NotNull
    public IItemStack getEmptyItemStack() {
        return new ItemStackWrapper(ItemStack.EMPTY);
    }

    @NotNull
    private <T> Optional<T> getRegistryElement(Registry<T> registryKey, ResourceLocation identifier) {
        return registryKey.getOptional(identifier);
    }

    @NotNull
    private <T> Stream<T> getRegistryTagContent(@NotNull Registry<T> registry, @NotNull TagKey<T> tag) {
        return registry.getTag(tag).stream()
                .flatMap(a -> a.stream().map(Holder::value));
    }

    private <T> boolean registryTagContains(@NotNull Registry<T> registry, @NotNull TagKey<T> tag, @NotNull T element) {
        return getRegistryTagContent(registry, tag).anyMatch(element::equals);
    }

    public void register() {
        getServerPacketHandler().registerServer();

        ServerTickEvents.END_SERVER_TICK.register(new LeafBreakingListener(this));
        PlayerBlockBreakEvents.BEFORE.register(new BlockBreakListener(this));
        ServerPlayConnectionEvents.JOIN.register(new PlayerJoinListener(this));

        CommandRegistrationCallback.EVENT.register(new ServerCommandRegistrationListener(this));
    }
}
