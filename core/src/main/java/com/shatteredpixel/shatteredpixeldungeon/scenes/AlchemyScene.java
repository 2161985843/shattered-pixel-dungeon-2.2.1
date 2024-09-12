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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndEnergizeItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;

import java.io.IOException;
import java.util.ArrayList;

public class AlchemyScene extends PixelScene {

	//max of 3 inputs, and 3 potential recipe outputs
	private static final InputButton[] inputs = new InputButton[3];
	private static final CombineButton[] combines = new CombineButton[3];
	private static final OutputSlot[] outputs = new OutputSlot[3];
	
	private Emitter smokeEmitter;
	private Emitter bubbleEmitter;
	private Emitter sparkEmitter;
	
	private Emitter lowerBubbles;
	private SkinnedBlock water;

	private Image energyIcon;
	private RenderedTextBlock energyLeft;
	private IconButton energyAdd;

	private static final int BTN_SIZE	= 28;

	{
		inGameScene = true;
	}
	
	@Override
	public void create() {
		super.create();

		// 创建一个新的SkinnedBlock对象用于表示水面
		water = new SkinnedBlock(
				Camera.main.width, Camera.main.height,
				Dungeon.level.waterTex() ){

			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get(); // 设置脚本为没有光照
			}

			@Override
			public void draw() {
				// 水面没有透明度组件，这样可以提高性能
				Blending.disable(); // 关闭混合模式
				super.draw(); // 调用父类的绘制方法
				Blending.enable(); // 重新启用混合模式
			}
		};
		water.autoAdjust = true; // 自动调整水面的显示
		add(water); // 将水面添加到场景中

// 创建一个渐变色的Image对象
		Image im = new Image(TextureCache.createGradient(0x66000000, 0x88000000, 0xAA000000, 0xCC000000, 0xFF000000));
		im.angle = 90; // 设置图片角度
		im.x = Camera.main.width; // 设置图片位置
		im.scale.x = Camera.main.height / 5f; // 设置图片缩放
		im.scale.y = Camera.main.width;
		add(im); // 将图片添加到场景中

// 创建一个Emitter对象用于生成气泡
		bubbleEmitter = new Emitter();
		bubbleEmitter.pos(0, 0, Camera.main.width, Camera.main.height); // 设置气泡发射区域
		bubbleEmitter.autoKill = false; // 不自动消失
		add(bubbleEmitter); // 将气泡发射器添加到场景中

		lowerBubbles = new Emitter(); // 创建另一个Emitter对象
		add(lowerBubbles); // 添加到场景中

// 渲染标题文本块
		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
		title.hardlight(Window.TITLE_COLOR); // 设置文本颜色
		title.setPos(
				(Camera.main.width - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title); // 对齐文本
		add(title); // 将标题添加到场景中

// 渲染描述文本块
		int w = 50 + Camera.main.width / 2;
		int left = (Camera.main.width - w) / 2;
		int pos = (Camera.main.height - 100) / 2;
		RenderedTextBlock desc = PixelScene.renderTextBlock(6);
		desc.maxWidth(w); // 设置文本最大宽度
		desc.text(Messages.get(AlchemyScene.class, "text")); // 设置文本内容
		desc.setPos(left + (w - desc.width()) / 2, pos);
		add(desc); // 将描述文本添加到场景中

		pos += desc.height() + 6;

// 创建背景图像
		NinePatch inputBG = Chrome.get(Chrome.Type.TOAST_TR);
		inputBG.x = left + 6;
		inputBG.y = pos;
		inputBG.size(BTN_SIZE + 8, 3 * BTN_SIZE + 4 + 8);
		add(inputBG); // 将背景图像添加到场景中

		pos += 4;

// 创建和添加输入按钮
		synchronized (inputs) {
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = new InputButton();
				inputs[i].setRect(left + 10, pos, BTN_SIZE, BTN_SIZE);
				add(inputs[i]);
				pos += BTN_SIZE + 2;
			}
		}

// 创建并配置CombineButton和OutputSlot对象
		for (int i = 0; i < inputs.length; i++) {
			combines[i] = new CombineButton(i);
			combines[i].enable(false);

			outputs[i] = new OutputSlot();
			outputs[i].item(null);

			if (i == 0) {
				// 第一个按钮总是可见
				combines[i].setRect(left + (w - 30) / 2f, inputs[1].top() + 5, 30, inputs[1].height() - 10);
				outputs[i].setRect(left + w - BTN_SIZE - 10, inputs[1].top(), BTN_SIZE, BTN_SIZE);
			} else {
				combines[i].visible = false;
				outputs[i].visible = false;
			}

			add(combines[i]); // 将CombineButton添加到场景中
			add(outputs[i]); // 将OutputSlot添加到场景中
		}

// 创建一个Emitter对象用于生成烟雾
		smokeEmitter = new Emitter();
		smokeEmitter.pos(outputs[0].left() + (BTN_SIZE - 16) / 2f, outputs[0].top() + (BTN_SIZE - 16) / 2f, 16, 16);
		smokeEmitter.autoKill = false; // 不自动消失
		add(smokeEmitter); // 将烟雾发射器添加到场景中


		pos += 10; // 增加pos值，用于调整位置

		lowerBubbles.pos(0, pos, Camera.main.width, Math.max(0, Camera.main.height - pos)); // 设置lowerBubbles的位置和大小
		lowerBubbles.pour(Speck.factory(Speck.BUBBLE), 0.1f); // 生成气泡

		ExitButton btnExit = new ExitButton() {
			@Override
			protected void onClick() {
				Game.switchScene(GameScene.class); // 切换到游戏场景
			}
		};
		btnExit.setPos(Camera.main.width - btnExit.width(), 0); // 设置退出按钮的位置
		add(btnExit); // 将退出按钮添加到场景中

		IconButton btnGuide = new IconButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE, null)) {
			@Override
			protected void onClick() {
				super.onClick();
				clearSlots(); // 清除槽位
				updateState(); // 更新状态
				AlchemyScene.this.addToFront(new Window() {
					{
						WndJournal.AlchemyTab t = new WndJournal.AlchemyTab(); // 创建炼金指南选项卡
						int w, h;
						if (landscape()) {
							w = WndJournal.WIDTH_L; h = WndJournal.HEIGHT_L;
						} else {
							w = WndJournal.WIDTH_P; h = WndJournal.HEIGHT_P;
						}
						resize(w, h); // 调整窗口大小
						add(t); // 将选项卡添加到窗口中
						t.setRect(0, 0, w, h); // 设置选项卡的位置和大小
					}
				});
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Document.ALCHEMY_GUIDE.title()); // 设置按钮悬浮时的文本
			}
		};
		btnGuide.setRect(0, 0, 20, 20); // 设置指南按钮的位置和大小
		add(btnGuide); // 将指南按钮添加到场景中

		String energyText = Messages.get(AlchemyScene.class, "energy") + " " + Dungeon.energy; // 设置能量文本
		if (toolkit != null) {
			energyText += "+" + toolkit.availableEnergy(); // 如果有工具包，添加额外的能量
		}

		energyLeft = PixelScene.renderTextBlock(energyText, 9); // 渲染能量文本块
		energyLeft.setPos(
				(Camera.main.width - energyLeft.width()) / 2,
				Camera.main.height - 8 - energyLeft.height()
		);
		energyLeft.hardlight(0x44CCFF); // 设置文本颜色
		add(energyLeft); // 将能量文本添加到场景中

		energyIcon = new ItemSprite(toolkit != null ? ItemSpriteSheet.ARTIFACT_TOOLKIT : ItemSpriteSheet.ENERGY); // 设置能量图标
		energyIcon.x = energyLeft.left() - energyIcon.width();
		energyIcon.y = energyLeft.top() - (energyIcon.height() - energyLeft.height()) / 2;
		align(energyIcon); // 对齐图标
		add(energyIcon); // 将能量图标添加到场景中

		energyAdd = new IconButton(Icons.get(Icons.PLUS)) {
			@Override
			protected void onClick() {
				WndEnergizeItem.openItemSelector(); // 打开能量物品选择器
			}
		};
		energyAdd.setRect(energyLeft.right(), energyLeft.top() - (16 - energyLeft.height()) / 2, 16, 16); // 设置添加按钮的位置和大小
		align(energyAdd); // 对齐添加按钮
		add(energyAdd); // 将添加按钮添加到场景中

		sparkEmitter = new Emitter(); // 创建火花发射器
		sparkEmitter.pos(energyLeft.left(), energyLeft.top(), energyLeft.width(), energyLeft.height()); // 设置发射器的位置和大小
		sparkEmitter.autoKill = false; // 不自动消失
		add(sparkEmitter); // 将火花发射器添加到场景中

		fadeIn(); // 淡入效果

		try {
			Dungeon.saveAll(); // 保存所有数据
			Badges.saveGlobal(); // 保存全局徽章数据
			Journal.saveGlobal(); // 保存全局日志
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e); // 记录异常
		}
	}
	
	@Override
	public void update() {
		super.update();
		water.offset( 0, -5 * Game.elapsed );
	}
	
	@Override
	protected void onBackPressed() {
		Game.switchScene(GameScene.class);
	}
	
	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(AlchemyScene.class, "select");
		}

		@Override
		public boolean itemSelectable(Item item) {
			return Recipe.usableInRecipe(item);
		}

		@Override
		public void onSelect( Item item ) {
			synchronized (inputs) {
				if (item != null && inputs[0] != null) {
					for (int i = 0; i < inputs.length; i++) {
						if (inputs[i].item() == null) {
							if (item instanceof LiquidMetal){
								inputs[i].item(item.detachAll(Dungeon.hero.belongings.backpack));
							} else {
								inputs[i].item(item.detach(Dungeon.hero.belongings.backpack));
							}
							break;
						}
					}
					updateState();
				}
			}
		}
	};
	
	private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
		ArrayList<T> filtered = new ArrayList<>();
		for (int i = 0; i < inputs.length; i++){
			Item item = inputs[i].item();
			if (item != null && itemClass.isInstance(item)){
				filtered.add((T)item);
			}
		}
		return filtered;
	}
	
	private void updateState(){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		ArrayList<Recipe> recipes = Recipe.findRecipes(ingredients);

		//disables / hides unneeded buttons
		for (int i = recipes.size(); i < combines.length; i++){
			combines[i].enable(false);
			outputs[i].item(null);

			if (i != 0){
				combines[i].visible = false;
				outputs[i].visible = false;
			}
		}

		if (recipes.isEmpty()){
			combines[0].setPos(combines[0].left(), inputs[1].top()+5);
			outputs[0].setPos(outputs[0].left(), inputs[1].top());
			return;
		}

		//positions active buttons
		float gap = recipes.size() == 2 ? 6 : 2;

		float height = inputs[2].bottom() - inputs[0].top();
		height -= recipes.size()*BTN_SIZE + (recipes.size()-1)*gap;
		float top = inputs[0].top() + height/2;

		//positions and enables active buttons
		for (int i = 0; i < recipes.size(); i++){

			Recipe recipe = recipes.get(i);

			int cost = recipe.cost(ingredients);

			outputs[i].visible = true;
			outputs[i].setRect(outputs[0].left(), top, BTN_SIZE, BTN_SIZE);
			outputs[i].item(recipe.sampleOutput(ingredients));
			top += BTN_SIZE+gap;

			int availableEnergy = Dungeon.energy;
			if (toolkit != null){
				availableEnergy += toolkit.availableEnergy();
			}

			combines[i].visible = true;
			combines[i].setRect(combines[0].left(), outputs[i].top()+5, 30, 20);
			combines[i].enable(cost <= availableEnergy, cost);

		}
		
	}
	
	private void combine( int slot ){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		if (ingredients.isEmpty()) return;

		ArrayList<Recipe> recipes = Recipe.findRecipes(ingredients);
		if (recipes.size() <= slot) return;

		Recipe recipe = recipes.get(slot);
		
		Item result = null;
		
		if (recipe != null){
			int cost = recipe.cost(ingredients);
			if (toolkit != null){
				cost = toolkit.consumeEnergy(cost);
			}
			Dungeon.energy -= cost;

			String energyText = Messages.get(AlchemyScene.class, "energy") + " " + Dungeon.energy;
			if (toolkit != null){
				energyText += "+" + toolkit.availableEnergy();
			}
			energyLeft.text(energyText);
			energyLeft.setPos(
					(Camera.main.width - energyLeft.width())/2,
					Camera.main.height - 8 - energyLeft.height()
			);

			energyIcon.x = energyLeft.left() - energyIcon.width();
			align(energyIcon);

			energyAdd.setPos(energyLeft.right(), energyAdd.top());
			align(energyAdd);
			
			result = recipe.brew(ingredients);
		}
		
		if (result != null){
			bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.01f, 100 );
			smokeEmitter.burst(Speck.factory( Speck.WOOL ), 10 );
			Sample.INSTANCE.play( Assets.Sounds.PUFF );

			int resultQuantity = result.quantity();
			if (!result.collect()){
				Dungeon.level.drop(result, Dungeon.hero.pos);
			}

			Statistics.itemsCrafted++;
			Badges.validateItemsCrafted();
			
			try {
				Dungeon.saveAll();
			} catch (IOException e) {
				ShatteredPixelDungeon.reportException(e);
			}
			
			synchronized (inputs) {
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i] != null && inputs[i].item() != null) {
						Item item = inputs[i].item();
						if (item.quantity() <= 0) {
							inputs[i].item(null);
						} else {
							inputs[i].slot.updateText();
						}
					}
				}
			}
			
			updateState();
			//we reset the quantity in case the result was merged into another stack in the backpack
			result.quantity(resultQuantity);
			outputs[0].item(result);
		}
		
	}
	
	public void populate(ArrayList<Item> toFind, Belongings inventory){
		clearSlots();
		
		int curslot = 0;
		for (Item finding : toFind){
			int needed = finding.quantity();
			ArrayList<Item> found = inventory.getAllSimilar(finding);
			while (!found.isEmpty() && needed > 0){
				Item detached;
				if (finding instanceof LiquidMetal) {
					detached = found.get(0).detachAll(inventory.backpack);
				} else {
					detached = found.get(0).detach(inventory.backpack);
				}
				inputs[curslot].item(detached);
				curslot++;
				needed -= detached.quantity();
				if (detached == found.get(0)) {
					found.remove(0);
				}
			}
		}
		updateState();
	}
	
	@Override
	public void destroy() {
		synchronized ( inputs ) {
			clearSlots();
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = null;
			}
		}
		
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}
		super.destroy();
	}
	
	public void clearSlots(){
		synchronized ( inputs ) {
			for (int i = 0; i < inputs.length; i++) {
				if (inputs[i] != null && inputs[i].item() != null) {
					Item item = inputs[i].item();
					if (!item.collect()) {
						Dungeon.level.drop(item, Dungeon.hero.pos);
					}
					inputs[i].item(null);
				}
			}
		}
	}

	public void createEnergy(){
		String energyText = Messages.get(AlchemyScene.class, "energy") + " " + Dungeon.energy;
		if (toolkit != null){
			energyText += "+" + toolkit.availableEnergy();
		}
		energyLeft.text(energyText);
		energyLeft.setPos(
				(Camera.main.width - energyLeft.width())/2,
				Camera.main.height - 8 - energyLeft.height()
		);

		energyIcon.x = energyLeft.left() - energyIcon.width();
		align(energyIcon);

		energyAdd.setPos(energyLeft.right(), energyAdd.top());
		align(energyAdd);

		bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.01f, 100 );
		sparkEmitter.burst(SparkParticle.FACTORY, 20);
		Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );

		updateState();
	}
	
	private class InputButton extends Component {
		
		protected NinePatch bg;
		protected ItemSlot slot;
		
		private Item item = null;
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			bg = Chrome.get( Chrome.Type.RED_BUTTON);
			add( bg );
			
			slot = new ItemSlot() {
				@Override
				protected void onPointerDown() {
					bg.brightness( 1.2f );
					Sample.INSTANCE.play( Assets.Sounds.CLICK );
				}
				@Override
				protected void onPointerUp() {
					bg.resetColor();
				}
				@Override
				protected void onClick() {
					super.onClick();
					Item item = InputButton.this.item;
					if (item != null) {
						if (!item.collect()) {
							Dungeon.level.drop(item, Dungeon.hero.pos);
						}
						InputButton.this.item(null);
						updateState();
					}
					AlchemyScene.this.addToFront(WndBag.getBag( itemSelector ));
				}

				@Override
				protected boolean onLongClick() {
					Item item = InputButton.this.item;
					if (item != null){
						AlchemyScene.this.addToFront(new WndInfoItem(item));
						return true;
					}
					return false;
				}
			};
			slot.enable(true);
			add( slot );
		}

		@Override
		protected void layout() {
			super.layout();
			
			bg.x = x;
			bg.y = y;
			bg.size( width, height );
			
			slot.setRect( x + 2, y + 2, width - 4, height - 4 );
		}

		public Item item(){
			return item;
		}

		public void item( Item item ) {
			if (item == null){
				this.item = null;
				slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
			} else {
				slot.item(this.item = item);
			}
		}
	}

	private class CombineButton extends Component {

		protected int slot;

		protected RedButton button;
		protected RenderedTextBlock costText;

		private CombineButton(int slot){
			super();

			this.slot = slot;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			button = new RedButton(""){
				@Override
				protected void onClick() {
					super.onClick();
					combine(slot);
				}
			};
			button.icon(Icons.get(Icons.ARROW));
			add(button);

			costText = PixelScene.renderTextBlock(6);
			add(costText);
		}

		@Override
		protected void layout() {
			super.layout();

			button.setRect(x, y, width(), height());

			costText.setPos(
					left() + (width() - costText.width())/2,
					top() - costText.height()
			);
		}

		public void enable( boolean enabled ){
			enable(enabled, 0);
		}

		public void enable( boolean enabled, int cost ){
			button.enable(enabled);
			if (enabled) {
				button.icon().tint(1, 1, 0, 1);
				button.alpha(1f);
				costText.hardlight(0x44CCFF);
			} else {
				button.icon().color(0, 0, 0);
				button.alpha(0.6f);
				costText.hardlight(0xFF0000);
			}

			if (cost == 0){
				costText.visible = false;
			} else {
				costText.visible = true;
				costText.text(Messages.get(AlchemyScene.class, "energy") + " " + cost);
			}

			layout();
		}

	}

	private class OutputSlot extends Component {

		protected NinePatch bg;
		protected ItemSlot slot;

		@Override
		protected void createChildren() {

			bg = Chrome.get(Chrome.Type.TOAST_TR);
			add(bg);

			slot = new ItemSlot() {
				@Override
				protected void onClick() {
					super.onClick();
					if (visible && item != null && item.trueName() != null){
						AlchemyScene.this.addToFront(new WndInfoItem(item));
					}
				}
			};
			slot.item(null);
			add( slot );
		}

		@Override
		protected void layout() {
			super.layout();

			bg.x = x;
			bg.y = y;
			bg.size(width(), height());

			slot.setRect(x+2, y+2, width()-4, height()-4);
		}

		public void item( Item item ) {
			slot.item(item);
		}
	}

	private static AlchemistsToolkit toolkit;

	public static void assignToolkit( AlchemistsToolkit toolkit ){
		AlchemyScene.toolkit = toolkit;
	}

	public static void clearToolkit(){
		AlchemyScene.toolkit = null;
	}

}
