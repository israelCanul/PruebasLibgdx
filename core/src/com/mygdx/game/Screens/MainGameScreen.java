package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    public MyActor actor;

    // variables del cuerpo en box2d
    private BodyDef bodyDef;
    private Body body;




    private TiledMapTileLayer suelo;

    public MainGameScreen(Game game) {
        super(game);
        //se carga el mapa tile
        map = new TmxMapLoader().load("mimundo.tmx");
        suelo = (TiledMapTileLayer)map.getLayers().get("coliciones");
        MapObjects objects = map.getLayers().get("colicion").getObjects();
        //System.out.print(objects.getCount());





        stage=new Stage(new FitViewport(800, 480));

        // se crea el mundo de box2d
        world = new World(new Vector2(0, -10), true);




        debugRenderer= new Box2DDebugRenderer();



        for (MapObject object:objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }
            Shape shape;
            MapObject temp=object;

            boolean suelo = Boolean.valueOf(temp.getProperties().get("suelo").toString());
            boolean bloque = Boolean.valueOf(temp.getProperties().get("bloque").toString());

            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangulo = (RectangleMapObject)object;


                getRectangle(rectangulo,suelo,bloque);
                System.out.println(suelo);

                //System.out.println(rectangulo.getRectangle().getHeight());*/
            }
        }

        world.setContactListener(new ContactListenerClass(this));

        //se crea la camara de tile
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800 / unitScale, 480 / unitScale);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / unitScale );




    }

    private void getRectangle(RectangleMapObject rectangleObject, boolean suelo, boolean bloque) {
        Rectangle rectangle = rectangleObject.getRectangle();
        createBox(rectangle.x, rectangle.y, rectangle.width, rectangle.height, suelo, bloque);
    }






    @Override
    public void show() {
        //se crea el stage de la pantalla dond evan a estar alojados todos los actores de la partida
        //createSuelo(0,2);

        actor=new MyActor(world);
        stage.addActor(actor);

        renderer.getBatch();
        renderer.setView(camera);
    }

    @Override
    public void hide() {
        stage.clear();
        actor.detach();
        map.dispose();
        world.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0.5f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        world.step(1 / 60f, 6, 2);

        camera.update();


        renderer.setView(camera);
        renderer.render();

        if (actor.getX() > 150 && actor.isAlive()) {
            float speed = Constants.PLAYER_SPEED *delta* Constants.unitScale;
            //System.out.println(speed);
            stage.getCamera().translate(speed, 0, 0);
            //camera.translate(0.01f, 0);
        }



        stage.draw();

        debugRenderer.render(world, camera.combined);
    }

    public void createBox(float x, float y, float witdh, float height, boolean suelo, boolean bloque){

        bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2((unitTileToBox2d(x) + (unitTileToBox2d(witdh) / 2)    /*+ 0.5f*/), (unitTileToBox2d(y) + (unitTileToBox2d(height) / 2)  /*+ 0.5f*/)));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(( unitTileToBox2d(witdh)/2 /*+ 0.5f*/), ( unitTileToBox2d(height)/2 /*+ 0.5f*/));
        body=world.createBody(bodyDef);
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
        System.out.println(to);

        return to;
    }

    public void createSuelo(float x,float y){
        bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2((x+0.5f),(y+0.5f)));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50.5f, 0.5f);
        body=world.createBody(bodyDef);
        body.createFixture(shape, 1f);
        body.resetMassData();
        shape.dispose();
    }

}
