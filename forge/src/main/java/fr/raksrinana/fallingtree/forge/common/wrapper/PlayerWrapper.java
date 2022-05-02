package fr.raksrinana.fallingtree.forge.common.wrapper;

import fr.raksrinana.fallingtree.common.config.enums.NotificationMode;
import fr.raksrinana.fallingtree.common.wrapper.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@ToString
public class PlayerWrapper implements IPlayer{
	@NotNull
	@Getter
	private final Player raw;
	
	@Override
	public void sendMessage(@NotNull IComponent component, @NotNull NotificationMode mode){
		var text = (Component) component.getRaw();
		if(raw instanceof ServerPlayer serverPlayer){
			switch(mode){
				case CHAT -> serverPlayer.sendMessage(text, Util.NIL_UUID);
				case ACTION_BAR -> serverPlayer.sendMessage(text, ChatType.GAME_INFO, Util.NIL_UUID);
			}
		}
		else{
			raw.sendMessage(text, Util.NIL_UUID);
		}
	}
	
	@Override
	@NotNull
	public IItemStack getMainHandItem(){
		return new ItemStackWrapper(raw.getMainHandItem());
	}
	
	@Override
	public void awardItemUsed(@NotNull IItem item){
		raw.awardStat(Stats.ITEM_USED.get((Item) item.getRaw()));
	}
	
	@Override
	public boolean isCreative(){
		return raw.isCreative();
	}
	
	@Override
	public boolean isCrouching(){
		return raw.isCrouching();
	}
	
	@Override
	@NotNull
	public UUID getUUID(){
		return raw.getUUID();
	}
	
	@Override
	@NotNull
	public ILevel getLevel(){
		return new LevelWrapper(raw.getCommandSenderWorld());
	}
	
	@Override
	@NotNull
	public Set<String> getTags(){
		return raw.getTags();
	}
}
