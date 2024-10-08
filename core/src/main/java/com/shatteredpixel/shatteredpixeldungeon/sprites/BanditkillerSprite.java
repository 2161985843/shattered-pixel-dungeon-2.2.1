package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class BanditkillerSprite extends MobSprite {
    public BanditkillerSprite () {
        super();

        texture( Assets.Sprites.BAKER );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1  );

        run = new Animation( 12, true );
        run.frames( frames, 0,2,3,6,4,5);

        attack = new Animation( 12, false );
        attack.frames( frames, 0,11,12,14,13,12,11,0 );

        die = new Animation( 12, false );
        die.frames( frames, 7,7,8,8,9,9,10,10 );

        play( idle );
    }
}