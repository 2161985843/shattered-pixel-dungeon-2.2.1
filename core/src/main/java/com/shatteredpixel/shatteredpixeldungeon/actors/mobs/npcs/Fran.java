package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.effects.AngryHeadParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.Die;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.pistol;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FranSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndPicture;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
/**
*芙兰
 */
public class Fran extends NPC {

    {
        spriteClass = FranSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    public boolean hasgivenitems = false;

    @Override
    protected boolean act() {
        throwItem();
        return super.act();
    }
    public static int ritualPos;
    @Override
    public int defenseSkill( Char enemy ) {
        return 0;
    }

    public static void spawnWandmaker(Level level, Room room ) {
        if (Dungeon.depth==5) {

            Fran npc = new Fran();
            boolean validPos;
            //Do not spawn wandmaker on the entrance, a trap, or in front of a door.
            do {
                validPos = true;
                npc.pos = level.pointToCell(room.random());
                if (npc.pos == level.entrance()){
                    validPos = false;
                }
                for (Point door : room.connected.values()){
                    if (level.trueDistance( npc.pos, level.pointToCell( door ) ) <= 1){
                        validPos = false;
                    }
                }
                if (level.traps.get(npc.pos) != null){
                    validPos = false;
                }
            } while (!validPos);
            level.mobs.add( npc );

        }
    }
    @Override
    public void damage( int dmg, Object src ) {

        ArrayList<Integer> candidates = new ArrayList<>(); // 创建一个用于存储候选位置的列表
        for (Mob m: Dungeon.level.mobs){
            if(m instanceof Fran){
                candidates.add(m.pos);
            }
        }
        int[] neighbours = {pos , pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
        for (int n : neighbours) { // 遍历相邻位置
            if (!Dungeon.level.solid[n]

                    && (Dungeon.level.passable[n] || Dungeon.level.avoid[n])
                    && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[n])) {
//                candidates.add(n); // 将符合条件的相邻位置加入候选列表
            }
        }
        if (candidates.size() > 0) { /// 在新位置生成新怪物的逻辑
            Swarm clone = new Swarm();
            destroy();
            sprite.killAndErase();
            // 在商店管理员的位置上发射粒子效果
            CellEmitter.get(pos).burst(AngryHeadParticle.FACTORY, 1);
            clone.pos = Random.element(candidates);
            GameScene.add(clone, 1f); // 添加克隆角色到游戏场景中（延迟添加）
        }

    }

    @Override
    public boolean add(Buff buff ) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }
  	private void tell( String text ) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show( new WndQuest( Fran.this, text ));
            }
        });
    }  private void showWindow(final String messageKey) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                // 创建一个包含图片和文本的窗口，并显示
                GameScene.show(new WndPicture(Assets.Sprites.EYE00, 0.2F, Messages.get(Fran.class, messageKey)));
//                GameScene.show(new Enchanter.WndEnchant());
            }
        });
    }
    int day = 1;
    public boolean interact(Char c) {

            switch (day) {
                case 1:
                    showWindow("fran_1");
                    this.day++;
                    break;
                case 2:
                    this.day++;
                    showWindow("fran_2");
                    break;
                case 3:
                    this.day++;
                    showWindow("fran_3");
                    break;
                default:
                    yell(  Messages.get(this, "fran_4") );
            }

        if (c != Dungeon.hero){
            return true;
        }

        boolean scissors = false;
        boolean tincture = false;




        if(!hasgivenitems){
            Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.FOOD ) ), Dungeon.hero.pos );
            for (Item i : Dungeon.hero.belongings.backpack){
                if(i instanceof Die){
                    scissors = true;
                }
            }
            for (Item i : Dungeon.hero.belongings.backpack){
                if(i instanceof pistol){
                    tincture = true;
                }
            }
            if(scissors == false ){
                new Sai().collect();
            }
            if(tincture == false){
                new pistol().collect();
            }
            hasgivenitems = true;

        return true;
    }
        return false;
    }
    private  final String DAY	= "day";
    private static final String HASGIVENITEMS = "hasgivenitems";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( HASGIVENITEMS, hasgivenitems );
        bundle.put( DAY, day );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        hasgivenitems = bundle.getBoolean(HASGIVENITEMS);
        day = bundle.getInt(DAY);
    }

}
