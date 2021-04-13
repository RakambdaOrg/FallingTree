package fr.raksrinana.fallingtree.fabric.config.validator;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class ItemIdRunner implements ValidatorRunner<ItemId>{
	private static final Pattern MINECRAFT_ID_PATTERN = Pattern.compile("#?[a-z0-9_.-]+:[a-z0-9/._-]+");
	private static final Text errorText = new TranslatableText("text.autoconfig.fallingtree.error.invalidItemResourceLocation");
	
	@Override
	public Optional<Text> apply(Object value, ItemId annotation){
		if(value == null){
			return Optional.of(errorText);
		}
		if(value instanceof String){
			String val = value.toString();
			if(annotation.allowEmpty() && val.isEmpty()){
				// OK
			}
			else{
				boolean valid = MINECRAFT_ID_PATTERN.matcher((String) value).matches();
				if(!valid){
					return Optional.of(errorText);
				}
			}
		}
		else if(value instanceof List){
			List<?> list = (List<?>) value;
			boolean valid = list.stream()
					.filter(Objects::nonNull)
					.map(Object::toString)
					.allMatch(val -> MINECRAFT_ID_PATTERN.matcher(val).matches());
			if(!valid){
				return Optional.of(errorText);
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Class<ItemId> getAnnotationClass(){
		return ItemId.class;
	}
}
