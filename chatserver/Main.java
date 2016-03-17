/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;

/**
 *
 * @author Borja
 */
public class Main {
    public static void main(String[] args) throws IOException {
        
         //Inicia el servidor, lo conecta e inicia las interfaces de lso clientes
         Servidor servidor = new Servidor();
         servidor.conectar();
         ICliente cli=new ICliente();
         cli.setLocation(800, 100);
         ICliente cli1=new ICliente();
          cli1.setLocation(100, 100);
         //El servidor comienza a aceptar conexiones
         servidor.recibir(); 

    }
}
