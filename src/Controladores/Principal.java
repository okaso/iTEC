/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ConexionBaseDeDatos.ConexionBD;
import Modelos.ModeloUsuarios;
import Interfaces.InterfazPrincipal;
import Interfaces.PanelLogin;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Principal implements ActionListener, KeyListener {

    ConexionBD Conexion;
    InterfazPrincipal IP;
    PanelLogin PL;
    JDialog Dialogo;
    ModeloUsuarios modeloUsuarios;

    public Principal(InterfazPrincipal IP) {
        this.Conexion = new ConexionBD();
        this.modeloUsuarios = new ModeloUsuarios(Conexion);

        this.IP = IP;
        this.IP.setVisible(true);

        PL = new PanelLogin();
        this.Dialogo = new JDialog(IP, true);

        PL.getBtnIngresar().addActionListener((ActionListener) this);
        PL.getBtnSalir().addActionListener((ActionListener) this);

        PL.getTxtUsername().addKeyListener((KeyListener) this);
        PL.getTxtPassword().addKeyListener((KeyListener) this);

        Dialogo.getContentPane().add(PL);
        Dialogo.setTitle("Datos de Acceso");
        Dialogo.setSize(323, 203);
        Dialogo.setUndecorated(true);
        centrarVentana(IP, Dialogo);
        Dialogo.setResizable(false);
        Dialogo.setVisible(true);

        this.IP.getItemCarreras().addActionListener((ActionListener) this);
        this.IP.getItemAsignaturas().addActionListener((ActionListener) this);
        this.IP.getItemEstudiantes().addActionListener((ActionListener) this);
        this.IP.getItemSalir().addActionListener((ActionListener) this);
        
        this.IP.getItemUsuarios().addActionListener(this);
        this.IP.getjMenuItem5().addActionListener((ActionListener) this);
    }

    private void centrarVentana(Component jFrame, Component dialogo) {
        Dimension tamanioVentana = jFrame.getSize();

        dialogo.setLocation(
                jFrame.getX() + ((tamanioVentana.width - dialogo.getSize().width) / 2),
                jFrame.getY() + ((tamanioVentana.height - dialogo.getSize().height) / 2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == IP.getItemCarreras()) {
           new Carreras(IP, this.Conexion);
        }
        if (e.getSource() == IP.getItemAsignaturas()) {
            new Asignaturas(IP, this.Conexion);
        }
         if (e.getSource() == IP.getItemUsuarios()) {
            new Usuarios(IP, this.Conexion);
        }
        if (e.getSource() == IP.getItemEstudiantes()) {
            new Estudiantes(IP, this.Conexion);
        }
        if (e.getSource() == IP.getItemSalir()) {
            System.exit(0);
        }
        if (e.getSource() == IP.getjMenuItem5()) {
            JOptionPane.showMessageDialog(IP, "Desarrollado por:\nJuan Pablo Segovia Vargas \n\ty\nJaime Tadio Flores\nPresentacion Examen Final", "Acerca de...", JOptionPane.INFORMATION_MESSAGE);
        }

        if (e.getSource() == PL.getBtnIngresar()) {
            String[] usuario = modeloUsuarios.getAcceso(PL.getTxtUsername().getText(), PL.getTxtPassword().getText());
            if (usuario[0].isEmpty()) {
                JOptionPane.showMessageDialog(PL, "EL usuario o contraseña no son válidos", "Acceso a Usuarios", JOptionPane.OK_OPTION);
            } else {
                if (usuario[2].equals("S")) {
                    IP.setTitle("Catálogo de Películas [Usuario: " + usuario[0] + ", Nombre: " + usuario[1] + ", Último acceso: " + usuario[3] + "]");
                    if (usuario[4].equals("operador")) {
                        IP.getjSeparator4().setVisible(false);
                        IP.getItemUsuarios().setVisible(false);
                    } else if (usuario[4].equals("administrador")) {
                        IP.getItemAsignaturas().setVisible(false);
                        IP.getItemCarreras().setVisible(false);
                        IP.getItemEstudiantes().setVisible(false);
                        IP.getjSeparator4().setVisible(false);
                    }
                    Dialogo.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(PL, "EL usuario no está habilitado", "Acceso a Usuarios", JOptionPane.OK_OPTION);
                }
            }
        }
        if (e.getSource() == PL.getBtnSalir()) {
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            PL.getBtnIngresar().doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
