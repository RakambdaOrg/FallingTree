package fr.rakambda.fallingtree.fabric.common.wrapper;

import fr.rakambda.fallingtree.common.config.enums.NotificationMode;
import fr.rakambda.fallingtree.common.wrapper.IComponent;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import fr.rakambda.fallingtree.common.wrapper.IItemStack;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@ToString
public class PlayerWrapper implements IPlayer{
	@NonNull
	@Getter
	private final Player raw;
	
	@Override
	public void sendMessage(@NonNull IComponent component, @NonNull NotificationMode mode){
		var text = (Component) component.getRaw();
		if(raw instanceof ServerPlayer serverPlayer){
			switch(mode){
				case CHAT -> serverPlayer.sendSystemMessage(text, false);
				case ACTION_BAR -> serverPlayer.sendSystemMessage(text, true);
			}
		}
		else{
			raw.displayClientMessage(text, true);
		}
	}
	
	@Override
	@NonNull
	public IItemStack getMainHandItem(){
		return new ItemStackWrapper(raw.getMainHandItem());
	}
	
	@Override
	public void awardItemUsed(@NonNull IItem item){
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
	@NonNull
	public UUID getUUID(){
		return raw.getUUID();
	}
	
	@Override
	@NonNull
	public ILevel getLevel(){
		return raw.level() instanceof ServerLevel serverLevel ? new ServerLevelWrapper(serverLevel) : new LevelWrapper(raw.level());
	}
	
	@Override
	@NonNull
	public Set<String> getTags(){
		return raw.entityTags();
	}
	
	@Override
	public boolean addTag(@NonNull String tag){
		return raw.addTag(tag);
	}
	
	@Override
	public boolean removeTag(@NonNull String tag){
		return raw.removeTag(tag);
	}
}
