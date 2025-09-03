package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;

public interface IComponent extends IWrapper{
	@NonNull
	IComponent append(@NonNull IComponent component);
}
