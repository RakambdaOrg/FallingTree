package fr.raksrinana.fallingtree.fabric.common.wrapper;

import fr.raksrinana.fallingtree.common.config.enums.BreakMode;
import fr.raksrinana.fallingtree.common.enchant.IFallingTreeEnchantment;
import fr.raksrinana.fallingtree.common.wrapper.IEnchantment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

@RequiredArgsConstructor
@ToString
public class EnchantmentWrapper implements IEnchantment{
	@NotNull
	@Getter
	private final Enchantment raw;
	
	@Override
	@NotNull
	public  Optional<BreakMode> getBreakMode(){
		if(raw instanceof IFallingTreeEnchantment ftEnchantment){
			return ftEnchantment.getBreakMode();
		}
		return Optional.empty();
	}
}
