package fr.mrcraftcod.fallingtree.common.config.cloth;

import fr.mrcraftcod.fallingtree.common.FallingTreeCommon;
import fr.mrcraftcod.fallingtree.common.wrapper.IComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class ClothHookBase{
	private static final Pattern MINECRAFT_ID_PATTERN = Pattern.compile("#?[a-z0-9_.-]+:[a-z0-9/._-]+");
	
	@NotNull
	@Getter(AccessLevel.PROTECTED)
	private final FallingTreeCommon<?> mod;
	
	@NotNull
	protected String getFieldName(@Nullable String category, @NotNull String fieldName){
		return Optional.ofNullable(category)
				.filter(c -> !c.isBlank())
				.map(c -> "text.autoconfig.fallingtree.option." + c + "." + fieldName)
				.orElseGet(() -> "text.autoconfig.fallingtree.option." + fieldName);
	}
	
	@NotNull
	protected Stream<IComponent> getTooltipsInternal(@Nullable String category, @NotNull String fieldName, int count){
		var tooltipKey = getFieldName(category, fieldName) + ".@Tooltip";
		var keys = new LinkedList<String>();
		if(count <= 1){
			keys.add(tooltipKey);
		}
		else{
			for(int i = 0; i < count; i++){
				keys.add(tooltipKey + "[" + i + "]");
			}
		}
		
		return keys.stream().map(mod::translate);
	}
	
	@NotNull
	protected Function<String, Optional<IComponent>> getMinecraftBlockIdCellError(){
		return value -> Optional.ofNullable(value)
				.map(v -> MINECRAFT_ID_PATTERN.matcher(v).matches())
				.filter(v -> !v)
				.map(v -> mod.translate("text.autoconfig.fallingtree.error.invalidBlockResourceLocation"));
	}
	
	@NotNull
	protected Function<String, Optional<IComponent>> getMinecraftItemIdCellError(){
		return value -> Optional.ofNullable(value)
				.map(v -> MINECRAFT_ID_PATTERN.matcher(v).matches())
				.filter(v -> !v)
				.map(v -> mod.translate("text.autoconfig.fallingtree.error.invalidItemResourceLocation"));
	}
}
