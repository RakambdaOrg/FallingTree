package fr.raksrinana.fallingtree.fabric.config.validator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Optional;
import java.util.function.Function;
import static java.util.Optional.empty;

public class MinRunner implements ValidatorRunner<Min>{
	private final Function<Integer, Component> errorTextBuilder;
	
	public MinRunner(){
		errorTextBuilder = min -> new TranslatableComponent("text.autoconfig.fallingtree.error.valueTooLow", Integer.toString(min));
	}
	
	@Override
	public Optional<Component> apply(Object value, Min annotation){
		var min = annotation.value();
		if(value instanceof Integer val){
			if(val < min){
				return Optional.of(errorTextBuilder.apply(min));
			}
		}
		else if(value instanceof Long val){
			if(val < min){
				return Optional.of(errorTextBuilder.apply(min));
			}
		}
		else if(value instanceof Double val){
			if(val < min){
				return Optional.of(errorTextBuilder.apply(min));
			}
		}
		else if(value instanceof Float val){
			if(val < min){
				return Optional.of(errorTextBuilder.apply(min));
			}
		}
		return empty();
	}
	
	@Override
	public Class<Min> getAnnotationClass(){
		return Min.class;
	}
}
