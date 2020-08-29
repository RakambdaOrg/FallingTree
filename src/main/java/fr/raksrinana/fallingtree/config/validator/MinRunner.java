package fr.raksrinana.fallingtree.config.validator;

import java.util.Optional;

public class MinRunner implements ValidatorRunner<Min>{
	@Override
	public Optional<String> apply(Object value, Min annotation){
		int min = annotation.value();
		if(value instanceof Integer){
			if((Integer) value < min){
				return Optional.of("Value must be greater or equal to " + min);
			}
		}
		else if(value instanceof Long){
			if((Long) value < min){
				return Optional.of("Value must be greater or equal to " + min);
			}
		}
		else if(value instanceof Double){
			if((Double) value < min){
				return Optional.of("Value must be greater or equal to " + min);
			}
		}
		else if(value instanceof Float){
			if((Float) value < min){
				return Optional.of("Value must be greater or equal to " + min);
			}
		}
		return Optional.empty();
	}
}
