/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionBaseDeDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class ConexionBD {
     private Connection Conexion = null;
     public ConexionBD(){
         try{
            Class.forName("com.mysql.jdbc.Driver");
            Conexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/instituto","root","");
            System.out.println("Conexion exitosa");
            //JOptionPane.showMessageDialog(null,"Conexion Exitosa..!!");
            
        }catch(Exception e){
            System.out.println("Error"+e.getMessage());
            
            JOptionPane.showMessageDialog(null,"base de datos apagado"+e.getMessage());
        }
     }
     public Connection getConnection(){
        return Conexion;
    }
    public void desconectar(){
        Conexion = null;
    }
    public boolean ejecutarConsulta(String consulta) {
        //se ejecuta la consulta
        try {
            PreparedStatement preparandoConsulta = this.getConnection().prepareStatement(consulta);
            preparandoConsulta.execute();
            preparandoConsulta.close();
//            System.out.println("Consulta ejecutada!");
            return true;
        }catch(SQLException e){
//            System.out.println("Consulta no ejecutada!");
            System.err.println( e.getMessage() );
        }
        return false;
    }
    
    public ResultSet getDatos(String consulta) {
        try {
            PreparedStatement preparandoConsulta = this.getConnection().prepareStatement(consulta);
            return preparandoConsulta.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
