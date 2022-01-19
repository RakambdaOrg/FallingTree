package fr.raksrinana.fallingtree.common.config;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlayerConfiguration{
	@Expose
	@NotNull
	private List<String> allowedTags = new ArrayList<>();
}
