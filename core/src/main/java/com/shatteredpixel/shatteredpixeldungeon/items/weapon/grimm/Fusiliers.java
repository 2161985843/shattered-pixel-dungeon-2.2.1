package com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Fusiliers extends pistol{
    {
        image = ItemSpriteSheet.GUN_Fusiliers;
        MAX_VOLUME=1;//子弹上限
        turn = 1;//连发次数
        time=1;//装载回合
        lianfa=false;//是否连发
    }
    public int min(int lvl) {
        int dmg =1+lvl;
        return Math.max(0, dmg);
    }
    @Override
    public int max(int lvl) {
        int dmg = 7+lvl*(3);
        return Math.max(0, dmg);
    }
    public int STRReq(int lvl) {
        return STRReq(1, lvl); //tier 1
    }
}
