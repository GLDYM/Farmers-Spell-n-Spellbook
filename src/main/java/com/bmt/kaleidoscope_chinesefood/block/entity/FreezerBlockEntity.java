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

public class FreezerBlockEntity extends RandomizableContainerBlockEntity {
    // 每层独立存储27格
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    // 标记这是上层还是下层
    private final boolean isTop;

    public FreezerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FREEZER.get(), pos, state);
        // 根据方块状态判断是上层还是下层
        this.isTop = state.getValue(com.bmt.kaleidoscope_chinesefood.block.FreezerBlock.TOP);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        // 根据上下层显示不同的名称
        if (isTop) {
            return Component.translatable("container.kaleidoscope_chinesefood.freezer_top");
        } else {
            return Component.translatable("container.kaleidoscope_chinesefood.freezer_bottom");
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new FreezerMenu(containerId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 27; // 每层27格
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
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
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                (double)this.worldPosition.getX() + 0.5D,
                (double)this.worldPosition.getY() + 0.5D,
                (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }
}