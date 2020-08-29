package fr.raksrinana.fallingtree.config.validator;

import java.util.Optional;

public class MaxRunner implements ValidatorRunner<Max>{
	@Override
	public Optional<String> apply(Object value, Max annotation){
		int max = annotation.value();
		if(value instanceof Integer){
			if((Integer) value > max){
				return Optional.of("Value must be less or equal to " + max);
			}
		}
		else if(value instanceof Long){
			if((Long) value > max){
				return Optional.of("Value must be less or equal to " + max);
			}
		}
		else if(value instanceof Double){
			if((Double) value > max){
				return Optional.of("Value must be less or equal to " + max);
			}
		}
		else if(value instanceof Float){
			if((Float) value > max){
				return Optional.of("Value must be less or equal to " + max);
			}
		}
		return Optional.empty();
	}
}
