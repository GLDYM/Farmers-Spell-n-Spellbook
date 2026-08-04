package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.spells.BadAppleSpell;
import com.chenjdy.farmers_spell.spells.ChaosSlashSpell;
import com.chenjdy.farmers_spell.spells.GoodberrySpell;
import com.chenjdy.farmers_spell.spells.PhantomLootSpell;
import com.chenjdy.farmers_spell.spells.PreserveCircleSpell;
import com.chenjdy.farmers_spell.spells.SealCoatSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
public class ModSpells {

    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, FarmersSpell.MODID);

    public static final DeferredHolder<AbstractSpell, AbstractSpell> GOODBERRY_SPELL = SPELLS.register("goodberry", GoodberrySpell::new);
    public static final DeferredHolder<AbstractSpell, AbstractSpell> PHANTOM_LOOT_SPELL = SPELLS.register("phantom_loot", PhantomLootSpell::new);
    public static final DeferredHolder<AbstractSpell, AbstractSpell> SEAL_COAT_SPELL = SPELLS.register("seal_coat", SealCoatSpell::new);
    public static final DeferredHolder<AbstractSpell, AbstractSpell> BAD_APPLE_SPELL = SPELLS.register("bad_apple", BadAppleSpell::new);
    public static final DeferredHolder<AbstractSpell, AbstractSpell> CHAOS_SLASH_SPELL = SPELLS.register("chaos_slash", ChaosSlashSpell::new);
    public static final DeferredHolder<AbstractSpell, AbstractSpell> PRESERVE_CIRCLE_SPELL = SPELLS.register("preserve_circle", PreserveCircleSpell::new);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }
}
