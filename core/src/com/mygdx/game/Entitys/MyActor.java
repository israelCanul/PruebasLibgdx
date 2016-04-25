package com.mygdx.game.Entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mygdx.game.Listeners.ContactListenerClass;

import static com.mygdx.game.Constants.*;

/**
 * Created by icanul on 4/5/16.
 */
public class MyActor extends Actor  {
    private final World world;
    Texture region;
    private BodyDef bodyDef;
    private Body body;
    private FixtureDef defFixture;
    private Fixture fixture;
    private boolean alive=true;
    private boolean jumping=false;
    private boolean mustJump=false;
    //detalles del player
    private float widthBox2d=1;//teniendo en cuenta que este valor es solo la mitad del ancho total
    private float heightBox2d=1.5f;
    private float initXBox2d=6;
    private float initYBox2d=5;
    private float density=0f;
    private float friction=0;
    private float restitution=0f;
    //informacion para animacion
    protected Texture textureAnimacionWalk;
    protected Texture textureAnimacionDown,textureAnimacionJump;
    protected TextureRegion currentFrame;
    protected TextureRegion[] walkFrames,downFrames,jumpFrames;
    protected TextureRegion idleFrame;
    private int status=9;
    private Integer initAnimationState=WALK;
    protected Animation walkAnimation, idleAnimation, currentAnimation,downAnimation,jumpAnimation;
    private float time;
    public boolean coliciono=false;






    public MyActor (World world) {
        this.world=world;
        //se llama a la funcio n para genera el actor y su cuerpo en box2d
        createBodyBox2d();
        //se definen sus tamaños de acuerdo a las variables globales
        setWidth(unitScale * 2);
        setHeight(unitScale * 3);
        // se inicializan las animaciones del actor
        initAnimation();


    }

   @Override
    public void draw (Batch batch, float parentAlpha) {
        setPosition(convertir((body.getPosition().x - widthBox2d)), convertir((body.getPosition().y - heightBox2d)));
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }


    public float convertir(Float valor){
        return unitScale * valor;
    }

    @Override
    public void act(float delta) {

        if (Gdx.input.justTouched() && !jumping) {
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

            body.applyForceToCenter(0, -IMPULSE_JUMP * 1.15f, true);
            //detectar que el salto ya llego a su punto mas alto para cambiar la posicion del player a caida
            if(body.getLinearVelocity().y<0){
                setStatus(DOWN);
            }
        }

        time+= Gdx.graphics.getDeltaTime();
        currentFrame = currentAnimation.getKeyFrame(time, true);
    }


    public void detach(){
        body.destroyFixture(fixture);
        textureAnimacionDown.dispose();
        textureAnimacionJump.dispose();
        textureAnimacionWalk.dispose();
        world.destroyBody(body);
    }

    public void createBodyBox2d(){
        bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(initXBox2d, initYBox2d));
        //figura cuadrada
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(widthBox2d, heightBox2d);
        //figura circular
        //CircleShape formaCirculo = new CircleShape();
        //formaCirculo.setRadius(0.5f);
        //se asigna el cuerpo desde el mundo
        body=world.createBody(bodyDef);
        //
        defFixture = new FixtureDef();  // Definimos el fixture
        defFixture.shape = shape;  // Le pasasamos la forma
       defFixture.density = density;  // Le pasamos la densidad
        defFixture.friction = friction;  // Le pasamos la friccion
        defFixture.restitution = restitution;  //Le agregamos la restitucion

        //se crea el fixture desde el cuerpo
        fixture=body.createFixture(defFixture);
        fixture.setUserData("Player");
        body.resetMassData();

        shape.dispose();

    }

    public void jump(){
        if(isAlive()){
            jumping=true;
            setStatus(JUMP);
            Vector2 position=body.getPosition();
            body.applyLinearImpulse(0, IMPULSE_JUMP, position.x, position.y, true);

        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && getTouchable() != Touchable.enabled) return null;
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? this : null;
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


    public void initAnimation(){
        //inicializamos las variables de animacion
        textureAnimacionWalk =new Texture("walk_evil.png");
        textureAnimacionDown =new Texture("down_player.png");
        textureAnimacionJump =new Texture("jump_player.png");

        TextureRegion[][] tmpWalk = TextureRegion.split(textureAnimacionWalk, textureAnimacionWalk.getWidth() / FRAME_COLS, textureAnimacionWalk.getHeight() / FRAME_ROWS);
        TextureRegion[][] tmpDown = TextureRegion.split(textureAnimacionDown, textureAnimacionDown.getWidth() / FRAME_COLS, textureAnimacionDown.getHeight() / FRAME_ROWS);
        TextureRegion[][] tmpJump = TextureRegion.split(textureAnimacionJump, textureAnimacionJump.getWidth() / FRAME_COLS, textureAnimacionJump.getHeight() / FRAME_ROWS);

        //quieto
        idleFrame = tmpWalk[0][0];
        //actor walk
        walkFrames = new TextureRegion[FRAME_COLS];
        for (int j = 0; j < FRAME_COLS; j++) {
            walkFrames[j] = tmpWalk[0][j];
        }
        //actor down
        downFrames = new TextureRegion[FRAME_COLS];
        for (int j = 0; j < FRAME_COLS; j++) {
            downFrames[j] = tmpDown[0][j];
        }
        //actor Jump
        jumpFrames = new TextureRegion[FRAME_COLS];
        for (int j = 0; j < FRAME_COLS; j++) {
            jumpFrames[j] = tmpJump[0][j];
        }



        //inicializando las animaciones
        idleAnimation = new Animation(0.4f, idleFrame);
        walkAnimation = new Animation(0.4f, walkFrames);
        downAnimation =new Animation(0.4f,downFrames);
        jumpAnimation =new Animation(0.4f,jumpFrames);
        //estableciando la animacion de inicio
        setStatus(initAnimationState);
    }

    //estatus de las animaciones
    //aqui se van anexando cada una de los estados y sus animaciones del actor
    public void setStatus(Integer status){
        if (this.status != status){
            this.status = status;
            switch(status){
                case WALK:
                    currentAnimation = walkAnimation;
                    break;
                case JUMP:
                    currentAnimation = jumpAnimation;
                    break;
                case DOWN:
                    currentAnimation = downAnimation;
                    break;
                default:
                    currentAnimation = idleAnimation;
                    break;
            }
        }
    }


}
