package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class Bornclairvoyant extends Properties{

    {
        type = buffType.POSITIVE;
    }


    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
        GameScene.updateFog();
    }
}
