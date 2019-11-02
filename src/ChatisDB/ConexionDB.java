package ChatisDB;

import Clases.Respuesta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
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
            System.out.println(ex);
        }

    }

    public void mostrar() {

        try {
            ResultSet rs;
            PreparedStatement sql = con.prepareStatement("select * from Usuario");

            rs = sql.executeQuery();

            while (rs.next()) {
                System.out.println("CELULAR: " + rs.getInt("Celular"));

                System.out.println("Nombre: " + rs.getString("Nombre"));
                System.out.println("Apellido: " + rs.getString("Apellido"));
                System.out.println("Respuesta: " + rs.getString("Respuesta"));

            }
        } catch (SQLException ex) {
            System.out.println(ex);

        }

    }

    public Respuesta iniciarSesion(int celular, String passwd) {
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

    public Respuesta cambiarNombre(int celular, String nombre, String apellido) {
        respuesta = new Respuesta();
        try {
            String consulta = "update Usuario set Nombre=?, Apellido=? where Celular=?";

            PreparedStatement sql = con.prepareStatement(consulta);
            sql.setInt(3, celular);
            sql.setString(1, nombre);
            sql.setString(2, apellido);
            System.out.println(sql);
            sql.executeUpdate();
            respuesta.setSuccess(true);

        } catch (SQLException ex) {
            System.out.println(" -> " + ex);
            return respuesta;

        }
        return respuesta;
    }

    public Respuesta enviarMsgUsuario(int transmisor, int receptor, String msg) {
        respuesta = new Respuesta();
        try {

        } catch (Exception ex) {
            System.out.println(" -> " + ex);
            return respuesta;
        }
        return respuesta;
    }

    public Respuesta enviarMsgGrupo(int transmisor, int idGrupo, String msg) {
        respuesta = new Respuesta();
        try {

        } catch (Exception ex) {
            System.out.println(" -> " + ex);
            return respuesta;
        }
        return respuesta;
    }

    public Respuesta compitasConectados(int celular) {
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
            sql.setInt(1, celular);
            System.out.println(sql);
            rs = sql.executeQuery();
            while (rs.next()) {
                miniJson += rs.getString("nombreCompi") + ",";
                miniJson += rs.getString("apodoCompi") + ",";
                miniJson += rs.getString("celularCompi");
                datos.add(miniJson);
                System.out.println("Compi añadido!" + rs.getString("nombreCompi"));
            }
            
            respuesta.setDatos(datos);
            respuesta.setSuccess(true);
        } catch (Exception ex) {
            System.out.println(" -> " + ex);
            return respuesta;
        }

        return respuesta;
    }
    
    public Respuesta registro(int Celular, String Nombre, String Apellido, String Passwd, String Respuesta){
        respuesta=new Respuesta();
        try{
            ResultSet res;        
            PreparedStatement sql=con.prepareStatement("SELECT Celular FROM usuario where Celular = ?");
            sql.setInt(1, Celular);
            res=sql.executeQuery();
            
             if (res.next()) {       //Si existe la fila    
                respuesta.setSuccess(false);
            } else {
                do {
                    sql=con.prepareStatement("insert into usuario values(?,?,?,? , AES_ENCRYPT(?,\"chetis\"))");
               
                    sql.setInt(1, Celular);
                    sql.setString(2, Nombre);
                    sql.setString(3, Apellido);
                    sql.setString(5, Passwd);
                    sql.setString(4, Respuesta);
                    System.out.println(sql);
                    sql.executeUpdate();
                    
                    respuesta.setSuccess(true);
                    return respuesta;
                } while (!res.next());
            }
            
        }catch(SQLException ex){
            System.out.println(" -> "+ex);
            return respuesta;
        }
        return respuesta;
    }
}
