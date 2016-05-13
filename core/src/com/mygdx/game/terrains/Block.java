package com.mygdx.game.terrains;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Maps.ExampleMap;
import com.mygdx.game.Screens.MainGameScreen;

import static com.mygdx.game.Constants.unitScale;

/**
 * Created by icanul on 5/13/16.
 */
public class Block {

    private Body body;
    private MapObject mapObject;
    public boolean movil;

    //bloque simple
    public Block(Body body, MapObject temp) {
        this.mapObject=temp;
        this.body=body;
        init();
    }
    //bloque con movimiento
    public Block(Body body, boolean movil,MapObject temp) {
        this.mapObject=temp;
        this.body=body;
        this.movil=movil;
    }


    public void init(){
        boolean suelo = Boolean.valueOf(mapObject.getProperties().get("suelo").toString());
        boolean bloque = Boolean.valueOf(mapObject.getProperties().get("bloque").toString());

        if (mapObject instanceof RectangleMapObject) {

            RectangleMapObject rectangulo = (RectangleMapObject)mapObject;
            getRectangle(rectangulo,suelo,bloque);
        }
    }




    private void getRectangle(RectangleMapObject rectangleObject, boolean suelo, boolean bloque) {
        Rectangle rectangle = rectangleObject.getRectangle();
        createBox(rectangle.x, rectangle.y, rectangle.width, rectangle.height, suelo, bloque);
    }


    public void createBox(float x, float y, float witdh, float height, boolean suelo, boolean bloque){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2((unitTileToBox2d(x) + (unitTileToBox2d(witdh) / 2)    /*+ 0.5f*/), (unitTileToBox2d(y) + (unitTileToBox2d(height) / 2)  /*+ 0.5f*/)));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(( unitTileToBox2d(witdh)/2 ), ( unitTileToBox2d(height)/2 ));
        body= MainGameScreen.world.createBody(bodyDef);
        body.createFixture(shape, 1f).setUserData("Objeto");
        body.resetMassData();
        if(suelo){
            body.setUserData("Objeto");
        }else{
            body.setUserData("Bloque");
        }

        shape.dispose();
    }


    private float unitTileToBox2d(float from){
        float to=0;
        to=from/unitScale;
        //System.out.println(to);

        return to;
    }

}
