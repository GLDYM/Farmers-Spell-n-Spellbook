package com.chenjdy.farmers_spell.creativetab;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ConglomerateOfItems {
    public final List<Object> conglomerate = new ArrayList<>();
    private final List<ItemStack> stacks = new ArrayList<>();

    public static ConglomerateOfItems create() {
        return new ConglomerateOfItems();
    }

    public List<ItemStack> getStacks() {
        return stacks;
    }

    public void resolveStacks(RegistryAccess registryAccess) {
        stacks.clear();

        for (Object o : conglomerate) {
            if (o instanceof Item i) {
                stacks.add(i.getDefaultInstance());
                continue;
            }

            if (o instanceof ItemStack i) {
                stacks.add(i);
                continue;
            }

            if (o instanceof RegistryObject<?> i) {
                Object resolved = i.get();
                if (resolved instanceof Item item) {
                    stacks.add(item.getDefaultInstance());
                } else if (resolved instanceof Block block) {
                    stacks.add(block.asItem().getDefaultInstance());
                }
                continue;
            }

            if (o instanceof ItemLike i) {
                stacks.add(i.asItem().getDefaultInstance());
                continue;
            }

            if (o instanceof Supplier<?> supplier) {
                Object result = supplier.get();
                if (result instanceof ItemStack is) {
                    stacks.add(is);
                } else if (result instanceof Item item) {
                    stacks.add(item.getDefaultInstance());
                } else if (result instanceof Block block) {
                    stacks.add(block.asItem().getDefaultInstance());
                }
            }

            if (o instanceof List<?> list) {
                if (list.stream().allMatch(ItemStack.class::isInstance)) {
                    @SuppressWarnings("unchecked")
                    List<ItemStack> itemStacks = (List<ItemStack>) list;
                    stacks.addAll(itemStacks);
                }
            }

            if (o instanceof RegistryDependentEntry entry) {
                stacks.addAll(entry.add(registryAccess));
            }
        }
    }

    public ConglomerateOfItems add(Item item) {
        conglomerate.add(item);
        return this;
    }

    public ConglomerateOfItems addBlock(Block block) {
        conglomerate.add(block);
        return this;
    }

    public ConglomerateOfItems add(ItemStack stack) {
        conglomerate.add(stack);
        return this;
    }

    public ConglomerateOfItems add(RegistryObject<? extends Item> registryObjectOfItem) {
        conglomerate.add(registryObjectOfItem);
        return this;
    }

    public ConglomerateOfItems addBlock(RegistryObject<? extends Block> registryObjectOfBlock) {
        conglomerate.add(registryObjectOfBlock);
        return this;
    }

    public ConglomerateOfItems add(ItemLike itemLike) {
        conglomerate.add(itemLike);
        return this;
    }

    public ConglomerateOfItems add(Supplier<ItemStack> itemStackSupplier) {
        conglomerate.add(itemStackSupplier);
        return this;
    }

    public ConglomerateOfItems add(List<ItemStack> listofStacks) {
        conglomerate.add(listofStacks);
        return this;
    }

    public ConglomerateOfItems add(RegistryDependentEntry entry) {
        conglomerate.add(entry);
        return this;
    }

    public interface RegistryDependentEntry {
        List<ItemStack> add(RegistryAccess registryAccess);
    }
}
