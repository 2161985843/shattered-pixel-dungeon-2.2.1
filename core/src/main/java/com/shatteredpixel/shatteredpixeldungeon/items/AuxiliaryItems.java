package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

abstract public  class AuxiliaryItems extends KindOfWeapon{
    protected static final float TIME_TO_EQUIP = 1f;

    protected String hitSound = Assets.Sounds.HIT;

    protected float hitSoundPitch = 1f;

    public void execute(Hero hero, String action) {
        if (hero.subClass == HeroSubClass.CHAMPION && action.equals(AC_EQUIP)){
            usesTargeting = false;
            String primaryName = Messages.titleCase(hero.belongings.auxiliary != null ? hero.belongings.auxiliary.trueName() : Messages.get(KindOfWeapon.class, "empty"));
            String secondaryName = Messages.titleCase(hero.belongings.secondWep != null ? hero.belongings.secondWep.trueName() : Messages.get(KindOfWeapon.class, "empty"));
            if (primaryName.length() > 18) primaryName = primaryName.substring(0, 15) + "...";
            if (secondaryName.length() > 18) secondaryName = secondaryName.substring(0, 15) + "...";
            GameScene.show(new WndOptions(
                    new ItemSprite(this),
                    Messages.titleCase(name()),
                    Messages.get(KindOfWeapon.class, "which_equip_msg"),
                    Messages.get(KindOfWeapon.class, "which_equip_primary", primaryName),
                    Messages.get(KindOfWeapon.class, "which_equip_secondary", secondaryName)
            ){
                @Override
                protected void onSelect(int index) {
                    super.onSelect(index);
                    if (index == 0 || index == 1){
                        //除了装备自己，物品还将自己重新分配到快速插槽
                        //这是一个特殊的情况，因为物品正在从库存中移除，但仍与英雄在一起。
                        int slot = Dungeon.quickslot.getSlot( AuxiliaryItems.this );
                        slotOfUnequipped = -1;
                        if (index == 0) {
                            doEquip(hero);
                        } else {
                            equipSecondary(hero);
                        }
                        if (slot != -1) {
                            Dungeon.quickslot.setSlot( slot, AuxiliaryItems.this );
                            updateQuickslot();
                            //如果该物品没有快速插入，但它所更换的装备是
                            //然后让物品占据未装备物品的快速插槽
                        } else if (slotOfUnequipped != -1 && defaultAction() != null) {
                            Dungeon.quickslot.setSlot( slotOfUnequipped, AuxiliaryItems.this );
                            updateQuickslot();
                        }
                    }
                }
            });
        } else {
            super.execute(hero, action);
        }
    }
    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.auxiliary() == this ;

    }
    @Override
    public boolean doEquip( Hero hero ) {
        boolean wasInInv = hero.belongings.contains(this);
        detachAll( hero.belongings.backpack );

        if (hero.belongings.weapon != null ||hero.belongings.auxiliary == null || hero.belongings.auxiliary.doUnequip( hero, true )) {

            hero.belongings.auxiliary = this;
            activate( hero );
            Talent.onItemEquipped(hero, this);
            Badges.validateDuelistUnlock();
            ActionIndicator.refresh();
            updateQuickslot();

            cursedKnown = true;
            if (cursed) {
                equipCursed( hero );
                GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
            }

            if (wasInInv && hero.hasTalent(Talent.SWIFT_EQUIP)) {
                if (hero.buff(Talent.SwiftEquipCooldown.class) == null){
                    hero.spendAndNext(-hero.cooldown());
                    Buff.affect(hero, Talent.SwiftEquipCooldown.class, 19f)
                            .secondUse = hero.pointsInTalent(Talent.SWIFT_EQUIP) == 2;
                    GLog.i(Messages.get(this, "swift_equip"));
                } else if (hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()) {
                    hero.spendAndNext(-hero.cooldown());
                    hero.buff(Talent.SwiftEquipCooldown.class).secondUse = false;
                    GLog.i(Messages.get(this, "swift_equip"));
                } else {
                    hero.spendAndNext(TIME_TO_EQUIP);
                }
            } else {
                hero.spendAndNext(TIME_TO_EQUIP);
            }
            return true;

        } else {

            collect( hero.belongings.backpack );
            return false;
        }
    }
    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        boolean second = hero.belongings.secondWep == this;

        if (second){
            //do this first so that the item can go to a full inventory
            hero.belongings.secondWep = null;
        }

        if (super.doUnequip( hero, collect, single )) {

            if (!second){
                hero.belongings.weapon = null;
            }
            return true;

        } else {

            if (second){
                hero.belongings.secondWep = this;
            }
            return false;

        }
    }


    public int min(){
        return min(buffedLvl());
    }

    public int max(){
        return max(buffedLvl());
    }

    abstract public int min(int lvl);
    abstract public int max(int lvl);

    public int damageRoll( Char owner ) {
        return Random.NormalIntRange( min(), max() );
    }

    public float accuracyFactor( Char owner, Char target ) {
        return 1f;
    }

    public float delayFactor( Char owner ) {
        return 1f;
    }

    public int reachFactor( Char owner ){
        return 1;
    }

    public boolean canReach( Char owner, int target){
        int reach = reachFactor(owner);
        if (Dungeon.level.distance( owner.pos, target ) > reach){
            return false;
        } else {
            boolean[] passable = BArray.not(Dungeon.level.solid, null);
            for (Char ch : Actor.chars()) {
                if (ch != owner) passable[ch.pos] = false;
            }

            PathFinder.buildDistanceMap(target, passable, reach);

            return PathFinder.distance[owner.pos] <= reach;
        }
    }

    public int defenseFactor( Char owner ) {
        return 0;
    }

    public int proc( Char attacker, Char defender, int damage ) {
        return damage;
    }

    public void hitSound( float pitch ){
        Sample.INSTANCE.play(hitSound, 1, pitch * hitSoundPitch);
    }


}
