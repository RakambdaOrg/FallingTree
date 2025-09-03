package fr.rakambda.fallingtree.common.config.real;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jspecify.annotations.NonNull;

public class ConfigLoader{
	private static final Gson gson = new GsonBuilder()
			.disableHtmlEscaping()
			.setPrettyPrinting()
			.excludeFieldsWithoutExposeAnnotation()
			.create();
	
	@NonNull
	static <T> T loadConfig(@NonNull T defaultConfiguration, @NonNull Class<T> clazz, @NonNull Path path) throws IOException{
		var config = defaultConfiguration;
		if(Files.isRegularFile(path)){
			try(var reader = Files.newBufferedReader(path)){
				config = gson.fromJson(reader, clazz);
			}
		}
		if(Objects.isNull(config)){
			throw new IOException("Read null value from config file");
		}
		return saveConfig(config, path);
	}
	
	@NonNull
	static <T> T saveConfig(@NonNull T config, @NonNull Path path) throws IOException{
		if(!Files.exists(path)){
			Files.createDirectories(path.getParent());
		}
		try(var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
			gson.toJson(config, writer);
		}
		return config;
	}
}
