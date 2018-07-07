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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class ModeloAsignaturas {

    ConexionBD Conexion;
    int Total;

    public ModeloAsignaturas(ConexionBD conexion) {
        this.Conexion = conexion;
        this.Total = 0;
    }

    public String[] getasignatura(int id) {
        String[] asignatura
                = new String[7];
        try {
            ResultSet resultado = Conexion.getDatos("SELECT * from asignaturas WHERE id=" + id);
            if (resultado.next()) {
                asignatura[0] = "" + id;
                asignatura[1] = resultado.getString("nombre");
                asignatura[2] = resultado.getString("descripcion");
                asignatura[3] = resultado.getString("horasmes");
                asignatura[4] = resultado.getString("trimestre");
                asignatura[5] = resultado.getString("docente");
                asignatura[6] = resultado.getString("carrera");
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return asignatura;
    }

    public boolean guardar(int id, String nombre, String descripcion, String horasmes, int trimestre, String docente, String carrera) {
        //Extrayendo el id de la carrera para guardarlo como llave foranea en asignatura
        String cons = "Select Id from carreras where nombre='" + carrera + "'";
        ConexionBD con = new ConexionBD();
        Connection cn = (Connection) con.getConnection();
        PreparedStatement pps;
        ResultSet rs;
        int id_fk = 0;
        try {
            Statement st = cn.createStatement();
            rs = st.executeQuery(cons);

            while (rs.next()) {
                id_fk = rs.getInt(1);

            }
        } catch (Exception ex) {
            // Logger.getLogger(ModeloAsignaturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (id == 0) {
            String consulta = "INSERT INTO asignaturas "
                    + "(nombre, descripcion, horasmes, trimestre, docente, carrera,id_fk) VALUES"
                    + "('" + nombre + "', '" + descripcion + "', '" + horasmes + "', " + trimestre + ",'" + docente + "','" + carrera + "'," + id_fk + ")";
            if (Conexion.ejecutarConsulta(consulta)) {
                return true;
            } else {
                return false;
            }
        } else {
            String consulta = "UPDATE asignaturas "
                    + " SET nombre='" + nombre + "', descripcion='" + descripcion + "', horasmes='" + horasmes + "', "
                    + " trimestre=" + trimestre + ", docente='" + docente + "', carrera='" + carrera + "',id_fk="+id_fk
                    + " WHERE id=" + id;
            if (Conexion.ejecutarConsulta(consulta)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean borrar(int id) {
        String consulta = "DELETE FROM asignaturas "
                + " WHERE id = " + id;
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
            String consulta = "SELECT P.Id, P.nombre, P.descripcion, "
                    + "P.horasmes, P.trimestre, P.docente, P.carrera  FROM asignaturas P "
                    + "INNER JOIN carreras C ON C.id = P.id_fk";
            if (!textoBusqueda.isEmpty()) {
                consulta += " WHERE P.nombre LIKE '%" + textoBusqueda + "%' OR P.titulo LIKE '%" + textoBusqueda + "%' OR P.descripcion LIKE '%" + textoBusqueda + "%' OR C.nombre LIKE '%" + textoBusqueda + "%'";
            }
//            System.out.println(consulta);
            ResultSet resultado = Conexion.getDatos(consulta);

            // Se crea el array de columnas
            String[] columnas = {"Id", "nombre", "descripcion","Horas/mes", "trimestre", "docente", "carrera"};

            resultado.last();
            Total = resultado.getRow();
            //Se crea una matriz con tantas filas y columnas que necesite
            Object[][] datos = new String[Total][7];

            if (resultado.getRow() > 0) {
                resultado.first();
                int i = 0;
                do {
                    datos[i][0] = resultado.getString("Id");
                    datos[i][1] = resultado.getString("nombre");
                    datos[i][2] = resultado.getString("descripcion");
                    datos[i][3]= resultado.getString("horasmes");
                    datos[i][4] = resultado.getString("trimestre");
                    datos[i][5] = resultado.getString("docente");
                    datos[i][6] = resultado.getString("carrera");
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
