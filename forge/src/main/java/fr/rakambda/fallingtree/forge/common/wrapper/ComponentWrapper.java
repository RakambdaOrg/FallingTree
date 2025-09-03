package fr.rakambda.fallingtree.forge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IComponent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
@ToString
public class ComponentWrapper implements IComponent{
	@NonNull
	@Getter
	private final MutableComponent raw;
	
	@Override
	@NonNull
	public IComponent append(@NonNull IComponent component){
		raw.append((Component) component.getRaw());
		return this;
	}
}
