package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.Bornclairvoyant;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.Dome;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.Die;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.Fusiliers;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.Fusiliers_A;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.pistol;

import java.util.ArrayList;
import java.util.Arrays;

public enum  EnchRecipe {
    ERASE(Fusiliers.class,  Dome.class, Bornclairvoyant.class),
    ERASEe(Fusiliers_A.class,  Doom.class, Bornclairvoyant.class),
    ERASE2(Die.class,  Dome.class, Amok.class);

    public ArrayList<Class<? extends Buff>> input = new ArrayList<>();
    public ArrayList<Class<? extends Item>> IE = new ArrayList<>();

    @SafeVarargs
    EnchRecipe(Class<? extends Item> ench, Class<? extends Buff>... in){
        this.IE.add(ench);
        this.input.addAll(Arrays.asList(in));

    }

    public static final int last_enchant_index = 1;
    //find a corresponding recipe and return it, or return null.

    public static EnchRecipe searchForRecipe(Item item) {
        for (EnchRecipe recipe : EnchRecipe.values()) {
            if (recipe.IE.contains(item.getClass())) {
                return recipe;
            }
        }
        return null;
    }
    // 根据给定的物品类获取对应的第一个 Buff 类
    public static Class<? extends Buff> getFirstBuffClassForItem(Item item) {
        EnchRecipe recipe = searchForRecipe(item); // 查找对应的配方
        if (recipe != null && !recipe.input.isEmpty()) {
            return recipe.input.get(0); // 返回第一个 Buff 类
        }
        return null; // 没有找到对应的 Buff 类
    }
    // Example method to get input classes of a recipe
    public ArrayList<Class<? extends Buff>> getInputClasses() {
        return new ArrayList<>(this.input);
    }
    //simply enchant
    //the return boolean indicates if ingredients should be consumed.
//    public static boolean enchant(EnchRecipe recipe){
//        if(recipe == null){
//            return false;
//        }
//        Inscription inscription = Reflection.newInstance(recipe.inscription);
//        if(inscription == null){
//            return false;
//        }
//        if(inscription instanceof CountInscription){
//            ((CountInscription) inscription).setTriggers(recipe.triggers);
//        }
//        if(toEnchant.inscription != null){
//            toEnchant.inscription.detachFromWeapon();
//        }
//        inscription.attachToWeapon(toEnchant);
//        return true;
//    }

}
