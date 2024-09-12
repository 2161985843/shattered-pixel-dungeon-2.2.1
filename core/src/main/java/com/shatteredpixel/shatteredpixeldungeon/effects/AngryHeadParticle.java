package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;

public class AngryHeadParticle extends PixelParticle.Shrinking {
    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((AngryHeadParticle) emitter.recycle(AngryHeadParticle.class)).reset(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public AngryHeadParticle() {
        super();

        color(0xFF0000); // 设置颜色为红色，表示愤怒
        lifespan = 0.8f; // 设置寿命

        acc.set(0, -100); // 设置加速度
    }

    public void reset(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan;

        size = 10; // 设置大小，这里假设愤怒的头比较大
        speed.set(0);
    }

    @Override
    public void update() {
        super.update();
        float p = left / lifespan;
        am = p > 0.5f ? (1 - p) * 5 : 1; // 根据剩余寿命调整透明度，使头部在生命周期的不同阶段有不同的透明度
    }
}
