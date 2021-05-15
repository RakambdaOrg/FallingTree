package fr.raksrinana.fallingtree.fabric.config.validator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Optional;
import java.util.function.Function;
import static java.util.Optional.empty;

public class MaxRunner implements ValidatorRunner<Max>{
	private final Function<Integer, Component> errorTextBuilder;
	
	public MaxRunner(){
		errorTextBuilder = max -> new TranslatableComponent("text.autoconfig.fallingtree.error.valueTooHigh", Integer.toString(max));
	}
	
	@Override
	public Optional<Component> apply(Object value, Max annotation){
		var max = annotation.value();
		if(value instanceof Integer val){
			if(val > max){
				return Optional.of(errorTextBuilder.apply(max));
			}
		}
		else if(value instanceof Long val){
			if(val > max){
				return Optional.of(errorTextBuilder.apply(max));
			}
		}
		else if(value instanceof Double val){
			if(val > max){
				return Optional.of(errorTextBuilder.apply(max));
			}
		}
		else if(value instanceof Float val){
			if(val > max){
				return Optional.of(errorTextBuilder.apply(max));
			}
		}
		return empty();
	}
	
	@Override
	public Class<Max> getAnnotationClass(){
		return Max.class;
	}
}
