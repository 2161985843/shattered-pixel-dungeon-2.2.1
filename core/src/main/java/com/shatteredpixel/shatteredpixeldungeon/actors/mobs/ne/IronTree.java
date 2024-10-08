package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IronTreeSprint;
import com.watabou.utils.Random;

public class IronTree extends Mob {
    {
        spriteClass = IronTreeSprint.class;

        HP = HT = 18000;
        defenseSkill = 0;
        nothingness=true;
        EXP = 1;

        loot = Generator.Category.SEED;
        lootChance = 0.75f;
        viewDistance = 1;

    }
    public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {

        return false;
    }
    @Override
    protected boolean act() {

        if (enemy == null || !Dungeon.level.adjacent(pos, enemy.pos)) {

            HP = Math.min(HT, HP + 5);
        }

        throwItems();
        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        if (src instanceof Burning) {
            destroy();
            sprite.die();
        } else {
            super.damage(dmg, src);
            if (dmg > 10) {
                // 当伤害超过10时掉落物品
                loot = Generator.Category.SEED;
                if (Random.Float() < lootChance()) {
                    Item lootItem = createLoot();
                    if (lootItem != null) {
                        Dungeon.level.drop(lootItem, pos).sprite.drop();
                    }
                }
            }
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc( enemy, damage );
        Buff.affect( enemy, Cripple.class, 10f );
        return super.attackProc(enemy, damage);
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(0, 0);
    }

    @Override
    public int attackSkill( Char target ) {
        return 25;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 8);
    }

    {
        immunities.add( ToxicGas.class );
    }

}
