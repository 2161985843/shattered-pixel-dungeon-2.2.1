package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GiantRatSprite;
/**
 *大鼠汉姆林
 */
public class GiantRat extends Mob {
    {
        spriteClass = GiantRatSprite.class;

        HP = HT = 60;
        defenseSkill = 5;
        baseSpeed = 0.5f;

        EXP = 10;
        maxLvl = 7;

        loot = new MysteryMeat();
        lootChance = 0.167f;
    }
}
