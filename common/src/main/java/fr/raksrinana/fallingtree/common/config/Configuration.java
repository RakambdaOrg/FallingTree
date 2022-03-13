package fr.raksrinana.fallingtree.common.config;

import com.google.gson.annotations.Expose;
import fr.raksrinana.fallingtree.common.config.enums.NotificationMode;
import fr.raksrinana.fallingtree.common.config.enums.SneakMode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Log4j2
public class Configuration{
	@Expose
	@NotNull
	private TreeConfiguration trees = new TreeConfiguration();
	@Expose
	@NotNull
	private ToolConfiguration tools = new ToolConfiguration();
	@Expose
	@NotNull
	private PlayerConfiguration player = new PlayerConfiguration();
	@Expose
	@NotNull
	private EnchantmentConfiguration enchantment = new EnchantmentConfiguration();
	@Expose
	@NotNull
	private SneakMode sneakMode = SneakMode.SNEAK_DISABLE;
	@Expose
	private boolean breakInCreative = false;
	@Expose
	@NotNull
	private NotificationMode notificationMode = NotificationMode.ACTION_BAR;
	
	public static Configuration read() throws RuntimeException{
		var path = getConfigPath();
		try{
			return ConfigLoader.loadConfig(new Configuration(), Configuration.class, path);
		}
		catch(IOException e){
			log.error("Failed to get FallingTree configuration from {}, using default", path, e);
			return new Configuration();
		}
	}
	
	public void onUpdate(){
		trees.invalidate();
		tools.invalidate();
		
		var path = getConfigPath();
		try{
			ConfigLoader.saveConfig(this, path);
		}
		catch(IOException e){
			log.error("Failed to saved FallingTree configuration to {}", path, e);
		}
	}
	
	private static Path getConfigPath(){
		return Paths.get(".").resolve("config").resolve("fallingtree.json");
	}
}
