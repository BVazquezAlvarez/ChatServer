/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JTextArea;

/**
 *
 * @author Borja
 */
public class Cliente implements Runnable {

    SSLSocketFactory factory;
    SSLSocket sslsocket;
    String conversacion = "Welcome";
    DataOutputStream os;
    public Servidor s = new Servidor();
    DataInputStream is;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    JTextArea historial;

    public Cliente(JTextArea historial) throws IOException {
        this.historial = historial;
    }

    public void conectar() throws IOException {
         //Pone las mismas credenciales del servidor y las activa. Crea el socket cliente y se conecta a local con el mismo puerto que el server
        System.setProperty("javax.net.ssl.trustStore", "chatserver");
        System.setProperty("javax.net.ssl.trustStorePassword", "administrador");
        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        System.out.println("Conectando cliente 1");
        sslsocket = (SSLSocket) factory.createSocket("localhost", 5555);
        System.out.println("Listo Cliente 1");
        final String[] enabledCipherSuites = {"SSL_DH_anon_WITH_RC4_128_MD5"};
        sslsocket.setEnabledCipherSuites(enabledCipherSuites);
        os = new DataOutputStream(sslsocket.getOutputStream());
        is = new DataInputStream(sslsocket.getInputStream());

    }

    public void enviar(String user, String mensaje) throws IOException {
        //Si el mensaje es "nabucodonosorcito" cierra el socket" y manda el mensaje informando a los otros clientes    
        if ("nabucodonosorcito".equals(mensaje)) {            
            historial.setText(conversacion + "\n**** CONEXION TERMINADA ****");
            os.write(("\n" + user + " ha salido de la conversacion").getBytes());
            sslsocket.close();
        } else {
            System.out.println("Enviando user y msg");
            os.write((user + ": " + mensaje).getBytes());
            System.out.println("Enviado todo");
        }

    }

    public void cerrar(String user) throws IOException {
        //metodo cerrar que desconecta el socket e informa al resto de cliente
        historial.setText(conversacion + "\n**** CONEXION TERMINADA ****");
        os.write(("\n" + user + " ha salido de la conversacion").getBytes());
        sslsocket.close();
    }

    @Override
    public void run() {
        try {
            //Bucle que recibe los mensajes del servidor
            while (true) {
                System.out.println("Va?");
                byte[] conversacionbyte = new byte[25];
                is.read(conversacionbyte);
                conversacion = conversacion + "\n" + (new String(conversacionbyte, UTF8_CHARSET));
                historial.setText(conversacion);

            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

}
