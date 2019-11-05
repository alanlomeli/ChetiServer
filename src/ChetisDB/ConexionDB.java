package ChetisDB;

import Clases.Grupo;
import Clases.Respuesta;
import Clases.Usuarios;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author alan
 */
public class ConexionDB {

    protected Connection con;
    private Respuesta respuesta;

    public ConexionDB() {
        respuesta = new Respuesta();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/ChatisDB", "root", "");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("-> Excepcion de tipo " + ex);
            System.out.println("-> ¿Sí tienes mysql corriendo, maldito incompetente?");
        }

    }

    public Vector<String> obtenerUsuariosSistema() {
        Vector<String> respuesta = new Vector<>();
        Gson gson = new Gson();

        try {
            ResultSet rs;
            PreparedStatement sql = con.prepareStatement("select * from Usuario");

            rs = sql.executeQuery();

            while (rs.next()) {
                Usuarios persona = new Usuarios();
                persona.setCelular(rs.getLong("Celular"));
                persona.setNombre(rs.getString("Nombre"));
                persona.setApellido(rs.getString("Apellido"));
                persona.setOnline(false);
                respuesta.add(gson.toJson(persona));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return respuesta;
    }

    public Respuesta iniciarSesion(long celular, String passwd) {
        respuesta = new Respuesta();
        try {
            ResultSet rs;
            PreparedStatement sql = con.prepareStatement("select Celular, Nombre, Apellido from Usuario where "
                    + "Celular=" + celular + " && AES_DECRYPT(Passwd,\"chetis\")=\"" + passwd + "\"");
            rs = sql.executeQuery();

            if (!rs.next()) {           //Si no hay filas
                respuesta.setSuccess(false);
            } else {

                do {
                    Vector datos = new Vector();
                    datos.add(rs.getString("Celular"));
                    datos.add(rs.getString("Nombre"));
                    datos.add(rs.getString("Apellido"));

                    respuesta.setSuccess(true);
                    respuesta.setDatos(datos);
                    return respuesta;
                } while (rs.next());
            }

        } catch (SQLException ex) {
            System.out.println(" -> " + ex);
            return respuesta;

        }
        return respuesta;
    }

    public Respuesta cambiarNombre(long celular, String nombre, String apellido) {
        respuesta = new Respuesta();
        try {
            String consulta = "update Usuario set Nombre=?, Apellido=? where Celular=?";

            PreparedStatement sql = con.prepareStatement(consulta);
            sql.setLong(3, celular);
            sql.setString(1, nombre);
            sql.setString(2, apellido);
            sql.executeUpdate();
            respuesta.setSuccess(true);

        } catch (SQLException ex) {
            System.out.println(" -> " + ex);
            return respuesta;

        }
        return respuesta;
    }

    public Respuesta enviarMsgGrupo(long transmisor, long idGrupo, String msg) {
        respuesta = new Respuesta();
        try {

        } catch (Exception ex) {
            System.out.println(" -> " + ex);
            return respuesta;
        }
        return respuesta;
    }

    public Respuesta compitasConectados(long celular) {
        respuesta = new Respuesta();
        ResultSet rs;
        Vector datos = new Vector();
        String miniJson = "";
        try {

            String consulta = "SELECT\n"
                    + "Usuario.Nombre AS nombreCompi,\n"
                    + "Amistad.Apodo AS apodoCompi,\n"
                    + "Amistad.Amigo_FK AS celularCompi\n"
                    + "FROM\n"
                    + "Usuario\n"
                    + "INNER JOIN Amistad ON Amistad.Amigo_FK = Usuario.Celular\n"
                    + "WHERE\n"
                    + "Amistad.Persona_FK = ?";
            PreparedStatement sql = con.prepareStatement(consulta);
            sql.setLong(1, celular);
            rs = sql.executeQuery();
            while (rs.next()) {
                miniJson += rs.getString("nombreCompi") + ",";
                miniJson += rs.getString("apodoCompi") + ",";
                miniJson += rs.getString("celularCompi");
                datos.add(miniJson);
            }

            respuesta.setDatos(datos);
            respuesta.setSuccess(true);
        } catch (Exception ex) {
            System.out.println(" -> " + ex);
            return respuesta;
        }

        return respuesta;
    }

    public Vector<String> obtenerCompitas(long celular) {
        respuesta = new Respuesta();
        ResultSet rs;
        Vector<String> datos = new Vector();
        try {
            String consulta = "SELECT\n"
                    + "Amistad.Apodo AS apodoCompi,\n"
                    + "Amistad.Amigo_FK AS celular\n"
                    + "FROM\n"
                    + "Usuario\n"
                    + "INNER JOIN Amistad ON Amistad.Amigo_FK = Usuario.Celular\n"
                    + "WHERE\n"
                    + "Amistad.Persona_FK = ?";
            PreparedStatement sql = con.prepareStatement(consulta);
            sql.setLong(1, celular);
            rs = sql.executeQuery();

            while (rs.next()) {
                datos.add(rs.getString("celular") + "," + rs.getString("apodoCompi"));
            }


        } catch (Exception ex) {
            System.out.println(" -> " + ex);
            return datos;
        }

        return datos;
    }


    public Respuesta registro(long Celular, String Nombre, String Apellido, String Passwd, String Respuesta) {
        respuesta = new Respuesta();
        try {
            ResultSet res;
            PreparedStatement sql = con.prepareStatement("SELECT Celular FROM Usuario where Celular = ?");
            sql.setLong(1, Celular);
            res = sql.executeQuery();

            if (res.next()) {       //Si existe la fila
                respuesta.setSuccess(false);
            } else {
                do {
                    sql = con.prepareStatement("insert into Usuario values(?,?,?,? , AES_ENCRYPT(?,\"chetis\"))");

                    sql.setLong(1, Celular);
                    sql.setString(2, Nombre);
                    sql.setString(3, Apellido);
                    sql.setString(5, Passwd);
                    sql.setString(4, Respuesta);
                    sql.executeUpdate();
                    Vector<String> datos = new Vector<>();
                    datos.add(Celular + "");
                    datos.add(Nombre);
                    datos.add(Apellido);
                    datos.add(Passwd);
                    respuesta.setDatos(datos);
                    respuesta.setSuccess(true);
                    return respuesta;
                } while (!res.next());
            }

        } catch (SQLException ex) {
            System.out.println(" -> " + ex);
            return respuesta;
        }
        return respuesta;
    }

    public Respuesta crearGrupo(Vector<String> datos) {
        String[] parts = datos.get(0).split(",");
        int id_insertado = 0;
        long creador = Long.parseLong(parts[0]);
        String nombre_grupo = parts[1];

        respuesta = new Respuesta();
        try {
            String consulta = "INSERT INTO Grupo (Nombre,Creador_FK)VALUES(?,?)";

            PreparedStatement sql = con.prepareStatement(consulta);
            sql.setLong(2, creador);
            sql.setString(1, nombre_grupo);
            sql.executeUpdate();



            ResultSet rs;
            consulta = "SELECT LAST_INSERT_ID() as ID";
            sql = con.prepareStatement(consulta);
            rs = sql.executeQuery();

            while (rs.next()) {
                id_insertado = rs.getInt("ID");
            }

            consulta = "INSERT INTO Integrantes_Grupo (Grupo_FK,Usuario_FK)VALUES(?,?)";
            sql = con.prepareStatement(consulta);
            sql.setLong(2, creador);
            sql.setInt(1, id_insertado);
            sql.executeUpdate();

            enviarSolicitudesGrupo(datos, id_insertado);
            respuesta.setSuccess(true);
        } catch (SQLException ex) {
            System.out.println(" -> " + ex);
            return respuesta;

        }
        return respuesta;
    }

    private void enviarSolicitudesGrupo(Vector<String> datos, int id_insertado) {

        respuesta = new Respuesta();
        try {
            for (int i = 1; i < datos.size(); i++) {

                String consulta = "INSERT INTO Solicitudes_Grupo (Grupo_FK,Usuario_FK)VALUES(?,?)";
                PreparedStatement sql = con.prepareStatement(consulta);
                sql.setLong(2, Long.parseLong(datos.get(i)));
                sql.setInt(1, id_insertado);
                sql.executeUpdate();

            }
            Vector<String> datoss = new Vector<>();
            datoss.add("777");
            respuesta.setSuccess(true);

        } catch (SQLException ex) {
            System.out.println(" -> " + ex);
        }

    }

    public HashMap<Integer, Grupo> generarListaGrupos(String celular) {
        Gson gson = new Gson();

        Vector<String> gruposEncontrados = new Vector<>();
        HashMap<Integer, Grupo> grupos = new HashMap<>();

        try {

            String consulta = "SELECT DISTINCT " +
                    "Integrantes_Grupo.Grupo_FK," +
                    "Grupo.Nombre," +
                    "Grupo.Creador_FK " +
                    "FROM " +
                    "Integrantes_Grupo " +
                    "INNER JOIN Grupo ON Integrantes_Grupo.Grupo_FK = Grupo.Grupo_ID " +
                    "WHERE " +
                    "Integrantes_Grupo.Usuario_FK=?";

            PreparedStatement sql = con.prepareStatement(consulta);
            sql.setLong(1, Long.parseLong(celular));

            ResultSet rs;
            rs = sql.executeQuery();

            while (rs.next()) {
                gruposEncontrados.add(rs.getInt("Grupo_FK") + "," + rs.getString("Nombre") + "," + rs.getLong("Creador_FK"));
            }

            for (int i = 0; i < gruposEncontrados.size(); i++) {
                Vector<Long> miembros = new Vector<>();
                Grupo grupo = new Grupo();
                String[] parts = gruposEncontrados.get(i).split(",");
                grupo.setCreador(Long.parseLong(parts[2]));
                grupo.setNombre(parts[1]);
                grupo.setID(Integer.parseInt(parts[0]));

                consulta = "SELECT " +
                        "Integrantes_Grupo.Usuario_FK " +
                        "FROM " +
                        "Integrantes_Grupo " +
                        "WHERE Integrantes_Grupo.Grupo_FK=?";

                sql = con.prepareStatement(consulta);
                sql.setInt(1, Integer.parseInt(parts[0]));
                rs = sql.executeQuery();

                while (rs.next()) {
                    miembros.add(rs.getLong("Usuario_FK"));
                }
                grupo.setMiembros(miembros);
                grupos.put(Integer.parseInt(parts[0]), grupo);

            }

        } catch (SQLException ex) {
            System.out.println(" -> " + ex);
        }
        //System.out.println(gson.toJson(grupos));
        return grupos;
    }

}
