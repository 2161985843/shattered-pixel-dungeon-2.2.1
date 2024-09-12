package com.shatteredpixel.shatteredpixeldungeon.items;

public class ChallengeItem extends Item {
    {
        unique = true;
    }
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}