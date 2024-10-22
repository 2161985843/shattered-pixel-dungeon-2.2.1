package com.shatteredpixel.shatteredpixeldungeon.items.limb;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Limb extends Item {

    {
        image = ItemSpriteSheet.LIMB;
// 表示物品的等级是否已知，默认值为false
        // 表示物品的等级是否已知，默认值为false
        levelKnown = true;
        // 表示该物品是否可以堆叠，默认值为false
        // 保护字段，表示物品的数量，默认为1
         quantity = 1;
        identify();
        unique = true;
    }


}
