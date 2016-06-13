/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.klaw.server;

import java.io.IOException;


/**
 *
 * @author Klaw Strife
 */
public class jap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ServerSocketCustom s = new ServerSocketCustom(1010, 2);
        s.addIncomingObjectListener((o, c) -> {
            System.out.println("Mensaje:" + o);
            System.out.println("ID Mensaje:" + c.getInetAddress().getHostAddress());
        });
        Thread hiloServer = new Thread(s);
        hiloServer.start();
    }

}
