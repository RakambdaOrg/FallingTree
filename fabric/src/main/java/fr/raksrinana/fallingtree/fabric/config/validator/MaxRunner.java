package fr.raksrinana.fallingtree.fabric.config.validator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Optional;
import java.util.function.Function;

public class MaxRunner implements ValidatorRunner<Max>{
	private final Function<Integer, Component> errorTextBuilder;
	
	public MaxRunner(){
		this.errorTextBuilder = max -> new TranslatableComponent("text.autoconfig.fallingtree.error.valueTooHigh", Integer.toString(max));
	}
	
	@Override
	public Optional<Component> apply(Object value, Max annotation){
		int max = annotation.value();
		if(value instanceof Integer){
			if((Integer) value > max){
				return Optional.of(errorTextBuilder.apply(max));
			}
		}
		else if(value instanceof Long){
			if((Long) value > max){
				return Optional.of(errorTextBuilder.apply(max));
			}
		}
		else if(value instanceof Double){
			if((Double) value > max){
				return Optional.of(errorTextBuilder.apply(max));
			}
		}
		else if(value instanceof Float){
			if((Float) value > max){
				return Optional.of(errorTextBuilder.apply(max));
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Class<Max> getAnnotationClass(){
		return Max.class;
	}
}
