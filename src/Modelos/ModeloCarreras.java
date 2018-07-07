/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import ConexionBaseDeDatos.ConexionBD;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class ModeloCarreras {

    ConexionBD Conexion;
    int Total;

    public ModeloCarreras(ConexionBD conexion) {
        this.Conexion = conexion;
        this.Total = 0;
    }

    public String[] getCarrera(int id) {
        String[] carrera = new String[7];
        try {
            ResultSet resultado = Conexion.getDatos("SELECT * FROM carreras WHERE id = " + id);
            if (resultado.next()) {
                carrera[0] = "" + id;
                
                carrera[1] = resultado.getString("nombre");
                carrera[2] = resultado.getString("descripcion");
                carrera[3] = resultado.getString("nroasignaturas");
                carrera[4] = resultado.getString("duracion");
                carrera[5] = resultado.getString("matricula");
                carrera[6] = resultado.getString("costo");
                
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return carrera;
    }


    public boolean guardar(int id, String nombre, String descripcion, int nroasignaturas, int duracion, String matricula, double costo) {
        if (id == 0) {
            String consulta = "INSERT INTO carreras "
                    + "(nombre, descripcion, nroasignaturas, duracion, matricula,costo) VALUES "
                    + "('" + nombre + "','" + descripcion + "'," + nroasignaturas + "," + duracion + ",'" + matricula + "'," + costo + ")";
            if (Conexion.ejecutarConsulta(consulta)) {
                return true;
            } else {
                return false;
            }
        } else {
            String consulta = "UPDATE carreras "
                    + "SET nombre='" + nombre + "', descripcion='" + descripcion + "', nroasignaturas=" + nroasignaturas+",duracion="+duracion+",matricula='"+matricula+"',costo="+costo
                    + " WHERE id=" + id;
            if (Conexion.ejecutarConsulta(consulta)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public boolean borrar(int id) {
        String consulta = "DELETE FROM carreras "
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
            String consulta = "SELECT * FROM carreras";
            if (!textoBusqueda.isEmpty()) {
                consulta += " WHERE nombre LIKE '%" + textoBusqueda + "%' OR nombre LIKE '%" + textoBusqueda + "%' OR descripcion LIKE '%" + textoBusqueda + "%'";
            }
            ResultSet resultado = Conexion.getDatos(consulta);

            // Se crea el array de columnas
            String[] columnas = {"Id", "Nombre", "Descripcion", "Asignaturas", "Trimestres", "Matricula", "Costo"};

            resultado.last();
            Total = resultado.getRow();
            //Se crea una matriz con tantas filas y columnas que necesite
            Object[][] datos = new String[Total][7];

            if (resultado.getRow() > 0) {
                resultado.first();
                int i = 0;
                do {
                    datos[i][0] = resultado.getString("id");

                    datos[i][1] = resultado.getString("nombre");
                    datos[i][2] = resultado.getString("descripcion");
                    datos[i][3] = resultado.getString("nroasignaturas");
                    datos[i][4] = resultado.getString("duracion");
                    datos[i][5] = resultado.getString("matricula");
                    datos[i][6] = resultado.getString("costo");

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

    public String[] getLista() {
        try {
            String consulta = "SELECT * FROM carreras";
            ResultSet resultado = Conexion.getDatos(consulta);

            resultado.last();
            //Se crea una matriz con tantas filas y columnas que necesite
            String[] datos = new String[resultado.getRow()];

            if (resultado.getRow() > 0) {
                resultado.first();
                int i = 0;
                do {
                    datos[i] = resultado.getString("nombre");
                    i++;
                } while (resultado.next());
            }
            resultado.close();
            return datos;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    

    public int getTotal() {
        return Total;
    }
}
