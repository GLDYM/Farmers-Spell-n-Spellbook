package com.chenjdy.farmers_spell.block.entity.container;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class AlchemistPotItemHandler extends ItemStackHandler {
    private final ItemStackHandler wrapped;
    private final int startIndex;
    private final int endIndex;

    public AlchemistPotItemHandler(ItemStackHandler wrapped, int startIndex, int endIndex) {
        super(endIndex - startIndex);
        this.wrapped = wrapped;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public AlchemistPotItemHandler(ItemStackHandler wrapped) {
        this(wrapped, 0, wrapped.getSlots());
    }

    @Override
    public int getSlots() {
        return endIndex - startIndex;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return wrapped.getStackInSlot(startIndex + slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return wrapped.insertItem(startIndex + slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return wrapped.extractItem(startIndex + slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return wrapped.getSlotLimit(startIndex + slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return wrapped.isItemValid(startIndex + slot, stack);
    }
}
