package org.lagomar.dam;

import java.util.Random;
import org.lagomar.dam.TankBattleGame;

public class Tank implements Runnable {
    private static final int MAX_HEALTH = 100;
    private static final int FIELD_SIZE = 20;
    private String name;
    private int x, y;
    private int health;
    private Battlefield battlefield;
    private Random random;

    public Tank(String name, Battlefield battlefield) {
        this.name = name;
        this.battlefield = battlefield;
        this.health = MAX_HEALTH;
        this.random = new Random();
        this.x = random.nextInt(FIELD_SIZE);
        this.y = random.nextInt(FIELD_SIZE);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public void run() {
        while (health > 0 && battlefield.isGameRunning()) {
            move();
            attack();
            try { Thread.sleep(500); } 
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        battlefield.removeTank(this);
    }

    private void move() {
        x += random.nextInt(3) - 1;
        y += random.nextInt(3) - 1;
        x = Math.max(0, Math.min(x, FIELD_SIZE - 1));
        y = Math.max(0, Math.min(y, FIELD_SIZE - 1));
    }

    private void attack() {
        for (Tank enemy : battlefield.getTanks()) {
            if (enemy != this && isInRange(enemy)) {
                synchronized (enemy) {
                    enemy.reduceHealth(10);
                }
            }
        }
    }

    private boolean isInRange(Tank enemy) {
        int distance = Math.abs(x - enemy.x) + Math.abs(y - enemy.y);
        return distance <= 3;
    }

    private synchronized void reduceHealth(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }
}
