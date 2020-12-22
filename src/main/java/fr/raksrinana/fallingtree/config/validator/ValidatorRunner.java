package fr.raksrinana.fallingtree.config.validator;

import net.minecraft.text.Text;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;
import static java.util.Objects.nonNull;

public interface ValidatorRunner<T extends Annotation>{
	default Optional<Text> apply(Object value, Field field){
		T annotation = getAnnotation(field);
		return apply(value, annotation);
	}
	
	default T getAnnotation(Field field){
		return field.getAnnotation(getAnnotationClass());
	}
	
	Optional<Text> apply(Object value, T annotation);
	
	Class<T> getAnnotationClass();
	
	default boolean validateIfAnnotated(Field field, Object category) throws IllegalAccessException{
		T annotation = getAnnotation(field);
		
		if(nonNull(annotation)){
			return !apply(field.get(category), annotation).isPresent();
		}
		return true;
	}
}
