package net.darwindontcare.lighting_god.entities;

import com.mojang.logging.LogUtils;
import net.darwindontcare.lighting_god.blocks.ModBlocks;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.BlockDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public class CustomFallingBlockEntity extends FallingBlock {
    private Player owner;
    private Block parentBlock;
    private boolean appliedForce = false;
    private static final float POWER = 1f;

    public CustomFallingBlockEntity() {
        super(Properties.of(Material.STONE, MaterialColor.STONE).strength(0.5F, 2.0F).color(MaterialColor.STONE).noOcclusion());
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean flag) {
        super.onPlace(blockState, level, blockPos, blockState2, flag);

        if (!level.isClientSide() && flag) {
            level.explode(getOwner(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 3, Level.ExplosionInteraction.TNT);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);
        if (!appliedForce && getOwner() != null) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(serverLevel, blockPos, this.defaultBlockState());
            double forwardX = -Math.sin(Math.toRadians(getOwner().getYRot())) * POWER;
            double forwardY = -Math.sin(Math.toRadians(getOwner().getXRot())) * POWER;
            double forwardZ = Math.cos(Math.toRadians(getOwner().getYRot())) * POWER;

            Vec3 playerForward = new Vec3(Math.round(forwardX), forwardY, Math.round(forwardZ));
            fallingBlockEntity.setDeltaMovement(playerForward);
            serverLevel.addFreshEntity(fallingBlockEntity);
        }
    }

    @Override
    protected void falling(FallingBlockEntity fallingBlockEntity) {
        super.falling(fallingBlockEntity);
        ServerLevel serverLevel = (ServerLevel) fallingBlockEntity.level;
        BlockPos blockPos = new BlockPos((int)fallingBlockEntity.position().x, (int)fallingBlockEntity.position().y, (int)fallingBlockEntity.position().z);
        BlockState currentBlockState = serverLevel.getBlockState(blockPos);
        if (currentBlockState.getMaterial().isSolid() && currentBlockState.getBlock() != parentBlock) {
            serverLevel.explode(getOwner(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 3, Level.ExplosionInteraction.TNT);
        }
        List<LivingEntity> nearbyEntities = serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(blockPos.getX(),  blockPos.getY() - 3, blockPos.getZ() - 3, blockPos.getX(),  blockPos.getY() + 3, blockPos.getZ() + 3)
        );
        for (Entity entity : nearbyEntities) {
            if (entity != getOwner()) {
                serverLevel.explode(getOwner(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 3, Level.ExplosionInteraction.TNT);
            }
        }
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setParentBlock(Block parentBlock) {
        this.parentBlock = parentBlock;
    }

    public Block getParentBlock() {
        return parentBlock;
    }

//    private CustomFallingBlockEntity(Level level, double x, double y, double z, BlockState blockState) {
//        this(EntityType.FALLING_BLOCK, level);
//        this.blockState = blockState;
//        this.blocksBuilding = true;
//        this.setPos(x, y, z);
//        this.setDeltaMovement(Vec3.ZERO);
//        this.xo = x;
//        this.yo = y;
//        this.zo = z;
//        this.setStartPos(this.blockPosition());
//    }
//
//    // Ler NBT
//    @Override
//    public void readAdditionalSaveData(CompoundTag compound) {
//        super.readAdditionalSaveData(compound);
//        Block block = Block.byItem(Item.byId(compound.getCompound("BlockState").getId()));
//        this.blockState = block.defaultBlockState();
//    }
//
//    // Escrever NBT
//    @Override
//    public void addAdditionalSaveData(CompoundTag compound) {
//        super.addAdditionalSaveData(compound);
//        CompoundTag blockStateTag = new CompoundTag();
//        blockStateTag.putString("Name", this.getBlockState().getBlock().getName().toString());
//        compound.put("BlockState", blockStateTag);
//    }
//
//    // Defina as propriedades adicionais, como velocidade inicial
//    @Override
//    protected void defineSynchedData() {
//        super.defineSynchedData();
//        // Você pode adicionar dados sincronizados aqui, se necessário.
//    }

    // Lógica de atualização
//    @Override
//    public void tick() {
//        super.tick();
//        BlockState currentBlockState = level.getBlockState(new BlockPos((int) this.position().x, (int) this.position().y, (int) this.position().z));
//        if (currentBlockState.getMaterial().isSolid() && currentBlockState.getBlock() != parentBlock) {
//            level.explode(owner, this.position().x, this.position().y, this.position().z, 3, Level.ExplosionInteraction.TNT);
//        }
//        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(
//                LivingEntity.class,
//                new AABB(this.position().x, this.position().y - 3, this.position().z - 3, this.position().x, this.position().y + 3, this.position().z + 3)
//        );
//        for (Entity entity : nearbyEntities) {
//            if (entity != owner) {
//                level.explode(owner, this.position().x, this.position().y, this.position().z, 3, Level.ExplosionInteraction.TNT);
//            }
//        }
//    }
}
