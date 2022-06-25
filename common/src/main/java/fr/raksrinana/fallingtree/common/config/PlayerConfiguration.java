package fr.raksrinana.fallingtree.common.config;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;

@Data
public class PlayerConfiguration{
	@Expose
	@NotNull
	private List<String> allowedTags = new ArrayList<>();
	
	//Cache
	private List<String> allowedTagsCache;
	
	public List<String> getAllowedTagsNormalized(){
		if(isNull(allowedTagsCache)){
			allowedTagsCache = allowedTags.stream()
					.filter(s -> !s.isBlank())
					.toList();
		}
		return allowedTagsCache;
	}
	
	public void invalidate(){
		allowedTagsCache = null;
	}
}
