package com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Pistol1  extends pistol {
    private static final String AC_SHOO ="SHOO" ;

    {
        image  = ItemSpriteSheet.WORN_SHORTSWORD;


        hitSoundPitch = 1.1f;
        usesTargeting=true;
        tier = 4;

        bones = false;
    }

    public String targetingPrompt() {
            return Messages.get(this, "prompt");
        }
    @Override
    public int level() {//计算灵魂弓的等级
        int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl;
        if (curseInfusionBonus) level += 3+ level/6;
        return level;
    }

    public int weaponLevel  = -1;

    public int lvl;
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.image = ItemSpriteSheet.ARTIFACT_SANDALS;
        int i = this.lvl;
        if (i >= 1000) {
            this.image = ItemSpriteSheet.ARTIFACT_SANDALS;
        } else if (i >= 750) {
            this.image = ItemSpriteSheet.SPIRIT_BOW;
        } else if (i >= 550) {
            this.image = ItemSpriteSheet.GRIMM_PISTOL;
        }
        this.lvl = bundle.getInt("lvl");
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        this.image = ItemSpriteSheet.ARTIFACT_SANDALS;
        int i = this.lvl;
        if (i >= 1000) {
            this.image = ItemSpriteSheet.ARTIFACT_SANDALS;
        } else if (i >= 750) {
            this.image = ItemSpriteSheet.SPIRIT_BOW;
        } else if (i >= 550) {
            this.image = ItemSpriteSheet.GRIMM_PISTOL;
        }
        bundle.put("lvl", this.lvl);
    }
    public int proc(Char charR, Char charR2, int i) {
        if (charR2.HP <= i) {
            this.lvl++;
        }
        return super.proc(charR, charR2, i);
    }
    public int image() {
        int i = this.lvl;
        if (i >= 3) {
            this.image = ItemSpriteSheet.ARTIFACT_SANDALS;
        } else if (i == 2) {
            this.image = ItemSpriteSheet.SPIRIT_BOW;
        } else if (i == 1) {
            this.image = ItemSpriteSheet.GRIMM_PISTOL;
        }
        return this.image;
    }





    public ArrayList<String> actions(Hero hero) {//按钮
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SHOO);
        return actions;
    }

    public void execute(Hero hero, String action) {

        super.execute(hero, action);

            if (action.equals(AC_SHOO)) {
                if(level() >= 0){
                    lvl ++;
                    GLog.i(Messages.get(pistol.class, "charge2"+weaponLevel));
                }

        }

    }


    //返回一个描述武器信息的字符串。
    public String info() {
        String info = desc();
        info += ("\n\n" + Messages.get( this, "dj", (this.lvl),(this.lvl),(this.lvl)));
        info += ("_"+this.lvl+"_");
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
