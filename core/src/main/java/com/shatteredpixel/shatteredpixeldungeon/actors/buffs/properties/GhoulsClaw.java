package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class GhoulsClaw extends Properties{
    private int strengthBonusIncrement = 1; // 力量加成增量值
    private static int StrengthBonus=1; // 当前总力量加成

    public static int getIncrementValue() {
        // 获取力量加成增量值的当前值
        return StrengthBonus;
    }

    // 方法示例，用于增加力量加成
    public void addStrengthBonus() {
        StrengthBonus += strengthBonusIncrement;
    }

    public  int getTotalStrengthBonus() {
        return StrengthBonus;
    }

    public String name() {
        return Messages.get(this, "name","+"+(StrengthBonus));
    }

    public String desc() {
        return Messages.get(this, "desc", StrengthBonus);
    }

    private static final String FLAT = "flat";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FLAT, StrengthBonus);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        StrengthBonus = bundle.getInt(FLAT);
    }
}
