package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne.GooMini;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne.Timer;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.GhoulsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulLordSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

/**
 *食尸鬼领主
 */
public class GhoulLord extends Mob{
    {
        HP = HT = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? 140 : 120;
        EXP = 15;
        defenseSkill = 10;
        spriteClass = GhoulLordSprite.class;


        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
        HUNTING = new Hunting();
    }
    public static boolean bossSD=false;    // 检查是否进入特殊状态


    public boolean summoning = false;
    public int summoningPos = -1;//召唤坐标1
    public int summoningPos1 = -1;//召唤坐标2
    protected boolean firstSummon = true;

    private GooMini mySkeleton;
    private int storedSkeletonID = -1;
    private int storedMAX = 2;//召唤CD


    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 45 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 100;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }
    @Override
    protected boolean act() {
        // 如果正在召唤并且当前状态不是 HUNTING
        if (summoning && state != HUNTING) {
            summoning = false; // 结束召唤
            // 如果角色的 sprite 是 GooSprite 类型，则执行特定的操作
            if (sprite instanceof GooSprite) {
                ((GooSprite) sprite).pumpUp(2); // 对 sprite 执行 pumpUp 操作，可能是增强效果
            }
        }
        // 调用父类的 act 方法并返回其结果
        return super.act();
    }
    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 3 ) == 0) {
            Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
            enemy.sprite.burst( 0x000000, 5 );
        }

        return damage;
    }
    @Override
    public void damage(int dmg, Object src) {
        if (!BossHealthBar.isAssigned()){
            BossHealthBar.assignBoss( this );
            Dungeon.level.seal();
        }

        boolean bleeding = (HP*2 <= HT);
        super.damage(dmg, src);
        if ((HP*2 <= HT) && !bleeding){
            bossSD = true;
            GhoulsBossLevel.toggleWaterTexture();
            Game.switchScene(InterlevelScene.class);
            BossHealthBar.bleed(true);
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "enraged"));

            yell(Messages.get(this, "gluuurp"));
        }
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null){
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmg);
            else                                                    lock.addTime(dmg*1.5f);
        }
    }
    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }
    @Override
    public float lootChance() {
        return super.lootChance() * ((6f - Dungeon.LimitedDrops.NECRO_HP.count) / 6f);
    }

    @Override
    public Item createLoot(){
        Dungeon.LimitedDrops.NECRO_HP.count++;
        return super.createLoot();
    }
    @Override
    public void die(Object cause) {
        Dungeon.level.unseal();
        // 如果存储的骷髅 ID 不为空
        if (storedSkeletonID != -1) {
            // 找到存储的骷髅
            Actor ch = Actor.findById(storedSkeletonID);
            storedSkeletonID = -1; // 清空存储的骷髅 ID
            // 如果找到的角色是 NecroSkeleton 类型
            if (ch instanceof GooMini) {
                mySkeleton = (GooMini) ch; // 将其赋值给 mySkeleton
            }
        }

        // 如果 mySkeleton 存在并且还活着
        if (mySkeleton != null && mySkeleton.isAlive()) {
            mySkeleton.die(null); // 使 mySkeleton 死亡
        }
        GhoulLord.bossSD=false;
        ((GhoulsBossLevel)Dungeon.level).eliminatePylon();
        // 调用父类的 die 方法
        super.die(cause);
    }

    private final String PUMPEDUP = "bossSD";
    private static final String SUMMONING = "summoning";
    private static final String FIRST_SUMMON = "first_summon";
    private static final String SUMMONING_POS = "summoning_pos";
    private static final String SUMMONING_POS1 = "summoning_pos1";
    private static final String MY_SKELETON = "my_skeleton";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( PUMPEDUP , bossSD );
        bundle.put( SUMMONING, summoning );
        bundle.put( FIRST_SUMMON, firstSummon );

        if (summoning){
            bundle.put( SUMMONING_POS, summoningPos);
            bundle.put( SUMMONING_POS1, summoningPos1);
        }

        if (mySkeleton != null){
            bundle.put( MY_SKELETON, mySkeleton.id() );
        } else if (storedSkeletonID != -1){
            bundle.put( MY_SKELETON, storedSkeletonID );
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        summoning = bundle.getBoolean( SUMMONING );
        bossSD = bundle.getBoolean( PUMPEDUP );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);


        if (bundle.contains(FIRST_SUMMON)) firstSummon = bundle.getBoolean(FIRST_SUMMON);
        if (summoning){
            summoningPos = bundle.getInt( SUMMONING_POS );
            summoningPos1 = bundle.getInt( SUMMONING_POS1 );
        }
        if (bundle.contains( MY_SKELETON )){
            storedSkeletonID = bundle.getInt( MY_SKELETON );
        }
    }
    @Override
    protected void spend( float time ) {

        storedMAX += time;
        super.spend( time );
    }
    public void onZapComplete(){
        if (mySkeleton == null || mySkeleton.sprite == null || !mySkeleton.isAlive()){
            return;
        }

        //heal skeleton first
        if (mySkeleton.HP < mySkeleton.HT){

            if (sprite.visible || mySkeleton.sprite.visible) {
                sprite.parent.add(new Beam.HealthRay(sprite.center(), mySkeleton.sprite.center()));
            }

            mySkeleton.HP = Math.min(mySkeleton.HP + mySkeleton.HT/5, mySkeleton.HT);
            if (mySkeleton.sprite.visible) mySkeleton.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );

            //otherwise give it adrenaline
        } else if (mySkeleton.buff(Adrenaline.class) == null) {

            if (sprite.visible || mySkeleton.sprite.visible) {
                sprite.parent.add(new Beam.HealthRay(sprite.center(), mySkeleton.sprite.center()));
            }

            Buff.affect(mySkeleton, Adrenaline.class, 3f);
        }

        next();
    }
    private void handleBlockingChar(Char blockingChar, int summoningPos) {
        if (blockingChar != null && !Char.hasProp(blockingChar, Property.IMMOVABLE)) {
            int pushPos = pos; // 当前角色位置

            // 查找有效的推移位置
            for (int c : PathFinder.NEIGHBOURS8) {
                Char neighbourChar = Actor.findChar(summoningPos + c); // 查找相邻角色
                if (isPositionValid(neighbourChar, summoningPos + c, blockingChar)) {
                    pushPos = summoningPos + c; // 更新推移位置
                }
            }

            // 如果找到了有效的推移位置
            if (pushPos != pos) {
                Actor.add(new Pushing(blockingChar, blockingChar.pos, pushPos)); // 添加推移效果
                blockingChar.pos = pushPos; // 更新角色的位置
                Dungeon.level.occupyCell(blockingChar); // 更新 dungeon 中的占用情况
            } else {
                // 如果没有有效的推移位置并且角色的阵营与当前角色不同
                if (blockingChar.alignment != alignment) {
                    damageBlockingChar(blockingChar);
                }
            }
        }
    }
    private boolean isPositionValid(Char neighbourChar, int position, Char blockingChar) {
        return neighbourChar == null // 该位置没有角色
                && Dungeon.level.passable[position] // 该位置可以通过
                && (Dungeon.level.openSpace[position] || !hasProp(blockingChar, Property.LARGE)) // 该位置是开放空间，或角色不是大型角色
                && Dungeon.level.trueDistance(pos, position) > Dungeon.level.trueDistance(pos, blockingChar.pos); // 确保距离更远
    }

    private void damageBlockingChar(Char blockingChar) {
        blockingChar.damage(Random.NormalIntRange(2, 10), this); // 对阻挡者造成随机伤害
        if (blockingChar == Dungeon.hero && !blockingChar.isAlive()) {
            Badges.validateDeathFromEnemyMagic(); // 记录死亡成就
            Dungeon.fail(this); // 游戏失败
            GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name()))); // 记录击杀信息
        }
    }
    private boolean berserk(){ return HP*2<HT; }
    // 召唤小兵
    public void summonMinion() {
        // 检查召唤位置是否有角色
        Char blockingChar = Actor.findChar(summoningPos);
        Char secondBlockingChar = Actor.findChar(summoningPos1); // 找到第二个阻挡的角色

        handleBlockingChar(blockingChar, summoningPos);
        handleBlockingChar(secondBlockingChar, summoningPos1);

        // 设置召唤状态
        summoning = firstSummon = false; // 设置状态，表示不再是第一次召唤

        // 创建新的骷髅小兵
        GooMini mySkeleton = new GooMini();
        Buff.affect(mySkeleton, Timer.class, berserk()? 5f:7f);
        mySkeleton.pos = summoningPos; // 设置骷髅的位置
        GameScene.add(mySkeleton); // 将骷髅添加到游戏场景中
        Dungeon.level.occupyCell(mySkeleton); // 更新 dungeon 中的占用情况
        ((GhoulsBossLevel)Dungeon.level).activatePylon();
        GooMini mySkeleton1 = new GooMini();
        Buff.affect(mySkeleton1, Timer.class, berserk()? 5f:7f);
        mySkeleton1.pos = summoningPos1; // 设置骷髅的位置
        GameScene.add(mySkeleton1); // 将骷髅添加到游戏场景中
        Dungeon.level.occupyCell(mySkeleton1); // 更新 dungeon 中的占用情况
        ((GhoulLordSprite) sprite).finishSummoning(); // 结束召唤动画

        for (Buff b : buffs(AllyBuff.class)){
            Buff.affect(mySkeleton, b.getClass());
        }
        for (Buff b : buffs(ChampionEnemy.class)){
            Buff.affect( mySkeleton, b.getClass());
        }
    }

    private class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV; // 更新敌人是否在视野中

            if (enemySeen) {
                target = enemy.pos; // 如果看到了敌人，设置目标为敌人的位置
            }
            // 检查是否存储了骷髅的 ID
            if (storedSkeletonID != -1) {
                Actor ch = Actor.findById(storedSkeletonID); // 找到存储的骷髅
                storedSkeletonID = -1; // 重置存储 ID
                if (ch instanceof GooMini) {
                    mySkeleton = (GooMini) ch; // 如果是骷髅，赋值给 mySkeleton
                }
            }

            if (summoning) { // 如果正在召唤小兵
                summonMinion(); // 调用召唤小兵的方法
                storedMAX=0;
                return true; // 返回，表示已处理完
            }

            // 检查 mySkeleton 是否有效
            if (mySkeleton != null &&
                    (!mySkeleton.isAlive() // 如果骷髅不再活着
                            || !Dungeon.level.mobs.contains(mySkeleton) // 或者不在地图上
                            || mySkeleton.alignment != alignment)) { // 或者阵营不匹配
                mySkeleton = null; // 设置为 null
            }

            // 如果看到了敌人，且敌人距离在 4 以内，并且没有骷髅，则召唤骷髅
            if (enemySeen && Dungeon.level.distance(pos, enemy.pos) <= 4 && mySkeleton == null&&storedMAX>10) {
                summoningPos = -1; // 初始化召唤位置
                summoningPos1 = -1; // 初始化第二个召唤位置
                // 构建距离地图，避免通过阻挡的地形

                PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), Dungeon.level.distance(pos, enemy.pos) + 3);

                // 寻找可以召唤的邻近位置
                for (int c : PathFinder.NEIGHBOURS8) {
                    int neighborPos = enemy.pos + c; // 缓存邻近位置

                    // 检查所有条件
                    if (Actor.findChar(neighborPos) == null // 该位置没有角色
                            && PathFinder.distance[neighborPos] != Integer.MAX_VALUE // 该位置可到达
                            && Dungeon.level.passable[neighborPos] // 该位置可以通过
                            && (!hasProp(GhoulLord.this, Property.LARGE) || Dungeon.level.openSpace[neighborPos]) // 检查大体型角色
                            && fieldOfView[neighborPos]) { // 该位置在视野内

                        // 计算距离
                        float currentTrueDistance = Dungeon.level.trueDistance(pos, neighborPos);

                        // 更新召唤位置
                        if (summoningPos == -1 || currentTrueDistance < Dungeon.level.trueDistance(pos, summoningPos)) {
                            summoningPos1 = summoningPos; // 保存之前的召唤位置
                            summoningPos = neighborPos; // 更新新的召唤位置
                        } else if (currentTrueDistance < Dungeon.level.trueDistance(pos, summoningPos1) && neighborPos != summoningPos) {
                            summoningPos1 = neighborPos; // 更新第二个召唤位置
                        }
                    }
                }
                // 如果找到有效的召唤位置
                if (summoningPos != -1 && summoningPos1 != -1) {
                    summoning = true; // 设置为正在召唤状态
                    sprite.zap(summoningPos); // 播放召唤动画
                    yell(Messages.get(this, "notice"));
                    spend(firstSummon ? TICK : 2 * TICK); // 消耗时间
                } else {
                    // 如果没有找到位置，则等待一回合
                    spend(TICK);
                }

                return true; // 返回，表示已处理完

                // 如果看到了敌人，并且有骷髅存在
            } else if (enemySeen && mySkeleton != null) {
                spend(TICK); // 消耗时间

                // 如果骷髅不在视野内
                if (!fieldOfView[mySkeleton.pos]) {
                    // 如果骷髅不在敌人旁边
                    // 将骷髅传送到离敌人最近的可见位置
                    if (!Dungeon.level.adjacent(mySkeleton.pos, enemy.pos)) {
                        int telePos = -1; // 初始化传送位置
                        for (int c : PathFinder.NEIGHBOURS8) {
                            if (Actor.findChar(enemy.pos + c) == null // 该位置没有角色
                                    && Dungeon.level.passable[enemy.pos + c] // 该位置可以通过
                                    && fieldOfView[enemy.pos + c] // 该位置在视野内
                                    && (Dungeon.level.openSpace[enemy.pos + c] || !Char.hasProp(mySkeleton, Property.LARGE)) // 检查大体型角色
                                    && Dungeon.level.trueDistance(pos, enemy.pos + c) < Dungeon.level.trueDistance(pos, telePos)) { // 确保距离最近
                                telePos = enemy.pos + c; // 更新传送位置
                            }
                        }

                        // 如果找到了有效的传送位置
                        if (telePos != -1) {
                            ScrollOfTeleportation.appear(mySkeleton, telePos); // 传送骷髅
                            mySkeleton.teleportSpend(); // 消耗骷髅的传送时间

                            // 播放传送动画
                            if (sprite != null && sprite.visible) {
                                sprite.zap(telePos);
                                return false; // 返回，表示处理完毕
                            } else {
                                onZapComplete(); // 如果 sprite 不可见，则完成施法
                            }
                        }
                    }

                    return true; // 返回，表示处理完毕

                } else {
                    // 如果骷髅的生命值低于最大值，或者没有施加肾上腺素 buff
                    if (mySkeleton.HP < mySkeleton.HT || mySkeleton.buff(Adrenaline.class) == null) {
                        // 播放骷髅的闪电效果
                        if (sprite != null && sprite.visible) {
                            sprite.zap(mySkeleton.pos);
                            return false; // 返回，表示处理完毕
                        } else {
                            onZapComplete(); // 如果 sprite 不可见，则完成施法
                        }
                    }
                }

                return true; // 返回，表示处理完毕

                // 否则，使用常规的猎杀行为
            } else {
                return super.act(enemyInFOV, justAlerted); // 调用父类的行为方法
            }
        }
    }

}
