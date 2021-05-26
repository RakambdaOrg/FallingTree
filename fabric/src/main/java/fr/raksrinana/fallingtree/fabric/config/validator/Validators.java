package fr.raksrinana.fallingtree.fabric.config.validator;

import me.shedaniel.autoconfig.ConfigData;
import java.util.Arrays;
import java.util.List;

public class Validators{
	private static final MinRunner MIN_RUNNER = new MinRunner();
	private static final MaxRunner MAX_RUNNER = new MaxRunner();
	private static final MinMaxRunner MIN_MAX_RUNNER = new MinMaxRunner();
	private static final BlockIdRunner BLOCK_ID_RUNNER = new BlockIdRunner();
	private static final ItemIdRunner ITEM_ID_RUNNER = new ItemIdRunner();
	
	public static final List<ValidatorRunner<?>> RUNNERS = Arrays.asList(MIN_RUNNER, MAX_RUNNER, MIN_MAX_RUNNER, BLOCK_ID_RUNNER, ITEM_ID_RUNNER);
	
	private Validators(){}
	
	public static <T> void runValidators(Class<T> categoryClass, T category, String categoryName) throws ConfigData.ValidationException{
		try{
			for(var field : categoryClass.getDeclaredFields()){
				for(var validator : RUNNERS){
					if(!validator.validateIfAnnotated(field, category)){
						throw new ConfigData.ValidationException("FallingTree config field " + categoryName + "." + field.getName() + " is invalid");
					}
				}
			}
		}
		catch(ReflectiveOperationException | RuntimeException e){
			throw new ConfigData.ValidationException(e);
		}
	}
}
