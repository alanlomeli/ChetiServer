import ChetisDB.ConexionDB;
import Clases.BuscarPeticiones;
import Clases.Usuarios;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

public class Main {


    public static void main(String[] args) {


        System.out.println("_________ .__            __  .__  _________                                       \n" +
                "\\_   ___ \\|  |__   _____/  |_|__|/   _____/ _____________________  __ ___________ \n" +
                "/    \\  \\/|  |  \\_/ __ \\   __\\  |\\_____  \\_/ __ \\_  __ \\_  __ \\  \\/ // __ \\_  __ \\\n" +
                "\\     \\___|   Y  \\  ___/|  | |  |/        \\  ___/|  | \\/|  | \\/\\   /\\  ___/|  | \\/\n" +
                " \\______  /___|  /\\___  >__| |__/_______  /\\___  >__|   |__|    \\_/  \\___  >__|   \n" +
                "        \\/     \\/     \\/                \\/     \\/                        \\/       ");
        System.out.println("-> Server started");

        ConexionDB bd = new ConexionDB();
        System.out.println("-> Base de datos iniciada");
        HashMap<Long,Usuarios> usuarios= new HashMap<>();
        Vector <String> rawUsuarios;
        rawUsuarios=bd.obtenerUsuariosSistema();
        Gson gson=new Gson();

      for (int i=0; i<rawUsuarios.size();i++){
            Usuarios persona=gson.fromJson(rawUsuarios.get(i), Usuarios.class);
           usuarios.put(persona.getCelular(),persona);

       }

        Thread h1;
        h1 = new BuscarPeticiones(1,usuarios);
        h1.start();


        System.out.println("-> Threads iniciados");


    }
}

    