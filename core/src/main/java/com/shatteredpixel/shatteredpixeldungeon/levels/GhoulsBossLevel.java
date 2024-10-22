package com.shatteredpixel.shatteredpixeldungeon.levels;


import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel.mainArena;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.updateMap;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GhoulLord;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pylon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne.GooMini;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Fran;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Group;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GhoulsBossLevel extends Level {

    private static final int SIZE = 5;


    {
        color1 = 0x48763c; // 定义颜色1
        color2 = 0x59994a; // 定义颜色2
    }

    private static final short G = Terrain.CUSTOM_DECO;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }
    public static boolean boosSummon = false; //
    @Override
    public String waterTex() {
        // 根据boss的状态返回不同的水纹理
        if (boosSummon) {
            return Assets.Environment.WATER_GHOULS; // 返回新的水纹理
        } else {
            return Assets.Environment.WATER_HALLS; // 返回原始水纹理
        }
    }

    // 新方法：切换水纹理状态
    public static void toggleWaterTexture() {

        boosSummon = !boosSummon; // 切换boosSummon的状态
    }
    private static final int[] pre_map = {
        48,	48,	48,	48, 48, 48, 48, 48, 48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48, 48, 48, 48, 48,	48,	48,	48,	48,	0,	0,	0,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48, 48,
        48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	0,	6,	0,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	0,	0,	0,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	0,	0,	0,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	0,	0,	48,	0,	0,	0,	48,	0,	0,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	0,	0,	48,	0,	0,	0,	48,	0,	0,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	0,	0,	48,	0,	0,	0,	48,	0,	0,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	0,	0,	9,	0,	0,	0,	9,	0,	0,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	0,	0,	48,	0,	0,	0,	48,	0,	0,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	47,	47,	48,	47,	8,	47,	48,	47,	47,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	8,	8,	8,	48,	0,	0,	0,	0,	0,	66,	0,	0,	0,	66,	0,	0,	0,	0,	0,	48,	8,	8,	8,	48,
        48,	8,	8,	8,	48,	8,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	48,	8,	8,	8,	48,
        48,	8,	8,	8,	47,	0,	0,	0,	0,	0,	66,	0,	0,	0,	66,	0,	0,	0,	0,	0,	47,	8,	8,	8,	48,
        48,	8,	8,	8,	47,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	9,	8,	8,	8,	48,
        48,	8,	8,	8,	47,	0,	0,	0,	66,	0,	0,	0,	0,	0,	0,	0,	66,	0,	0,	0,	47,	8,	8,	8,	48,
        48,	8,	8,	8,	48,	0,	0,	0,	0,	0,	0,	15,	15,	15,	0,	0,	0,	0,	0,	0,	48,	8,	8,	8,	48,
        48,	8,	8,	8,	48,	0,	0,	66,	0,	0,	0,	15,	3,	15,	0,	0,	0,	66,	0,  0,	48,	8,	8,	8,	48,
        48,	48,	48,	48,	48,	0,	0,	0,	0,	0,	0,	15,	15,	15,	0,	0,	0,	0,	0,  0,	48,	48,	48,	48, 48,
        48,	48,	48,	48,	48,	0,	0,	0,	66,	0,	0,	0,	0,	0,	0,	0,	66,	0,	0,	0,	48,	48,	48,	48,	48,
        48,	8,	8,	8,	48,	0,	0,	0,	0,	0,	0,	0,	0, 	0,	0,	0,	0,	0,	0,	0,	48,	8,	8,	8,	48,
        48,	8,	8,	8,	48,	8,	0,	0,	0,	0,  66,	0,	0,  0,  66,	0,	0,	0,	0,	0,	48,	8,	8,	8,	48,
        48,	8,	8,	8,	47,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	47,	8,	8,	8,	48,
        48,	8,	8,	8,	9,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	9,	8,	8,	8,	48,
        48,	8,	8,	8,	47,	0,	0,	0,	0,	0,	66,	0,	0,	0,	66,	0,	0,	0,	0,	0,	47,	8,	8,	8,	48,
        48,	8,	8,	8,	48,	8,	0,	0,	0,	66,	0,	0,	0,	0,	0,	66,	0,	0,	0,	0,	48,	8,	8,	8,	48,
        48,	8,	8,	8,	48,	0,	0,	0,	66,	0,	0,	0,	0,	0,	0,	0,	66,	0,	0,	0,	48,	8,	8,	8,	48,
        48,	48,	48,	48,	48,	66,	0,	66,	0,	0,	0,	0,	0,	0,	0,	0,	0,	66,	0,	66,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	9,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	0,	0,	0,	0,	48,	0,	0,	0,	0,	0,	48,	0,	0,	0,	0,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	0,	0,	0,	0,	9,	0,	0,	7,	0,	0,	9,	0,	0,	0,	0,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	0,	0,	0,	0,	48,	0,	0,	0,	0,	0,	48,	0,	0,	0,	0,	48,	48,	48,	48,	48,
        48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,	48,
};
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
    }// 定义可用的水源类型数组
    private static final Class<?>[] WATERS = {
            WaterOfAwareness.class, // 觉醒之水
            WaterOfHealth.class     // 健康之水
    };

    // 用于覆盖默认水源的字段，初始值为 null
    public Class<? extends WellWater> overrideWater = null;
    @Override
    protected boolean build() {
        setSize(25, 33);
        int exitCell = 32*3+13;
//        int exitCe = 32*3+13;
        LevelTransition exit = new LevelTransition(this, 25*2 +12, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(exit);

        entrance = 32*6+2;
//        int enterCll = 19*7+2;
//        int enterCell = 32*30+13;
//        LevelTransition enterE = new LevelTransition(this, enterCll, LevelTransition.Type.CUS);
//        transitions.add(enterE);
        LevelTransition enter = new LevelTransition(this, 25*30+12, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(enter);

        CustomTilemap via = new GhoulsBossLevel.townAbove();
        via.pos(0, 0);
        customTiles.add(via);

//        CustomTilemap vis = new townAbove();
//        vis.pos(0, 0);
//        customTiles.add(vis);
//        for (int i = 0; i < 8; i++) {
//            Item item = new ScrollOfUpgrade();
//            drop(item, this.width * 18 + 17); // 将卷轴放置在不同的位置，这里可能需要根据具体情况调整位置
//        }


        @SuppressWarnings("unchecked")
        Class<? extends WellWater> waterClass =
                overrideWater != null ?
                        overrideWater :
                        (Class<? extends WellWater>) Random.element( WATERS );


        WellWater.seed(437, 1, waterClass, this);

//
//        LazyTest n = new LazyTest();
//        drop(n, this.width * 18 + 18); //
//        Item item = new Spear();
//        drop(item, this.width * 18 + 18); //

        for (int map = 0; map < this.map.length; map++) this.map[map] = mapToTerrain(pre_map[map]);
        return true;
    }

    private int mapToTerrain(int code) {
        switch (code) {
            case 0:
            default:
                return Terrain.EMPTY; // 地板，表示空地
            case 72:
                return Terrain.RUINS; // 特殊空地，例如废墟
            case 9:
                return Terrain.DOOR; // 门
             case 3:
                 return Terrain.WELL ; // 废墟（已注释）
            case 8:
                return Terrain.WATER; // 水体或悬崖
            case 15:
                return Terrain.PEDESTAL ; // 炼金术相关区域
            case 7:
                return Terrain.EXIT; // 出口
            case 6:
                return Terrain.ENTRANCE; // 楼梯
            case 16:
                return Terrain.SECRET_DOOR; // 秘密门
            case 10:
                return Terrain.SIGN; // 血迹标记
            case 66:
                return Terrain.CUSTOM_PLOTS ; // 草地
            case 47:
                return Terrain.RUINS; // 书籍或废墟
            case 48:
                return Terrain.WALL; // 墙壁
        }

    }

    public static class townAbove extends CustomTilemap{

        {
            texture = Assets.Environment.GHOULS_LEVEL;
            tileW=25;
            tileH=33;

        }

        int TEX_WIDH=25*16;

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
            texture = Assets.Environment.GHOULS_LEVEL;
            tileH=33;
            tileW=25;
        }

        int TEX_WIDH=25*33;

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

        GhoulLord n = new GhoulLord();
        n.pos = (this.width * 25 + 12);
        mobs.add(n);
        Fran E = new Fran();
        E.pos = (this.width * 30 + 17);
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


    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.RUINS:
                return Messages.get(this, "summoning_name");
            case Terrain.STATUE:
                return Messages.get(this, "statut_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( int tile ) {
        switch (tile) {
            case Terrain.RUINS:
                return super.tileDesc( tile ) + "\n\n" + Messages.get(this, "summoning_desc");
            case Terrain.STATUE:
                return Messages.get(this, "statut_name");
            default:
                return super.tileDesc( tile );
        }
    }
    public void activatePylon(){
        if (GhoulLord.bossSD) {
            for (int i = (mainArena.top - 1) * width; i < length; i++) {
                if (map[i] == Terrain.INACTIVE_TRAP || map[i] == Terrain.WATER || map[i] == Terrain.CUSTOM_DECO) {
                    GameScene.add(Blob.seed(i, 1, GhoulsBossLevel.PylonEnergy.class));
                }
            }
        }
    }	public void eliminatePylon(){
            blobs.get(GhoulsBossLevel.PylonEnergy.class).fullyClear();

    }
    public static class PylonEnergy extends Blob {

        // 演化方法，负责更新能量状态
        @Override
        protected void evolve() {
            // 遍历所有单元格
            for (int cell = 0; cell < Dungeon.level.length(); cell++) {
                // 检查单元格是否在地图内部
                if (Dungeon.level.insideMap(cell)) {
                    off[cell] = cur[cell]; // 将当前状态赋值给 off

                    // 立即扩散到水单元格
                    if (off[cell] == 0 && Dungeon.level.water[cell]) {
                        off[cell]++; // 如果是水单元格，则将 off[cell] 增加
                    }

                    volume += off[cell]; // 更新能量体积

                    // 如果 off[cell] 大于 0
                    if (off[cell] > 0) {
                        // 找到当前单元格中的角色
                        Char ch = Actor.findChar(cell);
                        // 如果角色存在，且不是 DM300 实例，且不是飞行状态
                        if (ch != null && !(ch instanceof GhoulLord) && !ch.flying) {
                            Sample.INSTANCE.play(Assets.Sounds.LIGHTNING); // 播放闪电声
                            // 造成随机范围内的电击伤害
                            ch.damage(Random.NormalIntRange(1, 2), Electricity.class);
                            ch.sprite.flash(); // 闪烁角色的精灵
                            boolean hasBuff = true;
                            for (Buff buff : hero.buffs()) {
                                if (buff.getClass() == Chill.class) {
                                    hasBuff=false;
                                    break;
                                }
                            }
                            if (hasBuff){
                                Buff.append(hero, Chill.class,2);
                            }else {
                                Buff.prolong(hero, Chill.class,5);
                            }
                            // 如果是英雄角色
                            if (ch == Dungeon.hero) {
                                // 如果能量源精灵存在且是 PylonSprite 实例
                                if (energySourceSprite != null && energySourceSprite instanceof GooSprite) {
                                    // 英雄在 DM-300 超级充能期间受到伤害
                                    Statistics.qualifiedForBossChallengeBadge = false; // 不符合挑战徽章资格
                                }
                                Statistics.bossScores[2] -= 200; // 减少挑战分数
                                // 如果角色不再存活
                                if (!ch.isAlive()) {
                                    Dungeon.fail(GhoulLord.class); // 失败处理
                                    GLog.n(Messages.get(Electricity.class, "ondeath")); // 记录死亡消息
                                }
                            }
                        }
                    }
                }
            }
        }

        // 完全清除方法
        @Override
        public void fullyClear() {
            super.fullyClear(); // 调用父类方法
            energySourceSprite = null; // 清除能量源精灵
        }

        // 静态变量，存储能量源精灵
        private static CharSprite energySourceSprite = null;

        // 静态工厂，用于生成定向火花
        private static Emitter.Factory DIRECTED_SPARKS = new Emitter.Factory() {
            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                // 如果能量源精灵为 null，则寻找合适的精灵
                if (energySourceSprite == null) {
                    for (Char c : Actor.chars()) {
                        if (c instanceof GooMini && c.alignment != Char.Alignment.NEUTRAL) {
                            energySourceSprite = c.sprite; // 找到 Pylon 的精灵
                            break;
                        } else if (c instanceof GhoulLord) {
                            energySourceSprite = c.sprite; // 找到 DM300 的精灵
                        }
                    }
                    // 如果仍然为 null，返回
                    if (energySourceSprite == null) {
                        return;
                    }
                }

                // 生成火花粒子
                SparkParticle s = ((SparkParticle) emitter.recycle(SparkParticle.class));
                s.resetStatic(x, y); // 重置位置

                // 计算速度，指向能量源
                s.speed.set((energySourceSprite.x + energySourceSprite.width / 2f) - x,
                        (energySourceSprite.y + energySourceSprite.height / 2f) - y);
                s.speed.normalize().scale(DungeonTilemap.SIZE * 2f); // 标准化速度并放大

                // 微调火花的位置，使其不偏离单元格
                s.x -= s.speed.x / 8f;
                s.y -= s.speed.y / 8f;
            }

            @Override
            public boolean lightMode() {
                return true; // 开启光照模式
            }
        };

        // 获取瓦片描述
        @Override
        public String tileDesc() {
            return Messages.get(CavesBossLevel.class, "energy_desc"); // 返回能量描述
        }

        // 使用能量
        @Override
        public void use(BlobEmitter emitter) {
            super.use(emitter); // 调用父类方法
            energySourceSprite = null; // 清除能量源精灵
            emitter.pour(Speck.factory(Speck.BLACK_WATER), 2.125f); // 生成定向火花
        }
    }

}