/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.klaw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Klaw Strife
 */
public class ServerSocketCustom implements Runnable{
    
    private final ServerSocket server_socket;
    private boolean started;
    private ExecutorService thread_pool;
    private List<ClientSocketCustom> list_client;
    private int client_pool;
    private List<IncomingObjectListener> listeners;

    public ServerSocketCustom(int port, int client_pool) throws IOException {
        server_socket = new ServerSocket(port);
        started = false;
        thread_pool = Executors.newFixedThreadPool(client_pool);
        list_client = new ArrayList<>();
        this.client_pool = client_pool;
    }

    public ServerSocket getServer_socket() {
        return server_socket;
    }

    public List<ClientSocketCustom> getList_client() {
        return list_client;
    }
    
    public List<IncomingObjectListener> getListeners(){
        return listeners;
    }
    
    public void addIncomingObjectListener(IncomingObjectListener listener){
        if(listeners == null){
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }
    
    public void sendAllClients(Object obj) {
        list_client.forEach((client)-> client.send(obj));
    }

    public void revomeIncomingObjectListener(IncomingObjectListener listener){
        if(listeners != null){
            listeners.remove(listener);
        }
    }

    public void stopServer() {
        if (started) {
            if (!list_client.isEmpty()) {
                for (ClientSocketCustom client : list_client) {
                    client.closeConnection();
                }
            }
            started = false;
            if (!server_socket.isClosed()) {
                try {
                    server_socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerSocketCustom.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Iniciando Servidor...");
        started = true;
        while (started) {
            try {
                System.out.println("Esperando nueva conexion....");
                Socket client_socket = server_socket.accept();
                if (list_client.size() >= client_pool) {
                    System.out.println("Cliente rechazado, conexiones maximas permitidas");
                    client_socket.close();
                    client_socket = null;
                } else {
                    ClientSocketCustom client = new ClientSocketCustom(client_socket, this);
                    list_client.add(client);
                    thread_pool.execute(client);
                    System.out.printf("Cliente [%s] agregado\n", client_socket.getInetAddress().getHostAddress());
                }

            } catch (IOException ex) {
                Logger.getLogger(ServerSocketCustom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
