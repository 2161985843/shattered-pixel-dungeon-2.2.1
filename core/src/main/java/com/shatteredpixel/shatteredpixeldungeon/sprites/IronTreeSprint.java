package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class IronTreeSprint extends MobSprite {

    public IronTreeSprint() {
        super();

        texture( Assets.Sprites.IronTree );

        TextureFilm frames = new TextureFilm( texture, 16, 32 );

        idle = new Animation( 0, true );
        idle.frames( frames, 0);

        run = new Animation( 0, true );
        run.frames( frames, 0);

        operate = new Animation( 0, true );
        operate.frames( frames, 0,3,3,0,1,1,2);

        attack = new Animation( 24, false );
        attack.frames( frames, 0 );

        die = new Animation( 12, false );
        die.frames( frames, 0, 1, 2, 3 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFF88CC44;
    }
}
