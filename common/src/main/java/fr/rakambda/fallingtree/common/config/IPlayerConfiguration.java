package fr.rakambda.fallingtree.common.config;

import org.jspecify.annotations.NonNull;
import java.util.List;

public interface IPlayerConfiguration{
	@NonNull
	List<String> getAllowedTagsNormalized();
}
