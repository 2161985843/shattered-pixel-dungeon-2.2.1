package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class IronBranches extends MeleeWeapon{{

    image = ItemSpriteSheet.IRNO_DOTA2;
    hitSound = Assets.Sounds.HIT_SLASH;
    hitSoundPitch = 1.1f;

    tier = 1;

    bones = false;
}

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (hero.buff(Sword.CleaveTracker.class) != null){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Sword.cleaveAbility(hero, target, 1.33f, this);
    }
}
