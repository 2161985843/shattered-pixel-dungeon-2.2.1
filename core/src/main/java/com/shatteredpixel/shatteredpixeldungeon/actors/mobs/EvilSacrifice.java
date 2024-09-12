package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EvilSacrificeSprite;

public class EvilSacrifice extends Mob{
    {
        spriteClass = EvilSacrificeSprite.class;

        HP = HT = 30;
        defenseSkill = 15;
        baseSpeed = 2f;

        EXP = 7;
        maxLvl = 15;

        flying = true;

        loot = new PotionOfHealing();
        lootChance = 0.1667f; //by default, see lootChance()
    }

}
