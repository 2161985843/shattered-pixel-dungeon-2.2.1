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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SacrificialFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AnkhInvulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Berserk;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HoldFast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.GhoulsClaw;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.pistol;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flail;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Sign;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.StatusPane;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHero;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndResurrect;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Hero extends Char {

	{
		actPriority = HERO_PRIO;
		
		alignment = Alignment.ALLY;
	}
	
	public static final int MAX_LEVEL = 30;

	public static final int STARTING_STR = 10;
	
	private static final float TIME_TO_REST		    = 1f;
	private static final float TIME_TO_SEARCH	    = 2f;
	private static final float HUNGER_FOR_SEARCH	= 6f;
	
	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;
	public ArmorAbility armorAbility = null;
	public ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();
	public LinkedHashMap<Talent, Talent> metamorphedTalents = new LinkedHashMap<>();
	
	private int attackSkill = 10;
	private int defenseSkill = 5;

	public boolean ready = false;
	public boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;
	
	public boolean resting = false;
	
	public Belongings belongings;
	
	public int STR;
	
	public float awareness;
	
	public int lvl = 1;
	public int exp = 0;
	
	public int HTBoost = 0;
	
	private ArrayList<Mob> visibleEnemies;

	//This list is maintained so that some logic checks can be skipped
	// for enemies we know we aren't seeing normally, resulting in better performance
	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();

	public Hero() {
		super();

		HP = HT = 20;
		STR = STARTING_STR;

		belongings = new Belongings( this );
		
		visibleEnemies = new ArrayList<>();
	}

	public void updateHT( boolean boostHP ){
		int curHT = HT;
		
		HT = 20 + 5*(lvl-1) + HTBoost;
		float multiplier = RingOfMight.HTMultiplier(this);
		HT = Math.round(multiplier * HT);
		
		if (buff(ElixirOfMight.HTBoost.class) != null){
			HT += buff(ElixirOfMight.HTBoost.class).boost();
		}
		if (buff(ElixirOfMight.HTBoost.class) != null){
			HT += buff(ElixirOfMight.HTBoost.class).boost();
		}
		if (boostHP){
			HP += Math.max(HT - curHT, 0);
		}
		HP = Math.min(HP, HT);
	}

	public int STR() {
		int strBonus = 0;

		strBonus += RingOfMight.strengthBonus( this );
		
		AdrenalineSurge buff = buff(AdrenalineSurge.class);
		if (buff != null){
			strBonus += buff.boost();
		}

		if (hasTalent(Talent.STRONGMAN)){
			strBonus += (int)Math.floor(STR * (0.03f + 0.05f*pointsInTalent(Talent.STRONGMAN)));
		}

		return STR + strBonus;
	}

	private static final String CLASS       = "class";
	private static final String SUBCLASS    = "subClass";
	private static final String ABILITY     = "armorAbility";

	private static final String ATTACK		= "attackSkill";
	private static final String DEFENSE		= "defenseSkill";
	private static final String STRENGTH	= "STR";
	private static final String LEVEL		= "lvl";
	private static final String EXPERIENCE	= "exp";
	private static final String HTBOOST     = "htboost";
	
	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );

		bundle.put( CLASS, heroClass );
		bundle.put( SUBCLASS, subClass );
		bundle.put( ABILITY, armorAbility );
		Talent.storeTalentsInBundle( bundle, this );
		
		bundle.put( ATTACK, attackSkill );
		bundle.put( DEFENSE, defenseSkill );
		
		bundle.put( STRENGTH, STR );
		
		bundle.put( LEVEL, lvl );
		bundle.put( EXPERIENCE, exp );
		
		bundle.put( HTBOOST, HTBoost );

		belongings.storeInBundle( bundle );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {

		lvl = bundle.getInt( LEVEL );
		exp = bundle.getInt( EXPERIENCE );

		HTBoost = bundle.getInt(HTBOOST);

		super.restoreFromBundle( bundle );

		heroClass = bundle.getEnum( CLASS, HeroClass.class );
		subClass = bundle.getEnum( SUBCLASS, HeroSubClass.class );
		armorAbility = (ArmorAbility)bundle.get( ABILITY );
		Talent.restoreTalentsFromBundle( bundle, this );
		
		attackSkill = bundle.getInt( ATTACK );
		defenseSkill = bundle.getInt( DEFENSE );
		
		STR = bundle.getInt( STRENGTH );

		belongings.restoreFromBundle( bundle );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.level = bundle.getInt( LEVEL );
		info.str = bundle.getInt( STRENGTH );
		info.exp = bundle.getInt( EXPERIENCE );
		info.hp = bundle.getInt( Char.TAG_HP );
		info.ht = bundle.getInt( Char.TAG_HT );
		info.shld = bundle.getInt( Char.TAG_SHLD );
		info.heroClass = bundle.getEnum( CLASS, HeroClass.class );
		info.subClass = bundle.getEnum( SUBCLASS, HeroSubClass.class );
		Belongings.preview( info, bundle );
	}

	public boolean hasTalent( Talent talent ){
		return pointsInTalent(talent) > 0;
	}

	public int pointsInTalent( Talent talent ){
		for (LinkedHashMap<Talent, Integer> tier : talents){
			for (Talent f : tier.keySet()){
				if (f == talent) return tier.get(f);
			}
		}
		return 0;
	}

	public void upgradeTalent( Talent talent ){
		for (LinkedHashMap<Talent, Integer> tier : talents){
			for (Talent f : tier.keySet()){
				if (f == talent) tier.put(talent, tier.get(talent)+1);
			}
		}
		Talent.onTalentUpgraded(this, talent);
	}

	public int talentPointsSpent(int tier){
		int total = 0;
		for (int i : talents.get(tier-1).values()){
			total += i;
		}
		return total;
	}

	public int talentPointsAvailable(int tier){
		if (lvl < (Talent.tierLevelThresholds[tier] - 1)
			|| (tier == 3 && subClass == HeroSubClass.NONE)
			|| (tier == 4 && armorAbility == null)) {
			return 0;
		} else if (lvl >= Talent.tierLevelThresholds[tier+1]){
			return Talent.tierLevelThresholds[tier+1] - Talent.tierLevelThresholds[tier] - talentPointsSpent(tier) + bonusTalentPoints(tier);
		} else {
			return 1 + lvl - Talent.tierLevelThresholds[tier] - talentPointsSpent(tier) + bonusTalentPoints(tier);
		}
	}

	public int bonusTalentPoints(int tier){
		if (lvl < (Talent.tierLevelThresholds[tier]-1)
				|| (tier == 3 && subClass == HeroSubClass.NONE)
				|| (tier == 4 && armorAbility == null)) {
			return 0;
		} else if (buff(PotionOfDivineInspiration.DivineInspirationTracker.class) != null
					&& buff(PotionOfDivineInspiration.DivineInspirationTracker.class).isBoosted(tier)) {
			return 2;
		} else {
			return 0;
		}
	}
	
	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

	@Override
	public String name(){
		return className();
	}

	@Override
	public void hitSound(float pitch) {
		if (!RingOfForce.fightingUnarmed(this)) {
			// 如果角色没有使用徒手战斗
			belongings.attackingWeapon().hitSound(pitch);
		} else if (RingOfForce.getBuffedBonus(this, RingOfForce.Force.class) > 0) {
			// 如果角色处于被强化状态（Force class增益）
			// 根据角色的力量值调整声音的音调，逐渐降低音调直到75%
			super.hitSound( pitch * GameMath.gate( 0.75f, 1.25f - 0.025f*STR(), 1f) );
		} else {
			// 其他情况下，音调乘以1.1
			super.hitSound(pitch * 1.1f);
		}
	}

	@Override
	//处理角色进行格挡时的声音效果
	public boolean blockSound(float pitch) {
		if (belongings.weapon() != null && belongings.weapon().defenseFactor(this) >= 4) {
			// 如果角色装备了武器且武器的防御系数大于等于4
			// 播放格挡声音，并返回true表示成功格挡
			Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, pitch);
			return true;
		}
		// 如果未满足上述条件，则调用父类的blockSound方法处理格挡声音效果
		return super.blockSound(pitch);
	}

	public void live() {
		for (Buff b : buffs()){
			if (!b.revivePersists) b.detach();
		}
		Buff.affect( this, Regeneration.class );
		Buff.affect( this, Hunger.class );
	}
	
	public int tier() {
		Armor armor = belongings.armor();
		if (armor instanceof ClassArmor){
			return 6;
		} else if (armor != null){
			return armor.tier;
		} else {
			return 0;
		}
	}
	//于处理角色进行射击（使用导弹武器）的行为
	public boolean shoot(Char enemy, MissileWeapon wep) {
		this.enemy = enemy;
		boolean wasEnemy = enemy.alignment == Alignment.ENEMY
				|| (enemy instanceof Mimic && enemy.alignment == Alignment.NEUTRAL);

		// 暂时将英雄的武器设置为使用的导弹武器
		belongings.thrownWeapon = wep;
		belongings.abilityWeapon =wep;

		// 进行攻击行为，判断是否命中目标
		boolean hit = attack(enemy);

		// 解除隐身效果
		Invisibility.dispel();

		// 恢复英雄之前的武器
		belongings.thrownWeapon = null;
		belongings.abilityWeapon = null;
		// 如果命中目标且英雄是角斗士子类且目标为敌人
		// 触发角斗士子类特殊效果
		if (hit && subClass == HeroSubClass.GLADIATOR && wasEnemy) {
			Buff.affect(this, Combo.class).hit(enemy);
		}

		// 如果命中目标且英雄是决斗者职业且目标为敌人
		// 触发决斗者职业特殊效果
		if (hit && heroClass == HeroClass.DUELIST && wasEnemy) {
			Buff.affect(this, Sai.ComboStrikeTracker.class).addHit();
		}

		return hit;
	}
	
	@Override
	public int attackSkill(Char target) {
		// 获取角色当前使用的武器
		KindOfWeapon wep = belongings.attackingWeapon();

		// 初始化准确性为1
		float accuracy = 1;

		// 根据戒指的准确性增加倍数调整准确性
		accuracy *= RingOfAccuracy.accuracyMultiplier(this);

		// 如果是使用导弹武器
		if (wep instanceof MissileWeapon) {
			// 根据与目标的距离不同调整准确性
			if (Dungeon.level.adjacent(pos, target.pos)) {
				accuracy *= (0.5f + 0.2f * pointsInTalent(Talent.POINT_BLANK));
			} else {
				accuracy *= 1.5f;
			}
		}

		// 如果角色拥有Scimitar.SwordDance类的增益效果
		if (buff(Scimitar.SwordDance.class) != null) {
			accuracy *= 1.25f;
		}

		// 如果不是徒手战斗
		if (!RingOfForce.fightingUnarmed(this)) {
			// 返回攻击技能乘以准确性和武器的准确性因子
			return (int) (attackSkill * accuracy * wep.accuracyFactor(this, target));
		} else {
			// 否则只返回攻击技能乘以准确性
			return (int) (attackSkill * accuracy);
		}
	}

/**
 * 计算角色对敌人进行防御时的防御技能值
 * @param enemy 敌人角色
 * @return 返回计算后的防御技能值
 */
	@Override
	public int defenseSkill(Char enemy) {
		// 如果具有Combo.ParryTracker类的增益效果
		if (buff(Combo.ParryTracker.class) != null){
			// 如果可以攻击敌人且没有被敌人迷惑
			if (canAttack(enemy) && !isCharmedBy(enemy)){
				// 影响角色自己和敌人的Combo.RiposteTracker类的增益效果
				Buff.affect(this, Combo.RiposteTracker.class).enemy = enemy;
			}
			// 返回无限闪避值
			return INFINITE_EVASION;
		}

		// 如果具有RoundShield.GuardTracker类的增益效果
		if (buff(RoundShield.GuardTracker.class) != null){
			// 返回无限闪避值
			return INFINITE_EVASION;
		}

		// 初始化闪避值为角色的防御技能
		float evasion = defenseSkill;

		// 根据戒指的闪避增加倍数调整闪避值
		evasion *= RingOfEvasion.evasionMultiplier(this);

		// 如果具有Talent.RestoredAgilityTracker类的增益效果
		if (buff(Talent.RestoredAgilityTracker.class) != null){
			// 如果天赋“LIQUID_AGILITY”的技能点数为1
			if (pointsInTalent(Talent.LIQUID_AGILITY) == 1){
				evasion *= 4f;
			} else if (pointsInTalent(Talent.LIQUID_AGILITY) == 2){ // 如果天赋“LIQUID_AGILITY”的技能点数为2
				return INFINITE_EVASION; // 返回无限闪避值
			}
		}

		// 如果具有Quarterstaff.DefensiveStance类的增益效果
		if (buff(Quarterstaff.DefensiveStance.class) != null){
			evasion *= 3; // 闪避值乘以3
		}

		// 如果处于麻痹状态
		if (paralysed > 0) {
			evasion /= 2; // 闪避值除以2
		}

		// 如果有穿戴护甲
		if (belongings.armor() != null) {
			// 根据护甲的闪避因子调整闪避值
			evasion = belongings.armor().evasionFactor(this, evasion);
		}

		// 返回四舍五入后的闪避值
		return Math.round(evasion);
	}
	@Override
	public String defenseVerb() {
		// 检查是否进行了招架操作
		Combo.ParryTracker parry = buff(Combo.ParryTracker.class);
		if (parry != null) {
			parry.parried = true;
			// 如果连击次数小于9或者未达到强化连击技能的要求，则移除招架效果
			if (buff(Combo.class).getComboCount() < 9 || pointsInTalent(Talent.ENHANCED_COMBO) < 2) {
				parry.detach();
			}
			return Messages.get(Monk.class, "parried"); // 返回招架消息字符串
		}

		// 检查是否进行了护盾保护操作
		if (buff(RoundShield.GuardTracker.class) != null) {
			buff(RoundShield.GuardTracker.class).detach();
			Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			return Messages.get(RoundShield.GuardTracker.class, "guarded"); // 返回护盾保护消息字符串
		}

		// 检查是否进行了专注能力激活操作
		if (buff(MonkEnergy.MonkAbility.Focus.FocusActivation.class) != null) {
			buff(MonkEnergy.MonkAbility.Focus.FocusActivation.class).detach();
			// 如果sprite对象存在且可见，则播放音效
			if (sprite != null && sprite.visible) {
				Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			}
			return Messages.get(Monk.class, "parried"); // 返回招架消息字符串
		}

		// 如果以上条件都不满足，则调用父类的方法获取默认的防御动作描述
		return super.defenseVerb();
	}

	@Override
	//计算角色的伤害减免值（Damage Reduction）
	public int drRoll() {
		// 调用父类的drRoll方法，获取基础的护甲减伤值
		int dr = super.drRoll();

		// 如果有穿戴护甲
		if (belongings.armor() != null) {
			// 根据护甲的DRMin和DRMax属性随机生成一个护甲减伤值
			int armDr = Random.NormalIntRange(belongings.armor().DRMin(), belongings.armor().DRMax());
			// 如果角色的力量值小于护甲的力量要求
			if (STR() < belongings.armor().STRReq()){
				// 根据力量差值减少护甲减伤值
				armDr -= 2 * (belongings.armor().STRReq() - STR());
			}
			// 如果护甲减伤值大于0，则增加到总的护甲减伤值中
			if (armDr > 0) dr += armDr;
		}

		// 如果有装备武器且非空手战斗
		if (belongings.weapon() != null && !RingOfForce.fightingUnarmed(this))  {
			// 根据武器的defenseFactor属性随机生成一个武器减伤值
			int wepDr = Random.NormalIntRange(0, belongings.weapon().defenseFactor(this));
			// 如果角色的力量值小于武器的力量要求
			if (STR() < ((Weapon)belongings.weapon()).STRReq()){
				// 根据力量差值减少武器减伤值
				wepDr -= 2 * (((Weapon)belongings.weapon()).STRReq() - STR());
			}
			// 如果武器减伤值大于0，则增加到总的护甲减伤值中
			if (wepDr > 0) dr += wepDr;
		}

		// 如果具有HoldFast类的增益效果
		if (buff(HoldFast.class) != null){
			// 增加HoldFast类增益效果提供的额外护甲减伤值
			dr += buff(HoldFast.class).armorBonus();
		}

		// 返回最终的护甲减伤值
		return dr;
	}


/**
 * 计算角色造成的伤害值
 * @return 返回计算后的伤害值
 */
	@Override
	public int damageRoll() {
		KindOfWeapon wep = belongings.attackingWeapon();
		KindOfWeapon axy = belongings.attackingauxiliary();
		int dmg=0;

		// 如果非空手战斗
		if (!RingOfForce.fightingUnarmed(this)) {
			// 根据当前装备的武器进行伤害计算
			if (wep != null && belongings.auxiliary!=null &&!(belongings.auxiliary instanceof pistol) ) {

				dmg = wep.damageRoll(this) +axy.damageRoll(this );

			} else if (wep != null) {
				dmg = wep.damageRoll(this);
			} else if (axy != null) {
				dmg = axy.damageRoll(this);
			}
			if (buff(GhoulsClaw.class) != null) {
				dmg += GhoulsClaw.getTotalStrengthBonus();
			}

			// 如果不是远程武器，则增加武器伤害加成
			if (!(wep instanceof MissileWeapon)) dmg += RingOfForce.armedDamageBonus(this);

		} else {
			// 如果是空手战斗
			// 根据空手战斗造成的伤害计算
			dmg = RingOfForce.damageRoll(this);
			if (buff(GhoulsClaw.class) != null) {
				dmg += GhoulsClaw.getTotalStrengthBonus()*2;
			}
			// 如果空手战斗可以获得武器增益效果
			if (RingOfForce.unarmedGetsWeaponAugment(this)){
				dmg = ((Weapon)belongings.attackingWeapon()).augment.damageFactor(dmg);
			}
		}

		// 获取PhysicalEmpower增益效果
		PhysicalEmpower emp = buff(PhysicalEmpower.class);
		if (emp != null){
			// 增加伤害值
			dmg += emp.dmgBoost;
			emp.left--;
			// 如果增益效果次数用尽，则移除该增益效果
			if (emp.left <= 0) {
				emp.detach();
			}
			// 播放音效
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 0.75f, 1.2f);
		}

		// 如果角色不是Duelist类、拥有WEAPON_RECHARGING天赋且具有重装或神器充能增益效果
		if (heroClass != HeroClass.DUELIST
				&& hasTalent(Talent.WEAPON_RECHARGING)
				&& (buff(Recharging.class) != null || buff(ArtifactRecharge.class) != null)){
			// 根据天赋等级增加伤害值
			dmg = Math.round(dmg * 1.025f + (.025f * pointsInTalent(Talent.WEAPON_RECHARGING)));
		}

		// 如果伤害值小于0，则将伤害值设为0
		if (dmg < 0) dmg = 0;

		// 返回最终的伤害值
		return dmg;
	}
	/**
	 * 计算角色的移动速度
	 * @return 返回计算后的移动速度
	 */
	@Override
	public float speed() {
		float speed = super.speed(); // 获取基础移动速度

		// 根据RingOfHaste增益效果计算移动速度
		speed *= RingOfHaste.speedMultiplier(this);

		// 如果角色穿戴了护甲
		if (belongings.armor() != null) {
			speed = belongings.armor().speedFactor(this, speed); // 根据护甲的速度因子调整移动速度
		}

		// 获取Momentum增益效果
		Momentum momentum = buff(Momentum.class);
		if (momentum != null){
			// 根据Momentum增益效果调整移动速度
			((HeroSprite)sprite).sprint( momentum.freerunning() ? 1.5f : 1f );
			speed *= momentum.speedMultiplier();
		} else {
			((HeroSprite)sprite).sprint( 1f );
		}

		// 获取NaturesPower增益效果
		NaturesPower.naturesPowerTracker natStrength = buff(NaturesPower.naturesPowerTracker.class);
		if (natStrength != null){
			// 根据NaturesPower增益效果调整移动速度
			speed *= (2f + 0.25f*pointsInTalent(Talent.GROWING_POWER));
		}

		// 根据AscensionChallenge修改英雄移动速度
		speed = AscensionChallenge.modifyHeroSpeed(speed);

		// 返回最终的移动速度
		return speed;
	}
	@Override//用于判断角色是否能够进行意外袭击
	public boolean canSurpriseAttack(){
		KindOfWeapon w = belongings.attackingWeapon();
		if (!(w instanceof Weapon))             return true;
		if (RingOfForce.fightingUnarmed(this))  return true;
		if (STR() < ((Weapon)w).STRReq())       return false;
		if (w instanceof Flail)                 return false;

		return super.canSurpriseAttack();
	}
	//用于判断角色是否能够攻击指定的敌人
	public boolean canAttack(Char enemy){
		if (enemy == null || pos == enemy.pos || !Actor.chars().contains(enemy)) {
			return false;
		}

		//can always attack adjacent enemies
		if (Dungeon.level.adjacent(pos, enemy.pos)) {
			return true;
		}

		KindOfWeapon wep = hero.belongings.attackingWeapon();

		if (wep != null){
			return wep.canReach(this, enemy.pos);
		} else {
			return false;
		}
	}
	//计算角色的攻击延迟时间
	public float attackDelay() { // 如果角色拥有 LethalMomentumTracker 天赋效果，则移除该效果并返回 0
		if (buff(Talent.LethalMomentumTracker.class) != null){
			buff(Talent.LethalMomentumTracker.class).detach();
			return 0;
		}

		float delay = 1f;
		// 如果角色不是徒手战斗
		if (!RingOfForce.fightingUnarmed(this)) {
			// 返回武器攻击延迟乘以武器的延迟因子if
			return delay * belongings.attackingWeapon().delayFactor( this );
			
		} else {
			//通常，对非武装攻击施加愤怒的速度是不必要的
			//但是会有一个人得到一个愤怒+力量戒指组合
			//这是给那个家伙的，你会得到愤怒的拳头！
			float speed = RingOfFuror.attackSpeedMultiplier(this);

			//同上 Furor + 剑舞！
			if (buff(Scimitar.SwordDance.class) != null){
				speed += 0.6f;
			}

			//并增强 + Brawler 的姿态！我的天哪，与 2014 年相比，现在有这么多选择！
			if (RingOfForce.unarmedGetsWeaponAugment(this)){
				delay = ((Weapon)belongings.weapon).augment.delayFactor(delay);
			}

			return delay/speed;
		}
	}

	@Override
	public void spend( float time ) {
		super.spend(time);
	}

	@Override
	public void spendConstant(float time) {
		justMoved = false;
		super.spendConstant(time);
	}

	public void spendAndNextConstant(float time ) {
		busy();
		spendConstant( time );
		next();
	}

	public void spendAndNext( float time ) {
		busy();
		spend( time );
		next();
	}

	@Override
	public boolean act() {

		//calls to dungeon.observe will also update hero's local FOV.
		fieldOfView = Dungeon.level.heroFOV;

		if (buff(Endure.EndureTracker.class) != null){
			buff(Endure.EndureTracker.class).endEnduring();
		}

		if (!ready) {
			//do a full observe (including fog update) if not resting.
			if (!resting || buff(MindVision.class) != null || buff(Awareness.class) != null) {
				Dungeon.observe();
			} else {
				//otherwise just directly re-calculate FOV
				Dungeon.level.updateFieldOfView(this, fieldOfView);
			}
		}

		checkVisibleMobs();
		BuffIndicator.refreshHero();
		BuffIndicator.refreshBoss();

		if (paralysed > 0) {

			curAction = null;

			spendAndNext( TICK );
			return false;
		}

		boolean actResult;
		if (curAction == null) {

			if (resting) {
				spendConstant( TIME_TO_REST );
				next();
			} else {
				ready();
			}

			actResult = false;

		} else {

			resting = false;

			ready = false;

			if (curAction instanceof HeroAction.Move) {
				actResult = actMove( (HeroAction.Move)curAction );

			} else if (curAction instanceof HeroAction.Interact) {
				actResult = actInteract( (HeroAction.Interact)curAction );

			} else if (curAction instanceof HeroAction.Buy) {
				actResult = actBuy( (HeroAction.Buy)curAction );

			}else if (curAction instanceof HeroAction.PickUp) {
				actResult = actPickUp( (HeroAction.PickUp)curAction );

			} else if (curAction instanceof HeroAction.OpenChest) {
				actResult = actOpenChest( (HeroAction.OpenChest)curAction );

			} else if (curAction instanceof HeroAction.Unlock) {
				actResult = actUnlock((HeroAction.Unlock) curAction);

			} else if (curAction instanceof HeroAction.Mine) {
				actResult = actMine( (HeroAction.Mine)curAction );

			}else if (curAction instanceof HeroAction.LvlTransition) {
				actResult = actTransition( (HeroAction.LvlTransition)curAction );

			} else if (curAction instanceof HeroAction.Attack) {
				actResult = actAttack( (HeroAction.Attack)curAction );

			} else if (curAction instanceof HeroAction.Alchemy) {
				actResult = actAlchemy( (HeroAction.Alchemy)curAction );

			} else {
				actResult = false;
			}
		}

		if(hasTalent(Talent.BARKSKIN) && Dungeon.level.map[pos] == Terrain.FURROWED_GRASS){
			Barkskin.conditionallyAppend(this, (lvl*pointsInTalent(Talent.BARKSKIN))/2, 1 );
		}

		return actResult;
	}
	
	public void busy() {
		ready = false;
	}
	
	private void ready() {
		if (sprite.looping()) sprite.idle();
		curAction = null;
		damageInterrupt = true;
		waitOrPickup = false;
		ready = true;
		canSelfTrample = true;

		AttackIndicator.updateState();
		
		GameScene.ready();
	}
	
	public void interrupt() {
		if (isAlive() && curAction != null &&
			((curAction instanceof HeroAction.Move && curAction.dst != pos) ||
			(curAction instanceof HeroAction.LvlTransition))) {
			lastAction = curAction;
		}
		curAction = null;
		GameScene.resetKeyHold();
	}
	
	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		next();
	}

	private boolean canSelfTrample = false;
	public boolean canSelfTrample(){
		return canSelfTrample && !rooted && !flying &&
				//standing in high grass
				(Dungeon.level.map[pos] == Terrain.HIGH_GRASS ||
				//standing in furrowed grass and not huntress
				(heroClass != HeroClass.HUNTRESS && Dungeon.level.map[pos] == Terrain.FURROWED_GRASS) ||
				//standing on a plant
				Dungeon.level.plants.get(pos) != null);
	}
	
	private boolean actMove( HeroAction.Move action ) {

		if (getCloser( action.dst )) {
			canSelfTrample = false;
			return true;

		//Hero moves in place if there is grass to trample
		} else if (pos == action.dst && canSelfTrample()){
			canSelfTrample = false;
			Dungeon.level.pressCell(pos);
			spendAndNext( 1 / speed() );
			return false;
		} else {
			ready();
			return false;
		}
	}
	
	private boolean actInteract( HeroAction.Interact action ) {
		
		Char ch = action.ch;

		if (ch.isAlive() && ch.canInteract(this)) {
			
			ready();
			sprite.turnTo( pos, ch.pos );
			return ch.interact(this);
			
		} else {
			
			if (fieldOfView[ch.pos] && getCloser( ch.pos )) {

				return true;

			} else {
				ready();
				return false;
			}
			
		}
	}
	
	private boolean actBuy( HeroAction.Buy action ) {//了购买物品的动作逻辑
		int dst = action.dst;
		if (pos == dst) {

			ready();
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndTradeItem( heap ) );
					}
				});
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAlchemy( HeroAction.Alchemy action ) {
		int dst = action.dst;
		if (Dungeon.level.distance(dst, pos) <= 1) {

			ready();
			
			AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
			if (kit != null && kit.isCursed()){
				GLog.w( Messages.get(AlchemistsToolkit.class, "cursed"));
				return false;
			}

			AlchemyScene.clearToolkit();
			ShatteredPixelDungeon.switchScene(AlchemyScene.class);
			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	//used to keep track if the wait/pickup action was used
	// so that the hero spends a turn even if the fail to pick up an item
	public boolean waitOrPickup = false;
	private boolean actPickUp( HeroAction.PickUp action ) {//角色在游戏中拾取物品的逻辑处理
		int dst = action.dst;
		if (pos == dst) {

			Heap heap = Dungeon.level.heaps.get( pos );
			if (heap != null) {
				Item item = heap.peek();
				if (item.doPickUp( this )) {
					heap.pickUp();

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key
							|| item instanceof Guidebook) {
						//Do Nothing
					} else {

						//TODO make all unique items important? or just POS / SOU?
						boolean important = item.unique && item.isIdentified() &&
								(item instanceof Scroll || item instanceof Potion);
						if (important) {
							GLog.p( Messages.capitalize(Messages.get(this, "you_now_have", item.name())) );
						} else {
							GLog.i( Messages.capitalize(Messages.get(this, "you_now_have", item.name())) );
						}
					}

					curAction = null;
				} else {

					if (waitOrPickup) {
						spendAndNextConstant(TIME_TO_REST);
					}

					//allow the hero to move between levels even if they can't collect the item
					if (Dungeon.level.getTransition(pos) != null){
						throwItems();
					} else {
						heap.sprite.drop();
					}

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key) {
						//Do Nothing
					} else {
						GLog.newLine();
						GLog.n(Messages.capitalize(Messages.get(this, "you_cant_have", item.name())));
					}

					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

//个处理英雄在游戏中打开宝箱的方法
	private boolean actOpenChest(HeroAction.OpenChest action) {
		int dst = action.dst; // 获取动作的目标位置

		// 检查英雄是否在目标位置附近或者就在目标位置上
		if (Dungeon.level.adjacent(pos, dst) || pos == dst) {
			path = null; // 清空路径，表示不需要移动

			Heap heap = Dungeon.level.heaps.get(dst); // 获取目标位置的堆对象
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) { // 如果堆对象存在且类型不是堆或出售类型

				// 检查是否需要钥匙才能打开宝箱
				if ((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.depth)) < 1)
						|| (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.depth)) < 1)) {

					GLog.w(Messages.get(this, "locked_chest")); // 输出警告信息
					ready(); // 完成动作，进入就绪状态
					return false; // 返回 false，表示动作未完成
				}

				// 根据堆对象的类型执行不同的操作
				switch (heap.type) {
					case TOMB:
						Sample.INSTANCE.play(Assets.Sounds.TOMB); // 播放墓地音效
						PixelScene.shake(1, 0.5f); // 屏幕抖动
						break;
					case SKELETON:
					case REMAINS:
						break; // 对于骷髅和遗骸类型，不执行任何操作
					default:
						Sample.INSTANCE.play(Assets.Sounds.UNLOCK); // 播放解锁音效
				}

				sprite.operate(dst); // 对堆对象执行操作（例如打开宝箱）

			} else {
				ready(); // 完成动作，进入就绪状态
			}

			return false; // 返回 false，表示动作未完成

		} else if (getCloser(dst)) { // 如果英雄不在目标位置附近，则尝试移动到目标位置附近

			return true; // 返回 true，表示需要继续移动以接近目标位置

		} else {
			ready(); // 完成动作，进入就绪状态
			return false; // 返回 false，表示动作未完成
		}
	}
	private boolean actUnlock( HeroAction.Unlock action ) {
		//处理解锁动作的逻辑。根据目标位置是否与当前位置相邻，以及角色是否拥有相应的钥匙，来执行解锁操作
		int doorCell = action.dst;
		if (Dungeon.level.adjacent( pos, doorCell )) {
			path = null;
			
			boolean hasKey = false;
			int door = Dungeon.level.map[doorCell];
			
			if (door == Terrain.LOCKED_DOOR
					&& Notes.keyCount(new IronKey(Dungeon.depth)) > 0) {
				
				hasKey = true;
				
			} else if (door == Terrain.CRYSTAL_DOOR
					&& Notes.keyCount(new CrystalKey(Dungeon.depth)) > 0) {

				hasKey = true;

			} else if (door == Terrain.LOCKED_EXIT
					&& Notes.keyCount(new SkeletonKey(Dungeon.depth)) > 0) {

				hasKey = true;
				
			}
			
			if (hasKey) {
				
				sprite.operate( doorCell );
				
				Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
				
			} else {
				GLog.w( Messages.get(this, "locked_door") );
				ready();
			}

			return false;

		} else if (getCloser( doorCell )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	public boolean actMine(HeroAction.Mine action){
		if (Dungeon.level.adjacent(pos, action.dst)){
			path = null;
			if ((Dungeon.level.map[action.dst] == Terrain.WALL
					|| Dungeon.level.map[action.dst] == Terrain.WALL_DECO
					|| Dungeon.level.map[action.dst] == Terrain.MINE_CRYSTAL
					|| Dungeon.level.map[action.dst] == Terrain.MINE_BOULDER)
				&& Dungeon.level.insideMap(action.dst)){
				sprite.attack(action.dst, new Callback() {
					@Override
					public void call() {

						boolean crystalAdjacent = false;
						for (int i : PathFinder.NEIGHBOURS8) {
							if (Dungeon.level.map[action.dst + i] == Terrain.MINE_CRYSTAL){
								crystalAdjacent = true;
								break;
							}
						}

						//1 hunger spent total
						if (Dungeon.level.map[action.dst] == Terrain.WALL_DECO){
							DarkGold gold = new DarkGold();
							if (gold.doPickUp( hero )) {
								DarkGold existing = hero.belongings.getItem(DarkGold.class);
								if (existing != null && existing.quantity()%5 == 0){
									if (existing.quantity() >= 40) {
										GLog.p(Messages.get(DarkGold.class, "you_now_have", existing.quantity()));
									} else {
										GLog.i(Messages.get(DarkGold.class, "you_now_have", existing.quantity()));
									}
								}
								spend(-Actor.TICK); //picking up the gold doesn't spend a turn here
							} else {
								Dungeon.level.drop( gold, pos ).sprite.drop();
							}
							PixelScene.shake(0.5f, 0.5f);
							CellEmitter.center( action.dst ).burst( Speck.factory( Speck.STAR ), 7 );
							Sample.INSTANCE.play( Assets.Sounds.EVOKE );
							Level.set( action.dst, Terrain.EMPTY_DECO );

							//mining gold doesn't break crystals
							crystalAdjacent = false;

						//4 hunger spent total
						} else if (Dungeon.level.map[action.dst] == Terrain.WALL){
							buff(Hunger.class).affectHunger(-3);
							PixelScene.shake(0.5f, 0.5f);
							CellEmitter.get( action.dst ).burst( Speck.factory( Speck.ROCK ), 2 );
							Sample.INSTANCE.play( Assets.Sounds.MINE );
							Level.set( action.dst, Terrain.EMPTY_DECO );

						//1 hunger spent total
						} else if (Dungeon.level.map[action.dst] == Terrain.MINE_CRYSTAL){
							Splash.at(action.dst, 0xFFFFFF, 5);
							Sample.INSTANCE.play( Assets.Sounds.SHATTER );
							Level.set( action.dst, Terrain.EMPTY );

						//1 hunger spent total
						} else if (Dungeon.level.map[action.dst] == Terrain.MINE_BOULDER){
							Splash.at(action.dst, ColorMath.random( 0x444444, 0x777766 ), 5);
							Sample.INSTANCE.play( Assets.Sounds.MINE, 0.6f );
							Level.set( action.dst, Terrain.EMPTY );
						}

						for (int i : PathFinder.NEIGHBOURS9) {
							Dungeon.level.discoverable[action.dst + i] = true;
						}
						for (int i : PathFinder.NEIGHBOURS9) {
							GameScene.updateMap( action.dst+i );
						}

						if (crystalAdjacent){
							sprite.parent.add(new Delayer(0.2f){
								@Override
								protected void onComplete() {
									boolean broke = false;
									for (int i : PathFinder.NEIGHBOURS8) {
										if (Dungeon.level.map[action.dst+i] == Terrain.MINE_CRYSTAL){
											Splash.at(action.dst+i, 0xFFFFFF, 5);
											Level.set( action.dst+i, Terrain.EMPTY );
											broke = true;
										}
									}
									if (broke){
										Sample.INSTANCE.play( Assets.Sounds.SHATTER );
									}

									for (int i : PathFinder.NEIGHBOURS9) {
										GameScene.updateMap( action.dst+i );
									}
									spendAndNext(TICK);
									ready();
								}
							});
						} else {
							spendAndNext(TICK);
							ready();
						}

						Dungeon.observe();
					}
				});
			} else {
				ready();
			}
			return false;
		} else if (getCloser( action.dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actTransition(HeroAction.LvlTransition action ) {
		int stairs = action.dst;
		LevelTransition transition = Dungeon.level.getTransition(stairs);
		if (Dungeon.level.map[action.dst] == Terrain.SIGN){
			Sign.readPit();
		}

		if (rooted) {
			PixelScene.shake(1, 1f);
			ready();
			return false;

		} else if (!Dungeon.level.locked && transition != null && transition.inside(pos)) {

			if (Dungeon.level.activateTransition(this, transition)){
				curAction = null;
			} else {
				ready();
			}

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	// 角色进行攻击的方法
	private boolean actAttack( HeroAction.Attack action ) {

		enemy = action.target;

		if (enemy.isAlive() && canAttack( enemy ) && !isCharmedBy( enemy ) && enemy.invisible == 0&& enemy.nothingness ) {

			if (heroClass != HeroClass.DUELIST
					&& hasTalent(Talent.AGGRESSIVE_BARRIER)
					&& buff(Talent.AggressiveBarrierCooldown.class) == null
					&& (HP / (float)HT) < 0.20f*(1+pointsInTalent(Talent.AGGRESSIVE_BARRIER))){
				Buff.affect(this, Barrier.class).setShield(3);
				Buff.affect(this, Talent.AggressiveBarrierCooldown.class, 50f);
			}
			sprite.attack( enemy.pos );

			return false;

		} else {

			if (fieldOfView[enemy.pos] && getCloser( enemy.pos )) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	public Char enemy(){
		return enemy;
	}
	// 角色进行休息的方法
	public void rest( boolean fullRest ) {
		spendAndNextConstant( TIME_TO_REST );
		if (hasTalent(Talent.HOLD_FAST)){
			Buff.affect(this, HoldFast.class).pos = pos;
		}
		if (hasTalent(Talent.PATIENT_STRIKE)){
			Buff.affect(hero, Talent.PatientStrikeTracker.class).pos = hero.pos;
		}
		if (!fullRest) {
			if (sprite != null) {
				sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
			}
		}
		resting = fullRest;
	}
	/**
	 * 计算角色造成的伤害值
	 * @param enemy 敌人角色对象
	 * @param damage 攻击伤害值+附魔效果
	 * @return 返回计算后的伤害值
	 */
	@Override
	public int attackProc( final Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );

		KindOfWeapon wep;
		KindOfWeapon aux;

		if (RingOfForce.fightingUnarmed(this) && !RingOfForce.unarmedGetsWeaponEnchantment(this)){
			wep = null;
			aux = null;
		} else {
			wep = belongings.attackingWeapon();
			aux = belongings.attackingauxiliary();
		}

		if (wep != null && aux != null && belongings.auxiliary!=null&&!(belongings.auxiliary instanceof pistol)) {
			damage = wep.proc(this, enemy, damage) + aux.proc(this, enemy, 0)+aux.damageRoll(enemy );
		} else if (aux != null) {
			damage = aux.proc(this, enemy, damage);
		} else if (wep != null) {
			damage = wep.proc(this, enemy, damage);
		}

		switch (subClass) {
		case SNIPER:
			if (wep instanceof MissileWeapon && !(wep instanceof SpiritBow.SpiritArrow) && enemy != this) {
				Actor.add(new Actor() {
					
					{
						actPriority = VFX_PRIO;
					}
					
					@Override
					protected boolean act() {
						if (enemy.isAlive()) {
							int bonusTurns = hasTalent(Talent.SHARED_UPGRADES) ? wep.buffedLvl() : 0;
							Buff.prolong(Hero.this, SnipersMark.class, SnipersMark.DURATION + bonusTurns).set(enemy.id(), bonusTurns);
						}
						Actor.remove(this);
						return true;
					}
				});
			}
			break;
		default:
		}
		
		return damage;
	}
	/**
	 * 计算角色防御过程中承受的伤害值
	 * @param enemy 敌人角色对象
	 * @param damage 攻击造成的伤害值
	 * @return 返回计算后的实际承受伤害值
	 */
	@Override
	public int defenseProc( Char enemy, int damage ) {
		
		if (damage > 0 && subClass == HeroSubClass.BERSERKER){
			Berserk berserk = Buff.affect(this, Berserk.class);
			berserk.damage(damage);
		}

		if (belongings.armor() != null) {
			damage = belongings.armor().proc( enemy, this, damage );
		}

		WandOfLivingEarth.RockArmor rockArmor = buff(WandOfLivingEarth.RockArmor.class);
		if (rockArmor != null) {
			damage = rockArmor.absorb(damage);
		}
		
		return super.defenseProc( enemy, damage );
	}
	
	@Override//伤害处理方法
	public void damage( int dmg, Object src ) {
		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		//regular damage interrupt, triggers on any damage except specific mild DOT effects
		// unless the player recently hit 'continue moving', in which case this is ignored
		if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
			interrupt();
			resting = false;
		}

		if (this.buff(Drowsy.class) != null){
			Buff.detach(this, Drowsy.class);
			GLog.w( Messages.get(this, "pain_resist") );
		}

		Endure.EndureTracker endure = buff(Endure.EndureTracker.class);
		if (!(src instanceof Char)){
			//reduce damage here if it isn't coming from a character (if it is we already reduced it)
			if (endure != null){
				dmg = Math.round(endure.adjustDamageTaken(dmg));
			}
			//the same also applies to challenge scroll damage reduction   // 挑战卷轴的伤害减免
			if (buff(ScrollOfChallenge.ChallengeArena.class) != null){
				dmg *= 0.67f;
			}
			//and to monk meditate damage reduction// Monk冥想的伤害减免
			if (buff(MonkEnergy.MonkAbility.Meditate.MeditateResistance.class) != null){
				dmg *= 0.2f;
			}
		}
		// 检查CapeOfThorns.Thorns状态，并处理荆棘效果
		CapeOfThorns.Thorns thorns = buff( CapeOfThorns.Thorns.class );
		if (thorns != null) {
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char)src : null),  this);
		}

		dmg = (int)Math.ceil(dmg * RingOfTenacity.damageMultiplier( this ));

		//TODO improve this when I have proper damage source logic
		if (belongings.armor() != null && belongings.armor().hasGlyph(AntiMagic.class, this)
				&& AntiMagic.RESISTS.contains(src.getClass())){
			dmg -= AntiMagic.drRoll(this, belongings.armor().buffedLvl());
		}

		if (buff(Talent.WarriorFoodImmunity.class) != null){
			if (pointsInTalent(Talent.IRON_STOMACH) == 1)       dmg = Math.round(dmg*0.25f);
			else if (pointsInTalent(Talent.IRON_STOMACH) == 2)  dmg = Math.round(dmg*0.00f);
		}

		int preHP = HP + shielding();
		if (src instanceof Hunger) preHP -= shielding();
		super.damage( dmg, src );
		int postHP = HP + shielding();
		if (src instanceof Hunger) postHP -= shielding();
		int effectiveDamage = preHP - postHP;

		if (effectiveDamage <= 0) return;

		if (buff(Challenge.DuelParticipant.class) != null){
			buff(Challenge.DuelParticipant.class).addDamage(effectiveDamage);
		}

		//flash red when hit for serious damage.
		float percentDMG = effectiveDamage / (float)preHP; //percent of current HP that was taken
		float percentHP = 1 - ((HT - postHP) / (float)HT); //percent health after damage was taken
		// The flash intensity increases primarily based on damage taken and secondarily on missing HP.
		float flashIntensity = 0.25f * (percentDMG * percentDMG) / percentHP;
		//if the intensity is very low don't flash at all
		if (flashIntensity >= 0.05f){
			flashIntensity = Math.min(1/3f, flashIntensity); //cap intensity at 1/3
			GameScene.flash( (int)(0xFF*flashIntensity) << 16 );
			if (isAlive()) {
				if (flashIntensity >= 1/6f) {
					Sample.INSTANCE.play(Assets.Sounds.HEALTH_CRITICAL, 1/3f + flashIntensity * 2f);
				} else {
					Sample.INSTANCE.play(Assets.Sounds.HEALTH_WARN, 1/3f + flashIntensity * 4f);
				}
				//hero gets interrupted on taking serious damage, regardless of any other factor
				interrupt();
				resting = false;
				damageInterrupt = true;
			}
		}
	}
	//检查在当前角色的视野范围内是否存在敌对的角色
	public void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		Mob target = null;
		for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (fieldOfView[ m.pos ] && m.alignment == Alignment.ENEMY) {
				visible.add(m);
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}

				if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1){
					if (target == null){
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
					if (m instanceof Snake && Dungeon.level.distance(m.pos, pos) <= 4
							&& !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_EXAMINING)){
						GLog.p(Messages.get(Guidebook.class, "hint"));
						GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_EXAMINING);
						//we set to read here to prevent this message popping up a bunch
						Document.ADVENTURERS_GUIDE.readPage(Document.GUIDE_EXAMINING);
					}
				}
			}
		}

		Char lastTarget = QuickSlotButton.lastTarget;
		if (target != null && (lastTarget == null ||
							!lastTarget.isAlive() || !lastTarget.isActive() ||
							lastTarget.alignment == Alignment.ALLY ||
							!fieldOfView[lastTarget.pos])){
			QuickSlotButton.target(target);
		}
		
		if (newMob) {
			interrupt();
			if (resting){
				Dungeon.observe();
				resting = false;
			}
		}

		visibleEnemies = visible;
	}
	
	public int visibleEnemies() {
		return visibleEnemies.size();
	}
	
	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}

	public ArrayList<Mob> getVisibleEnemies(){
		return new ArrayList<>(visibleEnemies);
	}
	
	private boolean walkingToVisibleTrapInFog = false;
	
	//FIXME this is a fairly crude way to track this, really it would be nice to have a short
	//history of hero actions
	public boolean justMoved = false;
	
	private boolean getCloser( final int target ) {

		if (target == pos)
			return false;

		int step = -1;
		
		if (Dungeon.level.adjacent( pos, target )) {

			path = null;

			if (Actor.findChar( target ) == null) {
				if (Dungeon.level.passable[target] || Dungeon.level.avoid[target]) {
					step = target;
				}
				if (walkingToVisibleTrapInFog
						&& Dungeon.level.traps.get(target) != null
						&& Dungeon.level.traps.get(target).visible){
					return false;
				}
			}
			
		} else {

			boolean newPath = false;
			if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
				newPath = true;
			else if (path.getLast() != target)
				newPath = true;
			else {
				if (!Dungeon.level.passable[path.get(0)] || Actor.findChar(path.get(0)) != null) {
					newPath = true;
				}
			}

			if (newPath) {

				int len = Dungeon.level.length();
				boolean[] p = Dungeon.level.passable;
				boolean[] v = Dungeon.level.visited;
				boolean[] m = Dungeon.level.mapped;
				boolean[] passable = new boolean[len];
				for (int i = 0; i < len; i++) {
					passable[i] = p[i] && (v[i] || m[i]);
				}

				PathFinder.Path newpath = Dungeon.findPath(this, target, passable, fieldOfView, true);
				if (newpath != null && path != null && newpath.size() > 2*path.size()){
					path = null;
				} else {
					path = newpath;
				}
			}

			if (path == null) return false;
			step = path.removeFirst();

		}

		if (step != -1) {

			float delay = 1 / speed();

			if (Dungeon.level.pit[step] && !Dungeon.level.solid[step]
					&& (!flying || buff(Levitation.class) != null && buff(Levitation.class).detachesWithinDelay(delay))){
				if (!Chasm.jumpConfirmed){
					Chasm.heroJump(this);
					interrupt();
				} else {
					flying = false;
					remove(buff(Levitation.class)); //directly remove to prevent cell pressing
					Chasm.heroFall(target);
				}
				canSelfTrample = false;
				return false;
			}

			if (subClass == HeroSubClass.FREERUNNER){
				Buff.affect(this, Momentum.class).gainStack();
			}
			
			sprite.move(pos, step);
			move(step);

			spend( delay );
			justMoved = true;
			
			search(false);

			return true;

		} else {

			return false;
			
		}

	}
	//处理角色在游戏地图上进行移动和交互操作的方法
	public boolean handle( int cell ) {
		
		if (cell == -1) {
			return false;
		}
		if (Dungeon.level.map[cell] == Terrain.SIGN && cell == pos) {//告示牌
			curAction = new HeroAction.Alchemy( cell );
			Sign.readPit();

		}
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}
		
		Char ch = Actor.findChar( cell );
		Heap heap = Dungeon.level.heaps.get( cell );

		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {
			
			curAction = new HeroAction.Alchemy( cell );
			
		} else if (fieldOfView[cell] && ch instanceof Mob) {

			if (ch.alignment != Alignment.ENEMY && ch.buff(Amok.class) == null) {
				curAction = new HeroAction.Interact( ch );
			} else {
				curAction = new HeroAction.Attack( ch );
			}

		//TODO perhaps only trigger this if hero is already adjacent? reducing mistaps
		} else if (Dungeon.level instanceof MiningLevel &&
					belongings.getItem(Pickaxe.class) != null &&
				(Dungeon.level.map[cell] == Terrain.WALL
						|| Dungeon.level.map[cell] == Terrain.WALL_DECO
						|| Dungeon.level.map[cell] == Terrain.MINE_CRYSTAL
						|| Dungeon.level.map[cell] == Terrain.MINE_BOULDER)){

			curAction = new HeroAction.Mine( cell );

		} else if (heap != null
				//moving to an item doesn't auto-pickup when enemies are near...
				&& (visibleEnemies.size() == 0 || cell == pos ||
				//...but only for standard heaps. Chests and similar open as normal.
				(heap.type != Type.HEAP && heap.type != Type.FOR_SALE))) {

			switch (heap.type) {
			case HEAP:
				curAction = new HeroAction.PickUp( cell );
				break;
			case FOR_SALE:
				curAction = heap.size() == 1 && heap.peek().value() > 0 ?
					new HeroAction.Buy( cell ) :
					new HeroAction.PickUp( cell );
				break;
			default:
				curAction = new HeroAction.OpenChest( cell );
			}
			
		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.CRYSTAL_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {
			
			curAction = new HeroAction.Unlock( cell );
			
		} else if (Dungeon.level.getTransition(cell) != null
				//moving to a transition doesn't automatically trigger it when enemies are near
				//moving to a transition doesn't automatically trigger it when enemies are near
				&& (visibleEnemies.size() == 0 || cell == pos)
				&& !Dungeon.level.locked
				&& !Dungeon.level.plants.containsKey(cell)
				&& (Dungeon.depth < 26 || Dungeon.level.getTransition(cell).type == LevelTransition.Type.REGULAR_ENTRANCE) ) {

			curAction = new HeroAction.LvlTransition( cell );
			
		}  else {
			
			if (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell]
					&& Dungeon.level.traps.get(cell) != null && Dungeon.level.traps.get(cell).visible) {
				walkingToVisibleTrapInFog = true;
			} else {
				walkingToVisibleTrapInFog = false;
			}
			
			curAction = new HeroAction.Move( cell );
			lastAction = null;
			
		}

		return true;
	}
	
	public void earnExp( int exp, Class source ) {

		//xp granted by ascension challenge is only for on-exp gain effects
		if (source != AscensionChallenge.class) {
			this.exp += exp;
		}
		float percent = exp/(float)maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if (chains != null) chains.gainExp(percent);

		HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
		if (horn != null) horn.gainCharge(percent);
		
		AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
		if (kit != null) kit.gainCharge(percent);

		MasterThievesArmband.Thievery armband = buff(MasterThievesArmband.Thievery.class);
		if (armband != null) armband.gainCharge(percent);
		
		Berserk berserk = buff(Berserk.class);
		if (berserk != null) berserk.recover(percent);
		
		if (source != PotionOfExperience.class) {
			for (Item i : belongings) {
				i.onHeroGainExp(percent, this);
			}
			if (buff(Talent.RejuvenatingStepsFurrow.class) != null){
				buff(Talent.RejuvenatingStepsFurrow.class).countDown(percent*200f);
				if (buff(Talent.RejuvenatingStepsFurrow.class).count() <= 0){
					buff(Talent.RejuvenatingStepsFurrow.class).detach();
				}
			}
			if (buff(ElementalStrike.ElementalStrikeFurrowCounter.class) != null){
				buff(ElementalStrike.ElementalStrikeFurrowCounter.class).countDown(percent*20f);
				if (buff(ElementalStrike.ElementalStrikeFurrowCounter.class).count() <= 0){
					buff(ElementalStrike.ElementalStrikeFurrowCounter.class).detach();
				}
			}
		}
		
		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();
			if (lvl < MAX_LEVEL) {
				lvl++;
				levelUp = true;
				
				if (buff(ElixirOfMight.HTBoost.class) != null){
					buff(ElixirOfMight.HTBoost.class).onLevelUp();
				}
				
				updateHT( true );
				attackSkill++;
				defenseSkill++;

			} else {
				Buff.prolong(this, Bless.class, Bless.DURATION);
				this.exp = 0;

				GLog.newLine();
				GLog.p( Messages.get(this, "level_cap"));
				Sample.INSTANCE.play( Assets.Sounds.LEVELUP );
			}
			
		}
		
		if (levelUp) {
			
			if (sprite != null) {
				GLog.newLine();
				GLog.p( Messages.get(this, "new_level") );
				sprite.showStatus( CharSprite.POSITIVE, Messages.get(Hero.class, "level_up") );
				Sample.INSTANCE.play( Assets.Sounds.LEVELUP );
				if (lvl < Talent.tierLevelThresholds[Talent.MAX_TALENT_TIERS+1]){
					GLog.newLine();
					GLog.p( Messages.get(this, "new_talent") );
					StatusPane.talentBlink = 10f;
					WndHero.lastIdx = 1;
				}
			}
			
			Item.updateQuickslot();
			
			Badges.validateLevelReached();
		}
	}
	
	public int maxExp() {
		return maxExp( lvl );
	}
	
	public static int maxExp( int lvl ){
		return 5 + lvl * 5;
	}
	
	public boolean isStarving() {
		return Buff.affect(this, Hunger.class).isStarving();
	}
	
	@Override
	public boolean add( Buff buff ) {

		if (buff(TimekeepersHourglass.timeStasis.class) != null) {
			return false;
		}

		boolean added = super.add( buff );

		if (sprite != null && added) {
			String msg = buff.heroMessage();
			if (msg != null){
				GLog.w(msg);
			}

			if (buff instanceof Paralysis || buff instanceof Vertigo) {
				interrupt();
			}

		}
		
		BuffIndicator.refreshHero();

		return added;
	}
	
	@Override
	public boolean remove( Buff buff ) {
		if (super.remove( buff )) {
			BuffIndicator.refreshHero();
			return true;
		}
		return false;
	}
	
	@Override
	public float stealth() {
		float stealth = super.stealth();
		
		if (belongings.armor() != null){
			stealth = belongings.armor().stealthFactor(this, stealth);
		}
		
		return stealth;
	}
	//复活道具的使用
	@Override
	public void die( Object cause ) {
		
		curAction = null;

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for (Ankh i : belongings.getAllItems(Ankh.class)){
			if (ankh == null || i.isBlessed()) {
				ankh = i;
			}
		}

		if (ankh != null) {
			interrupt();
			resting = false;

			if (ankh.isBlessed()) {
				this.HP = HT / 4;

				PotionOfHealing.cure(this);
				Buff.prolong(this, AnkhInvulnerability.class, AnkhInvulnerability.DURATION);

				SpellSprite.show(this, SpellSprite.ANKH);
				GameScene.flash(0x80FFFF40);
				Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				GLog.w(Messages.get(this, "revive"));
				Statistics.ankhsUsed++;

				ankh.detach(belongings.backpack);

				for (Char ch : Actor.chars()) {
					if (ch instanceof DriedRose.GhostHero) {
						((DriedRose.GhostHero) ch).sayAnhk();
						return;
					}
				}
			} else {

				//this is hacky, basically we want to declare that a wndResurrect exists before
				//it actually gets created. This is important so that the game knows to not
				//delete the run or submit it to rankings, because a WndResurrect is about to exist
				//this is needed because the actual creation of the window is delayed here
				WndResurrect.instance = new Object();
				Ankh finalAnkh = ankh;
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndResurrect(finalAnkh) );
					}
				});

				if (cause instanceof Hero.Doom) {
					((Hero.Doom)cause).onDeath();
				}

				SacrificialFire.Marked sacMark = buff(SacrificialFire.Marked.class);
				if (sacMark != null){
					sacMark.detach();
				}

			}
			return;
		}
		
		Actor.fixTime();
		super.die( cause );
		reallyDie( cause );
	}
	
	public static void reallyDie( Object cause ) {
		
		int length = Dungeon.level.length();
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Dungeon.level.discoverable;
		
		for (int i=0; i < length; i++) {
			
			int terr = map[i];
			
			if (discoverable[i]) {
				
				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					Dungeon.level.discover( i );
				}
			}
		}
		
		Bones.leave();
		
		Dungeon.observe();
		GameScene.updateFog();
				
		hero.belongings.identify();

		int pos = hero.pos;

		ArrayList<Integer> passable = new ArrayList<>();
		for (Integer ofs : PathFinder.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Dungeon.level.heaps.get( cell ) == null) {
				passable.add( cell );
			}
		}
		Collections.shuffle( passable );

		ArrayList<Item> items = new ArrayList<>(hero.belongings.backpack.items);
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element( items );
			Dungeon.level.drop( item, cell ).sprite.drop( pos );
			items.remove( item );
		}

		for (Char c : Actor.chars()){
			if (c instanceof DriedRose.GhostHero){
				((DriedRose.GhostHero) c).sayHeroKilled();
			}
		}

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.gameOver();
				Sample.INSTANCE.play( Assets.Sounds.DEATH );
			}
		});

		if (cause instanceof Hero.Doom) {
			((Hero.Doom)cause).onDeath();
		}

		Dungeon.deleteGame( GamesInProgress.curSlot, true );
	}

	//effectively cache this buff to prevent having to call buff(...) a bunch.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and thread coordination implications if that method calls buff(...) frequently
	private Berserk berserk;

	@Override//根据角色的生命值和狂暴状态来确定角色是否还活着
	public boolean isAlive() {
		
		if (HP <= 0){
			if (berserk == null) berserk = buff(Berserk.class);
			return berserk != null && berserk.berserking();
		} else {
			berserk = null;
			return super.isAlive();
		}
	}

	@Override//角色移动时根据移动地块的类型播放不同的音效。
	public void move(int step, boolean travelling) {
		boolean wasHighGrass = Dungeon.level.map[step] == Terrain.HIGH_GRASS;

		super.move( step, travelling);
		
		if (!flying && travelling) {
			if (Dungeon.level.water[pos]) {
				Sample.INSTANCE.play( Assets.Sounds.WATER, 1, Random.Float( 0.8f, 1.25f ) );
			} else if (Dungeon.level.map[pos] == Terrain.EMPTY_SP) {
				Sample.INSTANCE.play( Assets.Sounds.STURDY, 1, Random.Float( 0.96f, 1.05f ) );
			} else if (Dungeon.level.map[pos] == Terrain.GRASS
					|| Dungeon.level.map[pos] == Terrain.EMBERS
					|| Dungeon.level.map[pos] == Terrain.FURROWED_GRASS){
				if (step == pos && wasHighGrass) {
					Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float( 0.96f, 1.05f ) );
				} else {
					Sample.INSTANCE.play( Assets.Sounds.GRASS, 1, Random.Float( 0.96f, 1.05f ) );
				}
			} else {
				Sample.INSTANCE.play( Assets.Sounds.STEP, 1, Random.Float( 0.96f, 1.05f ) );
			}
		}
	}
	
	@Override
	public void onAttackComplete() {

		if (enemy == null){
			curAction = null;
			super.onAttackComplete();
			return;
		}

		AttackIndicator.target(enemy);
		boolean wasEnemy = enemy.alignment == Alignment.ENEMY
				|| (enemy instanceof Mimic && enemy.alignment == Alignment.NEUTRAL);

		boolean hit = attack( enemy );
		
		Invisibility.dispel();
		spend( attackDelay() );

		if (hit && subClass == HeroSubClass.GLADIATOR && wasEnemy){
			Buff.affect( this, Combo.class ).hit( enemy );
		}

		if (hit && heroClass == HeroClass.DUELIST && wasEnemy){
			Buff.affect( this, Sai.ComboStrikeTracker.class).addHit();
		}

		RingOfForce.BrawlersStance brawlStance = buff(RingOfForce.BrawlersStance.class);
		if (brawlStance != null && brawlStance.hitsLeft() > 0){
			MeleeWeapon.Charger charger = Buff.affect(this, MeleeWeapon.Charger.class);
			charger.partialCharge -= RingOfForce.BrawlersStance.HIT_CHARGE_USE;
			while (charger.partialCharge < 0) {
				charger.charges--;
				charger.partialCharge++;
			}
			BuffIndicator.refreshHero();
			Item.updateQuickslot();
		}

		curAction = null;

		super.onAttackComplete();
	}
	
	@Override
	public void onMotionComplete() {
		GameScene.checkKeyHold();
	}
	
	@Override//开门，开箱的操作
	public void onOperateComplete() {
		
		if (curAction instanceof HeroAction.Unlock) {

			int doorCell = ((HeroAction.Unlock)curAction).dst;
			int door = Dungeon.level.map[doorCell];
			
			if (Dungeon.level.distance(pos, doorCell) <= 1) {
				boolean hasKey = true;
				if (door == Terrain.LOCKED_DOOR) {
					hasKey = Notes.remove(new IronKey(Dungeon.depth));
					if (hasKey) Level.set(doorCell, Terrain.DOOR);
				} else if (door == Terrain.CRYSTAL_DOOR) {
					hasKey = Notes.remove(new CrystalKey(Dungeon.depth));
					if (hasKey) {
						Level.set(doorCell, Terrain.EMPTY);
						Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
						CellEmitter.get( doorCell ).start( Speck.factory( Speck.DISCOVER ), 0.025f, 20 );
					}
				} else {
					hasKey = Notes.remove(new SkeletonKey(Dungeon.depth));
					if (hasKey) Level.set(doorCell, Terrain.UNLOCKED_EXIT);
				}
				
				if (hasKey) {
					GameScene.updateKeyDisplay();
					GameScene.updateMap(doorCell);
					spend(Key.TIME_TO_UNLOCK);
				}
			}
			
		} else if (curAction instanceof HeroAction.OpenChest) {
			
			Heap heap = Dungeon.level.heaps.get( ((HeroAction.OpenChest)curAction).dst );
			
			if (Dungeon.level.distance(pos, heap.pos) <= 1){
				boolean hasKey = true;
				if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
					Sample.INSTANCE.play( Assets.Sounds.BONES );
				} else if (heap.type == Type.LOCKED_CHEST){
					hasKey = Notes.remove(new GoldenKey(Dungeon.depth));
				} else if (heap.type == Type.CRYSTAL_CHEST){
					hasKey = Notes.remove(new CrystalKey(Dungeon.depth));
				}
				
				if (hasKey) {
					GameScene.updateKeyDisplay();
					heap.open(this);
					spend(Key.TIME_TO_UNLOCK);
				}
			}
			
		}
		curAction = null;

		if (!ready) {
			super.onOperateComplete();
		}
	}

	@Override
	public boolean isImmune(Class effect) {
		if (effect == Burning.class
				&& belongings.armor() != null
				&& belongings.armor().hasGlyph(Brimstone.class, this)){
			return true;
		}
		return super.isImmune(effect);
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return super.isInvulnerable(effect) || buff(AnkhInvulnerability.class) != null;
	}
//进行特定范围内的搜索操作
	public boolean search( boolean intentional ) {

		if (!isAlive()) return false;
		
		boolean smthFound = false;

		boolean circular = pointsInTalent(Talent.WIDE_SEARCH) == 1;
		int distance = heroClass == HeroClass.ROGUE ? 2 : 1;
		if (hasTalent(Talent.WIDE_SEARCH)) distance++;
		
		boolean foresight = buff(Foresight.class) != null;
		boolean foresightScan = foresight && !Dungeon.level.mapped[pos];

		if (foresightScan){
			Dungeon.level.mapped[pos] = true;
		}

		if (foresight) {
			distance = Foresight.DISTANCE;
			circular = true;
		}

		Point c = Dungeon.level.cellToPoint(pos);

		TalismanOfForesight.Foresight talisman = buff( TalismanOfForesight.Foresight.class );
		boolean cursed = talisman != null && talisman.isCursed();

		int[] rounding = ShadowCaster.rounding[distance];

		int left, right;
		int curr;
		for (int y = Math.max(0, c.y - distance); y <= Math.min(Dungeon.level.height()-1, c.y + distance); y++) {
			if (!circular){
				left = c.x - distance;
			} else if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
				left = c.x - rounding[Math.abs(c.y - y)];
			} else {
				left = distance;
				while (rounding[left] < rounding[Math.abs(c.y - y)]){
					left--;
				}
				left = c.x - left;
			}
			right = Math.min(Dungeon.level.width()-1, c.x + c.x - left);
			left = Math.max(0, left);
			for (curr = left + y * Dungeon.level.width(); curr <= right + y * Dungeon.level.width(); curr++){

				if ((foresight || fieldOfView[curr]) && curr != pos) {

					if ((foresight && (!Dungeon.level.mapped[curr] || foresightScan))){
						GameScene.effectOverFog(new CheckedCell(curr, foresightScan ? pos : curr));
					} else if (intentional) {
						GameScene.effectOverFog(new CheckedCell(curr, pos));
					}

					if (foresight){
						Dungeon.level.mapped[curr] = true;
					}
					
					if (Dungeon.level.secret[curr]){
						
						Trap trap = Dungeon.level.traps.get( curr );
						float chance;

						//searches aided by foresight always succeed, even if trap isn't searchable
						if (foresight){
							chance = 1f;

						//otherwise if the trap isn't searchable, searching always fails
						} else if (trap != null && !trap.canBeSearched){
							chance = 0f;

						//intentional searches always succeed against regular traps and doors
						} else if (intentional){
							chance = 1f;
						
						//unintentional searches always fail with a cursed talisman
						} else if (cursed) {
							chance = 0f;
							
						//unintentional trap detection scales from 40% at floor 0 to 30% at floor 25
						} else if (Dungeon.level.map[curr] == Terrain.SECRET_TRAP) {
							chance = 0.4f - (Dungeon.depth / 250f);
							
						//unintentional door detection scales from 20% at floor 0 to 0% at floor 20
						} else {
							chance = 0.2f - (Dungeon.depth / 100f);
						}

						//don't want to let the player search though hidden doors in tutorial
						if (SPDSettings.intro()){
							chance = 0;
						}
						
						if (Random.Float() < chance) {
						
							int oldValue = Dungeon.level.map[curr];
							
							GameScene.discoverTile( curr, oldValue );
							
							Dungeon.level.discover( curr );
							
							ScrollOfMagicMapping.discover( curr );
							
							if (fieldOfView[curr]) smthFound = true;
	
							if (talisman != null){
								if (oldValue == Terrain.SECRET_TRAP){
									talisman.charge(2);
								} else if (oldValue == Terrain.SECRET_DOOR){
									talisman.charge(10);
								}
							}
						}
					}
				}
			}
		}
		
		if (intentional) {
			sprite.showStatus( CharSprite.DEFAULT, Messages.get(this, "search") );
			sprite.operate( pos );
			if (!Dungeon.level.locked) {
				if (cursed) {
					GLog.n(Messages.get(this, "search_distracted"));
					Buff.affect(this, Hunger.class).affectHunger(TIME_TO_SEARCH - (2 * HUNGER_FOR_SEARCH));
				} else {
					Buff.affect(this, Hunger.class).affectHunger(TIME_TO_SEARCH - HUNGER_FOR_SEARCH);
				}
			}
			spendAndNext(TIME_TO_SEARCH);
			
		}
		
		if (smthFound) {
			GLog.w( Messages.get(this, "noticed_smth") );
			Sample.INSTANCE.play( Assets.Sounds.SECRET );
			interrupt();
		}

		if (foresight){
			GameScene.updateFog(pos, Foresight.DISTANCE+1);
		}
		
		return smthFound;
	}
	
	public void resurrect() {
		HP = HT;
		live();

		MagicalHolster holster = belongings.getItem(MagicalHolster.class);

		Buff.affect(this, LostInventory.class);
		Buff.affect(this, Invisibility.class, 3f);
		//lost inventory is dropped in interlevelscene

		//activate items that persist after lost inventory
		//FIXME this is very messy, maybe it would be better to just have one buff that
		// handled all items that recharge over time?
		for (Item i : belongings){
			if (i instanceof EquipableItem && i.isEquipped(this)){
				((EquipableItem) i).activate(this);
			} else if (i instanceof CloakOfShadows && i.keptThroughLostInventory() && hasTalent(Talent.LIGHT_CLOAK)){
				((CloakOfShadows) i).activate(this);
			} else if (i instanceof Wand && i.keptThroughLostInventory()){
				if (holster != null && holster.contains(i)){
					((Wand) i).charge(this, MagicalHolster.HOLSTER_SCALE_FACTOR);
				} else {
					((Wand) i).charge(this);
				}
			} else if (i instanceof MagesStaff && i.keptThroughLostInventory()){
				((MagesStaff) i).applyWandChargeBuff(this);
			}
		}

		updateHT(false);
	}

	@Override
	public void next() {
		if (isAlive())
			super.next();
	}

	public static interface Doom {
		public void onDeath();
	}
}
