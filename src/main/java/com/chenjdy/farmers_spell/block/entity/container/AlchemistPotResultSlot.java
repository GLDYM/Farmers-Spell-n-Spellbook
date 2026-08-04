package com.chenjdy.farmers_spell.block.entity.container;

import com.chenjdy.farmers_spell.block.entity.AlchemistPotBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AlchemistPotResultSlot extends SlotItemHandler {
    public final AlchemistPotBlockEntity alchemistPot;
    private final Player player;
    private int removeCount;

    public AlchemistPotResultSlot(Player player, AlchemistPotBlockEntity blockEntity, IItemHandler inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.alchemistPot = blockEntity;
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level(), this.player, this.removeCount);

        if (!this.player.level().isClientSide) {
            alchemistPot.awardUsedRecipes(this.player, alchemistPot.getDroppableInventory());
        }

        this.removeCount = 0;
    }
}
