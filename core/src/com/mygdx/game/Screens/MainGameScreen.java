package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Entitys.MyActor;


/**
 * Created by icanul on 4/5/16.
 */
public class MainGameScreen extends BaseScreen {
    private Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    float unitScale = 16;
    private TiledMapTileLayer suelo;

    public MainGameScreen(Game game) {
        super(game);
        map = new TmxMapLoader().load("mimundo.tmx");
        suelo = (TiledMapTileLayer)map.getLayers().get("coliciones");
        MapObjects objects = map.getLayers().get("colicion").getObjects();
        System.out.print(objects.getCount());


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800 / unitScale, 480 / unitScale);

        renderer = new OrthogonalTiledMapRenderer(map, 1 / unitScale );


    }

    @Override
    public void show() {
        //se crea el stage de la pantalla dond evan a estar alojados todos los actores de la partida
        stage=new Stage(new ExtendViewport(800, 480));

        MyActor actor=new MyActor();
        //actor.setSize(16f,16f);
        stage.addActor(actor);

        renderer.getBatch();
        renderer.setView(camera);
    }

    @Override
    public void hide() {
        stage.dispose();

        map.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0.5f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //se renderiza el mapa
        camera.update();
        renderer.setView(camera);
        renderer.render();

        stage.act();
       /* stage.getBatch().begin();*/
        //stage.getBatch().disableBlending();
        stage.draw();
        //stage.getBatch().enableBlending();
        /*stage.getBatch().end();*/
    }
}
