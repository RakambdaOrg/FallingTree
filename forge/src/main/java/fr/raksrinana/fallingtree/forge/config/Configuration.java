package fr.raksrinana.fallingtree.forge.config;

import com.google.gson.annotations.Expose;
import fr.raksrinana.fallingtree.forge.FallingTree;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Configuration{
	private static Configuration INSTANCE;
	
	@Expose
	private TreeConfiguration trees = new TreeConfiguration();
	@Expose
	private ToolConfiguration tools = new ToolConfiguration();
	@Expose
	private boolean reverseSneaking = false;
	@Expose
	private boolean breakInCreative = false;
	@Expose
	private NotificationMode notificationMode = NotificationMode.ACTION_BAR;
	
	public static Configuration getInstance() throws RuntimeException{
		if(Objects.isNull(INSTANCE)){
			var path = getConfigPath();
			try{
				INSTANCE = ConfigLoader.loadConfig(new Configuration(), Configuration.class, path);
			}
			catch(IOException e){
				FallingTree.logger.error("Failed to get FallingTree configuration from {}, using default", path, e);
				INSTANCE = new Configuration();
			}
		}
		return INSTANCE;
	}
	
	public void onUpdate(){
		ConfigCache.getInstance().invalidate();
		
		var path = getConfigPath();
		try{
			ConfigLoader.saveConfig(this, path);
		}
		catch(IOException e){
			FallingTree.logger.error("Failed to saved FallingTree configuration to {}", path, e);
		}
	}
	
	private static Path getConfigPath(){
		return Paths.get(".").resolve("config").resolve("fallingtree.json");
	}
}
