package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.limb.GhoulsClawLimb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulsSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Scanner;

/**
 *食尸鬼
 */
public class Ghouls  extends Mob {


    {
        // 角色的精灵类，定义角色的外观和动画
        spriteClass = GhoulsSprite.class;

        // 角色的当前生命值和最大生命值，均设置为45
        HP = HT = 25;

        defenseSkill = 9;

        EXP = 5;
        maxLvl = 10;


        // 初始状态设置为睡眠状态
        state = SLEEPING;

        // 定义角色掉落的物品类型
        loot = GhoulsClawLimb.class; // 掉落金钱
        lootChance = 0.3f; // 掉落的概率为20%

        // 角色的属性集合，包含该角色的特性
        properties.add(Property.UNDEAD); // 角色属性包括“亡灵”
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 10);
    }

    private boolean biteProc = false; // 改为实例变量

    public void setBiteProc(boolean value) {
        this.biteProc = value;
    }

    private int attackMode = 0; // 攻击模式切换变量

    public void voidattackMode(int sto) {
        this.attackMode = sto;
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        if (biteProc) {
            biteProc = false;
            // 随机偷取1到3点生命值
            int healthSteal = Random.Int(1, 4);
            HP += healthSteal; // 将偷取的生命值加到当前生命值上

            // 如果当前生命值超过最大生命值，给予角色一个伤害吸收护盾
            if (HP > HT) Buff.affect(this, Barrier.class).setShield(damage);
            // 确保当前生命值不超过最大生命值
            HP = Math.min(HP, HT);

            // 发射生命恢复的特效
            this.sprite.emitter().burst(Speck.factory(Speck.HEALING), healthSteal);
            // 显示恢复生命值的状态
            this.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healthSteal);
        }
        biteProc = false;
        return damage; // 返回施加的伤害
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 5);
    }
}
