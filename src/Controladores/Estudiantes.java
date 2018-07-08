/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ConexionBaseDeDatos.ConexionBD;
//import Interfaces.PanelEstudiantes;
import Interfaces.*;
import Interfaces.PanelListarEstudiantes;
import Interfaces.PanelNuevoEstudiante;
//import Modelos.ModeloEtudiantes;
import Modelos.ModeloCarreras;
import Modelos.ModeloEstudiantes;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class Estudiantes implements ActionListener, KeyListener{
    
     ModeloEstudiantes Modelo;
    ModeloCarreras modeloCategorias;

    PanelListarEstudiantes VistaListar;
    PanelNuevoEstudiante VistaFormulario;
    JDialog Dialogo;

    public Estudiantes(JFrame jFramePrincipal, ConexionBD conexionMySQL) {
        this.Modelo = new ModeloEstudiantes(conexionMySQL);
        this.modeloCategorias = new ModeloCarreras(conexionMySQL);

        this.VistaListar = new PanelListarEstudiantes();
        this.VistaListar.setSize(jFramePrincipal.getWidth(), jFramePrincipal.getHeight());
        this.VistaListar.getBtnNuevo().addActionListener((ActionListener) this);
        this.VistaListar.getBtnModificar().addActionListener((ActionListener) this);
        this.VistaListar.getBtnEliminar().addActionListener((ActionListener) this);
        this.VistaListar.getBtnActualizar().addActionListener((ActionListener) this);
        this.VistaListar.getTxtBusqueda().addKeyListener(this);
        this.VistaListar.getBtnBuscar().addActionListener(this);
        this.VistaListar.getBtnLimpiar().addActionListener(this);

        this.VistaFormulario = new PanelNuevoEstudiante();
        this.VistaFormulario.getBtnGuardar().addActionListener(this);
        this.VistaFormulario.getBtnCancelar().addActionListener(this);

        this.Dialogo = new JDialog(jFramePrincipal, true);

        cargarDatos(VistaListar.getTxtBusqueda().getText());

        jFramePrincipal.getContentPane().removeAll();
        jFramePrincipal.getContentPane().add(VistaListar);
        jFramePrincipal.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Botón Nuevo pulsado desde el Panel de JPanelPeliculasListar
        if (e.getSource() == this.VistaListar.getBtnNuevo()) {
            VistaFormulario.getTxtCarrera().setModel(new javax.swing.DefaultComboBoxModel<>(modeloCategorias.getLista()));
            //VistaFormulario.getTxtTrimestre().setModel(new javax.swing.DefaultComboBoxModel<>(modeloCategorias.getLista()));

            Dialogo.getContentPane().add(this.VistaFormulario);
            Dialogo.setTitle("Nuevo Estudiante");
            Dialogo.setSize(600, 350);
            centrarVentana(this.VistaListar, Dialogo);
            Dialogo.setResizable(false);
            Dialogo.setVisible(true);
        }

        //Botón Modificar pulsado desde el Panel de JPanelPeliculasListar
        if (e.getSource() == this.VistaListar.getBtnModificar()) {
            if (VistaListar.getTableEstudiantes().getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(VistaListar, "Debe seleccionar un registro");
            } else {
                VistaFormulario.getTxtDocumento().setEditable(false);
                String[] estudiante = Modelo.getEstudiante(
                        Integer.parseInt(((DefaultTableModel) VistaListar.getTableEstudiantes().getModel())
                                .getValueAt(VistaListar.getTableEstudiantes().getSelectedRow(), 0).toString()));

                VistaFormulario.getTxtCarrera().setModel(new javax.swing.DefaultComboBoxModel<>(modeloCategorias.getLista()));

                VistaFormulario.getTxtDocumento().setText(estudiante[0]);
                VistaFormulario.getTxtNombre().setText(estudiante[1]);
                VistaFormulario.getTxtApellido().setText(estudiante[2]);
                VistaFormulario.getTxtFechaNacimiento().setText(estudiante[3]);
                VistaFormulario.getTxtCarrera().setSelectedItem(estudiante[4]);

                Dialogo.getContentPane().add(this.VistaFormulario);
                Dialogo.setTitle("Modificar estudiante");
                Dialogo.setSize(600, 350);
                centrarVentana(this.VistaListar, Dialogo);
                Dialogo.setResizable(false);
                Dialogo.setVisible(true);
            }
        }

        //Botón Borrar pulsado desde el Panel de JPanelPeliculasListar
        if (e.getSource() == this.VistaListar.getBtnEliminar()) {
            if (VistaListar.getTableEstudiantes().getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(VistaListar, "Debe seleccionar un registro");
            } else {
                int dialogoResultado = JOptionPane.showConfirmDialog(VistaListar, "¿Esta segur@ de borrar el registro de estudiante?", "Pregunta", JOptionPane.YES_NO_OPTION);
                if (dialogoResultado == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(
                            ((DefaultTableModel)VistaListar.getTableEstudiantes().getModel())
                                    .getValueAt(VistaListar.getTableEstudiantes().getSelectedRow(), 0).toString());
                    if (Modelo.borrar(id)) {
                        
                    } else {
                        JOptionPane.showMessageDialog(VistaListar, "El Registro no se pudo borrar");
                    }
                    cargarDatos(VistaListar.getTxtBusqueda().getText());
                }
            }
        }
//        
//        //Botón Actualizar pulsado desde el Panel de JPanelPeliculasListar
        if (e.getSource() == this.VistaListar.getBtnActualizar()) {
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
//        
//        //Botón Buscar pulsado desde el Panel de JPanelPeliculasListar
        if (e.getSource() == this.VistaListar.getBtnBuscar()) {
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
//        
//        //Botón Limpiar pulsado desde el Panel de JPanelPeliculasListar
        if (e.getSource() == this.VistaListar.getBtnLimpiar()) {
            VistaListar.getTxtBusqueda().setText("");
            cargarDatos("");
        }
//        
        //Botón Guardar pulsado desde el Panel de JPanelPeliculasFormulario
        if (e.getSource() == this.VistaFormulario.getBtnGuardar()) {
            //String categoria = modeloCategorias.getCarrera(this.VistaFormulario.getTxtCarrera().getSelectedItem().toString());

            if (Modelo.guardar(
                    Integer.parseInt(this.VistaFormulario.getTxtDocumento().getText()),
                    this.VistaFormulario.getTxtNombre().getText(),
                    this.VistaFormulario.getTxtApellido().getText(),
                    this.VistaFormulario.getTxtFechaNacimiento().getText(),
                    this.VistaFormulario.getTxtCarrera().getSelectedItem().toString()
            )) {
                Dialogo.setVisible(false);
                limpiarFormulario();
                cargarDatos("");
            } else {
                JOptionPane.showMessageDialog(Dialogo, "No se pudo guardar la estudiante", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

//        //Botón Cancelar pulsado desde el Panel de JPanelPeliculasFormulario
        if (e.getSource() == this.VistaFormulario.getBtnCancelar()) {
            Dialogo.setVisible(false);
            limpiarFormulario();
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void cargarDatos(String textoBusqueda) {
        this.VistaListar.setDatos(this.Modelo.getLista(textoBusqueda), this.Modelo.getTotal());
        //Ocultamos la columna del Id para que no se vea
//        this.VistaListar.getTableEstudiantes().getColumnModel().getColumn(0).setWidth(0);
//        this.VistaListar.getTableEstudiantes().getColumnModel().getColumn(0).setMinWidth(0);
//        this.VistaListar.getTableEstudiantes().getColumnModel().getColumn(0).setMaxWidth(0);
    }

    public void limpiarFormulario() {
        this.VistaFormulario.getTxtDocumento().setText("0");
        this.VistaFormulario.getTxtNombre().setText("");
        this.VistaFormulario.getTxtApellido().setText("");
        this.VistaFormulario.getTxtFechaNacimiento().setText("");
        
        this.VistaFormulario.getTxtCarrera().setSelectedIndex(0);
        
    }

    public void centrarVentana(Component jFrame, Component dialogo) {
        Dimension tamanioVentana = jFrame.getSize();

        dialogo.setLocation(
                jFrame.getX() + ((tamanioVentana.width - dialogo.getSize().width) / 2),
                jFrame.getY() + ((tamanioVentana.height - dialogo.getSize().height) / 2));
    }
}
