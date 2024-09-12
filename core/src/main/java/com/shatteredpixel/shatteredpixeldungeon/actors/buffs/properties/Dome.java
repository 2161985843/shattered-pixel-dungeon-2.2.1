package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

public class Dome extends Properties{



        private int healingLeft;

        private float percentHealPerTick;
        private int flatHealPerTick;

        {
            //unlike other buffs, this one acts after the hero and takes priority against other effects
            //healing is much more useful if you get some of it off before taking damage
            actPriority = HERO_PRIO - 1;

            type = buffType.POSITIVE;
        }

        @Override
        public boolean act(){

            Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.FOOD ) ), Dungeon.hero.pos );
            target.HT=target.HT+100;
        spend( TICK );

        return true;
    }

        private int healingThisTick(){
        return (int) GameMath.gate(1,
                Math.round(healingLeft * percentHealPerTick) + flatHealPerTick,
                healingLeft);
    }

        public void setHeal(int amount, float percentPerTick, int flatPerTick){
        //multiple sources of healing do not overlap, but do combine the best of their properties
        healingLeft = Math.max(healingLeft, amount);
        percentHealPerTick = Math.max(percentHealPerTick, percentPerTick);
        flatHealPerTick = Math.max(flatHealPerTick, flatPerTick);
    }

        public void increaseHeal( int amount ){
        healingLeft += amount;
    }

        @Override
        public void fx(boolean on) {
        if (on) target.sprite.add( CharSprite.State.HEALING );
        else    target.sprite.remove( CharSprite.State.HEALING );
    }

        private static final String LEFT = "left";
        private static final String PERCENT = "percent";
        private static final String FLAT = "flat";

        @Override
        public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, healingLeft);
        bundle.put(PERCENT, percentHealPerTick);
        bundle.put(FLAT, flatHealPerTick);
    }

        @Override
        public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        healingLeft = bundle.getInt(LEFT);
        percentHealPerTick = bundle.getFloat(PERCENT);
        flatHealPerTick = bundle.getInt(FLAT);
    }
        @Override
        public String iconTextDisplay() {
        return Integer.toString(healingLeft);
    }

        @Override
        public String desc() {
        return Messages.get(this, "desc", healingThisTick(), healingLeft);
    }
    }

