package fr.rakambda.fallingtree.forge.compat;

import fr.rakambda.fallingtree.common.wrapper.IItemStack;
import fr.rakambda.fallingtree.common.wrapper.IPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TetraCompat{
	private static Map<Class<?>, Optional<Method>> honingProgressMethod = new ConcurrentHashMap<>();
	
	public static void tickHoningProgression(@NotNull IItemStack itemStack, @NotNull IPlayer player){
		try{
			var rawItemStack = (ItemStack) itemStack.getRaw();
			var rawItem = (Item) itemStack.getItem().getRaw();
			var rawPlayer = (Player) player.getRaw();
			
			var itemClass = rawItem.getClass();
			var method = honingProgressMethod.computeIfAbsent(itemClass, TetraCompat::getHoningMethod);
			
			if(method.isPresent()){
				method.get().invoke(rawItem, rawPlayer, rawItemStack, 1);
			}
		}
		catch(Exception e){
			//silence
		}
	}
	
	private static Optional<Method> getHoningMethod(Class<?> klass){
		try{
			return Optional.of(klass.getMethod("tickHoningProgression", LivingEntity.class, ItemStack.class, int.class));
		}
		catch(Exception e){
			return Optional.empty();
		}
	}
}
