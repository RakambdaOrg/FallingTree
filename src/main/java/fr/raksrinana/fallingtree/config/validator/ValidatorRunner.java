package fr.raksrinana.fallingtree.config.validator;

import java.util.Optional;

public interface ValidatorRunner<T>{
	Optional<String> apply(Object value, T annotation);
}
