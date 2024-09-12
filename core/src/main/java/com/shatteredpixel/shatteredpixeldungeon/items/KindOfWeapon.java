/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.watabou.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

abstract public class KindOfWeapon extends EquipableItem {
	
	protected static final float TIME_TO_EQUIP = 1f;

	protected String hitSound = Assets.Sounds.HIT;
	protected float hitSoundPitch = 1f;
	private boolean equipFull =false;;


	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_EQUIP)&&hero.subClass == HeroSubClass.CHAMPION){
			usesTargeting = false;
			String primaryName = Messages.titleCase(hero.belongings.weapon != null ? hero.belongings.weapon.trueName() : Messages.get(KindOfWeapon.class, "empty"));
			String auxiliaryName = Messages.titleCase(hero.belongings.auxiliary != null ? hero.belongings.auxiliary.trueName() : Messages.get(KindOfWeapon.class, "empty"));
			String secondaryName = Messages.titleCase( hero.belongings.secondWep != null ? hero.belongings.secondWep.trueName() : Messages.get(KindOfWeapon.class, "empty"));

			if (primaryName.length() > 18) primaryName = primaryName.substring(0, 15) + "...";
			if (secondaryName.length() > 18) secondaryName = secondaryName.substring(0, 15) + "...";

				GameScene.show(new WndOptions(
						new ItemSprite(this),
						Messages.titleCase(name()),
						Messages.get(KindOfWeapon.class, "which_equip_msg"),
						Messages.get(KindOfWeapon.class, "which_equip_primary", primaryName),
						Messages.get(KindOfWeapon.class, "which_equip_auxiliary", auxiliaryName),
						Messages.get(KindOfWeapon.class, "which_equip_secondary", secondaryName)
				) {
					@Override
					protected void onSelect(int index) {
						super.onSelect(index);
						if (index == 0 || index == 1 || index == 2) {
							// 除了装备自己，物品还将自己重新分配到快速插槽
							// 这是一个特殊的情况，因为物品正在从库存中移除，但仍与英雄在一起。
							int slot = Dungeon.quickslot.getSlot(KindOfWeapon.this);
							slotOfUnequipped = -1;
							if (index == 0 || index == 1) {
								doEquip(hero);
							} else {
								equipSecondary(hero);
							}
							if (slot != -1) {
								Dungeon.quickslot.setSlot(slot, KindOfWeapon.this);
								updateQuickslot();
								// 如果该物品没有快速插入，但它所更换的装备是
								// 然后让物品占据未装备物品的快速插槽
							} else if (slotOfUnequipped != -1 && defaultAction() != null) {
								Dungeon.quickslot.setSlot(slotOfUnequipped, KindOfWeapon.this);
								updateQuickslot();
							}
						}
					}
				});}

		 else {
			super.execute(hero, action);
		}

	}

	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.weapon() == this || hero.belongings.secondWep() == this|| hero.belongings.auxiliary() == this;
	}

	@Override
	public boolean doEquip(Hero hero) {
		// 记录英雄是否在装备前已经拥有该物品
		boolean wasInInv = hero.belongings.contains(this);
		detachAll( hero.belongings.backpack );
		boolean shouldEquip=false;

		String primaryName = Messages.titleCase(hero.belongings.weapon != null ? hero.belongings.weapon.trueName() : Messages.get(KindOfWeapon.class, "empty"));
		String auxiliaryName = Messages.titleCase(hero.belongings.auxiliary != null ? hero.belongings.auxiliary.trueName() : Messages.get(KindOfWeapon.class, "empty"));

		if ((this instanceof MeleeWeapon) && ((MeleeWeapon) this).twohands) {
			if (hero.belongings.weapon == null && hero.belongings.auxiliary == null)
				shouldEquip = true;

			else if (hero.belongings.weapon == null) {
				shouldEquip = hero.belongings.auxiliary.doUnequip(hero, true);
			} else if (hero.belongings.auxiliary == null) {
				shouldEquip = hero.belongings.weapon.doUnequip(hero, true);
			} else {
				shouldEquip = hero.belongings.weapon.doUnequip(hero, true) && hero.belongings.auxiliary.doUnequip(hero, true);
			}
			if (shouldEquip) {
				hero.belongings.weapon = this;
				hero.belongings.auxiliary = null;
				GLog.n(Messages.get(KindOfWeapon.class, "unequip_dd"));
			} else {
				this.equipFull = true;
				collect(hero.belongings.backpack);
				return false;
			}
		} else {
			if (hero.belongings.weapon == null ) {
				hero.belongings.weapon = this;

			} else if (hero.belongings.weapon instanceof MeleeWeapon && ((MeleeWeapon) hero.belongings.weapon).twohands
					&&hero.belongings.weapon.doUnequip(hero, true)) {
				hero.belongings.weapon = this;
				GLog.n(Messages.get(KindOfWeapon.class, "twohands_unequip"));

			}else if (hero.belongings.auxiliary == null&& ((MeleeWeapon) this).support) {
				hero.belongings.auxiliary = this;

			} else if (hero.belongings.weapon != null &&  hero.belongings.weapon.doUnequip(hero, true)) {
				hero.belongings.weapon = this;

			} else if (hero.belongings.auxiliary != null && hero.belongings.auxiliary.doUnequip(hero, true)) {
				hero.belongings.auxiliary = this;
			}

			else {
				collect(hero.belongings.backpack);
				return false;
			}
		}
		// 激活物品效果
		activate(hero);
		// 触发装备物品时的天赋效果
		Talent.onItemEquipped(hero, this);
		// 验证是否解锁了某些成就
		Badges.validateDuelistUnlock();
		// 刷新动作指示器
		ActionIndicator.refresh();
		// 更新快速使用的物品槽
		updateQuickslot();

		// 标记该物品是否有诅咒效果
		cursedKnown = true;

		// 如果该物品有诅咒效果，则进行相应处理
		if (cursed) {
			if (this == hero.belongings.weapon) {
				equipCursed(hero);
				GLog.n(Messages.get(KindOfWeapon.class, "equip_cursed"));
			} else if (this == hero.belongings.auxiliary) {
				equipCursed(hero);
				GLog.n(Messages.get(KindOfWeapon.class, "equip_cursed"));
			}
		}

		// 如果英雄具有快速装备天赋，则根据相应逻辑来处理快速装备的行为
		if (wasInInv && hero.hasTalent(Talent.SWIFT_EQUIP)) {
			if (hero.buff(Talent.SwiftEquipCooldown.class) == null) {
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
		// 返回true表示装备成功
		return true;
	}


	public boolean equipSecondary( Hero hero ){
		boolean wasInInv = hero.belongings.contains(this);
		detachAll( hero.belongings.backpack );

		if (hero.belongings.secondWep == null || hero.belongings.secondWep.doUnequip( hero, true )) {

			hero.belongings.secondWep = this;
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
		boolean first = hero.belongings.weapon == this;

		if (second){
			//do this first so that the item can go to a full inventory
			hero.belongings.secondWep = null;
		}

		if (super.doUnequip( hero, collect, single )) {

			if (first){
				hero.belongings.weapon = null;
			} else {
				// 如果是第二武器，则将辅助武器置为null
				hero.belongings.auxiliary = null;
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
