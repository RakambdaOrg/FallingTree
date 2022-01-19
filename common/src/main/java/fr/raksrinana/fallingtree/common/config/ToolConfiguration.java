package fr.raksrinana.fallingtree.common.config;

import com.google.gson.annotations.Expose;
import fr.raksrinana.fallingtree.common.FallingTreeCommon;
import fr.raksrinana.fallingtree.common.config.enums.DamageRounding;
import fr.raksrinana.fallingtree.common.wrapper.IItem;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import static java.util.Objects.isNull;

@Data
public class ToolConfiguration{
	@Expose
	@NotNull
	private List<String> allowed = new ArrayList<>();
	@Expose
	@NotNull
	private List<String> denied = new ArrayList<>();
	@Expose
	private boolean preserve = false;
	@Expose
	private boolean ignoreTools = false;
	@Expose
	private boolean requireEnchant = false;
	@Expose
	private double damageMultiplicand = 1d;
	@Expose
	@NotNull
	private DamageRounding damageRounding = DamageRounding.ROUND_DOWN;
	@Expose
	private double speedMultiplicand = 0d;
	
	//Cache
	private Set<IItem> deniedCache;
	private Set<IItem> allowedCache;
	
	@NotNull
	public Collection<IItem> getDeniedItems(@NotNull FallingTreeCommon<?> common){
		if(isNull(deniedCache)){
			deniedCache = common.getAsItems(getDenied());
		}
		return deniedCache;
	}
	
	@NotNull
	public Collection<IItem> getAllowedItems(@NotNull FallingTreeCommon<?> common){
		if(isNull(allowedCache)){
			allowedCache = common.getAsItems(getAllowed());
		}
		return allowedCache;
	}
	
	public void invalidate(){
		deniedCache = null;
		allowedCache = null;
	}
}
