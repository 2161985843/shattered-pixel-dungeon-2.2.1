package com.shatteredpixel.shatteredpixeldungeon.items.limb;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.Dome;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Limb extends Item {
    {
        image = ItemSpriteSheet.ORE;

        unique = true;
    }
    public enum Buff {
        SPEED_BOOST(Dome.class),
        DAMAGE_BOOST(Amok.class),
        DEFENSE_BOOST(Amok.class); // 使用 null 作为类类型并设置默认名称

        private final Class<?> clazz;

        // 构造函数，接受 Class 类型
        Buff(Class<?> clazz) {
            this.clazz = clazz;
        }

        // 获取 Class 对象的方法
        public Class<?> getClazz() {
            return clazz;
        }

    }
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
