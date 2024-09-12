package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.IronTree;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Fran;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.LevelTeleporter;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.MobAttributeViewer;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.generator.LazyTest;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.generator.TestMelee;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.generator.Testgrimm;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.Die;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.grimm.Fusiliers;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;

public class RuinsZeroLevel  extends Level {

    private static final int SIZE = 5;


    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_PRISON;
    }
    private static final short G = Terrain.CUSTOM_DECO;
    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }
    private static final int[] pre_map = {
            49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,
            49,7,1,1,1,1,1,1,1,1,1,1,1,49,1,4,4,4,         49,
            49,1,1,1,1,1,1,1,1,1,1,1,1,49,1,4,4,4,         49,
            49,1,1,1,1,1,1,1,1,1,1,1,1,10,1,49,49,49,      49,
            49,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6,          49,
            49,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,49,
            49,49,49,49,49,49,1,1,1,1,1,1,1,49,49,49,49,49, 49,
            49,3,1,3,1,49,1,1,1,1,1,1,1,49,1,1,1,1,49,
            49,3,1,3,1,2,1,1,1,1,1,1,1,2,1,1,1,1,   49,
            49,1,1,1,1,49,1,1,1,1,1,1,1,49,1,1,1,1, 49,
            49,3,1,3,1,49,1,1,1,1,1,1,1,49,1,1,1,1,  49,
            49,49,49,49,49,49,1,1,1,1,1,1,1,49,49,49,49,49,49,
            49,1,1,1,1,49,1,1,1,1,1,1,1,49,51,51,51,51,49,
            49,1,1,1,1,49,1,1,1,1,1,1,1,49,1,1,1,1,49,
            49,1,1,1,1,2,1,1,1,1,1,1,1,  2,1,5,1,1,49,
            49,1,1,1,1,49,1,1,1,1,1,1,1,49,1,1,1,1,49,
            49,1,1,1,1,49,1,1,1,1,1,1,1,49,1,1,1,1,49,
            49,1,1,1,1,49,1,1,1,1,1,1,1,49,1,1,1,1,49,
            49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49};
    @Override
    public int randomRespawnCell( Char ch ) {
        return width();
    }
    public LevelTransition getTransition(LevelTransition.Type type){
        // 如果转换对象列表为空，则返回 null
        if (transitions.isEmpty()){
            return null;
        }
        // 遍历转换对象列表中的每一个对象
        for (LevelTransition transition : transitions){
            // 如果没有指定类型，优先选择任何一种入口类型的转换对象
            if (type == null &&
                    (transition.type == LevelTransition.Type.REGULAR_ENTRANCE || transition.type == LevelTransition.Type.CUS)){
                return transition;
            }
            // 如果指定了类型，并且当前转换对象的类型与指定类型匹配，则返回该转换对象
            else if (transition.type == type){
                return transition;
            }
        }
        // 如果以上条件都不满足，根据是否指定了类型来决定下一步操作
        // 如果指定了类型，则递归调用 getTransition(null) 方法，继续寻找任意类型的转换对象并返回
        // 如果未指定类型，则返回转换对象列表中的第一个对象
        return type != null ? getTransition(null) : transitions.get(0);
    }
    @Override
    protected boolean build() {
        setSize(19, 19);
        int exitCell = 19*4+17;
        int exitCe = 19*1+1;
        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

       entrance = 19*6+2;
        int enterCll = 19*7+2;
        int enterCell = 19*1+1;
        LevelTransition enterE = new LevelTransition(this, enterCll, LevelTransition.Type.CUS);
        transitions.add(enterE);
        LevelTransition enter = new LevelTransition(this, enterCell, LevelTransition.Type.SURFACE);
        transitions.add(enter);

        CustomTilemap via = new townBehind();
        via.pos(0, 0);
        customTiles.add(via);
//        CustomTilemap vis = new townAbove();
//        vis.pos(0, 0);
//        customTiles.add(vis);
//        for (int i = 0; i < 8; i++) {
//            Item item = new ScrollOfUpgrade();
//            drop(item, this.width * 18 + 17); // 将卷轴放置在不同的位置，这里可能需要根据具体情况调整位置
//        }
//        Die c = new Die();

//        drop(c, this.width * 18 + 18); //
//
//        LazyTest n = new LazyTest();
//        drop(n, this.width * 18 + 18); //
//        Item item = new Spear();
//        drop(item, this.width * 18 + 18); //

        for (int map = 0; map < this.map.length; map++) this.map[map] = mapToTerrain(pre_map[map]);
        return true;
    }
    private int mapToTerrain(int code){
        switch (code){
            case 1:
            default:
                return Terrain.EMPTY;//地板
            case 0:
                return Terrain.EMPTY_SP;//特殊空地
            case 2:
                return Terrain.DOOR;//门
            case 3:
                return Terrain.RUINS;//废墟
            case 4:
                return Terrain.CHASM;//悬崖
            case 5:
                return Terrain.ALCHEMY;// 炼金术
            case 6:
                return Terrain.EXIT;//16,exit 就是出口
            case 7:
                return Terrain.ENTRANCE;//17,ENTRANCE 就是楼梯
            case 9:
                return Terrain.SECRET_DOOR;// 就是楼梯
            case 10:
                return Terrain.SIGN;// 血迹
            case 66:
                return Terrain.GRASS;
            case 51:
                return Terrain.BOOKSHELF;//书
            case 49:
                return Terrain.WALL;//48,WALL 就是墙壁

        }
    }

    public static class townAbove extends CustomTilemap{

        {
            texture = Assets.Environment.RUINS_ZEROA;
            tileH=19;
            tileW=19;
        }

        int TEX_WIDH=19*16;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = mapSimpleImage(0,0,TEX_WIDH);

            v.map( data, tileW );
            return v;
        }

    }
    public static class townBehind extends CustomTilemap{

        {
            texture = Assets.Environment.RUINS_ZEROA;
            tileH=19;
            tileW=19;
        }

        int TEX_WIDH=19*16;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = mapSimpleImage(0,0,TEX_WIDH);

            v.map( data, tileW );
            return v;
        }

    }
    public void playLevelMusic() {
        if (branch == 0){
            if (BossHealthBar.isBleeding()){
                Music.INSTANCE.play(Assets.Music.CAVES_BOSS_FINALE, true);
            } else {
                Music.INSTANCE.play(Assets.Music.CAVES_BOSS, true);
            }
            //if wall isn't broken
        } else if (map[14 + 13*width()] == Terrain.CUSTOM_DECO){
            Music.INSTANCE.end();
        } else {
            Music.INSTANCE.playTracks(CavesLevel.CAVES_TRACK_LIST, CavesLevel.CAVES_TRACK_CHANCES, false);
        }
    }
    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
        IronTree n = new IronTree();
        n.pos = (this.width * 16 + 15);
        mobs.add(n);
        Fran E = new Fran();
        E.pos = (this.width * 16 + 10);
        mobs.add(E);

    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {

//       Item item = Bones.get();
//        if (item != null) {
//          drop( item, 32*12+30 ).setHauntedIfCursed().type = Heap.Type.REMAINS;
//        }
    }

//
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.RUINS:
                return Messages.get(CavesLevel.class, "ruins_name");

            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( int tile ) {
        switch (tile) {
            case Terrain.RUINS:
                return super.tileDesc( tile ) + "\n\n" + Messages.get(CavesLevel.class, "ruins_desc");

            default:
                return super.tileDesc( tile );
        }
}
}

