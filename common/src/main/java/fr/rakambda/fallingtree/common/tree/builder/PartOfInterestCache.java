package fr.rakambda.fallingtree.common.tree.builder;

import fr.rakambda.fallingtree.common.tree.TreePart;
import fr.rakambda.fallingtree.common.tree.TreePartType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PartOfInterestCache{
	@NonNull
	@Getter
	private final TreePartType treePartType;
	@NonNull
	private final Collection<TreePart> parts;
	@NonNull
	private final Function<Stream<TreePart>, Optional<TreePart>> selector;
	
	@Nullable
	private TreePart cached = null;
	
	@NonNull
	public Optional<TreePart> getPart(){
		if(cached != null){
			return Optional.of(cached);
		}
		
		cached = selector.apply(parts.stream().filter(treePart -> treePart.treePartType() == treePartType)).orElse(null);
		return Optional.ofNullable(cached);
	}
	
	public void invalidate(){
		cached = null;
	}
	
	public void invalidateIfEqual(@NonNull TreePart treePart){
		if(treePart.equals(cached)){
			invalidate();
		}
	}
}
