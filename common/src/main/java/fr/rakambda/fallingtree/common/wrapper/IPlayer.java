package fr.rakambda.fallingtree.common.wrapper;

import fr.rakambda.fallingtree.common.config.enums.NotificationMode;
import org.jspecify.annotations.NonNull;
import java.util.Set;
import java.util.UUID;

public interface IPlayer extends IWrapper{
	void sendMessage(@NonNull IComponent component, @NonNull NotificationMode mode);
	
	@NonNull
	IItemStack getMainHandItem();
	
	void awardItemUsed(@NonNull IItem item);
	
	boolean isCreative();
	
	boolean isCrouching();
	
	@NonNull
	UUID getUUID();
	
	@NonNull
	ILevel getLevel();
	
	@NonNull
	Set<String> getTags();
	
	boolean addTag(@NonNull String tag);
	
	boolean removeTag(@NonNull String tag);
}
