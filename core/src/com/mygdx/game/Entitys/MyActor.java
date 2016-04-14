package com.mygdx.game.Entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.mygdx.game.Constants.unitScale;
/**
 * Created by icanul on 4/5/16.
 */
public class MyActor extends Actor {
    private final World world;
    Texture region;
    private BodyDef bodyDef;
    private Body body;

    public MyActor (World world) {
        this.world=world;
        region = new Texture("bolita.png");
        createBox();
        setWidth(unitScale);
        setHeight(unitScale);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        System.out.println("Ancho: " + getX() + " Alto: " + getY());

        //setPosition(convertir((body.getPosition().x - 0.5f)), convertir((body.getPosition().y - 0.5f)));
        setPosition(convertir((body.getPosition().x - 0.5f)), 480);
        batch.draw(region,getX(),getY(),getWidth(),getHeight());
    }

    public float convertir(Float valor){
        return unitScale * valor;
    }

    @Override
    public void act(float delta) {

    }

    public void detach(){
        world.destroyBody(body);
    }

    public void createBox(){
        bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(0.5f,0.5f));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f,0.5f);
        body=world.createBody(bodyDef);

        body.createFixture(shape, 1f);
        body.resetMassData();
        shape.dispose();
    }

}
