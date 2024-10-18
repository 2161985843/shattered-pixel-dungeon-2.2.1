package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.limb.Limb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class LimbBags  extends Bag {

    {
        image = ItemSpriteSheet.LIMBBAGS;
    }

    @Override
    public boolean canHold( Item item ) {
        if (item instanceof Limb ){
            return super.canHold(item);
        } else {
            return false;
        }
    }

    public int capacity(){
        return 19;
    }

    @Override
    public int value() {
        return 30;
    }
}
