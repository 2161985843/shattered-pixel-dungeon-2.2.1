package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.Bornclairvoyant;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.Dome;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.GhoulsClaw;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.limb.Limb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.Die;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.Fusiliers_A;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public enum  EnchRecipe {
    ERASE(Limb.class,  GhoulsClaw.class, GhoulsClaw.class),
    ERASEe(Fusiliers_A.class,  Doom.class, Bornclairvoyant.class),
    ERASE2(Die.class,  Dome.class, Amok.class);

    public ArrayList<Class<? extends Buff>> input = new ArrayList<>();
    public ArrayList<Class<? extends Item>> IE = new ArrayList<>();

    @SafeVarargs
    EnchRecipe(Class<? extends Item> ench, Class<? extends Buff>... in){
        this.IE.add(ench);
        this.input.addAll(Arrays.asList(in));

    }
    public static EnchRecipe searchForRecipe(Item item) {
        for (EnchRecipe recipe : EnchRecipe.values()) {
            if (recipe.IE.contains(item.getClass())) {
                return recipe;
            }
        }
        return null;
    }

    // Example method to get input classes of a recipe
    public ArrayList<Class<? extends Buff>> getInputClasses() {
        return new ArrayList<>(this.input);
    }
    public boolean canUseErase(ArrayList<Buff> currentBuffs) {
        for (Class<? extends Buff> buffClass : input) {
            for (Buff buff : currentBuffs) {
                if (buff.getClass() == buffClass) {
                    return true; // 如果当前 Buff 中存在与传入 Buff 相同的 Buff，则返回 true
                }
            }
        }
        return false; // 否则返回 false
    }
    public static <T extends Buff> T appendd(Class<T> buffClass) {
        T buff = Reflection.newInstance(buffClass); // 创建指定类型的 buff 实例
        return buff; // 返回创建的 buff 实例
    }

}
