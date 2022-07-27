package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.raksrinana.fallingtree.common.config.enums.NotificationMode;
import fr.raksrinana.fallingtree.common.wrapper.IComponent;
import fr.raksrinana.fallingtree.common.wrapper.IItem;
import fr.raksrinana.fallingtree.common.wrapper.IItemStack;
import fr.raksrinana.fallingtree.common.wrapper.ILevel;
import fr.raksrinana.fallingtree.common.wrapper.IPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
				case CHAT -> serverPlayer.sendSystemMessage(text, false);
				case ACTION_BAR -> serverPlayer.sendSystemMessage(text, true);
			}
		}
		else{
			raw.sendSystemMessage(text);
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
