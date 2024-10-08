package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArmedbanditsSprite;
import com.watabou.utils.Random;
/**
 *持枪匪徒
 */
public class Armedbandits extends Mob {
    {
    spriteClass = ArmedbanditsSprite.class;

    HP = HT = 15; // 生命值为20
    defenseSkill = 5; // 防御技能为5

    EXP = 5;

    loot = Generator.Category.MIS_T2;
    lootChance = 1f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 6 );
    }

    // 攻击技能设置
    @Override
    public int attackSkill(Char target) {
        return 8; // 攻击技能值为16
    }

    // 检查角色是否可以攻击敌人
    @Override
    protected boolean canAttack(Char enemy) {
        // 检查角色是否可以攻击敌人：
        // 角色与敌人之间是否不相邻，或者攻击轨迹是否命中敌人
        return !Dungeon.level.adjacent(pos, enemy.pos)
                && (super.canAttack(enemy) || new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
    }
    // 攻击过程处理
    @Override
    public int attackProc(Char enemy, int damage) {return super.attackProc(enemy, damage); }

    // 移动行为
    @Override
    protected boolean getCloser(int target) {

        if (state == HUNTING) {
            return enemySeen && getFurther(target); // 追击状态
        } else {
            return super.getCloser(target); // 否则调用父类方法
        }
    }

    // 仇恨管理
    @Override
    public void aggro(Char ch) {
        // 只能对可见目标施加仇恨
        if (ch == null || fieldOfView == null || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

    // 创建掉落物品
    @Override
    public Item createLoot() {
        MissileWeapon drop = (MissileWeapon) super.createLoot(); // 创建导弹武器
        drop.quantity((drop.quantity() + 1) / 2); // 数量减半
        return drop; // 返回掉落物品
    }

//    private boolean targeting = false; // 是否正在瞄准
//    private boolean shot = true; // 是否已经射击
//
//    private int cellToFire = 0; // 射击目标位置
//    protected boolean doAttack(Char enemy) {
//        if (Dungeon.level.adjacent(pos, enemy.pos)) {
//            shot = true; // 表示已射击
//            targeting = false; // 取消瞄准
//
//            return super.doAttack(enemy); // 调用父类的攻击方法
//        } else if (shot) {
//            targeting = true; // 开始瞄准
//            shot = false; // 重置射击状态
//            sprite.parent.add(new TargetedCell(enemy.pos, 0xFF0000)); // 显示目标
//            for (int c : PathFinder.NEIGHBOURS4) {
//                sprite.parent.add(new TargetedCell(enemy.pos + c, 0xFF0000)); // 显示邻近目标
//            }
//            cellToFire = enemy.pos; // 设置射击目标位置
//
//            spend(attackDelay()); // 消耗时间
//            return true; // 返回攻击成功
//        } else {
//            shot = true; // 表示已射击
//            targeting = false; // 取消瞄准
//            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
//                sprite.zap(cellToFire); // 触发电击动画
//                return false; // 返回攻击失败
//            } else {
//
//                return true; // 返回攻击成功
//            }
//        }

}
