package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.block.AmethystBeetrootBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import java.util.List;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CropSeedItem extends Item {
    private final DeferredHolder<AmethystBeetrootBlock, AmethystBeetrootBlock> cropBlock;

    public CropSeedItem(DeferredHolder<AmethystBeetrootBlock, AmethystBeetrootBlock> cropBlock, Item.Properties properties) {
        super(properties);
        this.cropBlock = cropBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (!isValidSupportBlock(state)) {
            return InteractionResult.PASS;
        }

        if (state.isFaceSturdy(level, pos, context.getClickedFace())) {
            BlockPos cropPos = pos.relative(context.getClickedFace());
            BlockState cropState = level.getBlockState(cropPos);
            
            if (cropState.canBeReplaced()) {
                AmethystBeetrootBlock crop = cropBlock.get();
                BlockState newState = crop.defaultBlockState()
                    .setValue(AmethystBeetrootBlock.FACING, context.getClickedFace());
                
                if (!level.isClientSide()) {
                    level.setBlock(cropPos, newState, 3);
                    
                    if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                        context.getItemInHand().shrink(1);
                    }
                }
                
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }
    
    private boolean isValidSupportBlock(BlockState state) {
        return state.getBlock() instanceof AmethystBlock 
            || state.getBlock() instanceof BuddingAmethystBlock 
            || state.is(Blocks.CALCITE);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".tooltip")
            .withStyle(ChatFormatting.GRAY)
            .withStyle(ChatFormatting.ITALIC));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
