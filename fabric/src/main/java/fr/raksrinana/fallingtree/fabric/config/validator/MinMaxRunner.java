package fr.raksrinana.fallingtree.fabric.config.validator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Optional;
import java.util.function.BiFunction;
import static java.util.Optional.empty;

public class MinMaxRunner implements ValidatorRunner<MinMax>{
	private final BiFunction<Integer, Integer, Component> errorTextBuilder;
	
	public MinMaxRunner(){
		errorTextBuilder = (min, max) -> new TranslatableComponent("text.autoconfig.fallingtree.error.valueNotInRange", Integer.toString(min), Integer.toString(max));
	}
	
	@Override
	public Optional<Component> apply(Object value, MinMax annotation){
		var min = annotation.min();
		var max = annotation.max();
		if(value instanceof Integer val){
			if(val < min || val > max){
				return Optional.of(errorTextBuilder.apply(min, max));
			}
		}
		else if(value instanceof Long val){
			if(val < min || val > max){
				return Optional.of(errorTextBuilder.apply(min, max));
			}
		}
		else if(value instanceof Double val){
			if(val < min || val > max){
				return Optional.of(errorTextBuilder.apply(min, max));
			}
		}
		else if(value instanceof Float val){
			if(val < min || val > max){
				return Optional.of(errorTextBuilder.apply(min, max));
			}
		}
		return empty();
	}
	
	@Override
	public Class<MinMax> getAnnotationClass(){
		return MinMax.class;
	}
}
