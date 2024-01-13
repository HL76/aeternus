/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.CustomLabel;
import aeternus.view.AeternusGUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MouseInfo;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class EngineMenu {
    private GameEngine game;
    private AeternusGUI gui;
    private PopupFloatingText pop;
    private JLabel engine = new JLabel();
    private ArrayList<JLabel> upgradeOptions = new ArrayList<JLabel>();
    
    public EngineMenu(AeternusGUI gui, GameEngine game){
        this.gui = gui;
        this.game = game;
        showEngineMenu();
    }
    
    //Renders menu for engine
    private void showEngineMenu(){
        pop = new PopupFloatingText(gui.findPanel("subMenu"), 15);
        gui.labelFactory(engine, false, true, new int[]{SwingConstants.LEFT, SwingConstants.TOP}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
        gui.findPanel("subMenu").add(engine,0 );
        engine.setBounds(200, 320, 500, 500);
        engine.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource(
                                                "/images/ENGINE.png")).getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
        engine.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                game.alterSouls(game.getSoulPower());
                gui.refreshSouls();
                pop.spawnEffect(String.valueOf(game.getSoulPower()), true, MouseInfo.getPointerInfo().getLocation(), 50);
            }
        });
        ArrayList<String[]> upgrades = game.getEngineUpgrades();
        int cnt = 0;
        for(String[] row : upgrades){
            addUpgradeOption(row[0], cnt, Double.parseDouble(row[2]), row[4], Double.parseDouble(row[3]));
            cnt++;
        }
    }
    
    //Adds the upgrade options
    private void addUpgradeOption(String name, int place, double price, String desc, double power){
        CustomLabel newL = new CustomLabel();
        gui.labelFactory(newL, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                Color.lightGray, 
                new Color(40, 40, 40), 
                null, 
                new Font("Agency FB", 0, 28));
        if(name.equals("Leave")){
            newL.setText(name);
        }else{
            newL.setText("<html><div style='text-align: center'>" + name + "<br>" + String.format("%.1f", price) + "<div><html>");
        }
        newL.setName(String.valueOf(price));
        newL.setToolTipText("<html>" + power + " Souls per Second<br>" + desc + "<html>");
        newL.setEnabled((game.getSouls()- price) >= 0);
        gui.findPanel("subMenu").add(newL);
        gui.findPanel("subMenu").setComponentZOrder(newL, 0);
        newL.setBounds(800, 100 + (place*120), 1000, 90);
        upgradeOptions.add(newL);
        newL.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(newL.isEnabled() && !newL.getText().equals("Leave")){
                    buyUpgrade(name);
                }else if(newL.getText().equals("Leave")){
                    for(JLabel j : upgradeOptions){
                        j.getParent().remove(j);
                    }
                    engine.getParent().remove(engine);
                    gui.setOptions(true);
                    gui.setOptionsVisibility(true);
                    gui.closeEngine();
                }
            }
        });
    }
    
    //Logic for buying upgrades
    private void buyUpgrade(String name){
        int indx = 0;
        for(String[] row : game.getEngineUpgrades()){
            if(row[0].equals(name)){
                int x = Integer.parseInt(row[1]);
                x++;
                game.alterSouls(-Double.parseDouble(row[2]));
                row[2] = String.valueOf(Double.parseDouble(row[2]) * 1.15);
                upgradeOptions.get(indx).setText("<html><div style='text-align: center'>" + name + "<br>" + String.format("%.1f", Double.parseDouble(row[2])) + "<div><html>");
                row[1] = String.valueOf(x);
                upgradeOptions.get(indx).setName(row[2]);
                upgradeOptions.get(indx).getParent().revalidate();
                upgradeOptions.get(indx).getParent().repaint();
            }
            indx++;
        }
    }
    
    //Refreshes upgrade options based on availability
    public void refreshUpgrades(){
        for(JLabel j : upgradeOptions){
            j.setEnabled((game.getSouls() - Double.parseDouble(j.getName())) >= 0);
            if(!j.isEnabled()){
                j.setBackground(new Color(30,30,30));
            }else{
                j.setBackground(new Color(40,40,40));
            }
        }
    }
}
