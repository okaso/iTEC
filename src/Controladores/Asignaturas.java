/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ConexionBaseDeDatos.ConexionBD;
import Interfaces.PanelAsignaturas;
import Interfaces.PanelListarAsignaturas;
import Modelos.ModeloAsignaturas;
import Modelos.ModeloCarreras;
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
public class Asignaturas implements ActionListener, KeyListener {

    ModeloAsignaturas Modelo;
    ModeloCarreras modeloCategorias;

    PanelListarAsignaturas VistaListar;
    PanelAsignaturas VistaFormulario;
    JDialog Dialogo;

    public Asignaturas(JFrame jFramePrincipal, ConexionBD conexionMySQL) {
        this.Modelo = new ModeloAsignaturas(conexionMySQL);
        this.modeloCategorias = new ModeloCarreras(conexionMySQL);

        this.VistaListar = new PanelListarAsignaturas();
        this.VistaListar.setSize(jFramePrincipal.getWidth(), jFramePrincipal.getHeight());
        this.VistaListar.getBtnNuevo().addActionListener((ActionListener) this);
        this.VistaListar.getBtnModificar().addActionListener((ActionListener) this);
        this.VistaListar.getBtnEliminar().addActionListener((ActionListener) this);
        this.VistaListar.getBtnActualizar().addActionListener((ActionListener) this);
        this.VistaListar.getTxtBusqueda().addKeyListener(this);
        this.VistaListar.getBtnBuscar().addActionListener(this);
        this.VistaListar.getBtnLimpiar().addActionListener(this);

        this.VistaFormulario = new PanelAsignaturas();
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
            Dialogo.setTitle("Nueva Asignatura");
            Dialogo.setSize(600, 350);
            centrarVentana(this.VistaListar, Dialogo);
            Dialogo.setResizable(false);
            Dialogo.setVisible(true);
        }

        //Botón Modificar pulsado desde el Panel de JPanelPeliculasListar
        if (e.getSource() == this.VistaListar.getBtnModificar()) {
            if (VistaListar.getTableAsignaturas().getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(VistaListar, "Debe seleccionar un registro");
            } else {
                String[] asignatura = Modelo.getasignatura(
                        Integer.parseInt(((DefaultTableModel) VistaListar.getTableAsignaturas().getModel())
                                .getValueAt(VistaListar.getTableAsignaturas().getSelectedRow(), 0).toString()));

                VistaFormulario.getTxtCarrera().setModel(new javax.swing.DefaultComboBoxModel<>(modeloCategorias.getLista()));

                VistaFormulario.getJTextFieldId().setText("" + asignatura[0]);
                VistaFormulario.getTxtAsignatura().setText(asignatura[1]);
                VistaFormulario.getTxtDescripcion().setText(asignatura[2]);
                VistaFormulario.getTxtHoraMes().setText(asignatura[3]);
                VistaFormulario.getTxtTrimestre().setText(asignatura[4]);
                VistaFormulario.getTxtNombreDocente().setText(asignatura[5]);
                VistaFormulario.getTxtCarrera().setSelectedItem(asignatura[6]);

                Dialogo.getContentPane().add(this.VistaFormulario);
                Dialogo.setTitle("Modificar Asignatura");
                Dialogo.setSize(600, 350);
                centrarVentana(this.VistaListar, Dialogo);
                Dialogo.setResizable(false);
                Dialogo.setVisible(true);
            }
        }

        //Botón Borrar pulsado desde el Panel de JPanelPeliculasListar
        if (e.getSource() == this.VistaListar.getBtnEliminar()) {
            if (VistaListar.getTableAsignaturas().getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(VistaListar, "Debe seleccionar un registro");
            } else {
                int dialogoResultado = JOptionPane.showConfirmDialog(VistaListar, "¿Esta segur@ de borrar el registro de Asignatura?", "Pregunta", JOptionPane.YES_NO_OPTION);
                if (dialogoResultado == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(
                            ((DefaultTableModel)VistaListar.getTableAsignaturas().getModel())
                                    .getValueAt(VistaListar.getTableAsignaturas().getSelectedRow(), 0).toString());
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
                    Integer.parseInt(this.VistaFormulario.getJTextFieldId().getText()),
                    this.VistaFormulario.getTxtAsignatura().getText(),
                    this.VistaFormulario.getTxtDescripcion().getText(),
                    this.VistaFormulario.getTxtHoraMes().getText(),
                    Integer.parseInt(this.VistaFormulario.getTxtTrimestre().getText()),
                    this.VistaFormulario.getTxtNombreDocente().getText(),
                    this.VistaFormulario.getTxtCarrera().getSelectedItem().toString()
            )) {
                Dialogo.setVisible(false);
                limpiarFormulario();
                cargarDatos("");
            } else {
                JOptionPane.showMessageDialog(Dialogo, "No se pudo guardar la Asignatura", "Error", JOptionPane.ERROR_MESSAGE);
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
        this.VistaListar.getTableAsignaturas().getColumnModel().getColumn(0).setWidth(0);
        this.VistaListar.getTableAsignaturas().getColumnModel().getColumn(0).setMinWidth(0);
        this.VistaListar.getTableAsignaturas().getColumnModel().getColumn(0).setMaxWidth(0);
    }

    public void limpiarFormulario() {
        this.VistaFormulario.getJTextFieldId().setText("0");
        this.VistaFormulario.getTxtNombreDocente().setText("");
        this.VistaFormulario.getTxtAsignatura().setText("");
        this.VistaFormulario.getTxtDescripcion().setText("");
        this.VistaFormulario.getTxtTrimestre().setText("");
        this.VistaFormulario.getTxtCarrera().setSelectedIndex(0);
        this.VistaFormulario.getTxtHoraMes().setText("");
    }

    public void centrarVentana(Component jFrame, Component dialogo) {
        Dimension tamanioVentana = jFrame.getSize();

        dialogo.setLocation(
                jFrame.getX() + ((tamanioVentana.width - dialogo.getSize().width) / 2),
                jFrame.getY() + ((tamanioVentana.height - dialogo.getSize().height) / 2));
    }
}
