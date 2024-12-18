package org.lagomar.dam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TankClient extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture tankTexture;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Map<Integer, Tank> tanks = new HashMap<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        tankTexture = new Texture("tank.png");

        try {
            socket = new Socket("localhost", 9022);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            while (true) {
                try {
                    String state = reader.readLine();
                    updateGameState(state);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();

        batch.begin();
        float alpha = 0.02f; 
        for (Tank tank : tanks.values()) {
            tank.renderX += (tank.x * 32 - tank.renderX) * alpha;
            tank.renderY += (tank.y * 32 - tank.renderY) * alpha;
            batch.draw(tankTexture, tank.renderX, tank.renderY, 32,32);
        }
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) writer.println("MOVE_UP");
        if (Gdx.input.isKeyPressed(Input.Keys.A)) writer.println("MOVE_LEFT");
        if (Gdx.input.isKeyPressed(Input.Keys.S)) writer.println("MOVE_DOWN");
        if (Gdx.input.isKeyPressed(Input.Keys.D)) writer.println("MOVE_RIGHT");
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) writer.println("SHOOT");
    }

    private void updateGameState(String state) {
        System.out.println (state);
        tanks.clear();
        String[] tankData = state.split(";");
        for (String data : tankData) {
            String[] values = data.split(",");
            int id = Integer.parseInt(values[0]);
            float x = Float.parseFloat(values[1]);
            float y = Float.parseFloat(values[2]);
            tanks.put(id, new Tank(id, x, y));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        tankTexture.dispose();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Tank {
    int id; 
    float x, y;
    float renderX, renderY;

    public Tank(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.renderX = x * 32;
        this.renderY = y * 32;
    }

    public void shoot() {
        System.out.println("Tank " + id + " shoots!");
    }
}

