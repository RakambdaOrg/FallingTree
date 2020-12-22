package fr.raksrinana.fallingtree.config;

import fr.raksrinana.fallingtree.FallingTree;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = FallingTree.MOD_ID)
public class Config{
	public static final CommonConfig COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	private static final Pattern MINECRAFT_ID_PATTERN = Pattern.compile("#?[a-z0-9_.-]+:[a-z0-9/._-]+");
	
	public static Function<String, Optional<ITextComponent>> getMinecraftBlockIdCellError(){
		return value -> {
			boolean valid = false;
			if(value != null){
				valid = MINECRAFT_ID_PATTERN.matcher(value).matches();
			}
			if(!valid){
				return Optional.of(new TranslationTextComponent("text.autoconfig.fallingtree.error.invalidBlockResourceLocation"));
			}
			return Optional.empty();
		};
	}
	
	public static Function<String, Optional<ITextComponent>> getMinecraftItemIdCellError(){
		return value -> {
			boolean valid = false;
			if(value != null){
				valid = MINECRAFT_ID_PATTERN.matcher(value).matches();
			}
			if(!valid){
				return Optional.of(new TranslationTextComponent("text.autoconfig.fallingtree.error.invalidItemResourceLocation"));
			}
			return Optional.empty();
		};
	}
	
	static{
		Pair<CommonConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON = commonPair.getLeft();
		COMMON_SPEC = commonPair.getRight();
	}
}
