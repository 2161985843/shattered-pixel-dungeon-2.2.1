package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class ArmedbanditsSprite extends MobSprite {
    protected Animation idle2;
    public ArmedbanditsSprite () {
        super();

        texture( Assets.Sprites.ARDBTS );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 14, 1  );

        run = new Animation( 10, true );
        run.frames( frames, 0,2,3,5,6,4);

        attack = new Animation( 12, false );
        attack.frames( frames, 11,13,13,12,11,0 );

        die = new Animation( 12, false );
        die.frames( frames, 7,7,8,8,9,9,10,10 );

        idle2 = new Animation( 5, true );
        idle2.frames( frames, 14,16,14,0,0,1,0,0);

        play( idle );
    }
    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {
            ((MissileSprite) parent.recycle(MissileSprite.class)).
                    reset(this, cell, new SpiritArrow(), new Callback() {
                        @Override
                        public void call() {
                            play(idle2);
                            ch.onAttackComplete();
                        }
                    });
            turnTo(ch.pos, cell);
            play(attack);
        } else {
            super.attack(cell);
        }

    }

}     class SpiritArrow extends MissileWeapon { //子弹武器

    {
        image = ItemSpriteSheet.SPIRIT_ARROW1;

        hitSound = Assets.Sounds.HIT_ARROW;
    }
}