package com.mygdx.game.Entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;

import static com.mygdx.game.Constants.IMPULSE_JUMP;
import static com.mygdx.game.Constants.PLAYER_SPEED;
import static com.mygdx.game.Constants.unitScale;
/**
 * Created by icanul on 4/5/16.
 */
public class MyActor extends Actor {
    private final World world;
    Texture region;
    private BodyDef bodyDef;
    private Body body;
    private FixtureDef defFixture;
    private boolean alive=true;
    private boolean jumping=false;
    private boolean mustJump=false;


    public MyActor (World world) {
        this.world=world;
        region = new Texture("bolita.png");
        //se llama a la funcio n para genera el actor y su cuerpo en box2d
        createBodyBox2d();
        //se definen sus tamaños de acuerdo a las variables globales
        setWidth(unitScale);
        setHeight(unitScale);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        //Color color = getColor();
        //System.out.println("Ancho: " + getX() + " Alto: " + getY());

        //setPosition(convertir((body.getPosition().x - 0.5f)), convertir((body.getPosition().y - 0.5f)));
        setPosition(convertir((body.getPosition().x - 0.5f)),convertir((body.getPosition().y - 0.5f)));
        batch.draw(region,getX(),getY(),getWidth(),getHeight());
    }

    public float convertir(Float valor){
        return unitScale * valor;
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.justTouched()) {
            jump();
        }
        // Jump if we were required to jump during a collision.
        if (mustJump) {
            mustJump = false;
            jump();
        }

        if (alive) {
            // Only change X speed. Do not change Y speed because if the player is jumping,
            // this speed has to be managed by the forces applied to the player. If we modify
            // Y speed, jumps can get very very weir.d
            float speedY = body.getLinearVelocity().y;
            body.setLinearVelocity(PLAYER_SPEED, speedY);
        }

        if (jumping) {
            body.applyForceToCenter(0, 0 * 1.15f, true);
        }
    }

    public void detach(){
        world.destroyBody(body);
    }

    public void createBodyBox2d(){
        bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(0.5f, 12.5f));
        //figura cuadrada
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        //figura circular
        CircleShape formaCirculo = new CircleShape();
        formaCirculo.setRadius(0.5f);
        //se asigna el cuerpo desde el mundo
        body=world.createBody(bodyDef);
        //
        defFixture = new FixtureDef();  // Definimos el fixture
        defFixture.shape = formaCirculo;  // Le pasasamos la forma
        defFixture.density = 1.0f;  // Le pasamos la densidad
        defFixture.friction = 0.0f;  // Le pasamos la friccion
        defFixture.restitution = 0.6f;  //Le agregamos la restitucion
        //se crea el fixture desde el cuerpo
        body.createFixture(defFixture);
        body.resetMassData();
        //se libera la informacion del shape
        formaCirculo.dispose();
        shape.dispose();

    }

    public void jump(){
        if(isAlive()){
            jumping=true;

            Vector2 position=body.getPosition();
            body.applyLinearImpulse(0,IMPULSE_JUMP,position.x,position.y,true);
        }
    }


    // Getter and setter festival below here.
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setMustJump(boolean mustJump) {
        this.mustJump = mustJump;
    }

}
