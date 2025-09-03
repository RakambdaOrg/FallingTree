package fr.rakambda.fallingtree.neoforge.common.wrapper;

import fr.rakambda.fallingtree.common.wrapper.IBiome;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
@ToString
@Log4j2
public class BiomeWrapper implements IBiome{
	@NonNull
	@Getter
	private final Holder<Biome> raw;
	
	@Override
	@NonNull
	public String getId(){
		return raw.getRegisteredName();
	}
}
