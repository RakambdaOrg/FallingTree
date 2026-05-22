package fr.rakambda.fallingtree.common.tree.builder;

import fr.rakambda.fallingtree.common.tree.Tree;
import fr.rakambda.fallingtree.common.tree.TreePart;
import fr.rakambda.fallingtree.common.tree.TreePartType;
import fr.rakambda.fallingtree.common.wrapper.IBlockPos;
import fr.rakambda.fallingtree.common.wrapper.ILevel;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PROTECTED;

public class ImmutableTree implements Tree{
	@Getter
	@NonNull
	private final ILevel level;
	@Getter
	@NonNull
	private final IBlockPos hitPos;
	@NonNull
	@Getter
	private final Set<TreePart> parts;
	
	@Getter(PROTECTED)
	private final Map<TreePartType, Integer> partCounts;
	@Getter(PROTECTED)
	private final PartOfInterestCache topMostLogCache;
	@Getter(PROTECTED)
	private final PartOfInterestCache bottomMostLogCache;
	@Getter(PROTECTED)
	private final PartOfInterestCache startLogCache;
	
	public ImmutableTree(@NonNull ILevel level, @NonNull IBlockPos hitPos, @NonNull Set<TreePart> parts){
		this(
				level, hitPos, parts,
				new LinkedHashMap<>(),
				new PartOfInterestCache(TreePartType.LOG, parts, s -> s.max(Comparator.comparing(treePart -> treePart.blockPos().getY()))),
				new PartOfInterestCache(TreePartType.LOG, parts, s -> s.min(Comparator.comparing(treePart -> treePart.blockPos().getY()))),
				new PartOfInterestCache(TreePartType.LOG_START, parts, Stream::findFirst)
		);
	}
	
	protected ImmutableTree(@NonNull ILevel level, @NonNull IBlockPos hitPos, @NonNull Set<TreePart> parts, Map<TreePartType, Integer> partCounts, PartOfInterestCache topMostLogCache, PartOfInterestCache bottomMostLogCache, PartOfInterestCache startLogCache){
		this.level = level;
		this.hitPos = hitPos;
		this.parts = parts;
		this.partCounts = partCounts;
		this.topMostLogCache = topMostLogCache;
		this.bottomMostLogCache = bottomMostLogCache;
		this.startLogCache = startLogCache;
	}
	
	@Override
	public int getBreakableCount(){
		return Arrays.stream(TreePartType.getValues())
				.filter(TreePartType::isBreakable)
				.mapToInt(this::getPartCount)
				.sum();
	}
	
	protected int getPartCount(@NonNull TreePartType treePartType){
		return partCounts.computeIfAbsent(treePartType, key -> 0);
	}
	
	@Override
	@NonNull
	public Optional<TreePart> getLastSequencePart(){
		return getParts().stream()
				.max(comparingInt(TreePart::sequence));
	}
	
	@Override
	@NonNull
	public Optional<TreePart> getLastSequenceLogPart(){
		return getParts().stream()
				.filter(part -> part.treePartType().isLog())
				.max(comparingInt(TreePart::sequence));
	}
	
	@Override
	@NonNull
	public Collection<TreePart> getBreakableLogs(){
		return getParts().stream()
				.filter(part -> part.treePartType().isLog())
				.filter(part -> part.treePartType().isBreakable())
				.collect(toSet());
	}
	
	@Override
	@NonNull
	public Collection<TreePart> getBreakableParts(){
		return getParts().stream()
				.filter(part -> part.treePartType().isBreakable())
				.collect(toSet());
	}
	
	@Override
	public int getLogCount(){
		return getPartCount(TreePartType.LOG);
	}
	
	@Override
	@NonNull
	public Optional<IBlockPos> getTopMostLog(){
		return getTopMostLogCache().getPart().map(TreePart::blockPos);
	}
	
	@Override
	@NonNull
	public Optional<IBlockPos> getBottomMostLog(){
		return getBottomMostLogCache().getPart().map(TreePart::blockPos);
	}
	
	@Override
	@NonNull
	public Optional<IBlockPos> getTopMostPart(){
		return getParts().stream()
				.map(TreePart::blockPos)
				.max(comparingInt(IBlockPos::getY));
	}
	
	@Override
	@NonNull
	public Collection<TreePart> getNetherWarts(){
		return getParts().stream()
				.filter(part -> part.treePartType() == TreePartType.NETHER_WART)
				.collect(toSet());
	}
	
	@Override
	@NonNull
	public Collection<TreePart> getMangroveRoots(){
		return getParts().stream()
				.filter(part -> part.treePartType() == TreePartType.MANGROVE_ROOTS)
				.collect(toSet());
	}
	
	@Override
	@NonNull
	public Optional<TreePart> getStart(){
		return getStartLogCache().getPart();
	}
	
	@Override
	@NonNull
	public Stream<TreePart> getPartsStream(){
		return getParts().stream();
	}
}
