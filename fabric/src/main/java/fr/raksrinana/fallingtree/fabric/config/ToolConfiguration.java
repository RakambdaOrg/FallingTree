package fr.raksrinana.fallingtree.fabric.config;

import com.google.gson.annotations.Expose;
import lombok.Data;
import net.minecraft.world.item.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class ToolConfiguration{
	@Expose
	private List<String> whitelisted = new ArrayList<>();
	@Expose
	private List<String> blacklisted = new ArrayList<>();
	@Expose
	private boolean preserve = false;
	@Expose
	private boolean ignoreTools = false;
	@Expose
	private boolean requireEnchant = true;
	@Expose
	private double damageMultiplicand = 1d;
	@Expose
	private DamageRounding damageRounding = DamageRounding.ROUND_DOWN;
	@Expose
	private double speedMultiplicand = 0d;
	
	public Collection<Item> getBlacklistedItems(){
		return ConfigCache.getInstance().getToolsBlacklisted(this::getBlacklisted);
	}
	
	public Collection<Item> getWhitelistedItems(){
		return ConfigCache.getInstance().getToolsWhitelisted(this::getWhitelisted);
	}
}
