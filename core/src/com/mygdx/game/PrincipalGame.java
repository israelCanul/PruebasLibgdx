package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.Screens.MainGameScreen;

/**
 * Created by icanul on 4/5/16.
 */
public class PrincipalGame extends Game{

    @Override
    public void create() {
        setScreen(new MainGameScreen(this));
    }

}
