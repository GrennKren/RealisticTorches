package com.chaosthedude.realistictorches.blocks;

import com.chaosthedude.realistictorches.blockentity.RealisticCampfireBlockEntity;
import com.chaosthedude.realistictorches.config.ConfigHandler;
import com.chaosthedude.realistictorches.registry.RealisticTorchesRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class RealisticCampfireBlock extends CampfireBlock {

    public static final String NAME = "campfire";

    public static final int TICK_INTERVAL = 1200; // Same as torch (1 minute)
    protected static final int INITIAL_BURN_TIME = ConfigHandler.getCampfireBurnoutTime();
    protected static final boolean SHOULD_BURN_OUT = INITIAL_BURN_TIME > 0;
    protected static final IntegerProperty BURNTIME = IntegerProperty.create("burntime", 0, SHOULD_BURN_OUT ? INITIAL_BURN_TIME : 1);
    protected static final IntegerProperty LITSTATE = IntegerProperty.create("litstate", 0, 2);

    public static final int LIT_STATE = 2;
    public static final int SMOLDERING_STATE = 1;
    public static final int UNLIT_STATE = 0;

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RealisticCampfireBlock() {
        super(true, 1, Block.Properties.ofFullCopy(Blocks.CAMPFIRE).lightLevel(getLightEmission()).randomTicks());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, false)
                .setValue(SIGNAL_FIRE, false)
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(LITSTATE, UNLIT_STATE)
                .setValue(BURNTIME, 0));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RealisticCampfireBlockEntity campfireBlockEntity) {
            ItemStack handStack = player.getItemInHand(hand);
            Optional<RecipeHolder<CampfireCookingRecipe>> optional = campfireBlockEntity.getCookableRecipe(handStack);
            if (optional.isPresent()) {
                if (!level.isClientSide) {
                    if (campfireBlockEntity.placeFood(player, player.getAbilities().instabuild ? handStack.copy() : handStack,
                            optional.get().value().getCookingTime())) {
                        player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
                return ItemInteractionResult.CONSUME;
            }
        }


        // Check if item can light campfire
        String itemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        boolean canLight = stack.getItem() == Items.FLINT_AND_STEEL ||
                stack.getItem() == RealisticTorchesRegistry.MATCHBOX_ITEM.get() ||
                ConfigHandler.getLightTorchItems().contains(itemId);

        if (canLight && state.getValue(LITSTATE) != LIT_STATE) {
            playLightingSound(level, pos);
            if (!player.isCreative() &&
                    (stack.getItem() != RealisticTorchesRegistry.MATCHBOX_ITEM.get() ||
                            ConfigHandler.getMatchboxDurability() > 0)) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            if (level.isRainingAt(pos)) {
                playExtinguishSound(level, pos);
            } else {
                changeToLit(level, pos, state);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RealisticCampfireBlockEntity) {
                Containers.dropContents(level, pos, ((RealisticCampfireBlockEntity)blockEntity).getItems());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        boolean lit = !(level.getFluidState(pos).getType() == Fluids.WATER);

        return this.defaultBlockState()
                .setValue(WATERLOGGED, !lit)
                .setValue(SIGNAL_FIRE, this.isSmokeSource(level.getBlockState(pos.below())))
                .setValue(LIT, lit)
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(LITSTATE, lit ? LIT_STATE : UNLIT_STATE)
                .setValue(BURNTIME, lit ? getInitialBurnTime() : 0);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide() && state.getValue(LITSTATE) > UNLIT_STATE && SHOULD_BURN_OUT) {
            level.scheduleTick(pos, this, TICK_INTERVAL);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    private boolean isSmokeSource(BlockState state) {
        return state.is(Blocks.HAY_BLOCK);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            boolean flag = state.getValue(LITSTATE) > UNLIT_STATE;
            if (flag) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                dowse(null, level, pos, state);
            }
            level.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(LIT, false).setValue(LITSTATE, UNLIT_STATE), 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        }
        return false;
    }

    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockpos = hit.getBlockPos();
        if (!level.isClientSide && projectile.isOnFire() && projectile.mayInteract(level, blockpos) && state.getValue(LITSTATE) <= SMOLDERING_STATE && !(Boolean)state.getValue(WATERLOGGED)) {
            changeToLit(level, blockpos, state);
        }

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BURNTIME, LITSTATE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RealisticCampfireBlockEntity(pos, state);

    }

    //@Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return state.getValue(LITSTATE) >= SMOLDERING_STATE ?
                    createTickerHelper(blockEntityType, RealisticTorchesRegistry.CAMPFIRE_BLOCK_ENTITY.get(), RealisticCampfireBlockEntity::particleTick) :
                    null;
        } else {
            return state.getValue(LITSTATE) >= SMOLDERING_STATE ?
                    createTickerHelper(blockEntityType, RealisticTorchesRegistry.CAMPFIRE_BLOCK_ENTITY.get(), RealisticCampfireBlockEntity::cookTick) :
                    createTickerHelper(blockEntityType, RealisticTorchesRegistry.CAMPFIRE_BLOCK_ENTITY.get(), RealisticCampfireBlockEntity::cooldownTick);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        if (state.getValue(LITSTATE) == UNLIT_STATE && state.getValue(LIT)) {
            changeToUnlit(level, pos, state);
            level.updateNeighborsAt(pos, this);
            return;
        }

        if (!level.isClientSide() && SHOULD_BURN_OUT && state.getValue(LITSTATE) > UNLIT_STATE) {
            int newBurnTime = state.getValue(BURNTIME) - 1;
            if (newBurnTime <= 0) {
                playExtinguishSound(level, pos);
                changeToUnlit(level, pos, state);
                level.updateNeighborsAt(pos, this);
            } else if (state.getValue(LITSTATE) == LIT_STATE &&
                    (newBurnTime <= INITIAL_BURN_TIME / 10 || newBurnTime <= 1)) {
                changeToSmoldering(level, pos, state, newBurnTime);
                level.updateNeighborsAt(pos, this);
            } else {
                level.setBlock(pos, state.setValue(BURNTIME, newBurnTime), 2);
                level.scheduleTick(pos, this, TICK_INTERVAL);
            }
        }
    }

    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isRainingAt(pos.above()) && state.getValue(LITSTATE) > UNLIT_STATE) {
            // 30% chance to reduce lit state when raining
            if (random.nextFloat() < 0.3f) { //0.3f) {
                int currentLitState = state.getValue(LITSTATE);
                int currentBurnTime = state.getValue(BURNTIME);

                if (currentLitState == LIT_STATE) {
                    changeToSmoldering(level, pos, state, Math.max(1, currentBurnTime / 2));
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
                } else if (currentLitState == SMOLDERING_STATE) {
                    playExtinguishSound(level, pos);
                    changeToUnlit(level, pos, state);
                }

                level.updateNeighborsAt(pos, this);
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        boolean lit = state.getValue(LITSTATE) == LIT_STATE;

        if (!lit) {
            return new ItemStack(RealisticTorchesRegistry.UNLIT_CAMPFIRE_ITEM.get());
        }

        return new ItemStack(RealisticTorchesRegistry.LIT_CAMPFIRE_ITEM.get());
    }


    public static IntegerProperty getBurnTime() {
        return BURNTIME;
    }

    public static IntegerProperty getLitStateProperty() {
        return LITSTATE;
    }

    public static int getInitialBurnTime() {
        return SHOULD_BURN_OUT ? INITIAL_BURN_TIME : 0;
    }

    public void changeToLit(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockState newState = RealisticTorchesRegistry.CAMPFIRE_BLOCK.get().defaultBlockState()
                .setValue(LITSTATE, LIT_STATE)
                .setValue(BURNTIME, getInitialBurnTime())
                .setValue(LIT, true)
                .setValue(FACING, state.getValue(FACING))
                .setValue(SIGNAL_FIRE, state.getValue(SIGNAL_FIRE))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));

        level.setBlock(pos, newState, 3);

        if (SHOULD_BURN_OUT && level instanceof ServerLevel serverLevel) {
            serverLevel.scheduleTick(pos, this, TICK_INTERVAL);
        }
    }

    public void changeToSmoldering(LevelAccessor level, BlockPos pos, BlockState state, int newBurnTime) {
        if (SHOULD_BURN_OUT) {
            BlockState newState = state
                    .setValue(LITSTATE, SMOLDERING_STATE)
                    .setValue(BURNTIME, newBurnTime)
                    .setValue(LIT, true);

            level.setBlock(pos, newState, 3);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.scheduleTick(pos, this, TICK_INTERVAL);
            }
        }
    }

    public void changeToUnlit(LevelAccessor level, BlockPos pos, BlockState state) {
        if (SHOULD_BURN_OUT) {
            if (ConfigHandler.isNoRelightEnabled()) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            } else {
                BlockState newState = state
                        .setValue(LITSTATE, UNLIT_STATE)
                        .setValue(BURNTIME, 0)
                        .setValue(LIT, false);

                level.setBlock(pos, newState, 3);
            }
        }
    }

    public void playLightingSound(LevelAccessor level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public void playExtinguishSound(LevelAccessor level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    private static ToIntFunction<BlockState> getLightEmission() {
        return (state) -> {
            if (state.getValue(LITSTATE) == LIT_STATE) {
                return 15; // Full light like vanilla campfire
            } else if (state.getValue(LITSTATE) == SMOLDERING_STATE) {
                return 10; // Dimmer light when smoldering
            }
            return 0; // No light when unlit
        };
    }
}