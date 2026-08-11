package com.chenjdy.farmers_spell.block.entity;

import com.chenjdy.farmers_spell.block.WisewoodCabinetBlock;
import com.chenjdy.farmers_spell.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class WisewoodCabinetBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> contents = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            playSound(state, ModSounds.BLOCK_CABINET_OPEN.get());
            updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            playSound(state, ModSounds.BLOCK_CABINET_CLOSE.get());
            updateBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof ChestMenu chestMenu
                    && chestMenu.getContainer() == WisewoodCabinetBlockEntity.this;
        }
    };

    public WisewoodCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WISEWOOD_CABINET.get(), pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.WISEWOOD_CABINET.get(),
                (blockEntity, context) -> new InvWrapper(blockEntity));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, contents, registries);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        contents = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, contents, registries);
        }
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return contents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        contents = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.farmersdelight.cabinet");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestMenu.threeRows(id, inventory, this);
    }

    public void startOpen(Player player) {
        if (level != null && !remove && !player.isSpectator()) {
            openersCounter.incrementOpeners(player, level, worldPosition, getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (level != null && !remove && !player.isSpectator()) {
            openersCounter.decrementOpeners(player, level, worldPosition, getBlockState());
        }
    }

    public void recheckOpen() {
        if (level != null && !remove) {
            openersCounter.recheckOpeners(level, worldPosition, getBlockState());
        }
    }

    private void updateBlockState(BlockState state, boolean open) {
        if (level != null) {
            level.setBlock(worldPosition, state.setValue(WisewoodCabinetBlock.OPEN, open), 3);
        }
    }

    private void playSound(BlockState state, SoundEvent sound) {
        if (level == null) {
            return;
        }

        Vec3i facing = state.getValue(WisewoodCabinetBlock.FACING).getNormal();
        double x = worldPosition.getX() + 0.5D + facing.getX() / 2.0D;
        double y = worldPosition.getY() + 0.5D + facing.getY() / 2.0D;
        double z = worldPosition.getZ() + 0.5D + facing.getZ() / 2.0D;
        level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }
}
