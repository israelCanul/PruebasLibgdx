package com.mygdx.game.Entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.mygdx.game.Constants.unitScale;
/**
 * Created by icanul on 4/5/16.
 */
public class MyActor extends Actor {
    Texture region;

    public MyActor () {
        region = new Texture("bolita.png");
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region,convertir(0),convertir(4),unitScale,unitScale);
    }

    public float convertir(Integer valor){
        return unitScale * valor;
    }


}
