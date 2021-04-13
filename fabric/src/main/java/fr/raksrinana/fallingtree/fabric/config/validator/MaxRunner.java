package fr.raksrinana.fallingtree.fabric.config.validator;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import java.util.Optional;
import java.util.function.Function;

public class MaxRunner implements ValidatorRunner<Max>{
	private final Function<Integer, Text> errorTextBuilder;
	
	public MaxRunner(){
		this.errorTextBuilder = max -> new TranslatableText("text.autoconfig.fallingtree.error.valueTooHigh", Integer.toString(max));
	}
	
	@Override
	public Optional<Text> apply(Object value, Max annotation){
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
