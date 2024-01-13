/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.view.AeternusGUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class EscapeMenu {
    private AeternusGUI gui;
    private GameEngine game;
    private JPanel j;
    private ArrayList<JLabel> escScreen = new ArrayList<>();
    
    public EscapeMenu(JPanel j, AeternusGUI gui, GameEngine game){
        this.gui = gui;
        this.game = game;
        this.j = j;
        addEsc();
    }
    
    //Adds escape button functionality
    private void addEsc(){
        j.requestFocus();
        j.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        j.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(escScreen.size() > 0){
                    escScreen.forEach(j -> {
                        j.getParent().remove(j);
                    });
                    escScreen.clear();
                    j.revalidate();
                    j.repaint();
                    if(gui.getLocationOptions().size() > 0){
                        gui.setOptions(true);
                    }else{
                        gui.setPOI(true);
                    }
                    gui.getInvLabel().setEnabled(true);
                }else{
                    if(!gui.getActiveLabyrinth()){
                        buildEscScreen(j);
                    }
                }
            }
        });
    }

    //Buidlds menu when button is clicked
    private void buildEscScreen(JPanel dest){
        JLabel bg = new JLabel();
        JLabel icon = new JLabel();
        JLabel savebttn = new JLabel();
        JLabel saveicon = new JLabel();
        JLabel exitbttn = new JLabel();
        JLabel confirmCard = new JLabel();
        JLabel confirmText = new JLabel();
        JLabel confirmbttn = new JLabel();
        JLabel backbttn = new JLabel();
        escScreen = new ArrayList<>(Arrays.asList(bg, icon, savebttn, saveicon, exitbttn, confirmCard, confirmText, confirmbttn, backbttn));
        escScreen.forEach(j -> {
            gui.labelFactory(j, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER},
                    new Color(255, 255, 255),
                    new Color(0, 0, 0, 150),
                    null,
                    new Font("Agency FB", 0, 30));
            dest.add(j, 0);
        });
        icon.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource(
                                                "/images/escapeMenuIcon.png")).getImage().getScaledInstance(280, 280, Image.SCALE_DEFAULT)));
        saveicon.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource(
                                                "/images/savingIcon.png")).getImage().getScaledInstance(140, 140, Image.SCALE_DEFAULT)));
        icon.setOpaque(false);
        bg.setBounds(0, 0, 1920, 1080);
        icon.setBounds(820, 80, 280, 280);
        savebttn.setBounds(840, 400, 240, 60);
        saveicon.setBounds(1760, 920, 140, 140);
        exitbttn.setBounds(840, 500, 240, 60);
        confirmCard.setBounds(610, 395, 670, 290);
        confirmText.setBounds(650, 420, 590, 90);
        confirmbttn.setBounds(660, 580, 240, 60);
        backbttn.setBounds(980, 580, 240, 60);
       
       //disable other clickables
       if(gui.getLocationOptions().size() > 0){
            gui.setOptions(false);
        }else{
            gui.setPOI(false);
        }
       
       if(gui.getShop() != null){
           gui.getShop().exitShop();
       }
       
       gui.getInvLabel().setEnabled(false);
       
       saveicon.setVisible(false);
       saveicon.setOpaque(false);
       savebttn.setText("Save");
       savebttn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    //save
                    saveicon.setVisible(true);
                    saveicon.paintImmediately(saveicon.getVisibleRect());
                    game.WriteSave();
                    Thread.sleep(1500);
                    saveicon.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource(
                            "/images/savingDoneIcon.png")).getImage().getScaledInstance(140, 140, Image.SCALE_DEFAULT)));
                    saveicon.paintImmediately(saveicon.getVisibleRect());
                    Thread.sleep(500);
                    saveicon.setVisible(false);
                    saveicon.getParent().revalidate();
                    saveicon.getParent().repaint();
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(EscapeMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
       exitbttn.setText("Exit game");
       exitbttn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                escScreen.forEach(j -> {
                    j.setVisible(false);
                });
                bg.setVisible(true);
                confirmCard.setVisible(true);
                confirmText.setVisible(true);
                confirmbttn.setVisible(true);
                backbttn.setVisible(true);
            }
        });
       confirmText.setText("Are you sure you would like to quit?");
       confirmbttn.setText("Quit");
       backbttn.setText("Back");
       confirmCard.setVisible(false);
       confirmText.setVisible(false);
       confirmbttn.setVisible(false);
       backbttn.setVisible(false);
       backbttn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                escScreen.forEach(j -> {
                    j.setVisible(true);
                });
                confirmCard.setVisible(false);
                confirmText.setVisible(false);
                confirmbttn.setVisible(false);
                backbttn.setVisible(false);
                saveicon.setVisible(false);
            }
        });
       
       confirmbttn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.exit(0);
            }
        });
       
    }
}
