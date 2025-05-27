package fr.rakambda.fallingtree.common.wrapper;

import org.jetbrains.annotations.NotNull;

public interface IBlockBreakEvent extends IWrapper{
	@NotNull
	IBlockPos getBlockPos();
}
