package org.lagomar.dam;

import java.util.ArrayList;
import java.util.List;

public class Battlefield {
    private List<Tank> tanks;
    private boolean gameRunning;

    public Battlefield() {
        tanks = new ArrayList<>();
        gameRunning = true;
    }

    public synchronized void addTank(Tank tank) {
        tanks.add(tank);
    }

    public synchronized void removeTank(Tank tank) {
        tanks.remove(tank);
        if (tanks.size() <= 1) {
            gameRunning = false;
        }
    }

    public synchronized List<Tank> getTanks() {
        return new ArrayList<>(tanks);
    }

    public boolean isGameRunning() {
        return gameRunning;
    }
}
