package fr.raksrinana.fallingtree.fabric.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConfigLoader{
	private static final Gson gson = new GsonBuilder()
			.disableHtmlEscaping()
			.setPrettyPrinting()
			.excludeFieldsWithoutExposeAnnotation()
			.create();
	
	static <T> T loadConfig(T config, Class<T> clazz, Path path) throws IOException{
		if(Files.isRegularFile(path)){
			try(var reader = Files.newBufferedReader(path)){
				config = gson.fromJson(reader, clazz);
			}
		}
		return saveConfig(config, path);
	}
	
	static <T> T saveConfig(T config, Path path) throws IOException{
		if(!Files.exists(path)){
			Files.createDirectories(path.getParent());
		}
		try(var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
			gson.toJson(config, writer);
		}
		return config;
	}
}
