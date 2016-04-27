package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Constants;
import com.mygdx.game.Entitys.MyActor;
import com.mygdx.game.Listeners.ContactListenerClass;
import com.mygdx.game.Maps.ExampleMap;


import static com.mygdx.game.Constants.unitScale;


/**
 * Created by icanul on 4/5/16.
 */
public class MainGameScreen extends BaseScreen {
    public static World world;
    private Box2DDebugRenderer debugRenderer;
    private Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    public MyActor actor;
    public ExampleMap mapa;
    // variables del cuerpo en box2d
    private BodyDef bodyDef;
    private Body body;




    private TiledMapTileLayer suelo;
    private Texture texture;
    private Sprite sprite;
    private SpriteBatch batch;

    public MainGameScreen(Game game) {
        super(game);
        //se crea el mapa
        mapa=new ExampleMap(this);
        //se crea el stage
        stage=new Stage(new FitViewport(800, 480));
        // se crea el mundo de box2d
        world = new World(new Vector2(0, -10), true);
        //se crea el render de box2d solo para debug
        debugRenderer= new Box2DDebugRenderer();
        //inicializar los objetos incrustados en el mapObject
        mapa.initMapObjects();
        //se anexa el listener al mundo
        world.setContactListener(new ContactListenerClass(this));
        //se crea la camara de tile
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800 / unitScale, 480 / unitScale);
        renderer = new OrthogonalTiledMapRenderer(mapa.map, 1 / unitScale );
    }

    @Override
    public void show() {
        actor=new MyActor(world);
        stage.addActor(actor);
        renderer.getBatch();
        batch = new SpriteBatch();
        renderer.setView(camera);
        texture = new Texture("background.png");
        sprite = new Sprite(texture);
        sprite.setSize(800,480);

    }

    @Override
    public void hide() {
        stage.clear();
        actor.detach();
        mapa.detach();
        world.dispose();
    }

    @Override
    public void render(float delta) {
        //Gdx.gl20.glClearColor(0, 0, 0.5f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        world.step(1 / 60f, 6, 2);

        camera.update();
        batch.begin();
        sprite.draw(batch);
        batch.end();




        if (actor.getX() > 350 && actor.isAlive() && !actor.coliciono) {
            float speed = Constants.PLAYER_SPEED * delta * Constants.unitScale;
            //System.out.println(speed);
            stage.getCamera().translate(speed, 0, 0);
            camera.translate(Constants.PLAYER_SPEED * delta, 0);
        }
        renderer.setView(camera);
        renderer.getBatch().begin();
        renderer.renderTileLayer(mapa.fondo_atr_1_layer);
        renderer.renderTileLayer(mapa.fondolayer);
        renderer.getBatch().end();

        stage.draw();


        renderer.getBatch().begin();
        renderer.renderTileLayer(mapa.fondo_ade_1_layer);
        renderer.getBatch().end();
        // debugRenderer.render(world, camera.combined);
    }


}
