package org.lagomar.dam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TankBattleGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture tankTexture;
    private Battlefield battlefield;

    @Override
    public void create() {
        batch = new SpriteBatch();
        tankTexture = new Texture("tank.png");

        // Crear el campo de batalla y tanques
        battlefield = new Battlefield();
        Tank tank1 = new Tank("Tanque 1", battlefield);
        Tank tank2 = new Tank("Tanque 2", battlefield);
        Tank tank3 = new Tank("Tanque 3", battlefield);

        battlefield.addTank(tank1);
        battlefield.addTank(tank2);
        battlefield.addTank(tank3);

        // Iniciar los hilos de los tanques
        new Thread(tank1).start();
        new Thread(tank2).start();
        new Thread(tank3).start();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (Tank tank : battlefield.getTanks()) {
            batch.draw(tankTexture, tank.getX() * 32, tank.getY() * 32,32,32);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        tankTexture.dispose();
    }
}
