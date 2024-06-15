package fr.rakambda.fallingtree.common.config.real;

import com.google.gson.annotations.Expose;
import fr.rakambda.fallingtree.common.config.IEnchantmentConfiguration;
import lombok.Data;

@Data
public class EnchantmentConfiguration implements IEnchantmentConfiguration{
	@Expose
	private boolean requireEnchantment = false;
}
