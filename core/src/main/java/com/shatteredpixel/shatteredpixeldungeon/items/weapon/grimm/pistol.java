package com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.Actor.TICK;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

/**
 * 枪的
 */

public class pistol extends MeleeWeapon {
    public static final String AC_SHOOT		= "SHOOT";
    public static final String AC_SHOOl	= "SHOOl";

    {
        image = ItemSpriteSheet.IRNO_DOTA2;
        defaultAction = AC_SHOOT;
        support=true;
        hitSoundPitch = 1.1f;
        usesTargeting=true;

        bones = false;
    }

    public   int MAX_VOLUME	= 3;//子弹上限
    public   int INITIAL_VOLUME = 3;//初始子弹数
    public   int time = 3;//装载耗时

    private int volume ;

    public pistol() {
        this.volume = INITIAL_VOLUME;
    }

    public void fill() {volume = MAX_VOLUME;updateQuickslot();}
    public ArrayList<String> actions(Hero hero) {//按钮
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && !cursed && hero.buff(Charm.class) == null) {
            actions.add(AC_SHOOT);
        }
        actions.add(AC_SHOOl);
        return actions;
    }
    private  final String VOLUME	= "volume";
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(VOLUME, volume);
    }
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        volume = bundle.getInt(VOLUME);
    }
    @Override//该方法返回武器在给定等级下的最小伤害值。
    public int min(int lvl) {
        int dmg =1+ Dungeon.hero.lvl/5
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
                + (curseInfusionBonus ? 1 + Dungeon.hero.lvl/30 : 0);
        return Math.max(0, dmg);
    }
    @Override
    public int max(int lvl) {
        int dmg = 10 ;
        return Math.max(0, dmg);
    }
    @Override
    public int damageRoll(Char owner) {
        int damage = Random.NormalIntRange( min(), max() );

//        switch (augment){
//            case NONE:
//                damage = Math.round(damage * 0.667f);
//                break;
//            case SPEED:
//                damage = Math.round(damage * 0.5f);
//                break;
//            case DAMAGE:
//                break;
//        }

        return damage;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        return super.proc(attacker, defender, damage);
    }
    @Override
    public void throwSound() { //播放攻击时的声音效果。
        Sample.INSTANCE.play( Assets.Sounds.ATK_SPIRITBOW, 1, Random.Float(0.87f, 1.15f) );
    }
    private Hero currentHero;
    @Override
    public void execute(Hero hero, String action) {
        this.currentHero = hero;
        super.execute(hero, action);
        if (action.equals(AC_SHOOT)) {
            if (volume > 0) {
//                SpellSprite.show(curUser, SpellSprite.PISTOL);
                curUser = hero;
                curItem = this;
                GameScene.selectCell(shooter);
            } else {
                volume += MAX_VOLUME;
                hero.spend((this.time));
                hero.busy();
                hero.sprite.operate(hero.pos);
                GLog.i(Messages.get(pistol.class, "charge1"));

            }
        }
        if (action.equals(AC_SHOOl)) {
            if(volume >= 0){
                volume += (MAX_VOLUME-volume);
            }
            hero.spend((this.time));
            hero.busy();
            hero.sprite.operate(hero.pos);
            GLog.i(Messages.get(pistol.class, "charge1"));
        }

    } @Override
    public int STRReq(int lvl) {
        return STRReq(1, lvl); //tier 1
    }
    public pistol.SpiritArrow knockArrow(){
        return new SpiritArrow();
    }
    public class SpiritArrow extends MissileWeapon { //子弹武器

        {
            image = ItemSpriteSheet.SPIRIT_ARROW1;

            hitSound = Assets.Sounds.HIT_ARROW;
        }
        @Override
        public float accuracyFactor(Char owner, Char target) {
         return super.accuracyFactor(owner, target);
        }
        @Override//附魔
        public int proc(Char attacker, Char defender, int damage) {
            return pistol.this.proc(attacker, defender, damage);
        }

        @Override
        public int STRReq(int lvl) {return pistol.this.STRReq();}
        @Override
        public int damageRoll(Char owner) {
            return pistol.this.damageRoll(owner);
        }

        public ItemSprite.Glowing glowing() {//炸弹逐渐变红
            return  new ItemSprite.Glowing(0xFF0000, 0.6f) ;
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );//查找指定位置上的敌人角色
//            GameScene.flash(0xFF0000);
            if (enemy == null||enemy == curUser) {//如果找不到敌人或者敌人是当前用户
                parent = null;
                // Splash.at( cell, 0xCC99FFFF, 1 );//则将parent设置为null，并在该位置创建一个水花效果（Splash）
                new ItemSprite.Glowing( 0xFF0000, 0.6f);
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 1);
                CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
            } else {
                if (!curUser.shoot(enemy, new SpiritArrow())) {
                    new ItemSprite.Glowing( 0xFF0000, 0.6f);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 1);
                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
                }

            }

        }
    }
    SpiritArrow spiritArrow = new SpiritArrow();

    public String status() {
        if (levelKnown) {
            updateQuickslot();
            return  volume  + "/" + MAX_VOLUME;
        } else {
            return null;
        }
    }
    public boolean lianfa=true;
    public   int turn = 2;//装载耗时
    private final CellSelector.Listener shooter = new CellSelector.Listener() {
       //这表示在选择目标后，会使用 knockArrow() 方法返回的值来进行箭矢的投射，投射的目标是传入的 target。

        public void onSelect( Integer target ) {
            if (target != null ){
                if ( isEquipped(currentHero) &&volume > 0) {
                    volume--;
                    if (lianfa){
                        for (int I=0;I<turn;I++){
                        hero.spend(-1f*TICK);
                        knockArrow().cast(curUser, target); }
                        hero.spend(1f);}
                    else {
                        knockArrow().cast(curUser, target);
                    }

                }else {
                    GLog.i( Messages.get(pistol.class, "warn"));
                }
            }
            else if (volume <= 0 ) {GLog.i( Messages.get(pistol.class, "charge") );}
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };



    @Override//返回一个描述武器信息的字符串。
    public String info() {
        String info = desc();

        info += "\n\n" + Messages.get( pistol.class, "stats",
                Math.round(augment.damageFactor(min())),
                Math.round(augment.damageFactor(max())),
                STRReq());

//        if (STRReq() > hero.STR()) {
//            info += " " + Messages.get(Weapon.class, "too_heavy");
//        } else if (hero.STR() > STRReq()){
//            info += " " + Messages.get(Weapon.class, "excess_str", hero.STR() - STRReq());
//        }
        info += "\n\n" + Messages.get( pistol.class, "stats1",MAX_VOLUME,turn,time);
        switch (augment) {
            case SPEED:
                info += "\n\n" + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += "\n\n" + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        info += "\n\n" + Messages.get(MissileWeapon.class, "distance");

        return info;
    }


}

