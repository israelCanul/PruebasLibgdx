package com.mygdx.game.Maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.mygdx.game.Screens.MainGameScreen;
import com.mygdx.game.terrains.Block;

import java.util.ArrayList;

import static com.mygdx.game.Constants.unitScale;

/**
 * Created by icanul on 4/25/16.
 */
public class ExampleMap {

    public final TiledMap map;
   public final TiledMapTileLayer fondolayer;
    public final TiledMapTileLayer objetoslayer;
    public final TiledMapTileLayer fondo_atr_1_layer;
    public final TiledMapTileLayer fondo_ade_1_layer;
    public ArrayList<Block> listaBlockes;

    private MainGameScreen mainGameScreen;
    private MapObjects objects;
    private Body body;


    public ExampleMap(MainGameScreen mainGameScreen){
        this.mainGameScreen=mainGameScreen;
        map = new TmxMapLoader().load("mimundo.tmx");
        fondo_atr_1_layer= (TiledMapTileLayer)map.getLayers().get("fondo_atras_1");
        fondolayer= (TiledMapTileLayer)map.getLayers().get("fondo");
        fondo_ade_1_layer = (TiledMapTileLayer)map.getLayers().get("fondo_adelante_1");
        objetoslayer = (TiledMapTileLayer)map.getLayers().get("objetos");

        objects = map.getLayers().get("colicion").getObjects();
    }


    public void initMapObjects(){
        listaBlockes=new ArrayList<Block>();

        for (MapObject object:objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }
            Shape shape;
            MapObject temp=object;

            boolean suelo = Boolean.valueOf(temp.getProperties().get("suelo").toString());
            boolean bloque = Boolean.valueOf(temp.getProperties().get("bloque").toString());

            listaBlockes.add(new Block(body,temp));

        }
    }





    public void detach(){
        map.dispose();
    }

}
