package com.bmt.kaleidoscope_chinesefood.block.entity;

import com.bmt.kaleidoscope_chinesefood.init.ModBlockEntities;
import com.bmt.kaleidoscope_chinesefood.menu.FreezerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FreezerBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private final boolean isTop;

    public FreezerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FREEZER.get(), pos, state);
        this.isTop = state.getValue(com.bmt.kaleidoscope_chinesefood.block.FreezerBlock.TOP);
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        if (isTop) {
            return Component.translatable("container.kaleidoscope_chinesefood.freezer_top");
        } else {
            return Component.translatable("container.kaleidoscope_chinesefood.freezer_bottom");
        }
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory) {
        return new FreezerMenu(containerId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }
    }

    public void drops() {
        if (this.level != null && !this.level.isClientSide) {
            net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(items.size());
            for (int i = 0; i < items.size(); i++) {
                inventory.setItem(i, items.get(i));
            }
            net.minecraft.world.Containers.dropContents(this.level, this.worldPosition, inventory);
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                (double)this.worldPosition.getX() + 0.5D,
                (double)this.worldPosition.getY() + 0.5D,
                (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }
}