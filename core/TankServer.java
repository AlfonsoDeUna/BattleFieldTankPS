package org.lagomar.dam;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class TankServer {
    private ConcurrentHashMap<Integer, Tank> tanks = new ConcurrentHashMap<>();
    private int nextTankId = 1;

    public TankServer() throws IOException {
        // Crear un hilo para el servidor
        
            ServerSocket serverSocket = new ServerSocket(9022);
            new Thread(() -> {
            while (true) {
                try{
                    java.net.Socket socket = serverSocket.accept();
                    new Thread(() -> handleClient(socket)).start();
                }catch (IOException e) {
                    e.printStackTrace();
                    
                } 
            }
        }).start();

    }

    private void handleClient(Socket socket) {
        System.out.println ("cliente conectado...");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            // Asignar un tanque al cliente
            int tankId = nextTankId++;
            tanks.put(tankId, new Tank(tankId, 0, 0)); // Tanque inicial en (0,0)

            // Enviar actualizaciones al cliente
            while (true) {
                String command = reader.readLine();
                if (command != null) {
                    processCommand(tankId, command);
                    sendGameState(writer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCommand(int tankId, String command) {
        Tank tank = tanks.get(tankId);
        switch (command) {
            case "MOVE_UP": tank.y += 0.5; System.out.println("mueve ..." + tank.y); break;
            case "MOVE_DOWN": tank.y -= 0.5; break;
            case "MOVE_LEFT": tank.x -= 0.5; break;
            case "MOVE_RIGHT": tank.x += 0.5; break;
            case "SHOOT": tank.shoot(); break; // todavía no funciona
        }
    }

    private void sendGameState(PrintWriter writer) {
        StringBuilder state = new StringBuilder();
        for (Tank tank : tanks.values()) {
            System.out.println ("Envío la posición nueva" + tank.y);
            state.append(tank.id).append(",").append(tank.x).append(",").append(tank.y).append(";"); // Formato ID,X,Y;
        }
        writer.println(state.toString());
    }

    public static void main(String[] args) {
        try {
            new TankServer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

