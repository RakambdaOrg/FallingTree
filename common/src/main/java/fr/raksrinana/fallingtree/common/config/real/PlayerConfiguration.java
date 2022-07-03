package fr.raksrinana.fallingtree.common.config.real;

import com.google.gson.annotations.Expose;
import fr.raksrinana.fallingtree.common.config.IPlayerConfiguration;
import fr.raksrinana.fallingtree.common.config.IResettable;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;

@Data
public class PlayerConfiguration implements IPlayerConfiguration, IResettable{
	@Expose
	@NotNull
	private List<String> allowedTags = new ArrayList<>();
	
	//Cache
	private List<String> allowedTagsCache;
	
	@Override
	@NotNull
	public List<String> getAllowedTagsNormalized(){
		if(isNull(allowedTagsCache)){
			allowedTagsCache = allowedTags.stream()
					.filter(s -> !s.isBlank())
					.toList();
		}
		return allowedTagsCache;
	}
	
	public void reset(){
		allowedTagsCache = null;
	}
}
