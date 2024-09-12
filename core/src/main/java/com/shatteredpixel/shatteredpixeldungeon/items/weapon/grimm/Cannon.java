package com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm;


import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Cannon  extends pistol{
    {
        image = ItemSpriteSheet.GUN_Cannon;
        MAX_VOLUME=1;//子弹上限
        turn = 1;//连发次数
        time=3;//装载回合
        lianfa=false;//是否连发
    }
    public int min(int lvl) {
        int dmg =10+lvl;
        return Math.max(0, dmg);
    }
    @Override
    public int max(int lvl) {
        int dmg = 20+lvl*(10);
        return Math.max(0, dmg);
    }
    public int STRReq(int lvl) {
        return STRReq(4, lvl); //tier 1
    }
}
