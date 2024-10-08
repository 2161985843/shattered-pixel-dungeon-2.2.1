package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne.Ghouls;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GhoulsSprite extends MobSprite {
    private Animation stab;
    private Animation prep;
    private Animation leap;
    private Animation bite;  // 新增的咬击动画


    public GhoulsSprite() {
        super();

        texture( Assets.Sprites.GHOULS );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 4, true );
        idle.frames( frames, 1, 0, 1, 2 );

        run = new Animation( 15, true );
        run.frames( frames, 3, 4, 5, 6, 7, 8 );

        attack = new Animation( 12, false );
        attack.frames( frames, 0, 9, 10, 9 );

        stab = new Animation( 12, false );
        stab.frames( frames, 0, 9, 11, 9 );

        bite = new Animation(12, false);  // 初始化咬击动画
        bite.frames(frames, 0, 13, 13, 14);  // 选择适合的帧

        prep = new Animation( 1, true );
        prep.frames( frames, 9 );

        leap = new Animation( 1, true );
        leap.frames( frames, 12 );

        die = new Animation( 15, false );
        die.frames( frames, 1 );

        play( idle );
    }

    public void leapPrep( int cell ){
        turnTo( ch.pos, cell );
        play( prep );
    }

    @Override
    public void jump( int from, int to, float height, float duration,  Callback callback ) {
        super.jump( from, to, height, duration, callback );
        play( leap );
    }
    private int attackMode = 0; // 攻击模式切换变量
    @Override
    public void attack( int cell ) {
        super.attack( cell );
        switch (attackMode) {
            case 0:
                play(attack); // 普通攻击
                break;
            case 1:
                play(stab);   // 刺击攻击
                break;
            case 2:
                play(bite);
                break;
        }

        // 切换攻击模式
        attackMode = (attackMode + 1) % 3; // 轮流切换0到2
        ((Ghouls)ch).voidattackMode(attackMode);
    }

    @Override

    public void onComplete(Animation anim) {
        if (anim == bite) {
            ((Ghouls)ch).setBiteProc(true); // 设置咬击状态为可用
            super.onComplete(attack); // 否则调用父类的完成逻辑

        } else if (anim == stab ){
            super.onComplete(attack);
        }else {
            super.onComplete(anim); // 否则调用父类的完成逻辑
        }
    }

}