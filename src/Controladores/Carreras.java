/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ConexionBaseDeDatos.ConexionBD;
import Interfaces.PanelListarCarreras;
import Interfaces.PanelNuevaCarrera;
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
public class Carreras implements ActionListener,KeyListener {

    ModeloCarreras Modelo;

    PanelListarCarreras VistaListar;
    PanelNuevaCarrera VistaFormulario;
    JDialog Dialogo;

    public Carreras(JFrame jFramePrincipal, ConexionBD conexionMySQL) {
        this.Modelo = new ModeloCarreras(conexionMySQL);

        this.VistaListar = new PanelListarCarreras();
        this.VistaListar.setSize(jFramePrincipal.getWidth(), jFramePrincipal.getHeight());
        this.VistaListar.getBtnNuevo().addActionListener(this);
        this.VistaListar.getBtnModificar().addActionListener(this);
        this.VistaListar.getBtnEliminar().addActionListener(this);
        this.VistaListar.getBtnActualizar().addActionListener(this);
        this.VistaListar.getTxtBusqueda().addKeyListener((KeyListener) this);
        this.VistaListar.getBtnBuscar().addActionListener(this);
        this.VistaListar.getBtnLimpiar().addActionListener(this);

        this.VistaFormulario = new PanelNuevaCarrera();
        this.VistaFormulario.getBtnAgregar().addActionListener(this);
        this.VistaFormulario.getBtnCancelar().addActionListener(this);

        this.Dialogo = new JDialog(jFramePrincipal, true);

        cargarDatos(VistaListar.getTxtBusqueda().getText());

        jFramePrincipal.getContentPane().removeAll();
        jFramePrincipal.getContentPane().add(VistaListar);
        jFramePrincipal.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Botón Nuevo pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnNuevo()) {
            Dialogo.getContentPane().add(this.VistaFormulario);
            Dialogo.setTitle("Nueva Categoría");
            Dialogo.setSize(620, 350);
            centrarVentana(this.VistaListar, Dialogo);
            Dialogo.setResizable(false);
            Dialogo.setVisible(true);
        }

        //Botón Modificar pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnModificar()) {
            if (VistaListar.getTableCarreras().getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(VistaListar, "Debe seleccionar un registro");
            }else {
                String[] carrera = Modelo.getCarrera(
                        Integer.parseInt(((DefaultTableModel)VistaListar.getTableCarreras().getModel())
                                .getValueAt(VistaListar.getTableCarreras().getSelectedRow(), 0).toString()));

                VistaFormulario.getJTextFieldId().setText(carrera[0]);
                VistaFormulario.getTxtCarrera().setText(carrera[1]);
                VistaFormulario.getTxtDescripcion().setText(carrera[2]);
                VistaFormulario.getTxtAsignaturas().setText(carrera[3]);
                VistaFormulario.getDuracion().setSelectedItem(carrera[4]);
                VistaFormulario.getMatricula().setSelectedItem(carrera[5]);
                VistaFormulario.getTxtCostoMatricula().setText(carrera[6]);
                
                Dialogo.getContentPane().add(this.VistaFormulario);
                Dialogo.setTitle("Modificar Carrera");
                Dialogo.setSize(620, 350);
                centrarVentana(this.VistaListar, Dialogo);
                Dialogo.setResizable(false);
                Dialogo.setVisible(true);
            }
        }

        //Botón Borrar pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnEliminar()) {
            if (VistaListar.getTableCarreras().getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(VistaListar, "Debe seleccionar un registro");
            } else {
                int dialogoResultado = JOptionPane.showConfirmDialog(VistaListar, "¿Esta segur@ de borrar el registro de Carrera?", "Pregunta", JOptionPane.YES_NO_OPTION);
                if (dialogoResultado == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(
                            ((DefaultTableModel)VistaListar.getTableCarreras().getModel())
                                    .getValueAt(VistaListar.getTableCarreras().getSelectedRow(), 0).toString());
                    if (Modelo.borrar(id)) {
                        
                    } else {
                        JOptionPane.showMessageDialog(VistaListar, "El Registro no se pudo borrar");
                    }
                    cargarDatos(VistaListar.getTxtBusqueda().getText());
                }
            }
        }
        //Botón Actualizar pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnActualizar()) {
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
        //Botón Buscar pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnBuscar()) {
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
//        
//        //Botón Limpiar pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnLimpiar()) {
            VistaListar.getTxtBusqueda().setText("");
            cargarDatos("");
        }
        //Botón Guardar pulsado desde el Panel de JPanelCategoriasFormulario
        if (e.getSource() == this.VistaFormulario.getBtnAgregar()) {
           
            if (Modelo.guardar(
                    Integer.parseInt(this.VistaFormulario.getJTextFieldId().getText()),
                    this.VistaFormulario.getTxtCarrera().getText(),
                    this.VistaFormulario.getTxtDescripcion().getText(),
                    Integer.parseInt(this.VistaFormulario.getTxtAsignaturas().getText()),
                    Integer.parseInt(this.VistaFormulario.getDuracion().getSelectedItem().toString()),
                    this.VistaFormulario.getMatricula().getSelectedItem().toString(),
                    Double.parseDouble(this.VistaFormulario.getTxtCostoMatricula().getText())
            )) {
                Dialogo.setVisible(false);
                limpiarFormulario();
                cargarDatos("");
            } else {
                JOptionPane.showMessageDialog(Dialogo, "No se pudo guardar la Carrera", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        //Botón Cancelar pulsado desde el Panel de JPanelCategoriasFormulario
        if (e.getSource() == this.VistaFormulario.getBtnCancelar()) {
            Dialogo.setVisible(false);
            limpiarFormulario();
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
    }

    public void cargarDatos(String textoBusqueda) {
        this.VistaListar.setDatos(this.Modelo.getLista(textoBusqueda), this.Modelo.getTotal());
        //Ocultamos la columna del Id para que no se vea
        this.VistaListar.getTableCarreras().getColumnModel().getColumn(0).setWidth(0);
        this.VistaListar.getTableCarreras().getColumnModel().getColumn(0).setMinWidth(0);
        this.VistaListar.getTableCarreras().getColumnModel().getColumn(0).setMaxWidth(0); 
    }
    
    public void limpiarFormulario() {
        this.VistaFormulario.getJTextFieldId().setText("0");
        this.VistaFormulario.getTxtCarrera().setText("");
        this.VistaFormulario.getTxtDescripcion().setText("");
        this.VistaFormulario.getTxtCostoMatricula().setText("");
        this.VistaFormulario.getTxtAsignaturas().setText("");
        this.VistaFormulario.getDuracion().setSelectedIndex(0);
        this.VistaFormulario.getMatricula().setSelectedIndex(0);
        
    }

    public void centrarVentana(Component jFrame, Component dialogo) {
        Dimension tamanioVentana = jFrame.getSize();

        dialogo.setLocation(
                jFrame.getX() + ((tamanioVentana.width - dialogo.getSize().width) / 2),
                jFrame.getY() + ((tamanioVentana.height - dialogo.getSize().height) / 2));
    }

    @Override
    public void keyTyped(KeyEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
