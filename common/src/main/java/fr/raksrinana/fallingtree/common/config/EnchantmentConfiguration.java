package fr.raksrinana.fallingtree.common.config;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class EnchantmentConfiguration{
	@Expose
	private boolean registerEnchant = false;
	@Expose
	private boolean hideEnchant = false;
}
