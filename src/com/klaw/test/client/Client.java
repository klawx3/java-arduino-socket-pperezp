/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.klaw.test.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Klaw Strife
 */
public class Client {
    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost",1010);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); 
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                String readLine = br.readLine();
                out.writeObject(readLine);
                out.flush();
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
