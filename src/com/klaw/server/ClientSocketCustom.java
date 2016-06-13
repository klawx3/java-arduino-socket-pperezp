/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.klaw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Klaw Strife
 */
public class ClientSocketCustom implements Runnable {
    private Socket client;
    private ServerSocketCustom server;
    private ObjectInputStream in;
    private ObjectOutputStream out;    
    private boolean active;
    
    public ClientSocketCustom(Socket client,ServerSocketCustom server){
        this.client = client;
        this.server = server;   
        
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientSocketCustom.class.getName()).log(Level.SEVERE, null, ex);
        }
        active = true;
    }

    @Override
    public void run() {
        while (active) {
            try {
                Object readObject = in.readObject();
                fireEvent(readObject);
            } catch (IOException | ClassNotFoundException ex) {
                if (ex instanceof ClassNotFoundException) {
                    System.err.println("Object not found!!");
                } else {
                    System.out.printf("Client [%s] shutdown conecction\n", client.getInetAddress().getHostAddress());
                }
                closeConnection();
                removeClientFromList();
            }
        }
    }

    public void send(Object obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientSocketCustom.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void fireEvent(Object incomingObject) {
        List<IncomingObjectListener> listeners = server.getListeners();
        if (listeners != null) {
            for(IncomingObjectListener inc : listeners){
                inc.objectRecived(incomingObject,client);
            }
        }
    }

    public void closeConnection() {
        active = false;
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientSocketCustom.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientSocketCustom.class.getName())
                        .log(Level.SEVERE, null, ex);
            } finally {
                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientSocketCustom.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }

        }
    }


    private void removeClientFromList() {
        Iterator<ClientSocketCustom> iterator = server.getList_client().iterator();        
        while(iterator.hasNext()){
            ClientSocketCustom client_ = iterator.next();
            if(client_.equals(this)){
                server.getList_client().remove(client_);
                break;
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.client);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientSocketCustom other = (ClientSocketCustom) obj;
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        return true;
    }

}
