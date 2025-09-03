package fr.rakambda.fallingtree.common.config.real;

import com.google.gson.annotations.Expose;
import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.enums.DurabilityMode;
import fr.rakambda.fallingtree.common.wrapper.IItem;
import fr.rakambda.fallingtree.common.config.IResettable;
import fr.rakambda.fallingtree.common.config.IToolConfiguration;
import fr.rakambda.fallingtree.common.config.enums.DamageRounding;
import lombok.Data;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import static java.util.Objects.isNull;

@Data
public class ToolConfiguration implements IToolConfiguration, IResettable{
	@Expose
	@NonNull
	private List<String> allowed = new ArrayList<>();
	@Expose
	@NonNull
	private List<String> denied = new ArrayList<>();
	@Expose
	private DurabilityMode durabilityMode = DurabilityMode.NORMAL;
	@Expose
	private boolean ignoreTools = false;
	@Expose
	private double damageMultiplicand = 1d;
	@Expose
	@NonNull
	private DamageRounding damageRounding = DamageRounding.ROUND_DOWN;
	@Expose
	private double speedMultiplicand = 0d;
	@Expose
	private boolean forceToolUsage = false;
	
	//Cache
	private Set<IItem> deniedCache;
	private Set<IItem> allowedCache;
	
	@NonNull
	public Collection<IItem> getDeniedItems(@NonNull FallingTreeCommon<?> common){
		if(isNull(deniedCache)){
			deniedCache = common.getAsItems(getDenied());
		}
		return deniedCache;
	}
	
	@NonNull
	public Collection<IItem> getAllowedItems(@NonNull FallingTreeCommon<?> common){
		if(isNull(allowedCache)){
			allowedCache = common.getAsItems(getAllowed());
		}
		return allowedCache;
	}
	
	public void reset(){
		deniedCache = null;
		allowedCache = null;
	}
}
