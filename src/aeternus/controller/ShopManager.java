/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.CustomLabel;
import aeternus.model.Item;
import aeternus.model.Weapon;
import aeternus.view.AeternusGUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MouseInfo;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class ShopManager {
    private AeternusGUI gui;
    private GameEngine game;
    private ArrayList<JLabel> shopMenu = new ArrayList<>();
    private String shopID = "";
    private PopupFloatingText pop;
    
    public ShopManager(AeternusGUI gui, GameEngine game, String shopID){
        this.gui = gui;
        this.game = game;
        this.shopID = shopID;
    }
    
    //Renders shop screen
    public void openShop(){
        pop = new PopupFloatingText(gui.findPanel("subMenu"), 35);
        JLabel shopBg = new JLabel();
        gui.labelFactory(shopBg, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
        gui.findPanel("subMenu").add(shopBg, 0);
        shopBg.setBounds(100, 100, 1720, 880);
        gui.setOptions(false);
        gui.setOptionsVisibility(false);
        shopMenu.add(shopBg);
        ArrayList<Item> stock = game.getStock(shopID);
        ArrayList<String[]> idList = game.getIds();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                CustomLabel shopSlot = new CustomLabel();
                JLabel slotGlow = new JLabel();
                gui.labelFactory(shopSlot, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
                if(stock.size() >= i*8 + j + 1){
                    shopSlot.setIcon(new ImageIcon(stock.get(i*8 + j).getImg().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
                    slotGlow.setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/rarity/" + stock.get(i*8 + j).getRarity() + ".png")).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
                    String id = stock.get(i*8 + j).getId();
                    for(String[] s : idList){
                        if(s[0].equals(id)){
                            String stat = decodeStat(stock.get(i*8 + j).getStat());
                            String flavor = "";
                            if(stock.get(i*8 + j) instanceof Weapon && stat.length() > 0){
                                flavor += "Scales from your " + stat;
                            }else if(stat.length() > 0){
                                flavor += "Increases your " + stat;
                            }
                            
                            shopSlot.setToolTipText("<html>" + s[1] + "<br>§" + stock.get(i*8 + j).getPrice() + "<br><b><em style='color: #" 
                                    + stock.get(i*8 + j).getColor() 
                                    + "'>" +  stock.get(i*8 + j).getRarity() 
                                    + "</em></b><br>" + flavor + "<html>");
                        }
                    }
                    int slotnum = i*8 + j;
                    shopSlot.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            purchaseItem(slotnum, shopID);
                        }
                    });
                }
                
                gui.findPanel("subMenu").add(shopSlot, 0);
                gui.findPanel("subMenu").add(slotGlow, 0);
                shopSlot.setName("slot" + j*8 + i);
                shopSlot.setBounds(190 + j*200, 150 + i*200, 150, 150);
                slotGlow.setBounds(190 + j*200, 150 + i*200, 150, 150);
                gui.setOptionsVisibility(false);
                shopMenu.add(shopSlot);
                shopMenu.add(slotGlow);
            }
        }
        JLabel exit = new JLabel();
        gui.labelFactory(exit, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                Color.white, 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 26));
        exit.setText("Leave");
        gui.findPanel("subMenu").add(exit, 0);
        exit.setBounds(760, 915, 400, 50);
        shopMenu.add(exit);
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitShop();
            }
        });
        
        JLabel refresh = new CustomLabel();
        gui.labelFactory(refresh, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                Color.white, 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 26));
        refresh.setToolTipText("<html>" + "This action will cost " + paidRefreshCost() + " souls." + "<html>");
        refresh.setText("Refresh Shop");
        gui.findPanel("subMenu").add(refresh, 0);
        refresh.setBounds(1400, 915, 400, 50);
        shopMenu.add(refresh);
        refresh.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(game.getSouls() > paidRefreshCost() && game.getInv().size() < 32){
                    game.alterSouls(-paidRefreshCost());
                    game.refreshShops();
                    for(JLabel j : shopMenu){
                        j.getParent().remove(j);
                    }
                    shopMenu.clear();
                    gui.setOptions(true);
                    gui.setOptionsVisibility(true);
                    gui.findPanel("subMenu").revalidate();
                    gui.findPanel("subMenu").repaint();
                openShop();
                }else if(game.getSouls() < paidRefreshCost()){
                    pop.spawnEffect("You cannot afford this.", false, MouseInfo.getPointerInfo().getLocation(), 50);
                }else{
                    pop.spawnEffect("You do not have inventory space!", false, MouseInfo.getPointerInfo().getLocation(), 50);
                }
            }
        });
        
        gui.findPanel("subMenu").revalidate();
        gui.findPanel("subMenu").repaint();
    }
    
    //Returns the price of a shop refresh when paid for.
    public double paidRefreshCost(){
        ArrayList<Item> stock = game.getStock(shopID);
        int out = 1;
        for(Item it : stock){
            out += it.getPrice();
        }
        return out *= Double.parseDouble(game.getStat("lvl"));
    }
    
    public void exitShop(){
        for(JLabel j : shopMenu){
            j.getParent().remove(j);
        }
        shopMenu.clear();
        gui.setOptions(true);
        gui.setOptionsVisibility(true);
        gui.findPanel("subMenu").revalidate();
        gui.findPanel("subMenu").repaint();
    }
    
    //Item purchase logic
    public Boolean purchaseItem(int slot, String shop){
        ArrayList<Item> stock = game.getStock(shop);
        ArrayList<Item> inv = game.getInv();
        if(game.getSouls() >= stock.get(slot).getPrice()){
            game.alterSouls(-stock.get(slot).getPrice());
            inv.add(stock.get(slot));
            stock.remove(slot);
            game.setInventory(inv);
            
            for(JLabel j : shopMenu){
                    j.getParent().remove(j);
                }
                shopMenu.clear();
                gui.setOptions(true);
                gui.setOptionsVisibility(true);
                gui.findPanel("subMenu").revalidate();
                gui.findPanel("subMenu").repaint();
            openShop();
            return true;
        }else{
            pop.spawnEffect("You cannot afford this.", false, MouseInfo.getPointerInfo().getLocation(), 50);
            return false;
        }
    }
    
    //Returns formal name of stat
    private String decodeStat(String st){
        String stat = "";
        switch(st){
            case "str":
                stat += "strength";
            break;
            case "con":
                stat += "constitution";
            break;
            case "dex":
                stat += "dexterity";
            break;
            case "int":
                stat += "intelligence";
            break;
            case "lck":
                stat += "luck";
            break;
        }
        return stat;
    }
    
    public void setAll(Boolean state){
        for(JLabel j : shopMenu){
            j.setVisible(state);
        }
    }
}
