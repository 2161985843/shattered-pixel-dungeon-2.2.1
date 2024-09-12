package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.TestGenerator;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Tool  extends Bag {

    {
        image = ItemSpriteSheet.POUCH;
    }

    @Override
    public boolean canHold( Item item ) {
        if (item instanceof TestGenerator||item instanceof TestItem){
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
