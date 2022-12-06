package fr.rakambda.fallingtree.common.wrapper;

import fr.rakambda.fallingtree.common.config.enums.NotificationMode;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import java.util.UUID;

public interface IPlayer extends IWrapper{
	void sendMessage(@NotNull IComponent component, @NotNull NotificationMode mode);
	
	@NotNull
	IItemStack getMainHandItem();
	
	void awardItemUsed(@NotNull IItem item);
	
	boolean isCreative();
	
	boolean isCrouching();
	
	@NotNull
	UUID getUUID();
	
	@NotNull
	ILevel getLevel();
	
	@NotNull
	Set<String> getTags();
	
	boolean addTag(@NotNull String tag);
	
	boolean removeTag(@NotNull String tag);
}
