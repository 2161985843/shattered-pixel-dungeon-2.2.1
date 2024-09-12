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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.Properties;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StatusPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Locale;

public class WndHero extends WndTabbed {

	private static final int WIDTH		= 120;
	private static final int HEIGHT		= 120;
	
	private StatsTab stats;
	private TalentsTab talents;
	private BuffsTab buffs;
	private NZ neizan;
	public static int lastIdx = 0;

	public WndHero() {

		super();

		resize( WIDTH, HEIGHT );

		stats = new StatsTab();
		add( stats );

		talents = new TalentsTab();
		add(talents);
		talents.setRect(0, 0, WIDTH, HEIGHT);

		buffs = new BuffsTab();
		add( buffs );
		buffs.setRect(0, 0, WIDTH, HEIGHT);
		buffs.setupList();

		neizan = new NZ();
		add( neizan );
		neizan.setRect(0, 0, WIDTH, HEIGHT);
		neizan.setupList();

		add( new IconTab( Icons.get(Icons.RANKINGS) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) {
					lastIdx = 0;
					if (!stats.visible) {
						stats.initialize();
					}
				}
				stats.visible = stats.active = selected;
			}
		} );
		add( new IconTab( Icons.get(Icons.TALENT) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 1;
				if (selected) StatusPane.talentBlink = 0;
				talents.visible = talents.active = selected;
			}
		} );
		add( new IconTab( Icons.get(Icons.BUFFS) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 2;
				buffs.visible = buffs.active = selected;
			}
		} );
		add( new IconTab( Icons.get(Icons.NEWS) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 3;
				neizan.visible = neizan.active = selected;
			}
		} );
		layoutTabs();

		talents.setRect(0, 0, WIDTH, HEIGHT);
		talents.pane.scrollTo(0, talents.pane.content().height() - talents.pane.height());
		talents.layout();

		select( lastIdx );
	}

	@Override
	public void offset(int xOffset, int yOffset) {
		super.offset(xOffset, yOffset);
		talents.layout();
		buffs.layout();
	}

	private class StatsTab extends Group {
		
		private static final int GAP = 6;
		
		private float pos;
		
		public StatsTab() {
			initialize();
		}

		public void initialize(){

			for (Gizmo g : members){
				if (g != null) g.destroy();
			}
			clear();
			
			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar(hero.heroClass, hero.tier()) );
			if (hero.name().equals(hero.className()))
				title.label( Messages.get(this, "title", hero.lvl, hero.className() ).toUpperCase( Locale.ENGLISH ) );
			else
				title.label((hero.name() + "\n" + Messages.get(this, "title", hero.lvl, hero.className())).toUpperCase(Locale.ENGLISH));
			title.color(Window.TITLE_COLOR);
			title.setRect( 0, 0, WIDTH-16, 0 );
			add(title);

			IconButton infoButton = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					super.onClick();
					if (ShatteredPixelDungeon.scene() instanceof GameScene){
						GameScene.show(new WndHeroInfo(hero.heroClass));
					} else {
						ShatteredPixelDungeon.scene().addToFront(new WndHeroInfo(hero.heroClass));
					}
				}

				@Override
				protected String hoverText() {
					return Messages.titleCase(Messages.get(WndKeyBindings.class, "hero_info"));
				}

			};
			infoButton.setRect(title.right(), 0, 16, 16);
			add(infoButton);

			pos = title.bottom() + 2*GAP;
			int maxHunger = (int) Hunger.STARVING;
			Hunger hungerBuff = Dungeon.hero.buff(Hunger.class);
			int strBonus = hero.STR() - hero.STR;
			if (strBonus > 0)           statSlot( Messages.get(this, "str"), hero.STR + " + " + strBonus );
			else if (strBonus < 0)      statSlot( Messages.get(this, "str"), hero.STR + " - " + -strBonus );
			else                        statSlot( Messages.get(this, "str"), hero.STR() );
			if (hero.shielding() > 0)   statSlot( Messages.get(this, "health"), hero.HP + "+" + hero.shielding() + "/" + hero.HT );
			else                        statSlot( Messages.get(this, "health"), (hero.HP) + "/" + hero.HT );
			if (hungerBuff != null) {
				int hunger = Math.max(0, maxHunger - hungerBuff.hunger());
				statSlot(Messages.get(this, "hungera"), hunger / 10 + "/" + maxHunger / 10);
			}

			statSlot( Messages.get(this, "exp"), hero.exp + "/" + hero.maxExp() );

			pos += GAP;

			statSlot( Messages.get(this, "gold"), Statistics.goldCollected );
			statSlot( Messages.get(this, "depth"), Statistics.deepestFloor );
			if (Dungeon.daily){
				if (!Dungeon.dailyReplay) {
					statSlot(Messages.get(this, "daily_for"), "_" + Dungeon.customSeedText + "_");
				} else {
					statSlot(Messages.get(this, "replay_for"), "_" + Dungeon.customSeedText + "_");
				}
			} else if (!Dungeon.customSeedText.isEmpty()){
				statSlot( Messages.get(this, "custom_seed"), "_" + Dungeon.customSeedText + "_" );
			} else {
				statSlot( Messages.get(this, "dungeon_seed"), DungeonSeed.convertToCode(Dungeon.seed) );
			}

			pos += GAP;
		}

		private void statSlot( String label, String value ) {
			
			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 8 );
			txt.setPos(0, pos);
			add( txt );
			
			txt = PixelScene.renderTextBlock( value, 8 );
			txt.setPos(WIDTH * 0.55f, pos);
			PixelScene.align(txt);
			add( txt );
			
			pos += GAP + txt.height();
		}
		
		private void statSlot( String label, int value ) {
			statSlot( label, Integer.toString( value ) );
		}
		
		public float height() {
			return pos;
		}
	}

	public class TalentsTab extends Component {

		TalentsPane pane;

		@Override
		protected void createChildren() {
			super.createChildren();
			pane = new TalentsPane(TalentButton.Mode.UPGRADE);
			add(pane);
		}

		@Override
		protected void layout() {
			super.layout();
			pane.setRect(x, y, width, height);
		}

	}
	
	private class BuffsTab extends Component {
		
		private static final int GAP = 2;
		
		private float pos;
		private ScrollPane buffList;
		private ArrayList<BuffSlot> slots = new ArrayList<>();

		@Override
		protected void createChildren() {

			super.createChildren();

			buffList = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = slots.size();
					for (int i=0; i < size; i++) {
						if (slots.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add(buffList);
		}
		
		@Override
		protected void layout() {
			super.layout();
			buffList.setRect(0, 0, width, height);
		}
		
		private void setupList() {


			Component content = buffList.content();
			for (Buff buff : Dungeon.hero.buffs()) {
				if (buff.icon() != BuffIndicator.NONE) {
					BuffSlot slot = new BuffSlot(buff);
					slot.setRect(0, pos, WIDTH, slot.icon.height());
					content.add(slot);
					slots.add(slot);
					pos += GAP + slot.height();
				}
			}
			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());
		}

		private class BuffSlot extends Component {

			private Buff buff;

			Image icon;
			RenderedTextBlock txt;

			public BuffSlot( Buff buff ){
				super();
				this.buff = buff;

				icon = new BuffIcon(buff, true);
				icon.y = this.y;
				add( icon );

				txt = PixelScene.renderTextBlock( Messages.titleCase(buff.name()), 8 );
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
				add( txt );

			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.maxWidth((int)(width - icon.width()));
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
			}
			
			protected boolean onClick ( float x, float y ) {
				if (inside( x, y )) {
					GameScene.show(new WndInfoBuff(buff));
					return true;
				} else {
					return false;
				}
			}
		}
	}
	private class NZ extends Component {

		private static final int GAP = 2;
		private float pos;
		private ScrollPane buffList;
		private ArrayList<BuffSlott> slots = new ArrayList<BuffSlott>();

		@Override
		protected void createChildren() {

			super.createChildren();

			buffList = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = slots.size();
					for (int i=0; i < size; i++) {
						if (slots.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add(buffList);
		}

		@Override
		protected void layout() {
			super.layout();
			buffList.setRect(0, 0, width, height);
		}

		private void setupList() {

			Component content = buffList.content();

			for (Buff buff : Dungeon.hero.buffs()) {
				if (buff instanceof Properties) {
					BuffSlott slot = new BuffSlott(buff);
					slot.setRect(0, pos, WIDTH, 10); // 修改为 slot.height()
					content.add(slot);
					slots.add(slot);
					pos += GAP + slot.height();

				}
			}

			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());
		}
		private class BuffSlott extends Component {

			private Buff buff;

			Image icon;
			RenderedTextBlock txt;

			public BuffSlott( Buff buff ){
				super();
				this.buff = buff;

				icon = new BuffIcon(buff, true);
				icon.y = this.y;
				add( icon );

				txt = PixelScene.renderTextBlock( Messages.titleCase(buff.name()), 8 );
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				txt.hardlight(ORANGE_COLOR);
				PixelScene.align(txt);
				add( txt );

			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.maxWidth((int)(width - icon.width()));
				txt.setPos(
						0,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
			}

			protected boolean onClick ( float x, float y ) {
				if (inside( x, y )) {
					GameScene.show(new WndInfoBuff(buff));
					return true;
				} else {
					return false;
				}
			}
		}
		private class itemss extends Component {
			private Image[] icons;
			private RenderedTextBlock title;
			private RenderedTextBlock message;
			private int MARGIN = 2;
			private int i = 0;
			private RenderedTextBlock[] info;

			private void addEquipmentSlot(HeroClass cls) {
				title = PixelScene.renderTextBlock(Messages.titleCase(cls.title()), 9);
				title.hardlight(ORANGE_COLOR);
				add(title);



				String[] desc_entries = cls.desc().split("\n\n");

				info = new RenderedTextBlock[desc_entries.length];

				for (int i = 0; i < desc_entries.length; i++){
					info[i] = PixelScene.renderTextBlock(desc_entries[i], 6);
					add(info[i]);
				}

				Hero hero = Dungeon.hero;
				 i++;
					// 根据不同英雄类型加载图标
				switch (cls){
					case WARRIOR: default:
						icons = new Image[]{ new ItemSprite(ItemSpriteSheet.SEAL),
								new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD),
								new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
						break;
					case MAGE:
						icons = new Image[]{ new ItemSprite(ItemSpriteSheet.MAGES_STAFF),
								new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE),
								new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
						break;
					case ROGUE:
						icons = new Image[]{ new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK),
								Icons.get(Icons.STAIRS),
								new ItemSprite(ItemSpriteSheet.DAGGER),
								new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
						break;
					case HUNTRESS:
						icons = new Image[]{ new ItemSprite(ItemSpriteSheet.SPIRIT_BOW),
								new Image(Assets.Environment.TILES_SEWERS, 32, 64, 16, 16),
								new ItemSprite(ItemSpriteSheet.GLOVES),
								new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
						break;
					case DUELIST:
						icons = new Image[]{ new ItemSprite(ItemSpriteSheet.RAPIER),
								new ItemSprite(hero.belongings.armor),
								new ItemSprite(ItemSpriteSheet.THROWING_SPIKE),
								new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
						break;
				}
				for (Image im : icons) {
					add(im);
				}

			}

			@Override
			protected void layout() {
				super.layout();
				title.setPos((width-title.width())/2, MARGIN);

				float pos = title.bottom()+4*MARGIN;

				for (int i = 0; i < info.length; i++){
					info[i].maxWidth((int)width - 20);
					info[i].setPos(20, pos);

					icons[i].x = (20-icons[i].width())/2;
					icons[i].y = 5*i + (info[i].height() - icons[i].height())/2;

					pos = info[i].bottom() + 4*MARGIN;
				}

				height = Math.max(height, pos - 4*MARGIN);
			}
		}


	}
}
