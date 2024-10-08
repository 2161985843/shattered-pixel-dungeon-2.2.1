package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BanditkillerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

/**
 *持刀匪徒
 */
public class Banditkiller extends Mob {
    {
        spriteClass = BanditkillerSprite.class;

        HP = HT = 16;
        defenseSkill = 15;
        baseSpeed = 2f;

        EXP = 7;
        maxLvl = 15;

        flying = true;

        loot = Gold.class;
        lootChance = 0.5f;
    }
    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 4 );
    }
    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 3);
    }
    @Override
    public void damage(int dmg, Object src) {
        if (src == Chasm.class) return;
        super.damage(dmg, src);
        boolean heroKilled = false;
        if (dmg > 5) {
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
            if (ch != null && ch.isAlive()) { // 检查角色是否存在且存活
                // 计算随机伤害
                int damage = Math.round(Random.NormalIntRange(3, 7));
                // 应用挑战修正
                damage = Math.round(damage * AscensionChallenge.statModifier(this));
                // 减去防御值，确保伤害不为负
                damage = Math.max(0, damage - (enemy.drRoll() + enemy.drRoll()));
                sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "fanj") );
                ch.damage(damage, this); // 施加伤害
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
            }
                if (heroKilled) {
                    Dungeon.fail( this );
                    GLog.n( Messages.get(this, "banditkiller_kill") );
                }
        }
        }

    }
    @Override
    public int defenseProc (Char enemy,int damage){

        return super.defenseProc(enemy, damage);
    }
    }

