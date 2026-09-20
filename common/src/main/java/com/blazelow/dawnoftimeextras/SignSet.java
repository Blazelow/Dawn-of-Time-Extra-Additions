package com.blazelow.dawnoftimeextras;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.List;

/**
 * One sign, its wall form, and the matching hanging pair, for a single material.
 *
 * <p>Signs need more scaffolding than an ordinary block. A {@link WoodType} has to be registered
 * before the blocks are built, because vanilla builds a sign model layer and a texture material
 * for every registered wood type - that is what lets the renderer find
 * {@code textures/entity/signs/<name>.png} with nothing wired up by hand.
 *
 * <p>The block entities exist only to carry a type of our own. Vanilla's {@code SIGN} and
 * {@code HANGING_SIGN} types have closed valid-block lists, so a sign of ours attached to one is
 * thrown out as invalid the moment it is placed. The type has to reach
 * {@link BlockEntity}'s constructor too - overriding {@code getType()} is not enough, because the
 * constructor validates against the private field and only the error message reads the getter.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SignSet {
    /** Every set built, so the client can attach a renderer to each. */
    public static final List<SignSet> ALL = new ArrayList<>();

    public final Block sign;
    public final Block wallSign;
    public final Block hangingSign;
    public final Block wallHangingSign;

    public final BlockEntityType<SetSignBlockEntity> signEntity;
    public final BlockEntityType<SetHangingSignBlockEntity> hangingSignEntity;

    public SignSet(String name, BlockSetType setType, MapColor colour) {
        WoodType woodType = WoodType.register(
                new WoodType(DawnOfTimeExtras.MOD_ID + ":" + name, setType));

        this.sign = new StandingSignBlock(woodType, properties(colour)) {
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new SetSignBlockEntity(signEntity, pos, state);
            }
        };
        this.wallSign = new WallSignBlock(woodType, properties(colour).dropsLike(this.sign)) {
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new SetSignBlockEntity(signEntity, pos, state);
            }
        };
        this.hangingSign = new CeilingHangingSignBlock(woodType, properties(colour)) {
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new SetHangingSignBlockEntity(hangingSignEntity, pos, state);
            }
        };
        this.wallHangingSign = new WallHangingSignBlock(woodType, properties(colour).dropsLike(this.hangingSign)) {
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                return new SetHangingSignBlockEntity(hangingSignEntity, pos, state);
            }
        };

        // Each block entity needs its own type, and the type needs the supplier - so the
        // supplier reads it back out of a holder that is filled in immediately afterwards. The
        // lambda only runs when a block entity is created, long after that.
        BlockEntityType<SetSignBlockEntity>[] signHolder = new BlockEntityType[1];
        signHolder[0] = BlockEntityType.Builder
                .of((pos, state) -> new SetSignBlockEntity(signHolder[0], pos, state), sign, wallSign)
                .build(null);
        this.signEntity = signHolder[0];

        BlockEntityType<SetHangingSignBlockEntity>[] hangingHolder = new BlockEntityType[1];
        hangingHolder[0] = BlockEntityType.Builder
                .of((pos, state) -> new SetHangingSignBlockEntity(hangingHolder[0], pos, state),
                        hangingSign, wallHangingSign)
                .build(null);
        this.hangingSignEntity = hangingHolder[0];

        register(name + "_sign", sign);
        register(name + "_wall_sign", wallSign);
        register(name + "_hanging_sign", hangingSign);
        register(name + "_wall_hanging_sign", wallHangingSign);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(name + "_sign"), signEntity);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(name + "_hanging_sign"), hangingSignEntity);

        // The wall forms have no item of their own; one item places both.
        Registry.register(BuiltInRegistries.ITEM, id(name + "_sign"),
                new SignItem(new Item.Properties().stacksTo(16), sign, wallSign));
        Registry.register(BuiltInRegistries.ITEM, id(name + "_hanging_sign"),
                new HangingSignItem(hangingSign, wallHangingSign, new Item.Properties().stacksTo(16)));

        ALL.add(this);
    }


    private static BlockBehaviour.Properties properties(MapColor colour) {
        return BlockBehaviour.Properties.of()
                .mapColor(colour)
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }

    private static void register(String name, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, id(name), block);
    }

    private static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(DawnOfTimeExtras.MOD_ID, name);
    }

    public static class SetSignBlockEntity extends SignBlockEntity {
        public SetSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }
    }

    /**
     * Extends {@link SignBlockEntity}, not {@code HangingSignBlockEntity}: that one takes no
     * block entity type, so it would pin vanilla's. The three things it adds are reproduced here.
     */
    public static class SetHangingSignBlockEntity extends SignBlockEntity {
        public SetHangingSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public int getTextLineHeight() {
            return 9;
        }

        @Override
        public int getMaxTextLineWidth() {
            return 60;
        }

        @Override
        public SoundEvent getSignInteractionFailedSoundEvent() {
            return SoundEvents.WAXED_HANGING_SIGN_INTERACT_FAIL;
        }
    }
}
