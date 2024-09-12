package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndPicture;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class Sign {
    private static final String leave = "警告：请冒险者始终在保险库中留下传送卷轴以方便离开。";
    //private static final String BOOKLVL = "Note to self: Always leave a teleport scroll in the vault.";

    public static void read( int pos ) {

        if (level instanceof DeadEndLevel) {

            GameScene.show( new WndMessage( Messages.get(Sign.class, "dead_end") ) );

        } else {

            if (Dungeon.depth <= 21) {
                GameScene.show( new WndMessage( Messages.get(Sign.class, "tip_"+Dungeon.depth) ) );
            } else {

                Level.set(pos, Terrain.EMBERS);
                GameScene.updateMap( pos );
                GameScene.discoverTile( pos, Terrain.SIGN );

                GLog.w( Messages.get(Sign.class, "burn") );

                CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
                Sample.INSTANCE.play( Assets.Music.CAVES_BOSS_FINALE );
            }

        }
    }

    public static void readPit() {
        if (Dungeon.depth ==2) {
//            level.generateRandomWater( 10);
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show( new WndMessage( Messages.get( Sign.class,"leave-1") ) );
                }
            });

        }
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show( new WndMessage( Messages.get( Sign.class,"leave") ) );
            }
        });
    }
}
