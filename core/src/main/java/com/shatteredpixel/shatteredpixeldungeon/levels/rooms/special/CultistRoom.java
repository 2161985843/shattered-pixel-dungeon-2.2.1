package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SacrificeRoom.prize;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.EvilSacrifice;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.IronTree;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;



public class CultistRoom  extends SpecialRoom{
    @Override
    public int minWidth() {
        return 9;
    }

    @Override
    public int minHeight() {
        return 9;
    }

    @Override
    public int maxWidth() {
        return 9;
    }

    @Override
    public int maxHeight() {
        return 9;
    }

    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public void paint(Level level) {

        Point center = new Point((left + right) / 2, top+2 );
        int eyeRadius = (right - left) / 4;

        Rect rect;
        Rect sect;
        // 获取房间的入口门对象
        Door door = entrance();
        // 设置门的类型为普通门
        door.set(Door.Type.REGULAR);
        // 根据门的位置确定矩形的位置和大小
        Painter.fill(level,this, EMPTY);
        // 绘制外围墙壁
        Painter.drawLine(level, new Point(left, top), new Point(right, top), Terrain.WALL);
        Painter.drawLine(level, new Point(right, top), new Point(right, bottom), Terrain.WALL);
        Painter.drawLine(level, new Point(right, bottom), new Point(left, bottom), Terrain.WALL);
        Painter.drawLine(level, new Point(left, bottom), new Point(left, top), Terrain.WALL);

        if (door.x == left || door.x == right) {
            if (door.x == left) {
                // 如果门在左边，则矩形在门右侧
                rect = new Rect(left, top+2, right+2, bottom-1);

                Painter.fill(level, rect, 1, Terrain.EMPTY_SP);
                Painter.fillWithGap(level, rect, 1, Terrain.WALL,false);
                sect = new Rect(left+7, top+2, right+2, bottom-1);
                Painter.fill(level, sect, 1, Terrain.PEDESTAL);
                Point treasurePos = new Point(); // 宝藏位置
                for (int i = 0; i < 3; i++) {
                    treasurePos.set(left + 8, top+3+i );
                    level.drop(prize(level), level.pointToCell(treasurePos)).type = Heap.Type.CHEST;
                }

            } else {
                // 如果门在右边，则矩形在门左侧
                rect = new Rect(left-1 , top+2, right+1, bottom-1);
                Painter.fill(level, rect, 1, Terrain.EMPTY_SP);
                Painter.fillWithGap(level, rect, 1, Terrain.WALL,false);


                sect = new Rect(left-1, top+2, right-6, bottom-1);
                Painter.fill(level, sect, 1, Terrain.PEDESTAL);

                Point treasurePos = new Point(); // 宝藏位置
                for (int i = 0; i < 3; i++) {
                    treasurePos.set(left, top+3+i );
                    level.drop(prize(level), level.pointToCell(treasurePos)).type = Heap.Type.CHEST;
                }
            }
        } else {
            if (door.y == top) {
                // 如果门在上方，则矩形在门下方
                rect = new Rect(left+2 , top, right-1, bottom+2);
                Painter.fill(level, rect, 1, Terrain.EMPTY_SP);
                Painter.fillWithGap(level, rect, 1, Terrain.WALL,true);

                sect = new Rect(left+2, top+7, right-1, bottom+2);
                Painter.fill(level, sect, 1, Terrain.PEDESTAL);

                Point treasurePos = new Point(); // 宝藏位置
                for (int i = 0; i < 3; i++) {
                    treasurePos.set(left+3+i, top+8 );
                    level.drop(prize(level), level.pointToCell(treasurePos)).type = Heap.Type.CHEST;
                }
            } else {
                // 如果门在下方，则矩形在门上方
                rect = new Rect(left+2 , top-1, right-1, bottom-6);
                Painter.fill(level, rect, 1, Terrain.EMPTY_SP);
                Painter.fillWithGap(level, rect, 1, Terrain.WALL,true);

                sect = new Rect(left+2, top-1, right-1, bottom-6);
                Painter.fill(level, sect, 1, Terrain.PEDESTAL);

                Point treasurePos = new Point(); // 宝藏位置
                for (int i = 0; i < 3; i++) {
                    treasurePos.set(left+3+i, top );
                    level.drop(prize(level), level.pointToCell(treasurePos)).type = Heap.Type.CHEST;
                }

            }
        }
//        Painter.drawEquilateralTriangle(level, center, eyeRadius + 5, Terrain.SIGN);

        int[] MBTPOS = new int[]{
                (top + 1) * level.width() + left +3,
                (top + 4) * level.width() + left +4,
                (top + 4) * level.width() + left +5,
                (top + 6) * level.width() + left +4,
                (top + 3) * level.width() + left +3,
                (top + 5) * level.width() + left +1,
        };

        int minMonsters = 3;
        int maxMonsters = 5;
        int pos;
        pos = level.pointToCell(random());
// 随机确定生成的怪物数量
        int monsterCount = (int) (Math.random() * (maxMonsters - minMonsters + 1)) + minMonsters;

// 随机选择一种怪物
        Mob guaranteedMob;
        if (depth >= 20) {
            guaranteedMob = new Eye();
        } else if (depth >= 15) {
            guaranteedMob = new Eye();
        } else if (depth >= 10) {
            guaranteedMob = new DM201();
        } else {
            guaranteedMob = new EvilSacrifice();
        }

            // 随机生成怪物
        for (int i = 0; i < monsterCount; i++) {
            // 随机选择生成怪物的位置
            int index = (int) (Math.random() * MBTPOS.length);
            Mob n;

            // 检查所选位置是否合法
            if (level.map[MBTPOS[index]] == Terrain.EMPTY && level.heaps.get(MBTPOS[index]) == null) {
                // 在指定位置生成确定的怪物
                 n = guaranteedMob;
            } else {
                if (depth >= 20) {
                    n = new Eye();
                } else if (depth >= 15) {
                    n = new Eye();
                } else if (depth >= 10) {
                    n = new DM201();
                } else {
                    n = new EvilSacrifice();
                }
            }
            n.pos = MBTPOS[index];
            level.mobs.add(n);
        }

//        for (Door door : connected.values()) {
//            door.set(Door.Type.HIDDEN);
//        }

    }
    private static Item prize(Level level ) {

        Item prize;

//        //50% chance for prize item
//        if (Random.Int(2) == 0){
//            prize = level.findPrizeItem();
//            if (prize != null)
//                return prize;
//        }

        //1 floor set higher in probability, never cursed
        do {
            if (Random.Int(2) == 0) {
                prize = Generator.randomWeapon((Dungeon.depth / 5) + 1);
            } else {
                prize = Generator.randomArmor((Dungeon.depth / 5) + 1);
            }
        } while (prize.cursed || Challenges.isItemBlocked(prize));
        prize.cursedKnown = true;

        //33% chance for an extra update.
        if (Random.Int(3) == 0){
            prize.upgrade();
        }

        return prize;
    }
}
