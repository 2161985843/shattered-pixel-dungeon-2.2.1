package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class GiantRatSprite extends MobSprite {

    public GiantRatSprite() {
        super();

        texture(Assets.Sprites.RAT);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 96, 96, 96, 97);

        run = new Animation(10, true);
        run.frames(frames, 102, 103, 104, 105, 106);

        attack = new Animation(15, false);
        attack.frames(frames, 98, 99, 100, 101,96);

        die = new Animation(10, false);
        die.frames(frames, 107, 108, 109, 110);

        play(idle);
    }
}