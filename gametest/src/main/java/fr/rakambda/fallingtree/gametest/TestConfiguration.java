package fr.rakambda.fallingtree.gametest;

import com.google.gson.annotations.Expose;
import fr.rakambda.fallingtree.common.FallingTreeCommon;
import fr.rakambda.fallingtree.common.config.real.Configuration;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

/**
 * Puts the mod configuration in a known state before a test runs.
 * <p>
 * The configuration is a single global object, and tests in a GameTest batch run in parallel. Tests that need non-default values must therefore use their own
 * test environment (tests are batched per environment), and every test in that environment must apply the same overrides.
 */
public final class TestConfiguration{
	private TestConfiguration(){
	}

	public static void reset(@NonNull FallingTreeCommon<?> mod){
		reset(mod, config -> {});
	}

	/**
	 * Resets the live configuration to its defaults, then applies the overrides.
	 * <p>
	 * Values are copied in place because {@link fr.rakambda.fallingtree.common.config.proxy.ProxyConfiguration} keeps references to the existing sub-configurations.
	 */
	public static void reset(@NonNull FallingTreeCommon<?> mod, @NonNull Consumer<Configuration> overrides){
		var configuration = mod.getOwnConfiguration();
		copyExposedFields(createDefault(), configuration);
		overrides.accept(configuration);

		configuration.getTrees().reset();
		configuration.getTools().reset();
		configuration.getPlayer().reset();
		mod.getProxyConfiguration().reset();
	}

	@NonNull
	private static Configuration createDefault(){
		try{
			var constructor = Configuration.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		}
		catch(ReflectiveOperationException e){
			throw new IllegalStateException("Failed to create default configuration", e);
		}
	}

	private static void copyExposedFields(@NonNull Object source, @NonNull Object target){
		for(Field field : source.getClass().getDeclaredFields()){
			if(Modifier.isStatic(field.getModifiers()) || !field.isAnnotationPresent(Expose.class)){
				continue;
			}
			try{
				field.setAccessible(true);
				var value = field.get(source);
				if(value != null && value.getClass().getPackage() == Configuration.class.getPackage()){
					copyExposedFields(value, field.get(target));
				}
				else{
					field.set(target, value);
				}
			}
			catch(IllegalAccessException e){
				throw new IllegalStateException("Failed to copy configuration field " + field.getName(), e);
			}
		}
	}
}
