package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class GhoulsClaw extends Properties{
    private int strengthBonusIncrement = 1; // 力量加成增量值
    private static int totalStrengthBonus; // 当前总力量加成

    public int getIncrementValue() {
        // 获取力量加成增量值的当前值
        return strengthBonusIncrement;
    }

    // 方法示例，用于增加力量加成
    public void addStrengthBonus() {
        totalStrengthBonus += strengthBonusIncrement;
    }

    public static int getTotalStrengthBonus() {
        return totalStrengthBonus;
    }

    public String name() {
        return Messages.get(this, "name","+"+(totalStrengthBonus));
    }

    public String desc() {
        return Messages.get(this, "desc", totalStrengthBonus);
    }

    private static final String FLAT = "flat";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FLAT, totalStrengthBonus);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        totalStrengthBonus = bundle.getInt(FLAT);
    }
}
