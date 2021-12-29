package fr.raksrinana.fallingtree.forge.config;

import com.google.gson.annotations.Expose;
import lombok.Data;
import net.minecraft.world.item.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class ToolConfiguration{
	@Expose
	private List<String> allowed = new ArrayList<>();
	@Expose
	private List<String> denied = new ArrayList<>();
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
	
	public Collection<Item> getDeniedItems(){
		return ConfigCache.getInstance().getToolsDenied(this::getDenied);
	}
	
	public Collection<Item> getAllowedItems(){
		return ConfigCache.getInstance().getToolsAllowed(this::getAllowed);
	}
}
