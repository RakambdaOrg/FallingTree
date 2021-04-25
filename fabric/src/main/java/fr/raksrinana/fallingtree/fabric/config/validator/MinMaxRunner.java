package fr.raksrinana.fallingtree.fabric.config.validator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Optional;
import java.util.function.BiFunction;

public class MinMaxRunner implements ValidatorRunner<MinMax>{
	private final BiFunction<Integer, Integer, Component> errorTextBuilder;
	
	public MinMaxRunner(){
		this.errorTextBuilder = (min, max) -> new TranslatableComponent("text.autoconfig.fallingtree.error.valueNotInRange", Integer.toString(min), Integer.toString(max));
	}
	
	@Override
	public Optional<Component> apply(Object value, MinMax annotation){
		int min = annotation.min();
		int max = annotation.max();
		if(value instanceof Integer){
			int val = (Integer) value;
			if(val < min || val > max){
				return Optional.of(errorTextBuilder.apply(min, max));
			}
		}
		else if(value instanceof Long){
			long val = (Long) value;
			if(val < min || val > max){
				return Optional.of(errorTextBuilder.apply(min, max));
			}
		}
		else if(value instanceof Double){
			double val = (Double) value;
			if(val < min || val > max){
				return Optional.of(errorTextBuilder.apply(min, max));
			}
		}
		else if(value instanceof Float){
			float val = (Float) value;
			if(val < min || val > max){
				return Optional.of(errorTextBuilder.apply(min, max));
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Class<MinMax> getAnnotationClass(){
		return MinMax.class;
	}
}
