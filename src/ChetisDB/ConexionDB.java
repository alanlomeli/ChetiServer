package ChetisDB;

import Clases.Respuesta;
import Clases.Usuarios;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Respuesta enviarMsgUsuario(long transmisor, long receptor, String msg) {
        respuesta = new Respuesta();
        try {

        } catch (Exception ex) {
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
        Vector <String>datos = new Vector();
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
                    datos.add(rs.getString("celular")+","+rs.getString("apodoCompi"));
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
}
