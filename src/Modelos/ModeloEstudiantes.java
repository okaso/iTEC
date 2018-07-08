/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import ConexionBaseDeDatos.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class ModeloEstudiantes {
    ConexionBD Conexion;
    int Total;

    public ModeloEstudiantes(ConexionBD conexion) {
        this.Conexion = conexion;
        this.Total = 0;
    }

    public String[] getEstudiante(int doc) {
        String[] estudiante
                = new String[7];
        try{ 
            ResultSet resultado = Conexion.getDatos("SELECT * from alumnos WHERE documento=" + doc);
            if (resultado.next()) {
                estudiante[0] = resultado.getString("documento");
                estudiante[1] = resultado.getString("nombres");
                estudiante[2] = resultado.getString("apellidos");
                estudiante[3] = resultado.getString("fnacimiento");
                estudiante[4] = resultado.getString("carrera");
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return estudiante;
    }

    public boolean guardar(int doc, String nombre, String apellido, String fnacimiento, String carrera) {
        //Extrayendo el id de la carrera para guardarlo como llave foranea en estudiante
        String cons = "Select Id from carreras where nombre='" + carrera + "'";
        ConexionBD con = new ConexionBD();
        Connection cn = (Connection) con.getConnection();
        
        ResultSet rs;
        int id_fk = 0;
        try {
            Statement st = cn.createStatement();
            rs = st.executeQuery(cons);

            while (rs.next()) {
                id_fk = rs.getInt(1);

            }
        } catch (Exception ex) {
            // Logger.getLogger(Modeloestudiantes.class.getName()).log(Level.SEVERE, null, ex);
        }
        //verificando si el documento existe para modificarse o para agregar uno nuevo
        
         String sql = "Select documento from alumnos where documento=" + doc + "";
     
        ResultSet s;
        int document = 0;
        try {
            Statement st = cn.createStatement();
            s = st.executeQuery(sql);

            while (s.next()) {
                if(document == s.getInt(1)){
                    document = 1;
                }

            }
        } catch (Exception ex) {
            // Logger.getLogger(Modeloestudiantes.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (document == 0) {
            PreparedStatement pps;
            String consulta = "INSERT INTO alumnos "
                    + "(documento, nombres, apellidos, fnacimiento, carrera,id_fk) VALUES"
                    + "(" + doc + ",'" + nombre + "','" + apellido + "','" + fnacimiento+"','" + carrera + "'," + id_fk + ")";
            
            if (Conexion.ejecutarConsulta(consulta)) {
                return true;
            } else {
                return false;
            }
        } else {
            String consulta = "UPDATE alumnos "
                    + " SET  nombres='" + nombre + "', apellidos='" + apellido + "', "
                    + ", fnacimiento='" + fnacimiento + "', carrera='" + carrera + "',id_fk="+id_fk
                    + " WHERE documento=" + doc;
            if (Conexion.ejecutarConsulta(consulta)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean borrar(int id) {
        String consulta = "DELETE FROM alumnos "
                + " WHERE documento = " + id;
        if (Conexion.ejecutarConsulta(consulta)) {
            return true;
        } else {
            return false;
        }
    }

    public DefaultTableModel getLista(String textoBusqueda) {
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        try {
            String consulta = "SELECT P.documento, P.nombres, P.apellidos, "
                    + "P.fnacimiento, P.carrera  FROM alumnos P "
                    + "INNER JOIN carreras C ON C.id = P.id_fk";
            if (!textoBusqueda.isEmpty()) {
                consulta += " WHERE P.nombres LIKE '%" + textoBusqueda + "%' OR P.apellidos LIKE '%" + textoBusqueda + "%' OR P.carrera LIKE '%" + textoBusqueda + "%' OR C.nombre LIKE '%" + textoBusqueda + "%'";
            }
//            System.out.println(consulta);
            ResultSet resultado = Conexion.getDatos(consulta);

            // Se crea el array de columnas
            String[] columnas = {"documento", "nombres", "apellidos","F/nacimiento", "carrera"};

            resultado.last();
            Total = resultado.getRow();
            //Se crea una matriz con tantas filas y columnas que necesite
            Object[][] datos = new String[Total][5];

            if (resultado.getRow() > 0) {
                resultado.first();
                int i = 0;
                do {
                    datos[i][0] = resultado.getString("documento");
                    datos[i][1] = resultado.getString("nombres");
                    datos[i][2] = resultado.getString("apellidos");
                    datos[i][3]= resultado.getString("fnacimiento");
                    datos[i][4] = resultado.getString("carrera");
                    
//                    datos[i][6] = resultado.getString("categoria");
                    i++;
                } while (resultado.next());
            }
            resultado.close();
            modeloTabla.setDataVector(datos, columnas);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return modeloTabla;
    }

    public int getTotal() {
        return Total;
    }
}
