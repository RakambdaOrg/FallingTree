package fr.rakambda.fallingtree.common.wrapper;

import org.jspecify.annotations.NonNull;

public interface IBlockBreakEvent extends IWrapper{
	@NonNull
	IBlockPos getBlockPos();
}
