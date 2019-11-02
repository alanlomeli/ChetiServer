import ChatisDB.ConexionDB;
import Clases.BuscarPeticiones;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        ConexionDB bd = new ConexionDB();
        // bd.mostrar();

        System.out.println("_________ .__            __  .__  _________                                \n" +
                "\\_   ___ \\|  |__ _____ _/  |_|__|/   _____/ ______________  __ ___________ \n" +
                "/    \\  \\/|  |  \\\\__  \\\\   __\\  |\\_____  \\_/ __ \\_  __ \\  \\/ // __ \\_  __ \\\n" +
                "\\     \\___|   Y  \\/ __ \\|  | |  |/        \\  ___/|  | \\/\\   /\\  ___/|  | \\/\n" +
                " \\______  /___|  (____  /__| |__/_______  /\\___  >__|    \\_/  \\___  >__|   \n" +
                "        \\/     \\/     \\/                \\/     \\/                 \\/       ");
        System.out.println("-> Server started");
        Thread h1 = new Thread();
        h1 = new BuscarPeticiones(1);
        h1.start();
        System.out.println("-> Threads iniciados");


    }
}

    