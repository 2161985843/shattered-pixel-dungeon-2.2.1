package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ne;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class GME {
    public static int gate(int min, int value, int max){
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    public static int[] NEIGHBOURS5(){
        int w = Dungeon.level.width();
        return new int[]{0, -1, 1, w, -w};
    }

    public static int[] NEIGHBOURS12(){
        int w = Dungeon.level.width();
        return new int[]{-1, -2, 1, 2, w, -w, -2*w, 2*w, w+1, w-1, -w+1, -w-1};
    }

    public static int[] NEIGHBOURS20(){
        int w = Dungeon.level.width();
        return new int[]{
                1, -1, w, -w, 1+w, 1-w, -1+w, -1-w, 2, -2, -2*w, 2*w,
                -2*w+1, -2*w-1, 2*w+1, 2*w-1, 2+w, 2-w, -2+w, -2-w
        };
    }

    public static float angle(float x, float y){
        float angle = PointF.angle(new PointF(x, y),
                new PointF(1f, 0));
        angle /= PointF.G2R;
        return angle;
    }

}