/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ConexionBaseDeDatos.ConexionBD;
import Interfaces.NuevoUsuario;
import Interfaces.PanelListarUsuarios;
import Modelos.ModeloUsuarios;
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
public class Usuarios implements ActionListener, KeyListener {
    ModeloUsuarios Modelo;
    
   PanelListarUsuarios VistaListar;
    NuevoUsuario VistaFormulario;
    JDialog Dialogo;
        
    public Usuarios(JFrame jFramePrincipal, ConexionBD conexionMySQL) {
        this.Modelo = new ModeloUsuarios(conexionMySQL);
        
        this.VistaListar = new PanelListarUsuarios();
        this.VistaListar.setSize(jFramePrincipal.getWidth(), jFramePrincipal.getHeight());
        this.VistaListar.getBtnNuevo().addActionListener(this);
        this.VistaListar.getBtnModificar().addActionListener(this);
        this.VistaListar.getBtnEliminar().addActionListener(this);
        this.VistaListar.getBtnLimpiar().addActionListener(this);
        this.VistaListar.getTxtBusqueda().addKeyListener(this);
        this.VistaListar.getBtnBuscar().addActionListener(this);
        this.VistaListar.getBtnActualizar().addActionListener(this);
        
        this.VistaFormulario = new NuevoUsuario();
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
        //Botón Nuevo pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnNuevo()) {
            Dialogo.getContentPane().add(this.VistaFormulario);
            Dialogo.setTitle("Nuevo Usuario");
            Dialogo.setSize(600, 320);
            centrarVentana(this.VistaListar, Dialogo);
            Dialogo.setResizable(false);
            Dialogo.setVisible(true);
        }
        
        //Botón Modificar pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnModificar()) {
            if (VistaListar.getTableUsuarios().getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(VistaListar, "Debe seleccionar un registro");
            } else {
                String[] usuario = Modelo.getUsuario(
                        Integer.parseInt(((DefaultTableModel)VistaListar.getTableUsuarios().getModel())
                                .getValueAt(VistaListar.getTableUsuarios().getSelectedRow(), 0).toString()));

                VistaFormulario.getjTextFieldId().setText("" + usuario[0]);
                VistaFormulario.getTxtUser().setText(usuario[1]);
                VistaFormulario.getTxtNombre().setText(usuario[2]);
                VistaFormulario.getTxtRol().setSelectedItem(usuario[3]);
                if (usuario[4].equals("S")) {
                    VistaFormulario.getjCheckBoxHabilitado().setSelected(true);
                } else {
                    VistaFormulario.getjCheckBoxHabilitado().setSelected(false);
                }

                Dialogo.getContentPane().add(this.VistaFormulario);
                Dialogo.setTitle("Modificar Usuario");
                Dialogo.setSize(600, 320);
                centrarVentana(this.VistaListar, Dialogo);
                Dialogo.setResizable(false);
                Dialogo.setVisible(true);
            }
        }
        
        //Botón Borrar pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnEliminar()) {
            if (VistaListar.getTableUsuarios().getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(VistaListar, "Debe seleccionar un registro");
            } else {
                int dialogoResultado = JOptionPane.showConfirmDialog(VistaListar, "¿Esta segur@ de borrar el registro de Usuario?", "Pregunta", JOptionPane.YES_NO_OPTION);
                if (dialogoResultado == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(
                            ((DefaultTableModel)VistaListar.getTableUsuarios().getModel())
                                    .getValueAt(VistaListar.getTableUsuarios().getSelectedRow(), 0).toString());
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
        
        //Botón Limpiar pulsado desde el Panel de JPanelCategoriasListar
        if (e.getSource() == this.VistaListar.getBtnLimpiar()) {
            VistaListar.getTxtBusqueda().setText("");
            cargarDatos("");
        }
        
        //Botón Guardar pulsado desde el Panel de JPanelCategoriasFormulario
        if (e.getSource() == this.VistaFormulario.getBtnGuardar()) {
            if (
                Modelo.guardar(
                    Integer.parseInt(this.VistaFormulario.getjTextFieldId().getText()),
                    this.VistaFormulario.getTxtUser().getText(), 
                    this.VistaFormulario.getTxtPassword().getText(),
                    this.VistaFormulario.getTxtNombre().getText(), 
                    this.VistaFormulario.getTxtRol().getSelectedItem().toString(),
                    this.VistaFormulario.getjCheckBoxHabilitado().isSelected()?"S":"N"
                )
            ) {
                Dialogo.setVisible(false);
                limpiarFormulario();
                cargarDatos("");
            } else {
                JOptionPane.showMessageDialog(Dialogo, "No se pudo guardar la Categoría", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        //Botón Cancelar pulsado desde el Panel de JPanelCategoriasFormulario
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
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            cargarDatos(VistaListar.getTxtBusqueda().getText());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    public void cargarDatos(String textoBusqueda) {
        this.VistaListar.setDatos(this.Modelo.getLista(textoBusqueda), this.Modelo.getTotal());
        //Ocultamos la columna del Id para que no se vea
        this.VistaListar.getTableUsuarios().getColumnModel().getColumn(0).setWidth(0);
        this.VistaListar.getTableUsuarios().getColumnModel().getColumn(0).setMinWidth(0);
        this.VistaListar.getTableUsuarios().getColumnModel().getColumn(0).setMaxWidth(0); 
    }
    
    public void limpiarFormulario() {
        this.VistaFormulario.getjTextFieldId().setText("0");
        this.VistaFormulario.getTxtUser().setText("");
        this.VistaFormulario.getTxtPassword().setText("");
        this.VistaFormulario.getTxtNombre().setText(""); 
        this.VistaFormulario.getTxtRol().setSelectedIndex(0);
        this.VistaFormulario.getjCheckBoxHabilitado().setSelected(true);
    }
    
    public void centrarVentana(Component jFrame, Component dialogo) {
        Dimension tamanioVentana = jFrame.getSize();
        
        dialogo.setLocation(
          jFrame.getX() + ((tamanioVentana.width - dialogo.getSize().width) / 2),
          jFrame.getY() + ((tamanioVentana.height - dialogo.getSize().height) / 2));
    }
}
