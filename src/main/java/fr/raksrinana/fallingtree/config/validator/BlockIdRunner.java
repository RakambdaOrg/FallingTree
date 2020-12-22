package fr.raksrinana.fallingtree.config.validator;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class BlockIdRunner implements ValidatorRunner<BlockId>{
	private static final Pattern MINECRAFT_ID_PATTERN = Pattern.compile("#?[a-z0-9_.-]+:[a-z0-9/._-]+");
	private static final Text errorText = new TranslatableText("text.autoconfig.fallingtree.error.invalidBlockResourceLocation");
	
	@Override
	public Optional<Text> apply(Object value, BlockId annotation){
		if(value == null){
			return Optional.of(errorText);
		}
		if(value instanceof String){
			boolean valid = MINECRAFT_ID_PATTERN.matcher((String) value).matches();
			if(!valid){
				return Optional.of(errorText);
			}
		}
		if(value instanceof List){
			boolean valid = ((List<?>) value).stream().allMatch(val -> MINECRAFT_ID_PATTERN.matcher(val.toString()).matches());
			if(!valid){
				return Optional.of(errorText);
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Class<BlockId> getAnnotationClass(){
		return BlockId.class;
	}
}
