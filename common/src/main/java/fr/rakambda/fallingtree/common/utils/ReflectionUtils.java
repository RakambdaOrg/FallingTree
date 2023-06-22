package fr.rakambda.fallingtree.common.utils;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

@Log4j2
public class ReflectionUtils {
    public static Optional<Field> getField(@NotNull Class<?> klass, @NotNull String... names) {
        var declaredFields = klass.getDeclaredFields();
        for (var name : names) {
            for (var declaredField : declaredFields) {
                if (Objects.equals(name, declaredField.getName())) {
                    return Optional.of(declaredField);
                }
            }
        }
        return Optional.empty();
    }

    public static boolean setField(@NotNull Object object, @NotNull Object value, @NotNull String... names) {
        var fieldOptional = getField(object.getClass(), names);
        if (fieldOptional.isEmpty()) {
            return false;
        }
        var field = fieldOptional.get();

        try {
            field.setAccessible(true);
            field.set(object, value);
            return true;
        } catch (IllegalAccessException e) {
            log.error("Failed to set field {}", field, e);
        }
        return false;
    }
}
