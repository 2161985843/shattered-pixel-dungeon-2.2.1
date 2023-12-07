package com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class pistol extends MeleeWeapon {
    public static final String AC_SHOOT		= "SHOOT";
    public static final String AC_SHOOl	= "SHOOl";

    {
        image = ItemSpriteSheet.GRIMM_PISTOL;
        defaultAction = AC_SHOOT;

        hitSoundPitch = 1.1f;
        usesTargeting=true;
        tier = 1;

        bones = false;
    }


    @Override//该方法返回武器在给定等级下的最小伤害值。
    public int min(int lvl) {
        int dmg = 1 + Dungeon.hero.lvl/5
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
                + (curseInfusionBonus ? 1 + Dungeon.hero.lvl/30 : 0);
        return Math.max(0, dmg);
    }
    public int STRReq(int lvl) {
        return STRReq(3, lvl); //tier 1
    }
    @Override
    public int max(int lvl) {
        int dmg = 1 + (int)(Dungeon.hero.lvl/2.5f)
                + 2*RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
                + (curseInfusionBonus ? 2 + Dungeon.hero.lvl/15 : 0);
        return Math.max(0, dmg);
    }
    @Override// 计算伤害
    public int damageRoll(Char owner) {
        int damage = augment.damageFactor(super.damageRoll(owner));

        if (owner instanceof Hero) {
            int exStr = ((Hero)owner).STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange( 0, exStr );
            }
        }
        return damage;
    }

    private  final int MAX_VOLUME	= 3;
    public int quantity = 1;
    private  final int INITIAL_VOLUME = 10;
    private int volume ;
    public pistol() {
        this.volume = INITIAL_VOLUME;
    }
    public ArrayList<String> actions(Hero hero) {//按钮
        ArrayList<String> actions = super.actions(hero);

        actions.add(AC_SHOOT);

        actions.add(AC_SHOOl);

        return actions;
    }
    private  final String VOLUME	= "volume";
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(VOLUME, volume);
    }
    public void fill() {
        volume = MAX_VOLUME;
        updateQuickslot();
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        volume = bundle.getInt(VOLUME);
    }

    public int level() {//计算灵魂弓的等级
        int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl;
        if (curseInfusionBonus) level += 1 + level/6;
        return level;
    }

    @Override
    public void throwSound() { //播放攻击时的声音效果。
        Sample.INSTANCE.play( Assets.Sounds.ATK_SPIRITBOW, 1, Random.Float(0.87f, 1.15f) );
    }
    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);
        if (action.equals(AC_SHOOT)) {
            if (volume > 0) {

                curUser = hero;
                curItem = this;
                GameScene.selectCell(shooter);
            } else {
                volume += MAX_VOLUME;
                hero.spend(1f);
                hero.busy();
                hero.sprite.operate(hero.pos);
                GLog.i(Messages.get(pistol.class, "charge1"));
            }
        }
        if (action.equals(AC_SHOOl)) {
            if(volume >= 0){
                volume += (MAX_VOLUME-volume);
            }

            hero.spend(1f);
            hero.busy();
            hero.sprite.operate(hero.pos);
            GLog.i(Messages.get(pistol.class, "charge1"));
        }

    }

    public pistol.SpiritArrow knockArrow(){
        return new SpiritArrow();
    }
    public static class SpiritArrow extends MissileWeapon { //子弹武器

        {
            image = ItemSpriteSheet.SPIRIT_ARROW1;

            hitSound = Assets.Sounds.HIT_ARROW;
        }

     @Override
        public float accuracyFactor(Char owner, Char target) {
         return super.accuracyFactor(owner, target);
        }

        public ItemSprite.Glowing glowing() {//炸弹逐渐变红
            return  new ItemSprite.Glowing(0xFF0000, 0.6f) ;
        }
        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );//查找指定位置上的敌人角色

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
        @Override// 计算伤害
        public int damageRoll(Char owner) {
            int damage = augment.damageFactor(super.damageRoll(owner));
            if (owner instanceof Hero) {
                int exStr = ((Hero)owner).STR() - STRReq();
                if (exStr > 0) {
                    // 不再将力量值的增加作为额外伤害
                     damage += Random.IntRange(0, exStr);

                    // 或者你可以将力量值的增加乘以一个修正系数，然后加到伤害值上
                    double strModifier = exStr; // 修正系数可以根据需要调整

                }
            }
            return damage;
        }

        @Override//该方法返回武器在给定等级下的最小伤害值。
        public  int min(int lvl) {
            int dmg = 12 + Dungeon.hero.lvl/5
                    + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
                    + (curseInfusionBonus ? 1 + Dungeon.hero.lvl/30 : 0);
            return Math.max(0, dmg);
        }

        @Override
        public int max(int lvl) {
            int dmg = 26 + (int)(Dungeon.hero.lvl/2.5f)
                    + 2*RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
                    + (curseInfusionBonus ? 2 + Dungeon.hero.lvl/15 : 0);
            return Math.max(0, dmg);
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


    private final CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override//这表示在选择目标后，会使用 knockArrow() 方法返回的值来进行箭矢的投射，投射的目标是传入的 target。

        public void onSelect( Integer target ) {
            if (target != null ){
                if ( volume > 0) {
                    volume--;
                    knockArrow().cast(curUser, target);

                }
            }
            else if (volume <= 0 )
            {GLog.i( Messages.get(pistol.class, "charge") );}

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
                Math.round(augment.damageFactor(spiritArrow.min())),
                Math.round(augment.damageFactor(spiritArrow.max())),
                STRReq());
        info += "\n\n" + Messages.get(pistol.class, "stats1",

                Math.round(augment.damageFactor(min())),
                Math.round(augment.damageFactor(max())),
                STRReq());

        if (STRReq() > Dungeon.hero.STR()) {
            info += " " + Messages.get(Weapon.class, "too_heavy");
        } else if (Dungeon.hero.STR() > STRReq()){
            info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
        }

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

        if (cursed && isEquipped( Dungeon.hero )) {
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

