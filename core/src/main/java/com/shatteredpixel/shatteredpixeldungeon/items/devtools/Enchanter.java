package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.properties.Dome;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.ChallengeItem;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoBuff;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Enchanter extends ChallengeItem {
    {
        unique = true;
        defaultAction = AC_ENCHANT;
        image = ItemSpriteSheet.KIT;
    }

    private static final String AC_ENCHANT = "enchant";
    private static boolean bool =false;
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ENCHANT);
        return actions;
    } public String name() {
        return Messages.get(Dome.class, "name");}

    @Override
    public void execute(Hero hero, String action) {
        if(action.equals(AC_ENCHANT)){
            GameScene.show(new WndEnchant());
        }else {
            super.execute(hero, action);
        }
    }

    public static class WndEnchant extends Window {
        private static final int BTN_SIZE = 24;
        private static final int WIDTH = 180;
        private static final int GAP = 2;

        private final ItemButton[] inputs = new ItemButton[1];
        private final ItemButton toEnchant;
        private final RedButton execute;

        private final RedButton ew;

        private   RenderedTextBlock title1;
        private   EnchRecipe recipe;
        public WndEnchant(){
            // 调用父类构造函数，创建一个银色窗口
            super(0, 0, Chrome.get(Chrome.Type.WINDOW_SILVER));

            // 设置窗口的大小
            resize(WIDTH, 100);

            // 渲染标题文本块，并设置其位置
            RenderedTextBlock title = PixelScene.renderTextBlock(M.L(Enchanter.class, "title"), 10);
            title.setPos(WIDTH / 2f - title.width() / 2, GAP);
            add(title);

            synchronized (inputs) {
                // 计算第一个按钮的横坐标
                float xpos = WIDTH / 8f - inputs.length * BTN_SIZE / 2f - GAP * 3 * ((inputs.length - 1) / 2f);
                // 创建并配置每个输入按钮
                for (int i = 0; i < inputs.length; ++i) {
                    inputs[i] = new ItemButton() {
                        @Override
                        protected void onClick() {
                            super.onClick();
                            // 如果按钮有物品
                            if (item != null) {
                                // 如果物品不能被收集，则将其丢弃
                                if (!item.collect()) {
                                    Dungeon.level.drop(item, hero.pos);
                                } add(title1);
                                remove(title1);
                                item = null;
                                // 清空按钮的物品并更新状态
                                slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
                                updateState();
                            } else {
                                // 如果按钮没有物品，则显示物品选择窗口
                                GameScene.show(WndBag.lastBag(inputSelector));
                            }
                        }
                        @Override
                        protected boolean onLongClick() {
                            // 长按时显示物品信息窗口
                            if (item != null) {
                                Game.scene().addToFront(new WndInfoItem(item));
                                return true;
                            }
                            return false;
                        }
                    };
                    // 设置按钮的位置和大小
                    inputs[i].setRect(WIDTH / 8f - inputs.length * BTN_SIZE / 2f - GAP * 3 * ((inputs.length - 1) / 2f) ,45, BTN_SIZE, BTN_SIZE);
                    xpos += BTN_SIZE + GAP * 3;
                    // 将按钮添加到窗口中
                    add(inputs[i]);
                }
            }

            ew = new RedButton("") {
                Image arrow;

                @Override
                protected void createChildren() {
                    super.createChildren();
                    // 创建箭头图标并添加到按钮中
                    arrow = Icons.get(Icons.ARROW);
                    add(arrow);
                }

                @Override
                protected void layout() {
                    super.layout();
                    // 设置箭头图标的角度和位置
                    arrow.angle = 0;
                    arrow.x = x + (width + arrow.width) / 2f-10;
                    arrow.y = y + (height - arrow.height) / 2f;
                    PixelScene.align(arrow);
                }

                @Override
                public void enable(boolean value) {
                    super.enable(value);
                    // 根据按钮启用状态更新箭头和背景的颜色和透明度
                    if (value) {
                        arrow.tint(1, 1, 0, 1);
                        arrow.alpha(1f);
                        bg.alpha(1f);
                    } else {
                        arrow.color(0, 0, 0);
                        arrow.alpha(0.6f);
                        bg.alpha(0.6f);
                    }
                }

                @Override
                protected void onClick() {
                    super.onClick();
                    if (inputs[0].item!=null) {
                        add(title1);
                        remove(title1);
                        if (inputs[0]!=null) {
                             recipe = EnchRecipe.searchForRecipe(inputs[0].item);
                            if (recipe != null) {
                                ArrayList<Class<? extends Buff>> buffClasses = recipe.getInputClasses();
                                if (!buffClasses.isEmpty()) {
                                    Class<? extends Buff> firstBuffClass = buffClasses.get(0); // 获取第一个 Buff 类
                                    Buff.append(hero, firstBuffClass);
                                    GLog.w(M.L(firstBuffClass, "name"));
                                }
                            }
                        }
                    }
                    // 执行附魔操作
                    inputs[0].item = null;
                    // 清空按钮的物品，并更新状态
                    inputs[0].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
                    updateState();
                    Sample.INSTANCE.play(Assets.Sounds.EVOKE); // 播放附魔音效
                }
            };
            // 设置执行按钮的位置和大小
            ew.setRect(WIDTH / 3f , inputs[0].bottom()/2 - GAP * 2, 27, 18);
            add(ew);
            // 创建并配置执行按钮
            execute = new RedButton("") {
                Image arrow;

                @Override
                protected void createChildren() {
                    super.createChildren();
                    // 创建箭头图标并添加到按钮中
                    arrow = Icons.get(Icons.ARROW);
                    add(arrow);
                }

                @Override
                protected void layout() {
                    super.layout();
                    // 设置箭头图标的角度和位置
                    arrow.angle = 0;
                    arrow.x = x + (width + arrow.width) / 2f-10;
                    arrow.y = y + (height - arrow.height) / 2f;
                    PixelScene.align(arrow);
                }

                @Override
                public void enable(boolean value) {
                    super.enable(value);
                    // 根据按钮启用状态更新箭头和背景的颜色和透明度
                    if (value) {
                        arrow.tint(1, 1, 0, 1);
                        arrow.alpha(1f);
                        bg.alpha(1f);
                    } else {
                        arrow.color(0, 0, 0);
                        arrow.alpha(0.6f);
                        bg.alpha(0.6f);
                    }
                }

                @Override
                protected void onClick() {
                    super.onClick();
                    if (inputs[0].item!=null) {
                        add(title1);
                        remove(title1);
                         recipe = EnchRecipe.searchForRecipe(inputs[0].item);
                        if (recipe != null) {
                            ArrayList<Class<? extends Buff>> buffClasses = recipe.getInputClasses();
                            if (!buffClasses.isEmpty()) {
                                Class<? extends Buff> lastBuffClass = buffClasses.get(buffClasses.size() - 1); // 获取第一个 Buff 类
                                Buff.append(hero, lastBuffClass);
                                GLog.w(M.L(lastBuffClass, "name"));
                            }
                        }
                    }
                    // 执行附魔操作
                    inputs[0].item = null;
                   // 清空按钮的物品，并更新状态
                    inputs[0].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
                    updateState();
                    Sample.INSTANCE.play(Assets.Sounds.EVOKE); // 播放附魔音效
                }
            };

            // 设置执行按钮的位置和大小
            execute.setRect(WIDTH / 3f , inputs[0].bottom() + GAP * 2-13, 27, 18);
            add(execute);

            Image arrow;
            arrow = Icons.get(Icons.ARROW);
            arrow.x=40;
            arrow.y=50;
            add(arrow);

            textButton button1 = new textButton("") {
                @Override
                protected void createChildren() {super.createChildren();}
                @Override
                protected void layout() {super.layout();}
                @Override
                protected void onClick() {
                    super.onClick();
                    if (inputs[0].item!=null) {
                         recipe = EnchRecipe.searchForRecipe(inputs[0].item);
                        if (recipe != null) {
                            ArrayList<Class<? extends Buff>> buffClasses = recipe.getInputClasses();
                            if (!buffClasses.isEmpty()) {
                                Class<? extends Buff> firstBuffClass = buffClasses.get(0); // 获取第一个 Buff 类
                                GameScene.show(new WndInfoBuffe(firstBuffClass));
                            }
                        }
                    }
                }
            };

            textButton button2 = new textButton("") {

                @Override
                protected void createChildren() {super.createChildren();}
                @Override
                protected void layout() {super.layout();}
                @Override
                protected void onClick() {
                    super.onClick();
                    if (inputs[0].item!=null) {
                         recipe = EnchRecipe.searchForRecipe(inputs[0].item);
                        if (recipe != null) {
                            ArrayList<Class<? extends Buff>> buffClasses = recipe.getInputClasses();
                            if (!buffClasses.isEmpty()) {
                                Class<? extends Buff> lastBuffClass = buffClasses.get(buffClasses.size() - 1); // 获取第一个 Buff 类
                                GameScene.show(new WndInfoBuffe(lastBuffClass));
                            }
                        }
                    }
                }
            };
            // 添加按钮并设置位置
            add(button1);
            add(button2);
            button1.setRect(WIDTH / 2f + 14 , inputs[0].bottom() / 2 - 16, 70,30); // Set the class for the first button
            button2.setRect(WIDTH / 2f + 14, inputs[0].bottom() / 2 +20, 70,30); // Set the class for the second button

            // 创建并配置用于选择要附魔物品的按钮
            toEnchant = new ItemButton() {
                @Override
                protected void onClick() {
                    super.onClick();
                    // 如果当前按钮有物品
                    if (item != null||bool) {
                        // 尝试收集物品，如果不能收集，则丢弃物品
                        if (!item.collect()) {
                            Dungeon.level.drop(item, hero.pos);
                        }
                        item = null;
                        // 清空按钮的物品，并更新状态
                        slot.item(new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
                        updateState();
                    } else {
                        // 显示选择武器的窗口
                        GameScene.show(WndBag.lastBag(new WndBag.ItemSelector() {
                            @Override
                            public String textPrompt() {
                                return M.L(Enchanter.class, "select_weapon"); // 提示文本
                            }

                            @Override
                            public boolean itemSelectable(Item item) {
                                return item instanceof MeleeWeapon; // 只允许选择近战武器
                            }

                            @Override
                            public void onSelect(Item item) {
                                if (item != null) {
                                    // 如果选择的物品已装备，提示用户先卸下
                                    if (item.isEquipped(hero)) {
                                        GLog.w(M.L(Enchanter.class, "unequip_first"));
                                        return;
                                    }
                                    // 设置按钮上的物品，并更新状态
                                    toEnchant.item(item.detach(hero.belongings.backpack));
                                    updateState();
                                }
                            }
                        }));
                    }
                }
                @Override
                protected boolean onLongClick() {
                    // 长按时显示物品信息窗口
                    if (item != null) {
                        Game.scene().addToFront(new WndInfoItem(item));
                        return true;
                    }
                    return false;
                }
            };
// 设置选择附魔物品按钮的位置和大小
            toEnchant.setRect(WIDTH / 8f - inputs.length * BTN_SIZE / 2f - GAP * 3 * ((inputs.length - 1) / 2f) ,45, BTN_SIZE, BTN_SIZE);
//            add(toEnchant);
// 重置槽位
            slotReset();

// 创建并配置配方按钮
            IconButton recipeButton = new IconButton() {
                @Override
                protected void onClick() {
                    super.onClick();
                    // 显示附魔信息窗口
                    GameScene.show(new WndInfoInscription());
                }
            };
            Image im = new Image(Assets.Sprites.ITEMS);
            im.frame(ItemSpriteSheet.film.get(ItemSpriteSheet.ALCH_PAGE)); // 设置配方图标
            recipeButton.icon(im);
            add(recipeButton);
// 设置配方按钮的位置和大小
            recipeButton.setRect(0, height - GAP - 12, 12, 12);

        }

        // 执行附魔操作
        private void doEnchant(EnchRecipe recipe) {
            // 检查要附魔的物品是否是武器
            if (!(toEnchant.item instanceof Weapon)) {
                GLog.w(M.L(Enchanter.class, "illegal_receiver"));
                return;
            }

            synchronized (inputs) {
                // 遍历输入槽位
                for (int i = 0; i < inputs.length; i++) {
                    if (inputs[i] != null && inputs[i].item != null) {
                        // 减少物品数量
                        inputs[i].item.quantity(inputs[i].item.quantity() - 1);
                        // 如果物品数量为0，清空槽位
                        if (inputs[i].item.quantity() <= 0) {
                            inputs[i].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
                            inputs[i].item = null;
                        } else {
                            inputs[i].slot.item(inputs[i].item);
                        }
                    }
                }
            }
            // 处理要附魔的物品
            Item item = toEnchant.item;
            if (item != null) {
                if (!item.collect()) {
                    Dungeon.level.drop(item, hero.pos);
                }
            }
            toEnchant.slot.item(new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
            updateState();
        }

        // 重置所有槽位的状态
        private void slotReset() {
            synchronized (inputs) {
                for (int i = 0; i < inputs.length; ++i) {
                    Item item = inputs[i].item;
                    if (item != null) {
                        if (!item.collect()) {
                            Dungeon.level.drop(item, hero.pos);
                        }
                    }
                    inputs[i].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
                }
            }
            Item item = toEnchant.item;
            if (item != null) {
                if (!item.collect()) {
                    Dungeon.level.drop(item, hero.pos);
                }
            }
            toEnchant.slot.item(new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
        }

        // 更新按钮状态
        private void updateState() {
            ArrayList<Class<? extends Item>> classes = new ArrayList<>();
            for (ItemButton itb : inputs) {
                if (itb.item != null) {
                    classes.add(itb.item.getClass());
                }
            }
        }
        @Override
        public void onBackPressed() {
            slotReset(); // 重置所有槽位
            super.onBackPressed(); // 调用父类方法处理其他逻辑
        }

        // 输入选择器
        protected WndBag.ItemSelector inputSelector = new WndBag.ItemSelector() {
            @Override
            public String textPrompt() {
                return M.L(Enchanter.class, "select_ingredient"); // 提示文本
            }

            @Override
            public boolean itemSelectable(Item item) {
                return item instanceof MeleeWeapon; // 只允许选择近战武器
            }
            @Override
            public void onSelect(Item item) {
                synchronized (inputs) {
                    if (item != null && inputs[0] != null) {
                        if (item.isEquipped(hero)) {
                            GLog.w(M.L(Enchanter.class, "unequip_first")); // 提示用户先卸下装备
                            return;
                        }

                        // 将选择的物品放入第一个空槽位
                        for (int i = 0; i < inputs.length; i++) {
                            if (inputs[i].item == null) {
                                inputs[i].item(item.detach(hero.belongings.backpack));
                                break;
                            }
                        }

                        EnchRecipe recipe = EnchRecipe.searchForRecipe(item);
                        if (recipe != null) {
                            ArrayList<Class<? extends Buff>> buffClasses = recipe.getInputClasses();
                            if (!buffClasses.isEmpty()) {
                                // 清除之前的标题
                                if (title1 != null) {
                                    remove(title1);
                                }

                                // 创建一个新的文本块以显示所有 Buff
                                StringBuilder buffText = new StringBuilder();
                                for (Class<? extends Buff> buffClass : buffClasses) {
                                    buffText.append(M.L(buffClass, "name")).append("\n\n\n\n\n");
                                }
                                title1 = PixelScene.renderTextBlock(buffText.toString(), 7);
                                title1.setPos(WIDTH / 2f + 25, inputs[0].bottom() / 2 - 4);
                                add(title1);

                            }
                        }
                        updateState();
                    }
                }
            }
        };


        public class WndInfoInscription extends Window {
            private static final int WIDTH = 120;
            private static final int HEIGHT = 160;
            private static final int BTN_HEIGHT = 16;
            private static final int GAP = 4;
            private static final int TEXT_SIZE = 6;

            private final ArrayList<Float> upperBoundary;
            private final ArrayList<Float> bottomBoundary;

            private final ArrayList<Integer> triggers;
            private ScrollPane sp;
            public WndInfoInscription(){
                super(0, 0, Chrome.get(Chrome.Type.WINDOW_SILVER));
                upperBoundary = new ArrayList<>(50);
                bottomBoundary = new ArrayList<>(50);

                triggers = new ArrayList<>(50);
                resize(WIDTH, HEIGHT);
                sp = new ScrollPane(new Component()){
                    @Override
                    public void onClick(float x, float y) {
                        super.onClick(x, y);

                    }
                };
                add(sp);
                Component content = sp.content();
                float pos = GAP * 2;
                RenderedTextBlock hint = PixelScene.renderTextBlock(M.L(Enchanter.class, "hint")+"\n\n", TEXT_SIZE);
                hint.maxWidth(WIDTH);
                content.add(hint);
                hint.setPos(0, pos);
                PixelScene.align(hint);
                pos += GAP + hint.height();
                int i = 1;

                    StringBuilder sb = new StringBuilder();
                    sb.append(i);
                    sb.append(')');

                    sb.append("\n");

                    sb.append('\n');
                    RenderedTextBlock rtb = PixelScene.renderTextBlock(sb.toString(), TEXT_SIZE);
                    rtb.maxWidth(WIDTH - 2);
                    rtb.setPos(0, pos);
                    PixelScene.align(rtb);
                    pos += rtb.height() + TEXT_SIZE;
                    ++i;


                    content.add(rtb);
                }


                RedButton pageUp = new RedButton(M.L(Enchanter.class, "page_up"), 9){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        sp.scrollTo(0, Math.max(sp.content().camera().scroll.y - sp.height() + TEXT_SIZE, 0));
                    }
                };


                RedButton pageDown = new RedButton(M.L(Enchanter.class, "page_down"), 9){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        sp.scrollTo(0, Math.min(sp.content().camera().scroll.y + sp.height() - TEXT_SIZE, sp.content().height() - sp.height()));
                    }
                };


            }



            public class WndFastEnchantInfo extends Window{
                private static final int WIDTH = 120;
                private static final int MARGIN = 4;
                public WndFastEnchantInfo(String text, ArrayList<Class<? extends Item>> recipe){
                    super();

                    ArrayList<Class<? extends Item>> lack = new ArrayList<>();
                    Belongings inv = hero.belongings;
                    for (Class<? extends Item> finding : recipe){
                        boolean found = false;
                        for(Item item: inv){
                            if(item.getClass().equals(finding)){
                                found = true;
                                break;
                            }
                        }
                        if(!found){
                            lack.add(finding);
                        }
                    }

                    if(lack.isEmpty()) {
                        text += M.L(Enchanter.class, "can_enchant");
                    }else {
                        text += M.L(Enchanter.class, "lack_item");
                        StringBuilder textBuilder = new StringBuilder(text);
                        for (Class<? extends Item> lackClass : lack) {
                            textBuilder.append(Reflection.newInstance(lackClass).trueName());
                            textBuilder.append("  ");
                        }
                        text = textBuilder.toString();
                    }

                    RenderedTextBlock info = PixelScene.renderTextBlock( text, 6 );
                    info.maxWidth(WIDTH - MARGIN * 2);
                    info.setPos(MARGIN, MARGIN);
                    add( info );

                    RedButton rb = new RedButton(M.L(Enchanter.class, "quick_enchant")){
                        @Override
                        protected void onClick() {
                            super.onClick();
                            slotReset();

                            Belongings inventory = hero.belongings;
                            int curslot = 0;
                            for (Class<? extends Item> finding : recipe){
                                for(Item item: inventory){
                                    if(item.getClass().equals(finding)){
                                        Item toUse = item.detach(inventory.backpack);
                                        inputs[curslot].item(toUse);
                                        ++curslot;
                                        break;
                                    }
                                }
                            }

                            updateState();
                            onBackPressed();

                        }
                    };
                    add(rb);
                    rb.setRect(MARGIN, info.bottom() + MARGIN, WIDTH - 2 * MARGIN, 18);
                    rb.active = lack.isEmpty();
                    rb.alpha(lack.isEmpty()?1f:0.4f);

                    resize(WIDTH, (int)rb.bottom() + MARGIN );
                }
            }

        }

        /*
        protected WndBag.ItemSelector inputSelector = item -> {
            synchronized (inputs) {
                if (item != null && inputs[0] != null) {
                    if(item.isEquipped(Dungeon.hero)){
                        GLog.w(M.L(Enchanter.class, "unequip_first"));
                        return;
                    }
                    for (int i = 0; i < inputs.length; i++) {
                        if (inputs[i].item == null) {
                            inputs[i].item(item.detach(Dungeon.hero.belongings.backpack));
                            break;
                        }
                    }
                    updateState();
                }
            }
        };#

         */
    }

    class ItemButton extends Component {

        protected NinePatch bg;
        protected ItemSlot slot;

        public Item item = null;

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
                    ItemButton.this.onClick();
                }

                @Override
                protected boolean onLongClick() {
                    return ItemButton.this.onLongClick();
                }
            };
            slot.enable(true);
            add( slot );
        }

        protected void onClick() {}
        protected boolean onLongClick() {
            return false;
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size( width, height );

            slot.setRect( x + 2, y + 2, width - 4, height - 4 );
        }

        public void item( Item item ) {
            slot.item( this.item = item );
        }
        private void showInformation(String info) {
            // 弹窗或其他显示方式
            GLog.i(info);
        }

    }
    // 定义一个继承自 Component 的 ItemButton 类
class textButton extends Component {

    // 背景的 NinePatch 对象，用于显示按钮的背景
    protected NinePatch bg;
    // 一个 ItemSlot 对象，用于显示和操作物品
    protected ItemSlot slot;

    // 当前按钮显示的物品，初始值为 null
    public Item item = null;

        public textButton(String s) {
        }

        // 重写父类的 createChildren 方法，初始化子组件
    @Override
    protected void createChildren() {
        super.createChildren(); // 调用父类的 createChildren 方法

        // 获取一个红色按钮的背景 NinePatch 对象
        bg = Chrome.get(Chrome.Type.TOAST);
        // 将背景添加到按钮中
        add(bg);

        // 创建一个新的 ItemSlot 对象
        slot = new ItemSlot() {
            @Override
            protected void onPointerDown() {
                // 当指针按下时，增加背景亮度，并播放点击声音
                bg.brightness(1.2f);
                Sample.INSTANCE.play(Assets.Sounds.CLICK);
            }

            @Override
            protected void onPointerUp() {
                // 当指针抬起时，重置背景颜色
                bg.resetColor();
            }

            @Override
            protected void onClick() {
                // 调用 ItemButton 实例的 onClick 方法
                textButton.this.onClick();
            }

            @Override
            protected boolean onLongClick() {
                // 调用 ItemButton 实例的 onLongClick 方法，并返回结果
                return textButton.this.onLongClick();
            }
        };
        // 启用 ItemSlot 对象
        slot.enable(true);
        // 将 ItemSlot 添加到按钮中
        add(slot);
    }

    // 点击事件的处理方法，子类可以重写以实现自定义行为
    protected void onClick() {}

    // 长按事件的处理方法，子类可以重写以实现自定义行为，默认返回 false
    protected boolean onLongClick() {
        return false;
    }

    // 布局调整方法，设置背景和 ItemSlot 的位置和大小
    @Override
    protected void layout() {
        super.layout(); // 调用父类的 layout 方法

        // 设置背景的位置和大小，使其填充整个按钮区域
        bg.x = x;
        bg.y = y;
        bg.size(width, height);

        // 设置 ItemSlot 的位置和大小，留出内边距
        slot.setRect(x + 2, y + 2, width - 4, height - 4);
    }

    // 设置按钮显示的物品，并将其传递给 ItemSlot
    public void item(Item item) {
        slot.item(this.item = item);
    }

    // 显示信息的方法，当前实现为日志打印
    private void showInformation(String info) {
        // 弹窗或其他显示方式的实现可以在这里添加
        GLog.i(info);
    }
}
class WndInfoBuffe extends Window {

    private static final float GAP	= 2;

    private static final int WIDTH = 120;

    public WndInfoBuffe(Class<? extends Buff>  buff){
        super();

        IconTitle titlebar = new IconTitle();


        String buffText = M.L(buff, "name");
        String buffText1 = M.L(buff, "desc");

        titlebar.label( Messages.titleCase(buffText), Window.TITLE_COLOR );
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        RenderedTextBlock txtInfo = PixelScene.renderTextBlock(buffText1, 6);
        txtInfo.maxWidth(WIDTH);
        txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2*GAP);
        add( txtInfo );

        resize( WIDTH, (int)txtInfo.bottom() + 2 );
    }

}


