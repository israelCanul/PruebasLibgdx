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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Constants;
import com.mygdx.game.Entitys.MyActor;

import static com.mygdx.game.Constants.unitScale;


/**
 * Created by icanul on 4/5/16.
 */
public class MainGameScreen extends BaseScreen {
    private final World world;
    private Box2DDebugRenderer debugRenderer;
    private Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    private TiledMapTileLayer suelo;

    public MainGameScreen(Game game) {
        super(game);
        //se carga el mapa tile
        map = new TmxMapLoader().load("mimundo.tmx");
        suelo = (TiledMapTileLayer)map.getLayers().get("coliciones");
        MapObjects objects = map.getLayers().get("colicion").getObjects();
        System.out.print(objects.getCount());

        // se crea el mundo de box2d
        world = new World(new Vector2(0, -10), true);
        debugRenderer= new Box2DDebugRenderer();


        //se crea la camara de tile
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800 / unitScale, 480 / unitScale);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / unitScale );

    }

    @Override
    public void show() {
        //se crea el stage de la pantalla dond evan a estar alojados todos los actores de la partida
        stage=new Stage(new ExtendViewport(800, 480));

        MyActor actor=new MyActor();
        stage.addActor(actor);

        renderer.getBatch();
        renderer.setView(camera);
    }

    @Override
    public void hide() {
        stage.dispose();
        map.dispose();
        world.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0.5f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1 / 60f, 6, 2);



        camera.update();
        renderer.setView(camera);
        renderer.render();

        stage.act();
        stage.draw();

        debugRenderer.render(world, camera.combined);
    }
}
