package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class GhoulsTongue extends Properties{
    private int strengthBonusIncrement = 1; // 力量加成增量值
    private static int StrengthBonus=1; // 当前总力量加成

    {
        if (StrengthBonus==5){
        GLog.n(Messages.get(this, "5"));}
        if (StrengthBonus==10){
            GLog.n(Messages.get(this, "10"));}
        if (StrengthBonus==11){
            GLog.n(Messages.get(this, "20"));
        }
    }

    public static int getIncrementValue() {
        // 获取力量加成增量值的当前值
        return StrengthBonus;
    }

    // 方法示例，用于增加力量加成
    public void addStrengthBonus() {
        StrengthBonus += 1;
    }

    public String name() {
        return Messages.get(this, "name","+"+(StrengthBonus));
    }

    public String desc() {
        return Messages.get(this, "desc");
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
