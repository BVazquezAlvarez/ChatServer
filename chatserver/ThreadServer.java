package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Borja
 */
public class ThreadServer implements Runnable {

    DataInputStream is;
    DataOutputStream os;
    SSLSocket s;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    LinkedList<Socket> sockets = new LinkedList<Socket>();

    public ThreadServer(Socket socket, LinkedList lista) throws IOException {
        s = (SSLSocket) socket;
        sockets = lista;
    }

    @Override
    public void run() {

        try {
            is = new DataInputStream(s.getInputStream());
            
            //Buble que escucha a los clientes, recibiendo sus mensajes
            while (true) {
                byte[] conversacion = new byte[25];
                is.read(conversacion);
                System.out.println("Conversacion es .>" + new String(conversacion, UTF8_CHARSET));
                //Devuelve la conversacion completa al resto de usuarios
                for (int i = 0; i < sockets.size(); i++) {
                    os = new DataOutputStream(sockets.get(i).getOutputStream());
                    os.write(conversacion);
                    System.out.println("Devuelta " + i);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
