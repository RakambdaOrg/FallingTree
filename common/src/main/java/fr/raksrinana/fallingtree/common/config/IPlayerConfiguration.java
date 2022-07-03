package fr.raksrinana.fallingtree.common.config;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface IPlayerConfiguration{
	@NotNull
	List<String> getAllowedTagsNormalized();
}
