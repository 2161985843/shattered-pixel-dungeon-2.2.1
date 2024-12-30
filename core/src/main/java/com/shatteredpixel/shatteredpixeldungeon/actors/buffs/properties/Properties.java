package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.HashSet;
import java.util.Set;

public class Properties extends Buff {
    {
        revivePersists=true;
    }

    public enum Tag {

        Myth,//神话
        Abyss,//深渊
        Legend,//传奇，
        Hero,//勇者，
        Superhuman,//超凡
        Ordinary;//通常
        Tag() {

        }

    }
    public String name() {
        return Messages.get(this, "name"); // 获取角色名称
    }

    public String desc() {
        return Messages.get(this, "desc"); // 获取角色描述
    }
    // 是否允许获得该技能
    public boolean isAcquireAllowed(Hero hero) {
        return false;
    }

    // 获得技能时的行为
    public void onGain() {}

    // 失去技能时的行为
    public void onLose() {}

    // 检查是否可以获得该技能
    protected boolean canBeGain(Hero hero) {
        return true;
    }

    private int level;
    private final Set<Tag> tags = new HashSet<>();
    // 构造函数

    private int strengthBonusIncrement = 1; // 力量加成增量值
    private static int StrengthBonus=1; // 当前总力量加成



    public static int getIncrementValue() {
        // 获取力量加成增量值的当前值
        return StrengthBonus;
    }


}
