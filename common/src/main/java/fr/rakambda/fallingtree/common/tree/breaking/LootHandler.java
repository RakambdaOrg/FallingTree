package fr.rakambda.fallingtree.common.tree.breaking;

import java.util.concurrent.atomic.AtomicInteger;

public class LootHandler{
	private final int maxDropping;
	
	private AtomicInteger currentlyBroken = new AtomicInteger(0);
	
	public LootHandler(int wantToBreakCount, float trunkLootPercentage){
		maxDropping = (int) Math.ceil(wantToBreakCount * trunkLootPercentage);
	}
	
	/**
	 * @return true if loot should be dropped, false otherwise
	 */
	public boolean breakNewTrunk(){
		return currentlyBroken.accumulateAndGet(1, Integer::sum) <= maxDropping;
	}
}
