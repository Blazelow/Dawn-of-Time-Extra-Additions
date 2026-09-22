package com.blazelow.dawnoftimeextras;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.dawnoftime.dawnoftime.block.french.StoneBricksArrowslitBlock;
import org.dawnoftime.dawnoftime.block.japanese.CharredSpruceRailingBlock;
import org.dawnoftime.dawnoftime.block.japanese.LittleFlagBlock;
import org.dawnoftime.dawnoftime.block.templates.SidedWindowBlock;
import org.dawnoftime.dawnoftime.block.templates.LanternBlock;
import org.dawnoftime.dawnoftime.block.templates.PortcullisBlock;
import org.dawnoftime.dawnoftime.block.general.IronColumnBlock;
import net.minecraft.world.item.DyeColor;
import org.dawnoftime.dawnoftime.block.french.StoneBricksMachicolationBlock;
import org.dawnoftime.dawnoftime.util.VoxelShapes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Blocks that fill gaps in Dawn Of Time's own sets, built from their art so they sit beside it.
 *
 * <p>Tatami: {@link #TATAMI_BLOCK} is a static accent tile and {@code tatami_block_extendable}
 * (see {@link TatamiBlockExtendableBlock}) connects seamlessly on all four sides in a room of
 * any shape. Earlier designs built on Dawn Of Time's own 1x2-footprint tatami_mat.png (a plain
 * {@code tatami}/{@code tatami_bordered} pair) and a thin, carpet-shaped extendable mat were
 * removed.
 *
 * <p>Balusters and sided columns: Dawn Of Time ships limestone versions of both. These add
 * quartz and stone, reusing their geometry and their behaviour.
 *
 * <p>Wood balusters: their baluster only comes in waxed oak, so every vanilla wood gets one.
 *
 * <p>Reinforced wrought iron fences: theirs reinforce the ironwork with limestone; these offer
 * stone and quartz instead, for both the black and the golden ironwork.
 *
 * <p>Signs: charred spruce and red painted timber frame, each a sign and a
 * hanging sign - see {@link SignSet}.
 *
 * <p>Futon: theirs is a normal single bed. This adds a 2x2 double that two players can use at
 * once - see {@link DoubleFutonBlock} for why that means two beds rather than one.
 */
public class DawnOfTimeExtras {
    public static final String MOD_ID = "dawnoftimeextras";

    private static final List<Block> CREATIVE_ORDER = new ArrayList<>();
    /**
     * Every registered block, grouped by which Dawn Of Time cultural/design family it was
     * derived from - see {@link ExtraAdditionsCategory}'s own javadoc for how that assignment
     * is made. Built directly off every {@code register(category, name, block)} call, in
     * registration order within each category - never inferred from a block's own id or
     * material at runtime. Used both to build the Extra Additions creative tab's own full
     * (unfiltered) item list and to narrow it to one category at a time in
     * {@link ExtraAdditionsCreativeMixin}.
     */
    public static final Map<ExtraAdditionsCategory, List<Block>> CATEGORY_BLOCKS = new LinkedHashMap<>();
    static {
        for (ExtraAdditionsCategory category : ExtraAdditionsCategory.values()) {
            CATEGORY_BLOCKS.put(category, new ArrayList<>());
        }
    }
    /**
     * The fountain blocks, whose models draw water on a tint index. Without a colour handler
     * that water renders flat white, so the client picks these up and tints them.
     */
    public static final List<Block> WATER_TINTED = new ArrayList<>();
    /**
     * The subset whose inventory icon shows water too. The faucet is left out on purpose: its
     * item model carries a stream on the same tint index, and Dawn Of Time leaves that untinted
     * so the icon is just the tap.
     */
    public static final List<Block> WATER_TINTED_ICONS = new ArrayList<>();
    /**
     * The blocks Dawn Of Time draws on the cutout layer. Their art has holes in it - the fire
     * in a hearth, the gaps in ironwork, the woven edge of a mat - and on the default solid
     * layer those come out as opaque black rather than as holes. Which of ours belong here is
     * decided by which of theirs each is a reskin of, so it is matched by name.
     */
    public static final List<Block> CUTOUT = new ArrayList<>();

    /** Exact names, or a suffix when every material or wood of a piece belongs. */
    private static final String[] CUTOUT_NAMES = {
            "tatami_block", "pale_green_tatami_block", "_fireplace",
            "_crenelation", "_little_flag", "_hanging_noren_flag", "_fancy_lantern", "_portcullis", "_bricks_arrowslit", "_bricks_machicolation", "_wrought_iron_fence", "_irori_fireplace",
            // The wood batch's genuinely glass-holed pieces - copied Dawn Of Time's own
            // "render_type": "cutout" into their model JSON same as everything else here, but
            // that field is a NeoForge extension Fabric never reads (see the comment in
            // DawnOfTimeExtrasClient - it is exactly why Dawn Of Time's own
            // charred_spruce_glass_pane/charred_spruce_window render solid on Fabric too, a
            // gap in their own RenderLayers.java this addon cannot patch). The paper wall
            // family is NOT here on purpose - checked directly, none of its own model files
            // declare "render_type" at all, because its "paper" is fully opaque paint, not a
            // real alpha hole the way glass is - it never needed cutout in the first place.
            // "_window" does still catch "_paper_wall_window" as a side effect of the shared
            // suffix (nothing ends in "_window" that needs to stay off this list), but that is
            // harmless: cutout and solid render identically for a texture with no partial-alpha
            // pixels to begin with.
            "_glass_pane", "_window", "_fancy_railing",
            // Dawn Of Time's own red_paper_lantern is genuinely registered cutout on their own
            // side too (confirmed directly in their decompiled RenderLayers.java,
            // `BlockRenderLayerMap.INSTANCE.putBlock(RED_PAPER_LANTERN, ... .cutout())`) - its
            // texture has real alpha=0 gaps inside the model's own visible area, not just
            // unused atlas space, so this one genuinely needs the same fix rather than being a
            // "solid would look identical anyway" case like the paper wall family above.
            "_paper_lantern", "_cushion"};
    /**
     * Extra lines under a block's name in the menu, keyed by block. Filled here, shown by the
     * client.
     *
     * <p>Only for blocks that would otherwise say nothing. Anything extending a Dawn Of Time
     * class writes its own lines already - their gargoyle and their chimney both do - and
     * adding ours on top printed the whole description twice. The masonry is a plain vanilla
     * {@link Block}, so its note has to come from here.
     */
    public static final Map<Block, String[]> TOOLTIPS = new LinkedHashMap<>();

    private static final String[] CONNECTING_NOTE = {
            "tooltip.dawnoftimeextras.connected_texture_label",
            "tooltip.dawnoftimeextras.connected_texture"};

    private static final String[] COLUMN_NOTE = {
            "tooltip.dawnoftimeextras.column_label",
            "tooltip.dawnoftimeextras.column"};

    // A static accent tile, deliberately not seamless/connecting on its own - no axis state
    // needed, every side already looks the same, so there is nothing for a rotation to
    // change. Renamed from TATAMI_FRAMED (registered id tatami_framed -> tatami_block) on
    // request, 2026-09-14 - a pure rename, no behaviour change; its own visible textures were
    // already retextured to match an isolated tatami_block_extendable in an earlier round,
    // unrelated to this rename. Its former siblings TATAMI/TATAMI_BORDERED (and the whole
    // small_tatami_mat_extendable thin mat) were removed.
    public static final Block TATAMI_BLOCK = new Block(tatami());

    // A pale green colour variant of TATAMI_BLOCK - same static, non-connecting design, same
    // properties, just a different texture.
    public static final Block PALE_GREEN_TATAMI_BLOCK = new Block(tatami());



    /** The same carved column in Dawn Of Time's charred spruce timber frame colours, so it is
     *  timber rather than stone: wood sounds, wood strength, and it burns. */
    public static final Block CHARRED_SPRUCE_SIDED_COLUMN = new SidedColumnBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .ignitedByLava());

    /** Dawn Of Time ships only the light grey futon; these are the other dye colours. */
    /**
     * The materials every piece that comes in more than quartz and stone is offered in. Adding
     * one here is the whole job -
     * every family below is registered off this list.
     */
    private static final String[] EXTRA_STONES = {"deepslate", "tuff", "granite", "andesite", "diorite", "basalt", "calcite", "dripstone", "netherrack", "blackstone", "end_stone", "prismarine", "obsidian", "dark_prismarine", "sandstone", "purpur", "red_sandstone"};

    /** The named stones, then every extra one. */
    private static String[] stones(String... base) {
        String[] all = Arrays.copyOf(base, base.length + EXTRA_STONES.length);
        System.arraycopy(EXTRA_STONES, 0, all, base.length, EXTRA_STONES.length);
        return all;
    }

    /** The map colour each of our stones shows from above, and on a map. */
    private static final Map<String, MapColor> STONE_COLOURS = Map.ofEntries(
            Map.entry("quartz", MapColor.QUARTZ),
            Map.entry("limestone", MapColor.SAND),
            Map.entry("stone", MapColor.STONE),
            Map.entry("deepslate", MapColor.DEEPSLATE),
            Map.entry("tuff", MapColor.TERRACOTTA_GRAY),
            Map.entry("granite", MapColor.DIRT),
            Map.entry("andesite", MapColor.STONE),
            Map.entry("diorite", MapColor.QUARTZ),
            Map.entry("basalt", MapColor.COLOR_BLACK),
            Map.entry("calcite", MapColor.TERRACOTTA_WHITE),
            Map.entry("dripstone", MapColor.PODZOL),
            Map.entry("netherrack", MapColor.NETHER),
            Map.entry("blackstone", MapColor.COLOR_BLACK),
            Map.entry("end_stone", MapColor.SAND),
            Map.entry("prismarine", MapColor.COLOR_CYAN),
            Map.entry("obsidian", MapColor.COLOR_BLACK),
            Map.entry("dark_prismarine", MapColor.COLOR_CYAN),
            Map.entry("sandstone", MapColor.SAND),
            Map.entry("purpur", MapColor.COLOR_MAGENTA),
            Map.entry("red_sandstone", MapColor.COLOR_ORANGE));

    private static final String[] DYES = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "cyan", "purple", "blue", "brown", "green", "red", "black"};

    /** All sixteen, for the painted wave - unlike {@link #DYES} this includes light grey, since
     *  there is no existing light grey wave to leave out in its place. */
    private static final String[] PAINTED_DYES = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};

    /** Vanilla's own map colour for each dye, so the wave's colour reads the same on a map. */
    private static final Map<String, MapColor> DYE_MAP_COLOURS = Map.ofEntries(
            Map.entry("white", MapColor.SNOW),
            Map.entry("orange", MapColor.COLOR_ORANGE),
            Map.entry("magenta", MapColor.COLOR_MAGENTA),
            Map.entry("light_blue", MapColor.COLOR_LIGHT_BLUE),
            Map.entry("yellow", MapColor.COLOR_YELLOW),
            Map.entry("lime", MapColor.COLOR_LIGHT_GREEN),
            Map.entry("pink", MapColor.COLOR_PINK),
            Map.entry("gray", MapColor.COLOR_GRAY),
            Map.entry("light_gray", MapColor.COLOR_LIGHT_GRAY),
            Map.entry("cyan", MapColor.COLOR_CYAN),
            Map.entry("purple", MapColor.COLOR_PURPLE),
            Map.entry("blue", MapColor.COLOR_BLUE),
            Map.entry("brown", MapColor.COLOR_BROWN),
            Map.entry("green", MapColor.COLOR_GREEN),
            Map.entry("red", MapColor.COLOR_RED),
            Map.entry("black", MapColor.COLOR_BLACK));

    /** Every vanilla wood in 1.21.1, each getting the baluster Dawn Of Time only gave waxed oak. */
    private static final String[] WOODS = {
            "oak", "spruce", "birch", "jungle", "acacia", "dark_oak",
            "mangrove", "cherry", "bamboo", "crimson", "warped"};




    public static final Block DOUBLE_FUTON = new DoubleFutonBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(0.2F)
            .sound(SoundType.WOOL)
            .noOcclusion()
            .ignitedByLava());

    private static BlockBehaviour.Properties tatami() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.SAND)
                .strength(0.8F)
                .sound(SoundType.WOOL);
    }


    /** Nether woods do not burn and the odd ones out have their own footstep sounds. */
    private static BlockBehaviour.Properties futon() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOL)
                .strength(0.2F)
                .sound(SoundType.WOOL)
                .noOcclusion()
                .ignitedByLava();
    }

    private static BlockBehaviour.Properties wood(String name) {
        boolean nether = name.equals("crimson") || name.equals("warped");
        SoundType sound = switch (name) {
            case "bamboo" -> SoundType.BAMBOO_WOOD;
            case "cherry" -> SoundType.CHERRY_WOOD;
            case "crimson", "warped" -> SoundType.NETHER_WOOD;
            default -> SoundType.WOOD;
        };
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(WOOD_COLOURS.getOrDefault(name, MapColor.WOOD))
                .strength(2.0F, 3.0F)
                .sound(sound)
                .noOcclusion();
        return nether ? properties : properties.ignitedByLava();
    }

    private static final java.util.Map<String, MapColor> WOOD_COLOURS = java.util.Map.of(
            "spruce", MapColor.PODZOL,
            "birch", MapColor.SAND,
            "jungle", MapColor.DIRT,
            "acacia", MapColor.COLOR_ORANGE,
            "dark_oak", MapColor.COLOR_BROWN,
            "mangrove", MapColor.COLOR_RED,
            "cherry", MapColor.COLOR_PINK,
            "bamboo", MapColor.COLOR_YELLOW,
            "crimson", MapColor.CRIMSON_STEM,
            "warped", MapColor.WARPED_STEM);

    /** A fire burning in the block lights the room, at a campfire's brightness. */
    private static BlockBehaviour.Properties hearth(BlockBehaviour.Properties properties) {
        return properties.lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0);
    }

    /**
     * A solid stone cube. Distinct from {@link #stone} in that it keeps its occlusion: that one
     * is for the carved pieces, whose models do not fill their block and so must not cull the
     * faces around them.
     */
    private static BlockBehaviour.Properties solidStone(MapColor colour) {
        return BlockBehaviour.Properties.of()
                .mapColor(colour)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE);
    }

    private static BlockBehaviour.Properties stone(MapColor colour) {
        return BlockBehaviour.Properties.of()
                .mapColor(colour)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE)
                .noOcclusion();
    }

    /**
     * Registers every block and item. Loader-neutral: the loader entry points call this at the
     * right moment (Fabric's initialiser, NeoForge's RegisterEvent) and then register the tab
     * from {@link #tabItems()} themselves, since a creative tab builder differs per loader.
     */
    public static void init() {
        if (initialised) {
            return;
        }
        initialised = true;
        register(ExtraAdditionsCategory.JAPANESE, "tatami_block", TATAMI_BLOCK);
        // The old `tatami`/`tatami_bordered` (a 1x2-footprint mat built from Dawn Of Time's
        // own tatami_mat.png) and `small_tatami_mat_extendable` (a thin, carpet-shaped mat
        // with real per-instance connecting geometry, built by extending Dawn Of Time's own
        // small_tatami_mat) were removed entirely on request, 2026-09-14 - superseded by
        // `tatami_block`/`tatami_block_extendable` below, which cover the same "connects
        // seamlessly in a room" idea with a genuine full block instead of a thin carpet-style
        // one.
        //
        // The thin mat always left anything placed on it floating a full block up - its own
        // collision shape (Block.box(0,0,0,16,1,16), decompiled) was identical to vanilla's
        // own CarpetBlock, and Minecraft always places a new block in the next full grid cell
        // above a non-replaceable block regardless of how thin its actual shape is. Rather
        // than keep re-patching the thin mat's own shape (trades away its own carpet-like
        // walkability), `tatami_block_extendable` is a genuine full block instead - same
        // connecting behaviour, `tatami()`'s own properties (matches this mod's other
        // floor-tile pieces). Renamed from small_tatami_block_extendable on request,
        // 2026-09-14 - a pure rename, no behaviour change.
        register(ExtraAdditionsCategory.JAPANESE, "tatami_block_extendable",
                new TatamiBlockExtendableBlock(tatami()));
        // Pale colour variants - same classes, same properties, only the texture differs.
        register(ExtraAdditionsCategory.JAPANESE, "pale_green_tatami_block", PALE_GREEN_TATAMI_BLOCK);
        register(ExtraAdditionsCategory.JAPANESE, "pale_green_tatami_block_extendable",
                new TatamiBlockExtendableBlock(tatami()));
        for (String stone : stones("quartz", "stone")) {
            register(ExtraAdditionsCategory.FRENCH, stone + "_baluster", new BalusterBlock(stone(STONE_COLOURS.get(stone))));
        }
        for (String stone : stones("quartz", "stone")) {
            register(ExtraAdditionsCategory.FRENCH, stone + "_sided_column",
                    new SidedColumnBlock(stone(STONE_COLOURS.get(stone))));
        }
        register(ExtraAdditionsCategory.FRENCH, "charred_spruce_sided_column", CHARRED_SPRUCE_SIDED_COLUMN);
        register(ExtraAdditionsCategory.JAPANESE, "light_gray_double_futon", DOUBLE_FUTON);
        for (String dye : DYES) {
            register(ExtraAdditionsCategory.JAPANESE, dye + "_futon", new FutonBlock(futon()));
            register(ExtraAdditionsCategory.JAPANESE, dye + "_double_futon", new DoubleFutonBlock(futon()));
        }
        for (String stone : stones("stone", "quartz", "limestone")) {
            // Dawn Of Time's own sandstone crenelation is this whole piece's source art, so a
            // "sandstone" one of ours would just be a copy of it - the exact
            // block they already ship, registered a second time under our name.
            if (stone.equals("sandstone")) {
                continue;
            }
            register(ExtraAdditionsCategory.PERSIAN, stone + "_crenelation",
                    new CrenelationBlock(stone(STONE_COLOURS.get(stone))));
        }
        // Dawn Of Time's own stone brick arrowslit and machicolation, with their own collision
        // shapes. Plain stone's are theirs already, so it is left out.
        // Dawn Of Time's lattice windows: their own SidedWindowBlock with their own shapes and the
        // properties they give it (a copy of glass). Stone brick ones for quartz and every extra
        // stone, waxed oak ones for every vanilla wood and charred spruce.
        for (String stone : stones("quartz")) {
            register(ExtraAdditionsCategory.GERMAN, "lattice_" + stone + "_bricks_window", new SidedWindowBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), VoxelShapes.SIDED_WINDOW_SHAPES));
        }
        for (String wood : WOODS) {
            register(ExtraAdditionsCategory.GERMAN, "lattice_" + wood + "_window", new SidedWindowBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), VoxelShapes.SIDED_WINDOW_SHAPES));
        }
        register(ExtraAdditionsCategory.GERMAN, "lattice_charred_spruce_window", new SidedWindowBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), VoxelShapes.SIDED_WINDOW_SHAPES));
        // Dawn Of Time's iron fancy lantern, column and portcullis, recoloured to other metals: their
        // own classes, shapes and properties (read from their registry, a copy of iron bars/door).
        // All three metals get all three pieces.
        for (String material : new String[]{"gold", "diamond", "netherite"}) {
            register(ExtraAdditionsCategory.GERMAN, material + "_fancy_lantern", new LanternBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).noOcclusion().lightLevel(state -> 15),
                    VoxelShapes.IRON_FANCY_LANTERN_SHAPES));
            register(ExtraAdditionsCategory.GERMAN, material + "_column", new IronColumnBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));
            register(ExtraAdditionsCategory.GERMAN, material + "_portcullis", new PortcullisBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)));
        }
        // A noren flag that hangs and extends downwards (this mod's own, see HangingNorenFlagBlock).
        for (String colour : new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
                "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"}) {
            register(ExtraAdditionsCategory.JAPANESE, colour + "_hanging_noren_flag", new HangingNorenFlagBlock(
                    BlockBehaviour.Properties.of().mapColor(DyeColor.byName(colour, DyeColor.WHITE))
                            .strength(0.3F).sound(SoundType.WOOL).noOcclusion().noCollission()));
        }
        // Dawn Of Time's noren flag comes in white only; these are the other fifteen dyes.
        for (String colour : new String[]{"orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"}) {
            register(ExtraAdditionsCategory.JAPANESE, colour + "_little_flag", new LittleFlagBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                            .mapColor(DyeColor.byName(colour, DyeColor.WHITE)).noOcclusion()));
        }
        for (String stone : stones("quartz")) {
            register(ExtraAdditionsCategory.FRENCH, stone + "_bricks_arrowslit", new StoneBricksArrowslitBlock(
                    stone(STONE_COLOURS.get(stone)), VoxelShapes.STONE_BRICKS_ARROWSLIT_SHAPES));
            register(ExtraAdditionsCategory.FRENCH, stone + "_bricks_machicolation", new StoneBricksMachicolationBlock(
                    stone(STONE_COLOURS.get(stone)), VoxelShapes.STONE_BRICKS_MACHICOLATION_SHAPES));
        }
        for (String stone : stones("quartz", "stone", "limestone")) {
            Block column = register(ExtraAdditionsCategory.PRE_COLOMBIAN, stone + "_column",
                    new ColumnBlock(stone(STONE_COLOURS.get(stone))));
            TOOLTIPS.put(column, COLUMN_NOTE);
        }
        // Masonry comes as a family - the block, then the five shapes Dawn Of Time cut
        // from theirs. Stairs, slab and wall are vanilla's own classes; the plate and edge
        // are theirs, and ours are in PlateBlock and EdgeBlock.
        for (String stone : stones("quartz", "limestone")) {
            MapColor colour = STONE_COLOURS.get(stone);
            Block masonry = register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry", new Block(solidStone(colour)));
            // Only the cube says so. The shapes cut from it knit just the same, but Dawn Of
            // Time notes it on the block alone and a line on every piece is noise.
            TOOLTIPS.put(masonry, CONNECTING_NOTE);
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_stairs",
                    new StairBlock(masonry.defaultBlockState(), solidStone(colour)));
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_slab", new SlabBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_wall", new WallBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_plate", new PlateBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_edge", new EdgeBlock(solidStone(colour)));
        }
        // Polished limestone: neither vanilla nor Dawn Of Time has one. Same five shapes as their
        // cobbled_limestone (French family), so it files under French. Two families: the plain
        // one, and a "(Connecting)" one whose pieces join their neighbours through Fusion.
        {
            MapColor colour = STONE_COLOURS.get("limestone");
            Block polished = register(ExtraAdditionsCategory.FRENCH, "polished_limestone", new Block(solidStone(colour)));
            register(ExtraAdditionsCategory.FRENCH, "polished_limestone_slab", new SlabBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.FRENCH, "polished_limestone_stairs",
                    new StairBlock(polished.defaultBlockState(), solidStone(colour)));
            register(ExtraAdditionsCategory.FRENCH, "polished_limestone_plate", new PlateBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.FRENCH, "polished_limestone_edge", new EdgeBlock(solidStone(colour)));
        }
        {
            MapColor colour = STONE_COLOURS.get("limestone");
            Block connecting = register(ExtraAdditionsCategory.FRENCH, "polished_limestone_connecting",
                    new Block(solidStone(colour)));
            TOOLTIPS.put(connecting, CONNECTING_NOTE);
            register(ExtraAdditionsCategory.FRENCH, "polished_limestone_connecting_slab", new SlabBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.FRENCH, "polished_limestone_connecting_stairs",
                    new StairBlock(connecting.defaultBlockState(), solidStone(colour)));
            register(ExtraAdditionsCategory.FRENCH, "polished_limestone_connecting_plate", new PlateBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.FRENCH, "polished_limestone_connecting_edge", new EdgeBlock(solidStone(colour)));
        }
        // Chimneys stack like the columns; gargoyles just face. Dawn Of Time already covers
        // limestone for the gargoyle and has its own limestone chimney on a different design,
        // so these fill what each of them is missing rather than repeating it.
        for (String stone : stones("quartz", "limestone")) {
            register(ExtraAdditionsCategory.GERMAN, stone + "_chimney", new ChimneyBlock(stone(STONE_COLOURS.get(stone))));
        }
        // The hearth those chimneys draw for. Dawn Of Time build theirs in stone bricks and in
        // limestone - two different designs of the same block - so each was missing from the
        // other's family: quartz, stone and the extras get limestone's design below, and
        // limestone gets this, the stone bricks one, here. No tooltip on either: their block
        // writes its own, and ours would only repeat it.
        for (String stone : stones("quartz", "limestone")) {
            register(ExtraAdditionsCategory.GERMAN, stone + "_german_fireplace",
                    new FireplaceBlock(hearth(stone(STONE_COLOURS.get(stone)))));
        }
        // Vanilla has no obsidian slab, stairs or wall; the other obsidian recipes need a real slab.
        register(ExtraAdditionsCategory.GERMAN, "obsidian_slab",
                new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)));
        register(ExtraAdditionsCategory.GERMAN, "obsidian_stairs",
                new StairBlock(Blocks.OBSIDIAN.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)));
        register(ExtraAdditionsCategory.GERMAN, "obsidian_wall",
                new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).forceSolidOn()));
        for (String stone : stones("quartz", "stone")) {
            register(ExtraAdditionsCategory.FRENCH, stone + "_french_fireplace",
                    new FireplaceBlock(hearth(stone(STONE_COLOURS.get(stone)))));
        }
        for (String stone : stones("stone", "quartz")) {
            register(ExtraAdditionsCategory.FRENCH, stone + "_gargoyle", new GargoyleBlock(stone(STONE_COLOURS.get(stone))));
        }
        // The fountain: a sunken basin, a raised one, and the jet that feeds either.
        for (String basin : stones("quartz", "limestone")) {
            MapColor colour = STONE_COLOURS.get(basin);
            for (Block basinPiece : new Block[]{
                    register(ExtraAdditionsCategory.GERMAN, basin + "_pool", new PoolBlock(stone(colour))),
                    register(ExtraAdditionsCategory.GERMAN, basin + "_small_pool", new SmallPoolBlock(stone(colour))),
                    register(ExtraAdditionsCategory.GERMAN, basin + "_water_jet", new WaterJetBlock(stone(colour)))}) {
                WATER_TINTED.add(basinPiece);
                WATER_TINTED_ICONS.add(basinPiece);
            }
            WATER_TINTED.add(register(ExtraAdditionsCategory.GERMAN, basin + "_faucet", new FaucetBlock(stone(colour))));
        }
        for (String wood : WOODS) {
            register(ExtraAdditionsCategory.GERMAN, wood + "_baluster", new BalusterBlock(wood(wood)));
            register(ExtraAdditionsCategory.FRENCH, wood + "_sided_column", new SidedColumnBlock(wood(wood)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_irori_fireplace", new IroriFireplaceBlock(hearth(wood(wood))));
            // Dawn Of Time's own timber frame is charred_spruce (plus red_painted); this mod's
            // eleven fill out the rest of the vanilla woods, following that pair's simpler
            // "japanese" design (plain block + a plain RotatedPillarBlock post) rather than
            // waxed_oak's unrelated "german" one (corner/crossed/squared shapes, a connected
            // vertical-post pillar), so the two families aren't interchangeable.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_timber_frame", new Block(wood(wood)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_timber_frame_pillar", new RotatedPillarBlock(wood(wood)));
            // Glass pane, every wood including spruce - Dawn Of Time has no native
            // spruce_glass_pane (charred_spruce is their only one), so nothing to skip here.
            // Properties are Dawn Of Time's own exact choice, decompiled:
            // `Properties.ofFullCopy(Blocks.GLASS)`.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_glass_pane", new GlassPaneBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
            // Fancy railing, every wood - Dawn Of Time never shipped a native
            // spruce_fancy_railing, so nothing to skip. Properties are Dawn Of Time's own
            // exact choice again, decompiled: the same
            // `Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_BLACK)
            // .strength(2.0F, 6.0F).noOcclusion()` the legless chair uses.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_fancy_railing", new FancyRailingBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).noOcclusion()));
            // Railing, every wood - Dawn Of Time's own CharredSpruceRailingBlock, used as it is,
            // with the same properties as their fancy railing.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_railing", new CharredSpruceRailingBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).noOcclusion()));
            // Window, every wood - Dawn Of Time's own is a bare vanilla TransparentBlock (the
            // same class their glass block uses), decompiled and confirmed, so no wrapper
            // class of this mod's own is needed here at all.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_window", new TransparentBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
            // Shutters + tall shutters, every wood - same Dawn Of Time properties as the
            // legless chair/fancy railing, decompiled from their own registration for both.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_shutters", new ShuttersBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).noOcclusion()));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_tall_shutters", new TallShuttersBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).noOcclusion()));
            // Paper wall family, every wood - Dawn Of Time's own five blocks have no material
            // variants at all to skip. Properties are Dawn Of Time's own exact choice,
            // decompiled: `Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F)`, the
            // same as the paper door - a fresh instance per block, since a Properties object
            // is consumed by the block it is passed to. Plain paper_wall is their own
            // BottomPaneBlock (it alone has a bottom-row model); squared/window/flowery/flat
            // all share their own PillarPaneBlock, one shared class for all four confirmed
            // directly in their registry rather than assumed from the four separate ids.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_paper_wall", new PaperWallBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_paper_wall_squared", new PillarPaperWallBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_paper_wall_window", new PillarPaperWallBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_paper_wall_flowery", new PillarPaperWallBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_paper_wall_flat", new PillarPaperWallBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F)));
            // Sliding paper door - Dawn Of Time's *other* paper door, the material-independent
            // one, distinct from the wood-framed paper_door registered below. Same
            // PaperDoorBlock wrapper, same properties - Dawn Of Time uses the identical
            // Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F) for both of its
            // own paper doors. No wood skipped - this design has no native per-wood version
            // to conflict with.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_sliding_paper_door", new PaperDoorBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F)));
            // Foundation column (the log-ended design, + its slab) - every wood, no exception:
            // this design has no native spruce version to conflict with either. Same
            // FoundationBlock as the plain design (Dawn Of Time uses the identical Java class
            // for both of its own foundation shapes), plus FoundationSlabBlock for the slab -
            // same properties again, decompiled from Dawn Of Time's own
            // charred_spruce_foundation_slab registration.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_foundation_column", new FoundationBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_foundation_column_slab", new FoundationSlabBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
        }
        // Roof support, in every vanilla wood but spruce - Dawn Of Time already ships
        // spruce_roof_support natively, so it is skipped here. A separate explicit list rather than
        // filtering WOODS with an `if` at runtime, like the painted_stone gap colours above.
        //
        // Properties are Dawn Of Time's own exact choice for this block, decompiled and
        // confirmed directly rather than guessed: `Properties.ofFullCopy(Blocks.STONE_BRICKS)
        // .noOcclusion()`, for every one of their own four materials including the three wood
        // ones - stone hardness and sound, not flammable, on every material, not just this
        // mod's new woods. Not this file's own wood() helper, which would invent a softer,
        // flammable version Dawn Of Time never actually built.
        String[] woodsExceptSpruce = {
                "oak", "birch", "jungle", "acacia", "dark_oak",
                "mangrove", "cherry", "bamboo", "crimson", "warped"};
        for (String wood : woodsExceptSpruce) {
            register(ExtraAdditionsCategory.JAPANESE, wood + "_roof_support", new RoofSupportBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
            // Paper door, same spruce exception, same reason. Properties are Dawn Of Time's
            // own exact choice, decompiled: `Properties.ofFullCopy(Blocks.WHITE_WOOL)
            // .strength(1.5F, 1.5F)` with BlockSetType.BAMBOO (see PaperDoorBlock.java) - not
            // the frame wood's own set, matched exactly rather than assumed to follow the
            // frame material.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_paper_door", new PaperDoorBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                            .strength(1.5F, 1.5F)));
            // Foundation (the plain cube design) - spruce itself already has this natively;
            // charred_spruce does not (only the column design below), registered separately
            // just after this loop. Properties are Dawn Of Time's own exact choice for both
            // of its own foundation designs, decompiled: `Properties.ofFullCopy(Blocks.OAK_WOOD)
            // .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)`, flammable (fire 2/3) via
            // FoundationBlock's own setBurnable call.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_foundation", new FoundationBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
            // Boards (the parquet-style plank block) - spruce itself already has this
            // natively, same reason roof support/legless chair/paper door/foundation all
            // skip it. Base block is a BoardsBlock (RotatedPillarBlockDoT, axis-rotatable
            // like a log) - Dawn Of Time's own spruce_boards class, not the plain BlockDoT
            // their charred_spruce_boards uses instead (a different design on their own
            // side, decompiled and confirmed). Properties decompiled directly:
            // `Properties.ofFullCopy(Blocks.OAK_WOOD).strength(3.0F, 5.0F)`, no mapColor
            // override - unlike the rest of this wood family, which forces COLOR_BLACK.
            Block boards = register(ExtraAdditionsCategory.JAPANESE, wood + "_boards", new BoardsBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(3.0F, 5.0F)));
            // Boards' own edge/slab/stairs/plate - Dawn Of Time's own exact choice for all
            // four, decompiled: edge, slab and plate all get `Properties.ofFullCopy(
            // Blocks.OAK_WOOD).mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)`, matching
            // every other flammable wood piece in this mod; stairs gets a plain
            // `Properties.ofFullCopy(Blocks.OAK_WOOD)` with no override at all. Plate reuses
            // this mod's own existing PlateBlock (already used by the masonry family) rather
            // than a new wrapper - it matches Dawn Of Time's own plate shape exactly.
            register(ExtraAdditionsCategory.JAPANESE, wood + "_boards_edge", new EdgeBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_boards_slab", new BoardsSlabBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_boards_stairs", new BoardsStairsBlock(() -> boards,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_boards_plate", new PlateBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
        }
        register(ExtraAdditionsCategory.JAPANESE, "charred_spruce_foundation", new FoundationBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                        .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
        // charred_spruce_boards itself is Dawn Of Time's own native block - but they never
        // gave it an edge/slab/stairs/plate the way spruce_boards has all four (checked
        // directly, confirmed a genuine gap, same shape as the paper door/foundation gaps
        // just above - plate added separately after Blazelow flagged it was missed the first
        // round). Its base block reference is looked up the same deferred way
        // RoofSupportBlock's gray_roof_tiles_slab is, so load order between the two mods
        // doesn't matter.
        Supplier<Block> charredSpruceBoards = () -> BuiltInRegistries.BLOCK.get(
                ResourceLocation.fromNamespaceAndPath("dawnoftimebuilder", "charred_spruce_boards"));
        register(ExtraAdditionsCategory.JAPANESE, "charred_spruce_boards_edge", new EdgeBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                        .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
        register(ExtraAdditionsCategory.JAPANESE, "charred_spruce_boards_slab", new BoardsSlabBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                        .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
        register(ExtraAdditionsCategory.JAPANESE, "charred_spruce_boards_stairs", new BoardsStairsBlock(charredSpruceBoards,
                BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
        register(ExtraAdditionsCategory.JAPANESE, "charred_spruce_boards_plate", new PlateBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                        .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
        // charred_spruce has no native paper door at all (unlike roof support/legless
        // chair/timber frame, which Dawn Of Time already ships for it) - a genuine gap,
        // registered separately the same way charred_spruce_foundation is just above.
        register(ExtraAdditionsCategory.JAPANESE, "charred_spruce_paper_door", new PaperDoorBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                        .strength(1.5F, 1.5F)));
        // Dawn Of Time's own irori is spruce; ours add the rest of the woods and their charred
        // spruce, which has no vanilla plank to take a colour from.
        register(ExtraAdditionsCategory.JAPANESE, "charred_spruce_irori_fireplace", new IroriFireplaceBlock(
                hearth(wood("spruce").mapColor(MapColor.TERRACOTTA_WHITE))));
        for (String metal : new String[]{"black", "golden"}) {
            for (String stone : stones("stone", "quartz")) {
                register(ExtraAdditionsCategory.FRENCH, stone + "_reinforced_" + metal + "_wrought_iron_fence",
                        new ReinforcedFenceBlock(BlockBehaviour.Properties.of()
                                .mapColor(STONE_COLOURS.get(stone))
                                .strength(2.0F, 6.0F)
                                .requiresCorrectToolForDrops()
                                .sound(SoundType.METAL)
                                .noOcclusion()));
            }
        }

        // Dawn Of Time's own painted_stone only exists in five colours (blue, green, red,
        // white, yellow - the ones its own art was built from) - the other eleven get registered here so the wave recipe below can
        // craft from a real painted_stone in every colour, the way Dawn Of Time's own
        // red_painted_blue_wave recipe crafts from two painted_stone blocks rather than
        // wool+terracotta. A separate explicit list rather than filtering PAINTED_DYES at
        // runtime, so Dawn Of Time's own five colours are not registered a second time.
        String[] paintedStoneGaps = {
                "orange", "magenta", "light_blue", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "brown", "black"};
        for (String colour : paintedStoneGaps) {
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_stone", new Block(solidStone(DYE_MAP_COLOURS.get(colour))));
        }

        // Dawn Of Time ships exactly one combination of this natively - a red background with
        // a blue wave carved into it (dawnoftimebuilder:red_painted_blue_wave) - so that one
        // is skipped here rather than duplicated under this mod's own namespace. This comment
        // said as much before 2026-09-13 but the loop itself never actually skipped it -
        // Blazelow found two "Red Painted Blue Wave" entries side by side in the creative
        // menu, one from each mod, before this `continue` was added. Every other pairing of
        // the sixteen dye colours, background and wave independently, fills out the rest of
        // what their own art already made possible.
        for (String base : PAINTED_DYES) {
            for (String wave : PAINTED_DYES) {
                if (base.equals("red") && wave.equals("blue")) {
                    continue;
                }
                register(ExtraAdditionsCategory.PRE_COLOMBIAN, base + "_painted_" + wave + "_wave",
                        new Block(solidStone(DYE_MAP_COLOURS.get(base))));
            }
        }

        // Paper lantern - Dawn Of Time's own red_paper_lantern, in every other vanilla dye
        // colour. Properties decompiled directly: `Properties.ofFullCopy(Blocks.RED_WOOL)
        // .noOcclusion().noCollission().lightLevel(state -> 12)` - copied from that exact
        // colour's own wool block (not a fixed base like most other pieces in this mod use),
        // looked up by name since vanilla has no colour-indexed array of wool blocks to pull
        // from directly.
        for (String colour : PAINTED_DYES) {
            if (colour.equals("red")) {
                continue;
            }
            Block wool = BuiltInRegistries.BLOCK.get(
                    ResourceLocation.fromNamespaceAndPath("minecraft", colour + "_wool"));
            register(ExtraAdditionsCategory.JAPANESE, colour + "_paper_lantern", new PaperLanternColourBlock(
                    BlockBehaviour.Properties.ofFullCopy(wool)
                            .noOcclusion().noCollission().lightLevel(state -> 12)));
        }

        // Cushions - Dawn Of Time's own white_cushion in every other vanilla dye colour. Properties
        // are theirs (oak wood base, strength 2/6, no occlusion), with the map colour taken from
        // that colour's own wool.
        for (String colour : PAINTED_DYES) {
            if (colour.equals("white")) {
                continue;
            }
            Block wool = BuiltInRegistries.BLOCK.get(
                    ResourceLocation.fromNamespaceAndPath("minecraft", colour + "_wool"));
            register(ExtraAdditionsCategory.JAPANESE, colour + "_cushion", new CushionBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(wool.defaultMapColor()).strength(2.0F, 6.0F).noOcclusion()));
        }

        new SignSet("charred_spruce", BlockSetType.SPRUCE, MapColor.TERRACOTTA_WHITE);
        new SignSet("red_painted_timber_frame", BlockSetType.ACACIA, MapColor.COLOR_RED);
        // The timber frame signs in every vanilla wood (literal names, one set each, so the asset
        // checker can see them). Colour and sound set follow the wood.
        new SignSet("oak_timber_frame", BlockSetType.OAK, Blocks.OAK_PLANKS.defaultMapColor());
        new SignSet("spruce_timber_frame", BlockSetType.SPRUCE, Blocks.SPRUCE_PLANKS.defaultMapColor());
        new SignSet("birch_timber_frame", BlockSetType.BIRCH, Blocks.BIRCH_PLANKS.defaultMapColor());
        new SignSet("jungle_timber_frame", BlockSetType.JUNGLE, Blocks.JUNGLE_PLANKS.defaultMapColor());
        new SignSet("acacia_timber_frame", BlockSetType.ACACIA, Blocks.ACACIA_PLANKS.defaultMapColor());
        new SignSet("dark_oak_timber_frame", BlockSetType.DARK_OAK, Blocks.DARK_OAK_PLANKS.defaultMapColor());
        new SignSet("mangrove_timber_frame", BlockSetType.MANGROVE, Blocks.MANGROVE_PLANKS.defaultMapColor());
        new SignSet("cherry_timber_frame", BlockSetType.CHERRY, Blocks.CHERRY_PLANKS.defaultMapColor());
        new SignSet("bamboo_timber_frame", BlockSetType.BAMBOO, Blocks.BAMBOO_PLANKS.defaultMapColor());
        new SignSet("crimson_timber_frame", BlockSetType.CRIMSON, Blocks.CRIMSON_PLANKS.defaultMapColor());
        new SignSet("warped_timber_frame", BlockSetType.WARPED, Blocks.WARPED_PLANKS.defaultMapColor());
        // Both sign sets are built from Dawn Of Time's own "japanese" timber-frame art
        // (charred_spruce, red_painted) - see ExtraAdditionsCategory's own javadoc. SignSet
        // registers through its own local register() method rather than this file's, so it
        // never reached CREATIVE_ORDER/CATEGORY_BLOCKS on its own; added here explicitly,
        // right after both sets exist, the same items the old BUILDING_BLOCKS population below
        // used to add by hand (sign + hanging sign only, matching vanilla convention of never
        // showing the wall-mounted variant in creative).
        for (SignSet set : SignSet.ALL) {
            CATEGORY_BLOCKS.get(ExtraAdditionsCategory.JAPANESE).add(set.sign);
            CATEGORY_BLOCKS.get(ExtraAdditionsCategory.JAPANESE).add(set.hangingSign);
        }

        // The Extra Additions creative tab itself is registered by each loader (a creative tab
        // builder is loader-specific), from tabItems() and TAB_ICON below.
        TAB_ICON = Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "creative_tab_icon"), new Item(new Item.Properties()));
    }

    private static boolean initialised = false;

    /** The tab's icon: a texture of its own (64x64) shown as an item, never in any category. */
    public static Item TAB_ICON;

    /** Every item across every category, in category order - what the tab, search and /give see. */
    public static List<Block> tabItems() {
        List<Block> allItems = new ArrayList<>();
        for (ExtraAdditionsCategory category : ExtraAdditionsCategory.values()) {
            allItems.addAll(CATEGORY_BLOCKS.get(category));
        }
        return allItems;
    }

    /**
     * The one real Extra Additions tab, kept for {@code ExtraAdditionsCreativeMixin} to compare
     * against in its own {@code selectTab} hook - the same way Dawn Of Time's own mixin compares
     * against {@code DoTBCreativeModeTabsRegistry.INSTANCE.DOT_TAB}, decompiled.
     */
    public static CreativeModeTab EXTRA_ADDITIONS_TAB;

    private static Block register(ExtraAdditionsCategory category, String name, Block block) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
        Registry.register(BuiltInRegistries.BLOCK, id, block);
        Registry.register(BuiltInRegistries.ITEM, id, new BlockItem(block, new Item.Properties()));
        CREATIVE_ORDER.add(block);
        CATEGORY_BLOCKS.get(category).add(block);
        for (String cutout : CUTOUT_NAMES) {
            if (cutout.startsWith("_") ? name.endsWith(cutout) : name.equals(cutout)) {
                CUTOUT.add(block);
                break;
            }
        }
        return block;
    }
}
