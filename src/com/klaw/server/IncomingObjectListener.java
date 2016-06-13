/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.klaw.server;

import java.net.Socket;

/**
 *
 * @author Klaw Strife
 */
public interface IncomingObjectListener {
    void objectRecived(Object object,Socket client);
}
