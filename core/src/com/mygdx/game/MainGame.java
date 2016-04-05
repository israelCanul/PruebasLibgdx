package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MainGame implements ApplicationListener{

	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	OrthographicCamera camera;
	float unitScale = 16;
	private TiledMapTileLayer suelo;

	@Override
	public void create() {
		//se carga el tilemap
		map = new TmxMapLoader().load("mimundo.tmx");
		suelo = (TiledMapTileLayer)map.getLayers().get("coliciones");
		MapObjects objects = map.getLayers().get("colicion").getObjects();
		System.out.print(objects.getCount());


		/*for (int x=0;x<suelo.getWidth();x++){
			for (int y=0;y<suelo.getHeight();y++){

				if(suelo.getCell(x,y).getTile().getId()==0){
					System.out.println("entro");
				}
			}
		}*/

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800 / unitScale, 480 / unitScale);

		renderer = new OrthogonalTiledMapRenderer(map, 1 / unitScale );
		renderer.getBatch();
		renderer.setView(camera);

	}

	@Override
	public void dispose() {
		map.dispose();

	}

	@Override
	public void pause() {

	}

	@Override
	public void render() {
		Gdx.gl20.glClearColor(0, 0, 0.5f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//habilita transparencia

		//Gdx.gl.glAlphaFunc(GL20.GL_GREATER, 0.5f);
		/*Gdx.gl20.glEnable(GL20.GL_GREATER);
		Gdx.gl20.glBlendFunc(GL20.GL_GREATER,1);
		Gdx.gl20.glEnable(GL20.GL_ALPHA);*/



		//
		camera.update();
		renderer.setView(camera);
		renderer.render();
	}

	@Override
	public void resize(int arg0, int arg1) { /*...*/ }

	@Override
	public void resume() { /*...*/ }

}