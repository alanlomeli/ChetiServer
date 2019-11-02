/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import ChetisDB.ConexionDB;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author alan
 */
public class BuscarPeticiones extends Thread {

    public int id;
    ServerSocket socket;
    byte[] respuestaByte;
    ConexionDB db;
    Respuesta responder;

    @Override
    public void run() {
        String respuesta;
        Comunicacion comunicacion; //Objeto de comunicacion para guardar lo obtenido del cliente

        Socket c;
        while (true) {
            try {

                c = socket.accept();

                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                respuesta = br.readLine();
                Gson gson = new Gson();
                comunicacion = gson.fromJson(respuesta, Comunicacion.class); //Se iguala el json string al objeto comunicacion

                switch (comunicacion.getTipo()) {
                    case "login":

                        responder = db.iniciarSesion(Integer.parseInt(comunicacion.datos.get(0)), comunicacion.datos.get(1));
                        System.out.print("-> Login detectado de ");

                        break;
                    case "cambiarNombre":
                        responder = db.cambiarNombre(Integer.parseInt(comunicacion.datos.get(0)), comunicacion.datos.get(1), comunicacion.datos.get(2));
                        System.out.print("-> Cambio de nombre detectado de ");

                        break;
                        case "registro":
                        System.out.println("Se recibieron los datos: " + comunicacion.datos.get(0) + ","
                                + comunicacion.datos.get(1) + "," + comunicacion.datos.get(2) + "," + comunicacion.datos.get(3) + ","
                                + comunicacion.datos.get(4));
                        // Celular,  Nombre,  Apellido,  Passwd,  Respuesta
                        responder = db.registro(Integer.parseInt(comunicacion.datos.get(0)), comunicacion.datos.get(1), comunicacion.datos.get(2),
                                comunicacion.datos.get(3), comunicacion.datos.get(4));//NO SE SI LA CONTRASEÑA SE MANDA ASI C:***
                        System.out.print("-> Registro detectado de ");
                        break;
                    case "compitasConectados":
                        responder = db.compitasConectados(Integer.parseInt(comunicacion.datos.get(0)));
                        System.out.print("-> Compitas de  ");
                        break;

                }
                System.out.print(c.getRemoteSocketAddress().toString());
                System.out.println(" respuesta: " + gson.toJson(responder));
                bw.write(gson.toJson(responder));  //LE responde al server
                bw.newLine();
                bw.flush();
                bw.close();
                br.close();
            } catch (Exception e) {
                System.out.println("-> Excepcion de tipo " + e);

            }

        }

    }

    public BuscarPeticiones(int id) {
        this.id = id;
        respuestaByte = new byte[400];
        db = new ConexionDB();
        try {
            socket = new ServerSocket(1234);
        } catch (Exception ex) {
        }
    }
}
