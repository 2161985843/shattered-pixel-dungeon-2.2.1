package com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Changtong_a  extends pistol{
    {
        image = ItemSpriteSheet.GUN_Fusiliers_A;

    }
    public int min(int lvl) {
        int dmg =1+ Dungeon.hero.lvl/5
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
                + (curseInfusionBonus ? 1 + Dungeon.hero.lvl/30 : 0);
        return Math.max(0, dmg);
    }
    @Override
    public int max(int lvl) {
        int dmg = 20 ;
        return Math.max(0, dmg);
    }
}
