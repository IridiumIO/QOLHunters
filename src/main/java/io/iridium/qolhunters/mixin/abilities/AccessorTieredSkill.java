package io.iridium.qolhunters.mixin.abilities;

import iskallia.vault.skill.base.LearnableSkill;
import iskallia.vault.skill.base.TieredSkill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = TieredSkill.class, remap = false)
public interface AccessorTieredSkill {
    @Accessor
    List<LearnableSkill> getTiers();
}
