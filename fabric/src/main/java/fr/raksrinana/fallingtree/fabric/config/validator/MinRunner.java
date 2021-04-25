package fr.raksrinana.fallingtree.fabric.config.validator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Optional;
import java.util.function.Function;

public class MinRunner implements ValidatorRunner<Min>{
	private final Function<Integer, Component> errorTextBuilder;
	
	public MinRunner(){
		this.errorTextBuilder = min -> new TranslatableComponent("text.autoconfig.fallingtree.error.valueTooLow", Integer.toString(min));
	}
	
	@Override
	public Optional<Component> apply(Object value, Min annotation){
		int min = annotation.value();
		if(value instanceof Integer){
			if((Integer) value < min){
				return Optional.of(errorTextBuilder.apply(min));
			}
		}
		else if(value instanceof Long){
			if((Long) value < min){
				return Optional.of(errorTextBuilder.apply(min));
			}
		}
		else if(value instanceof Double){
			if((Double) value < min){
				return Optional.of(errorTextBuilder.apply(min));
			}
		}
		else if(value instanceof Float){
			if((Float) value < min){
				return Optional.of(errorTextBuilder.apply(min));
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Class<Min> getAnnotationClass(){
		return Min.class;
	}
}
