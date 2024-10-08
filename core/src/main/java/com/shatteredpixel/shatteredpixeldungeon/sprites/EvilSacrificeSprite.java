package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class EvilSacrificeSprite  extends MobSprite {
    public EvilSacrificeSprite () {
        super();

        texture( Assets.Sprites.EVSS );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 14,1 );

        run = new Animation( 12, true );
        run.frames( frames, 0, 11,12,13,0);

        attack = new Animation( 12, false );
        attack.frames( frames, 7, 8, 9,10 );

        zap = new Animation( 8, false );
        zap.frames( frames, 5, 5, 1 );

        die = new Animation( 12, false );
        die.frames( frames, 1, 2, 3, 4, 5,6 );

        play( idle );
    }
}
