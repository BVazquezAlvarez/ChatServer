/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.LinkedList;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author Borja
 */
public class Servidor {

    SSLServerSocketFactory factory;
    SSLServerSocket sslserversocket;
    SSLSocket sslsocket;
    String conversacion = "";
    DataInputStream is;
    DataOutputStream os;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    LinkedList<Socket> lista = new LinkedList<Socket>();

    public void conectar() throws IOException {
        //Pone las credenciales del server y las activa. Crea el servidor por SSL socket
        System.setProperty("javax.net.ssl.trustStore", "chatserver");
        System.setProperty("javax.net.ssl.trustStorePassword", "administrador");
        System.out.println("Conectando..");
        factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        sslserversocket = (SSLServerSocket) factory.createServerSocket(5555);
        final String[] enabledCipherSuites = {"SSL_DH_anon_WITH_RC4_128_MD5"};
        sslserversocket.setEnabledCipherSuites(enabledCipherSuites);

    }
 //Metodo que escucha a los clientes que se van a conectar, acepta e inicia el hilo del servidor
    public void recibir() throws IOException {
        while (true) {
            System.out.println("Aceptando...");
            //Acepta al cliente y lo añade a la lista de sockets
            sslsocket = (SSLSocket) sslserversocket.accept();
            lista.add(sslsocket);
            System.out.println("Listo..");
            //Inicia el hilo
            Runnable run = new ThreadServer(sslsocket, lista);
            Thread hilo = new Thread(run);
            hilo.start();
        }

    }

    //Cierra el socket cliente
    public void cerrar() throws IOException {
        sslsocket.close();

    }
}
