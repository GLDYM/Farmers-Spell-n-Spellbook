package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.CinderousStoveBlock;
import com.chenjdy.farmers_spell.entity.FoodgeistEntity;
import com.chenjdy.farmers_spell.entity.FoodgeistSpawnHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class FoodgeistStoveSummonHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (!player.isShiftKeyDown()) {
            return;
        }

        ItemStack heldStack = player.getItemInHand(event.getHand());
        if (!FoodgeistEntity.isModFoodItem(heldStack)) {
            return;
        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!(level.getBlockState(pos).getBlock() instanceof CinderousStoveBlock)) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel
                && FoodgeistSpawnHelper.summonOrTeleport(serverLevel, pos, player)
                && !player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }
    }
}
