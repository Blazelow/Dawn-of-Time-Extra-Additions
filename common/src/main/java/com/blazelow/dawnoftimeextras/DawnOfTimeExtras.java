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
import org.dawnoftime.dawnoftime.block.templates.LatticeBlock;
import org.dawnoftime.dawnoftime.block.templates.BlockDoT;
import org.dawnoftime.dawnoftime.block.templates.ConnectedVerticalBlock;
import org.dawnoftime.dawnoftime.block.templates.SlabBlockDoT;
import org.dawnoftime.dawnoftime.block.templates.WaterloggedHorizontalAxisBlock;
import org.dawnoftime.dawnoftime.block.templates.WaterloggedHorizontalBlock;
import org.dawnoftime.dawnoftime.block.templates.ConnectedVerticalSidedBlock;
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

public class DawnOfTimeExtras {
    public static final String MOD_ID = "dawnoftimeextras";

    private static final List<Block> CREATIVE_ORDER = new ArrayList<>();

    public static final Map<ExtraAdditionsCategory, List<Block>> CATEGORY_BLOCKS = new LinkedHashMap<>();
    static {
        for (ExtraAdditionsCategory category : ExtraAdditionsCategory.values()) {
            CATEGORY_BLOCKS.put(category, new ArrayList<>());
        }
    }

    public static final List<Block> WATER_TINTED = new ArrayList<>();

    public static final List<Block> WATER_TINTED_ICONS = new ArrayList<>();

    public static final List<Block> CUTOUT = new ArrayList<>();

    private static final String[] CUTOUT_NAMES = {
            "tatami_block", "pale_green_tatami_block", "_fireplace",
            "_crenelation", "_little_flag", "_hanging_noren_flag", "_fancy_lantern", "_portcullis", "_bricks_arrowslit", "_bricks_machicolation", "_wrought_iron_fence", "_irori_fireplace",
            "_wave_template", "_round_template", "_spiral_template", "_painted_lattice", "_serpent_sculpted_column",

            "_glass_pane", "_window", "_fancy_railing",

            "_paper_lantern", "_cushion"};

    public static final Map<Block, String[]> TOOLTIPS = new LinkedHashMap<>();

    private static final String[] CONNECTING_NOTE = {
            "tooltip.dawnoftimeextras.connected_texture_label",
            "tooltip.dawnoftimeextras.connected_texture"};

    private static final String[] COLUMN_NOTE = {
            "tooltip.dawnoftimeextras.column_label",
            "tooltip.dawnoftimeextras.column"};

    public static final Block TATAMI_BLOCK = new Block(tatami());

    public static final Block PALE_GREEN_TATAMI_BLOCK = new Block(tatami());

    public static final Block CHARRED_SPRUCE_SIDED_COLUMN = new SidedColumnBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .ignitedByLava());

    private static final String[] EXTRA_STONES = {"deepslate", "tuff", "granite", "andesite", "diorite", "basalt", "calcite", "dripstone", "netherrack", "blackstone", "end_stone", "prismarine", "obsidian", "dark_prismarine", "sandstone", "purpur", "red_sandstone"};

    private static String[] stones(String... base) {
        String[] all = Arrays.copyOf(base, base.length + EXTRA_STONES.length);
        System.arraycopy(EXTRA_STONES, 0, all, base.length, EXTRA_STONES.length);
        return all;
    }

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

    private static final String[] PAINTED_DYES = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};

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

    private static BlockBehaviour.Properties hearth(BlockBehaviour.Properties properties) {
        return properties.lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0);
    }

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

    public static void init() {
        if (initialised) {
            return;
        }
        initialised = true;
        register(ExtraAdditionsCategory.JAPANESE, "tatami_block", TATAMI_BLOCK);

        register(ExtraAdditionsCategory.JAPANESE, "tatami_block_extendable",
                new TatamiBlockExtendableBlock(tatami()));

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

            if (stone.equals("sandstone")) {
                continue;
            }
            register(ExtraAdditionsCategory.PERSIAN, stone + "_crenelation",
                    new CrenelationBlock(stone(STONE_COLOURS.get(stone))));
        }

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

        for (String material : new String[]{"gold", "diamond", "netherite"}) {
            register(ExtraAdditionsCategory.FRENCH, material + "_fancy_lantern", new LanternBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).noOcclusion().lightLevel(state -> 15),
                    VoxelShapes.IRON_FANCY_LANTERN_SHAPES));
            register(ExtraAdditionsCategory.FRENCH, material + "_column", new IronColumnBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));
            register(ExtraAdditionsCategory.GERMAN, material + "_portcullis", new PortcullisBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)));
        }

        for (String colour : new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
                "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"}) {
            register(ExtraAdditionsCategory.JAPANESE, colour + "_hanging_noren_flag", new HangingNorenFlagBlock(
                    BlockBehaviour.Properties.of().mapColor(DyeColor.byName(colour, DyeColor.WHITE))
                            .strength(0.3F).sound(SoundType.WOOL).noOcclusion().noCollission()));
        }

        for (String colour : new String[]{"orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"}) {
            register(ExtraAdditionsCategory.JAPANESE, colour + "_little_flag", new LittleFlagBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                            .mapColor(DyeColor.byName(colour, DyeColor.WHITE)).noOcclusion()));
        }

        for (String colour : new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
                "gray", "light_gray", "cyan", "purple", "brown", "green", "red", "black"}) {
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_wave_template",
                    new LatticeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion()).setBurnable());
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_round_template",
                    new LatticeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion()).setBurnable());
        }

        for (String base : PAINTED_DYES) {
            for (String round : PAINTED_DYES) {
                if (base.equals("red") && round.equals("blue")) {
                    continue;
                }
                register(ExtraAdditionsCategory.PRE_COLOMBIAN, base + "_painted_" + round + "_round",
                        new Block(solidStone(DYE_MAP_COLOURS.get(base))));
            }
        }

        for (String base : PAINTED_DYES) {
            for (String spiral : PAINTED_DYES) {
                if (base.equals("red") && spiral.equals("white")) {
                    continue;
                }
                register(ExtraAdditionsCategory.PRE_COLOMBIAN, base + "_painted_" + spiral + "_spiral",
                        new Block(solidStone(DYE_MAP_COLOURS.get(base))));
            }
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("white")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_spiral_template",
                    new LatticeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion()).setBurnable());
        }

        for (String base : PAINTED_DYES) {
            for (String glyph : PAINTED_DYES) {
                if (base.equals("red") && glyph.equals("blue")) {
                    continue;
                }
                register(ExtraAdditionsCategory.PRE_COLOMBIAN, base + "_painted_" + glyph + "_frieze_edge",
                        new EdgeBlock(solidStone(DYE_MAP_COLOURS.get(base))));
            }
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("white")) {
                continue;
            }
            if (colour.equals("red")) {
                continue;
            }
            Block puucBase = register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_puuc_limestone",
                    new Block(solidStone(DYE_MAP_COLOURS.get(colour))));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_puuc_limestone_stairs",
                    new StairBlock(puucBase.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_puuc_limestone_slab",
                    new SlabBlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_puuc_limestone_plate",
                    new PlateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_puuc_limestone_edge",
                    new EdgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        }

        for (String colour : PAINTED_DYES) {
            for (String piece : new String[]{"decorated", "tight_lattice", "crossed", "wave"}) {
                register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_puuc_limestone_" + piece,
                        new Block(solidStone(DYE_MAP_COLOURS.get(colour))));
            }
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("white")) {
                continue;
            }
            if (colour.equals("red")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_lattice",
                    new LatticeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion()).setBurnable());
        }

        for (String colour : new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
                "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "black"}) {
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_ornamented_plastered_stone",
                    new BlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("green")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_ornamented_plastered_stone_frieze_green",
                    new CrenelationBlock(stone(DYE_MAP_COLOURS.get(colour))));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_sculpted_plastered_stone_frieze_green",
                    new CrenelationBlock(stone(DYE_MAP_COLOURS.get(colour))));
        }
        for (String colour : PAINTED_DYES) {
            if (colour.equals("red")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_ornamented_plastered_stone_frieze_red",
                    new CrenelationBlock(stone(DYE_MAP_COLOURS.get(colour))));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_sculpted_plastered_stone_frieze_red",
                    new CrenelationBlock(stone(DYE_MAP_COLOURS.get(colour))));
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("green")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_small_plastered_stone_frieze_green",
                    new EdgeBlock(solidStone(DYE_MAP_COLOURS.get(colour))));
        }
        for (String colour : PAINTED_DYES) {
            if (colour.equals("red")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_small_plastered_stone_frieze_red",
                    new EdgeBlock(solidStone(DYE_MAP_COLOURS.get(colour))));
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("green")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone_frieze_green",
                    new CrenelationBlock(stone(DYE_MAP_COLOURS.get(colour))));
        }
        for (String colour : PAINTED_DYES) {
            if (colour.equals("red")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone_frieze_red",
                    new CrenelationBlock(stone(DYE_MAP_COLOURS.get(colour))));
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("green")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN,
                    colour + "_ornamented_chiseled_plastered_stone_green",
                    new BlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        }
        for (String colour : PAINTED_DYES) {
            if (colour.equals("red")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN,
                    colour + "_ornamented_chiseled_plastered_stone_red",
                    new BlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        }

        for (String colour : PAINTED_DYES) {
            register(ExtraAdditionsCategory.PRE_COLOMBIAN,
                    colour + "_ornamented_chiseled_plastered_stone_gold",
                    new BlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        }

        for (String colour : new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
                "gray", "light_gray", "cyan", "purple", "blue", "brown", "red", "black"}) {
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_feathered_serpent_sculpture",
                    new WaterloggedHorizontalBlock(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion(),
                            VoxelShapes.FEATHERED_SERPENT_SCULPTURE_SHAPES));
        }

        for (String colour : new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
                "gray", "light_gray", "cyan", "purple", "blue", "brown", "red", "black"}) {
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_serpent_sculpted_column",
                    new ConnectedVerticalSidedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS),
                            VoxelShapes.SERPENT_SCULPTED_COLUMN_SHAPES));
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("red")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_chiseled_plastered_stone_red",
                    new BlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        }
        for (String colour : PAINTED_DYES) {
            if (colour.equals("green")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_chiseled_plastered_stone_green",
                    new BlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("white")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_chiseled_plastered_stone_plain",
                    new BlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN,
                    colour + "_chiseled_plastered_stone_frieze_plain",
                    new CrenelationBlock(stone(DYE_MAP_COLOURS.get(colour))));
        }

        for (String colour : new String[]{"white", "orange", "magenta", "light_blue", "lime", "pink",
                "gray", "light_gray", "cyan", "purple", "brown", "black"}) {
            Block base = register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone",
                    new BlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone_column",
                    new ConnectedVerticalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS),
                            VoxelShapes.PLASTERED_STONE_COLUMN_SHAPES));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone_edge",
                    new EdgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone_plate",
                    new PlateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone_slab",
                    new SlabBlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone_stairs",
                    new StairBlock(base.defaultBlockState(),
                            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_plastered_stone_window",
                    new WaterloggedHorizontalAxisBlock(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion(),
                            VoxelShapes.PLASTERED_STONE_WINDOW_SHAPES));
        }

        for (String stone : stones("quartz")) {
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, stone + "_plastered_window",
                    new WaterloggedHorizontalAxisBlock(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion(),
                            VoxelShapes.PLASTERED_STONE_WINDOW_SHAPES));
        }
        for (String wood : WOODS) {
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, wood + "_plastered_window",
                    new WaterloggedHorizontalAxisBlock(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion(),
                            VoxelShapes.PLASTERED_STONE_WINDOW_SHAPES));
        }
        register(ExtraAdditionsCategory.PRE_COLOMBIAN, "charred_spruce_plastered_window",
                new WaterloggedHorizontalAxisBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion(),
                        VoxelShapes.PLASTERED_STONE_WINDOW_SHAPES));
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

        for (String stone : stones("quartz", "limestone")) {
            MapColor colour = STONE_COLOURS.get(stone);
            Block masonry = register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry", new Block(solidStone(colour)));

            TOOLTIPS.put(masonry, CONNECTING_NOTE);
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_stairs",
                    new StairBlock(masonry.defaultBlockState(), solidStone(colour)));
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_slab", new SlabBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_wall", new WallBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_plate", new PlateBlock(solidStone(colour)));
            register(ExtraAdditionsCategory.GERMAN, stone + "_bricks_masonry_edge", new EdgeBlock(solidStone(colour)));
        }

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

        for (String stone : stones("quartz", "limestone")) {
            register(ExtraAdditionsCategory.GERMAN, stone + "_chimney", new ChimneyBlock(stone(STONE_COLOURS.get(stone))));
        }

        for (String stone : stones("quartz", "limestone")) {
            register(ExtraAdditionsCategory.GERMAN, stone + "_german_fireplace",
                    new FireplaceBlock(hearth(stone(STONE_COLOURS.get(stone)))));
        }

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

            register(ExtraAdditionsCategory.JAPANESE, wood + "_timber_frame", new Block(wood(wood)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_timber_frame_pillar", new RotatedPillarBlock(wood(wood)));

            register(ExtraAdditionsCategory.JAPANESE, wood + "_glass_pane", new GlassPaneBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));

            register(ExtraAdditionsCategory.JAPANESE, wood + "_fancy_railing", new FancyRailingBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).noOcclusion()));

            register(ExtraAdditionsCategory.JAPANESE, wood + "_railing", new CharredSpruceRailingBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).noOcclusion()));

            register(ExtraAdditionsCategory.JAPANESE, wood + "_window", new TransparentBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));

            register(ExtraAdditionsCategory.JAPANESE, wood + "_shutters", new ShuttersBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).noOcclusion()));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_tall_shutters", new TallShuttersBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).noOcclusion()));

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

            register(ExtraAdditionsCategory.JAPANESE, wood + "_sliding_paper_door", new PaperDoorBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(1.5F, 1.5F)));

            register(ExtraAdditionsCategory.JAPANESE, wood + "_foundation_column", new FoundationBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
            register(ExtraAdditionsCategory.JAPANESE, wood + "_foundation_column_slab", new FoundationSlabBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));
        }

        String[] woodsExceptSpruce = {
                "oak", "birch", "jungle", "acacia", "dark_oak",
                "mangrove", "cherry", "bamboo", "crimson", "warped"};
        for (String wood : woodsExceptSpruce) {
            register(ExtraAdditionsCategory.JAPANESE, wood + "_roof_support", new RoofSupportBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));

            register(ExtraAdditionsCategory.JAPANESE, wood + "_paper_door", new PaperDoorBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                            .strength(1.5F, 1.5F)));

            register(ExtraAdditionsCategory.JAPANESE, wood + "_foundation", new FoundationBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                            .mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F)));

            Block boards = register(ExtraAdditionsCategory.JAPANESE, wood + "_boards", new BoardsBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(3.0F, 5.0F)));

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

        register(ExtraAdditionsCategory.JAPANESE, "charred_spruce_paper_door", new PaperDoorBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                        .strength(1.5F, 1.5F)));

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

        String[] paintedStoneGaps = {
                "orange", "magenta", "light_blue", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "brown", "black"};
        for (String colour : paintedStoneGaps) {
            Block paintedBase = register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_stone", new Block(solidStone(DYE_MAP_COLOURS.get(colour))));

            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_stone_stairs",
                    new StairBlock(paintedBase.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_stone_slab",
                    new SlabBlockDoT(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_stone_plate",
                    new PlateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_stone_edge",
                    new EdgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        }

        for (String colour : PAINTED_DYES) {
            if (colour.equals("red")) {
                continue;
            }
            register(ExtraAdditionsCategory.PRE_COLOMBIAN, colour + "_painted_crenelation",
                    new CrenelationBlock(stone(DYE_MAP_COLOURS.get(colour))));
        }

        for (String base : PAINTED_DYES) {
            for (String wave : PAINTED_DYES) {
                if (base.equals("red") && wave.equals("blue")) {
                    continue;
                }
                register(ExtraAdditionsCategory.PRE_COLOMBIAN, base + "_painted_" + wave + "_wave",
                        new Block(solidStone(DYE_MAP_COLOURS.get(base))));
            }
        }

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

        for (SignSet set : SignSet.ALL) {
            CATEGORY_BLOCKS.get(ExtraAdditionsCategory.JAPANESE).add(set.sign);
            CATEGORY_BLOCKS.get(ExtraAdditionsCategory.JAPANESE).add(set.hangingSign);
        }

        TAB_ICON = Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "creative_tab_icon"), new Item(new Item.Properties()));
    }

    private static boolean initialised = false;

    public static Item TAB_ICON;

    public static List<Block> tabItems() {
        List<Block> allItems = new ArrayList<>();
        for (ExtraAdditionsCategory category : ExtraAdditionsCategory.values()) {
            allItems.addAll(CATEGORY_BLOCKS.get(category));
        }
        return allItems;
    }

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
