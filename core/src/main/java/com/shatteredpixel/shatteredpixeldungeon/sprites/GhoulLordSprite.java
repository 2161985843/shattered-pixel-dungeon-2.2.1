package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GhoulLord;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

/**
 *食尸鬼领主素材
 */
public class GhoulLordSprite extends MobSprite {

    private Animation charging;
    private Emitter summoningBones;
    private Emitter summoningBones2;
    public GhoulLordSprite(){
        super();

        texture( Assets.Sprites.GHOUL_LORD );
        TextureFilm film = new TextureFilm( texture, 16, 14 );

        idle = new Animation( 1, true );
        idle.frames( film, 0, 0, 0, 1, 0, 0, 0, 0, 1 );

        run = new Animation( 8, true );
        run.frames( film, 0, 0, 0, 2, 3, 4 );

        zap = new Animation( 10, false );
        zap.frames( film, 1,3,4,5,6,7,8 );

        charging = new Animation( 5, true );
        charging.frames( film, 11, 12 );

        die = new Animation( 10, false );
        die.frames( film,  10,10,14,14,15,15  );

        attack = zap.clone();

        idle();
    }

    // 关联角色
    @Override
    public void link(Char ch) {
        super.link(ch); // 调用父类的link方法
        // 如果角色是GhoulLord且正在召唤，播放两个召唤位置的动画
        if (ch instanceof GhoulLord && ((GhoulLord) ch).summoning) {
            zap(((GhoulLord) ch).summoningPos);   // 播放第一个召唤位置的动画
            zap(((GhoulLord) ch).summoningPos1);  // 播放第二个召唤位置的动画
        }
    }

    // 更新状态
    @Override
    public void update() {
        super.update(); // 调用父类的update方法
        // 更新第一个召唤位置的可见性
        if (summoningBones != null && ((GhoulLord) ch).summoningPos != -1) {
            summoningBones.visible = Dungeon.level.heroFOV[((GhoulLord) ch).summoningPos];
        }
        // 更新第二个召唤位置的可见性
        if (summoningBones2 != null && ((GhoulLord) ch).summoningPos1 != -1) {
            summoningBones2.visible = Dungeon.level.heroFOV[((GhoulLord) ch).summoningPos1];
        }
    }

    // 角色死亡处理
    @Override
    public void die() {
        super.die(); // 调用父类的die方法
        // 处理第一个召唤骨骼的发射器
        if (summoningBones != null) {
            summoningBones.on = false;
            summoningBones = null;
        }
        // 处理第二个召唤骨骼的发射器
        if (summoningBones2 != null) {
            summoningBones2.on = false;
            summoningBones2 = null;
        }
    }

    // 角色被击杀处理
    @Override
    public void kill() {
        super.kill(); // 调用父类的kill方法
        // 处理第一个召唤骨骼的发射器
        if (summoningBones != null) {
            summoningBones.on = false;
            summoningBones = null;
        }
        // 处理第二个召唤骨骼的发射器
        if (summoningBones2 != null) {
            summoningBones2.on = false;
            summoningBones2 = null;
        }
    }

    // 取消召唤
    public void cancelSummoning() {
        // 处理第一个召唤骨骼的发射器
        if (summoningBones != null) {
            summoningBones.on = false;
            summoningBones = null;
        }
        // 处理第二个召唤骨骼的发射器
        if (summoningBones2 != null) {
            summoningBones2.on = false;
            summoningBones2 = null;
        }
    }

    // 完成召唤
    public void finishSummoning() {
        // 处理第一个召唤骨骼的发射器
        if (summoningBones != null) {
            if (summoningBones.visible) {
                Sample.INSTANCE.play(Assets.Sounds.BONES);
                summoningBones.burst(Speck.factory(Speck.RATTLE), 5); // 产生5个粒子
            } else {
                summoningBones.on = false; // 如果不可见，关闭发射器
            }
            summoningBones = null; // 清空发射器引用
        }

        // 处理第二个召唤骨骼的发射器
        if (summoningBones2 != null) {
            if (summoningBones2.visible) {
                Sample.INSTANCE.play(Assets.Sounds.BONES);
                summoningBones2.burst(Speck.factory(Speck.RATTLE), 5); // 产生5个粒子
            } else {
                summoningBones2.on = false; // 如果不可见，关闭发射器
            }
            summoningBones2 = null; // 清空发射器引用
        }

        idle(); // 回到待机状态
    }

    // 充能方法
    public void charge() {
        play(charging); // 播放充能动画
    }

    // 施法方法
    @Override
    public void zap(int cell) {
        super.zap(cell); // 调用父类的zap方法
        // 如果角色是GhoulLord且正在召唤
        if (ch instanceof GhoulLord && ((GhoulLord) ch).summoning) {
            // 处理第一个召唤骨骼的发射器
            if (summoningBones != null) {
                summoningBones.on = false; // 关闭现有发射器
            }
            summoningBones = CellEmitter.get(((GhoulLord) ch).summoningPos); // 获取第一个召唤位置的发射器
            summoningBones.pour(Speck.factory(Speck.BLACK_WATER), 0.2f); // 产生粒子效果
            summoningBones.visible = Dungeon.level.heroFOV[((GhoulLord) ch).summoningPos]; // 根据视野更新可见性

            // 处理第二个召唤骨骼的发射器
            if (summoningBones2 != null) {
                summoningBones2.on = false; // 关闭现有发射器
            }
            summoningBones2 = CellEmitter.get(((GhoulLord) ch).summoningPos1); // 获取第二个召唤位置的发射器
            summoningBones2.pour(Speck.factory(Speck.BLACK_WATER), 0.2f); // 产生粒子效果
            summoningBones2.visible = Dungeon.level.heroFOV[((GhoulLord) ch).summoningPos1]; // 根据视野更新可见性

            // 如果当前可见或召唤位置可见，播放充能声音
            if (visible || summoningBones.visible || (summoningBones2 != null && summoningBones2.visible)) {
                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP, 1f, 0.8f);
            }
        }
    }

    // 动画完成处理
    @Override
    public void onComplete(Animation anim) {
        super.onComplete(anim); // 调用父类的onComplete方法
        // 如果完成的是施法动画
        if (anim == zap) {
            if (ch instanceof GhoulLord) {
                if (((GhoulLord) ch).summoning) {
                    charge(); // 如果正在召唤，则充能
                } else {

                    idle(); // 回到待机状态
                }
            } else {
                idle(); // 其他情况也回到待机状态
            }
        }
    }
}

