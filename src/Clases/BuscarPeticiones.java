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
import java.net.SocketException;
import java.util.*;

/**
 * @author alan
 */
public class BuscarPeticiones extends Thread {

    private ServerSocket socket;
    private ConexionDB db;
    private Respuesta responder;
    private HashMap<Long, Usuarios> usuarios;
    private int IDThread;
    private ListaUsuarios lista;

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
                        responder = db.iniciarSesion(Long.parseLong(comunicacion.datos.get(0)), comunicacion.datos.get(1));
                        if (responder.success) {

                            usuarios.get(Long.parseLong(comunicacion.datos.get(0))).setOnline(true);
                            usuarios.get(Long.parseLong(comunicacion.datos.get(0))).setIp(c.getRemoteSocketAddress().toString());

                        }
                        System.out.print("-> Login detectado de ");
                        break;

                    case "offline":
                        usuarios.get(Long.parseLong(comunicacion.datos.get(0))).setOnline(false);
                        System.out.print("-> " + Long.parseLong(comunicacion.datos.get(0)) + " se ha desconectado con ip de");
                        responder.datos = null;
                        break;

                    case "cambiarNombre":
                        responder = db.cambiarNombre(Long.parseLong(comunicacion.datos.get(0)), comunicacion.datos.get(1), comunicacion.datos.get(2));
                        System.out.print("-> Cambio de nombre detectado de ");
                        if(responder.success()){
                            usuarios.get(Long.parseLong(comunicacion.datos.get(0))).setNombre(comunicacion.datos.get(1));
                        }
                        break;

                    case "registro":
                        // Celular,  Nombre,  Apellido,  Passwd,  Respuesta
                        responder = db.registro(Long.parseLong(comunicacion.datos.get(0)), comunicacion.datos.get(1),
                                comunicacion.datos.get(2), comunicacion.datos.get(3), comunicacion.datos.get(4));//NO SE SI LA CONTRASEÑA SE MANDA ASI C:***
                        if (responder.success()) {
                            Usuarios nuevaPersona = new Usuarios(Long.parseLong(comunicacion.datos.get(0)),
                                    comunicacion.datos.get(1), comunicacion.datos.get(2), true, c.getRemoteSocketAddress().toString());
                            usuarios.put(Long.parseLong(comunicacion.datos.get(0)), nuevaPersona);
                        }
                        System.out.print("-> Registro detectado de ");
                        break;
                    case "MsgUsuario":
                        try {
                            System.out.println("-> Se ha detectado un mensaje");
                            Gson gsonMsg = new Gson();
                            String respuestaMsgJson;
                            String ip=lista.getPersonas().get(Long.parseLong(comunicacion.datos.get(1))).getIp();
                            ip=ip.substring(1);
                            String[] recorte;
                            recorte = ip.split(":");
                            ip = recorte[0];
                            System.out.println(ip);
                            Socket socketMsg = new Socket(ip, 1234);
                            BufferedReader brMsg = new BufferedReader(new InputStreamReader(socketMsg.getInputStream()));
                            BufferedWriter bwMsg = new BufferedWriter(new OutputStreamWriter(socketMsg.getOutputStream()));
                            Vector<String> vectorMsg = new Vector<>(2, 2);
                            vectorMsg.addElement(comunicacion.datos.get(0));//quien envia el msg
                            vectorMsg.addElement(comunicacion.datos.get(2));//msj a enviar
                            bwMsg.write(gsonMsg.toJson(new Comunicacion("msg", vectorMsg)) + "\n");
                            bwMsg.flush();
                            respuestaMsgJson = brMsg.readLine();
                            socketMsg.close();
                            brMsg.close();
                            bwMsg.close();
                            gsonMsg = new Gson();
                            Respuesta respuestaMsg = gsonMsg.fromJson(respuestaMsgJson, Respuesta.class);
                            if (respuestaMsg.success()){
                                System.out.println("-> Se ha enviado el Msg correctamente! (:");
                                responder.setSuccess(true);
                            }else{
                                System.out.println("-> No se envío el msg Maldito Incompetente!! >:v");
                                responder.setSuccess(false);
                            }
                        }catch (Exception ex){
                            System.out.println(ex);
                        }
                        break;

                    case "MsgGrupo":
                        //pedir un vector de objetos de usuarios de consulta

                        break;

                    case "verUsuarios":

                        Vector<String> listaCompitas;
                        HashMap<Long, Usuarios> listaCompitasHash = new HashMap<>();
                        HashMap<Long, Usuarios> listaUsuariosHash = new HashMap<>();
                        HashMap<Integer, Grupo> listaGrupoHash = new HashMap<>();

                        listaCompitas = db.obtenerCompitas(Long.parseLong(comunicacion.datos.get(0)));
                        for (int i = 0; i < listaCompitas.size(); i++) {
                            String[] parts = listaCompitas.get(i).split(",");
                            long celular = Long.parseLong(parts[0]);
                            String apodo = parts[1];
                            listaCompitasHash.put(usuarios.get(celular).getCelular(),usuarios.get(celular));
                            listaCompitasHash.get(usuarios.get(celular).getCelular()).setApodo(apodo);

                        }
                        for (long key : usuarios.keySet()) {

                            if (!listaCompitasHash.containsKey(key)) {
                                listaUsuariosHash.put(key,usuarios.get(key));
                            }
                        }
                        listaGrupoHash= db.generarListaGrupos(comunicacion.datos.get(0));
                        lista = new ListaUsuarios(listaCompitasHash,listaUsuariosHash,listaGrupoHash);
                        Vector <String> datos= new Vector<>();
                        datos.add(gson.toJson(lista));
                        responder= new Respuesta();
                        responder.setSuccess(true);
                        responder.setDatos(datos);
                        System.out.println(" -> Se ha solicitado la lista de usuarios");
                        break;

                    case "compitasConectados":
                        responder = db.compitasConectados(Long.parseLong(comunicacion.datos.get(0)));
                        System.out.print("-> Ver compitas conectados de  ");
                        break;

                    case "crearGrupo":
                        responder = db.crearGrupo(comunicacion.datos);
                        System.out.print("-> Se ha detectado creacion de grupo  ");
                        break;
                    case "actualizarGrupo":
                        responder = db.actualizarGrupo(comunicacion.datos);
                        System.out.print("-> Se ha detectado un cambio en un grupo ");
                        break;
                    case "salirGrupo":
                        responder = db.salirGrupo(comunicacion.datos);
                        System.out.print("-> Se ha detectado una salida en un grupo ");
                        break;

                    case "solicitudCompita":
                        responder = db.solicitudCompita(comunicacion.datos.get(0), comunicacion.datos.get(1), comunicacion.datos.get(2));
                        System.out.print("-> Solicitud compita de ");
                        break;

                }
                System.out.print(c.getRemoteSocketAddress().toString());
                System.out.println(" respuesta: " + gson.toJson(responder)+" Thread:"+IDThread);
                bw.write(gson.toJson(responder));  //Le responde al cliente
                bw.newLine();
                bw.flush();
                bw.close();
                br.close();
            } catch (SocketException e) {
                System.out.println("-> Excepcion de tipo " + e+" Thread:"+IDThread);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public BuscarPeticiones(int id,HashMap<Long, Usuarios> usuarios) {
        this.usuarios = usuarios;
        IDThread=id;
        db = new ConexionDB();
        try {
            socket = new ServerSocket(1234);
        } catch (Exception ex) {
        }
    }
}
