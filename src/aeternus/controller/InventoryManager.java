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
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class InventoryManager {
    private AeternusGUI aeg;
    private ArrayList<JLabel> inventoryMenu = new ArrayList<>();
    private GameEngine game;
    private String mode;
    private PopupFloatingText pop;
    
    public InventoryManager(AeternusGUI aeg, GameEngine game){
        this.aeg = aeg;
        this.game = game;
    }
    
    public ArrayList<JLabel> getInventoryMenu() {
        return inventoryMenu;
    }
    
    //Renders Inventory
    public void showInventory(JPanel dest, String b){
        pop = new PopupFloatingText(dest, 35);
        inventoryMenu.clear();
        game.calculateStats();
        mode = b;
        JLabel invBg = new JLabel();
        JLabel invAvatar = new JLabel();
        CustomLabel invHelmet = new CustomLabel();
        CustomLabel invChestplate = new CustomLabel();
        CustomLabel invWeapon = new CustomLabel();
        CustomLabel invMagicItem1 = new CustomLabel();
        CustomLabel invMagicItem2 = new CustomLabel();
        JLabel invXp = new JLabel();
        JLabel warningLabel = new JLabel();
        JLabel invStatBox = new JLabel();
        JLabel helmOverlay = new JLabel();
        JLabel chestOverlay = new JLabel();
        JLabel weaponOverlay = new JLabel();
        JLabel charm1Overlay = new JLabel();
        JLabel charm2Overlay = new JLabel();
        CustomLabel strLabel = new CustomLabel();
        CustomLabel conLabel = new CustomLabel();
        CustomLabel dexLabel = new CustomLabel();
        CustomLabel intLabel = new CustomLabel();
        CustomLabel lckLabel = new CustomLabel();
        inventoryMenu = new ArrayList<>(List.of(invBg, invAvatar, invHelmet, invChestplate, invWeapon, invMagicItem1, invMagicItem2, invXp, invStatBox, 
                strLabel, conLabel, dexLabel, intLabel, lckLabel, warningLabel, helmOverlay, chestOverlay, weaponOverlay, charm1Overlay, charm2Overlay));
        
        for(JLabel j : inventoryMenu){
            aeg.labelFactory(j, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
            dest.add(j, 0);
        }
        
        //background
        invBg.setBounds(100, 100, 1720, 880);
        dest.add(invBg, 0);
        
        //player img
        invAvatar.setBounds(140, 140, 300, 300);
        invAvatar.setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/PLAYER.png")).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT)));
        invAvatar.getParent().setComponentZOrder(invAvatar, 0);
        
        //equipment
        invHelmet.setBounds(460, 140, 140, 140);
        invChestplate.setBounds(460, 300, 140, 140);
        invWeapon.setBounds(460, 460, 140, 140);
        invMagicItem1.setBounds(140, 460, 140, 140);
        invMagicItem2.setBounds(300, 460, 140, 140);
        ArrayList<CustomLabel> gear = new ArrayList<>();
        ArrayList<JLabel> gearGlow = new ArrayList<>();
        gear.add(invHelmet);
        gear.add(invChestplate);
        gear.add(invWeapon);
        gear.add(invMagicItem1);
        gear.add(invMagicItem2);
        gearGlow.add(helmOverlay);
        gearGlow.add(chestOverlay);
        gearGlow.add(weaponOverlay);
        gearGlow.add(charm1Overlay);
        gearGlow.add(charm2Overlay);
        int cnt = 0;
        for(CustomLabel custom : gear){
            custom.getParent().setComponentZOrder(custom, 0);
            gearGlow.get(cnt).getParent().setComponentZOrder(gearGlow.get(cnt), 0);
            gearGlow.get(cnt).setOpaque(false);
            gearGlow.get(cnt).setBounds(custom.getBounds());
            if(game.getEquipped()[cnt] != null){
                Item x = game.getEquipped()[cnt];
                custom.setIcon(new ImageIcon(game.getEquipped()[cnt].getImg().getScaledInstance(140, 140, Image.SCALE_DEFAULT)));
                gearGlow.get(cnt).setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/rarity/" + x.getRarity() + ".png")).getImage().getScaledInstance(140, 140, Image.SCALE_DEFAULT)));
                for(String[] s : game.getIds()){
                        if(s[0].equals(game.getEquipped()[cnt].getId())){
                            if(mode.length() == 0){
                                String stat = decodeStat(x.getStat());
                                String flavor = "";
                                if(x instanceof Weapon && stat.length() > 0){
                                    flavor += "Scales from your " + stat;
                                }else if(stat.length() > 0){
                                    flavor += "Increases your " + stat;
                                }

                                custom.setToolTipText("<html>" + s[1] + "<br><b><em style='color: #" 
                                        + x.getColor() 
                                        + "'>" +  x.getRarity() 
                                        + "</em></b><br>" + flavor + "<html>");
                            }else if(mode.equals("burn")){
                                custom.setToolTipText("<html>" + "You cannot burn items that you are wearing!" + "<html>");
                            }else if(mode.equals("merge")){
                                custom.setToolTipText("<html>" + "You cannot merge items that you are wearing!" + "<html>");
                            }
                        }
                }
                custom.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if(mode.length() == 0){
                            removeItem(x, dest);
                        }
                    }
                });
            }else{
                custom.setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/items/itemImages/emptyGear.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
                String type = "";
                switch(cnt){
                    case 0:
                        type += "Helmet";
                    break;
                    case 1:
                        type += "Chestplate";
                    break;
                    case 2:
                        type += "Weapon";
                    break;
                    case 3:
                        type += "#1 Charm";
                    break;
                    case 4:
                        type += "#2 Charm";
                    break;
                }
                custom.setToolTipText("<html>" + type + " Slot" +"<html>");
            }
            cnt++;
        }
        
        //xp bar
        invXp.setBounds(140, 620, 460, 60);
        invXp.setText("Level: " + game.getStat("lvl") + ((game.getPoints() > 0) ? "(Avalable points: " + game.getPoints() + ")" : ""));
        invXp.getParent().setComponentZOrder(invXp, 0);
        
        //stats
        invStatBox.setBounds(140, 700, 460, 240);
        invStatBox.getParent().setComponentZOrder(invStatBox, 0);
        
        for(int i = 0; i < 5; i++){
            inventoryMenu.get(i+9).setBounds(140, 700+(i*50), 460, 40);
            inventoryMenu.get(i+9).getParent().setComponentZOrder(inventoryMenu.get(i+9), 0);
        }
        
        String points = "";
        if(game.getPoints() > 0){
            points += "+";
            for(int i = 0; i < 5; i++){
                AtomicInteger idx = new AtomicInteger();
                idx.set(i);
                inventoryMenu.get(i+9).addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        game.addStat(idx.get());
                        hideInventory(dest);
                        showInventory(dest, mode);
                    }
                });
            }
        }
        
        strLabel.setText("Strength: " + game.getStat("str") + points);
        conLabel.setText("Constitution: " + game.getStat("con") + points);
        dexLabel.setText("Dexterity: " + game.getStat("dex") + points);
        intLabel.setText("Intelligence: " + game.getStat("int") + points);
        lckLabel.setText("Luck: " + game.getStat("lck") + points);
        
        strLabel.setToolTipText("<html>" + "Your strength determines your chance at withstanding an attack. The formula is as follows:<br> Strength / 5" + "<html>");
        conLabel.setToolTipText("<html>" + "Your constitution directly correlates with your hp. It is a 1:1 ratio." +"<html>");
        dexLabel.setToolTipText("<html>" + "Your dexterity determines how well you can dodge attacks. The formula for evasion is as follows:<br> Dexterity / 5" +"<html>");
        intLabel.setToolTipText("<html>" + "Your intelligence determines how you pick your attack spots. The higher it is the bigger the critical damage you deal. <br>Each point correlates to a % above 100" +"<html>");
        lckLabel.setToolTipText("<html>" + "Your luck determines the chance at a critical strike on your opponent. The formula is as follows:<br> Luck / Monster level." +"<html>");
        
        //inventory
        ArrayList<String[]> idList = game.getIds();
        ArrayList<Item> inv = game.getInv();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                CustomLabel invSlot = new CustomLabel();
                JLabel invGlow = new JLabel();
                aeg.labelFactory(invSlot, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
                if(inv.size() >= i*6 + j + 1){
                    invSlot.setIcon(new ImageIcon(inv.get(i*6 + j).getImg().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
                    invGlow.setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/rarity/" + inv.get(i*6 + j).getRarity() + ".png")).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
                    Item item = inv.get(i*6 + j);
                    for(String[] s : idList){
                        if(s[0].equals(item.getId())){
                            String stat = decodeStat(item.getStat());
                            String flavor = "";
                            if(item instanceof Weapon && stat.length() > 0){
                                flavor += "Scales from your " + stat;
                            }else if(stat.length() > 0){
                                flavor += "Increases your " + stat;
                            }
                            
                            invSlot.setToolTipText("<html>" + s[1] + "<br><b><em style='color: #" 
                                    + item.getColor() 
                                    + "'>" +  item.getRarity() 
                                    + "</em></b><br>" + flavor + "<html>");
                            
                        }
                    }
                }
                invSlot.setName(String.valueOf(i*6 + j));
                invSlot.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if(invSlot.getIcon() != null){
                            if(mode.equals("burn")){
                                destroyItem(Integer.parseInt(invSlot.getName()), dest);
                            }else if(mode.equals("merge")){
                                openMerge(Integer.parseInt(invSlot.getName()), dest);
                            }else{
                                equipItem(Integer.parseInt(invSlot.getName()), dest);
                            }
                            
                        }
                    }
                });
                
                dest.add(invSlot, 0);
                dest.add(invGlow, 0);
                invSlot.setBounds(620 + j*200, 160 + i*200, 150, 150);
                invGlow.setBounds(620 + j*200, 160 + i*200, 150, 150);
                inventoryMenu.add(invSlot);
                inventoryMenu.add(invGlow);
            }
        }
        
        //warning label
        String warningMessage = "";
        if(mode.equals("burn")){
            warningMessage += "Careful! Any item you click in your inventory is destroyed forever!";
        }else if(mode.equals("merge")){
            warningMessage += "Pick an item that you will bring onto the merge screen";
        }else if(game.getInv().size() > 20){
            if(warningMessage.length() == 0){
                warningMessage += "Your inventory space is running out. Consider burning or merging some items.";
            }
        }
        warningLabel.setText(warningMessage);
        warningLabel.setBackground(new Color(50,0,0,150));
        warningLabel.setOpaque(true);
        warningLabel.setBounds(455, 50, 1010, 80);
        warningLabel.setVisible(warningMessage.length() > 0);
        dest.add(warningLabel, 0);
        
        if(aeg.getLocationOptions().size() > 0){
            aeg.setOptions(false);
        }else{
            aeg.setPOI(false);
        }
        
        dest.revalidate();
        dest.repaint();
    }
    
    //Shows merge option for inventory
    
    private ArrayList<JLabel> mergeScreen = new ArrayList<>();
    private void openMerge(int itemIdx, JPanel destination){
        //find match
        ArrayList<Item> inv = game.getInv();
        Item item1 = inv.get(itemIdx);
        Item item2 = null;
        
        for(Item i : inv){
            if(i.getName().equals(item1.getName()) && i.getRarity().equals(item1.getRarity()) && inv.indexOf(i) != itemIdx){
                item2 = i;
            }
        }
        if(item2 == null){
            pop.spawnEffect("You need at least 2 of this item!", false, MouseInfo.getPointerInfo().getLocation(), 50);
            return;
        }

        JLabel mBg = new JLabel();
        JLabel it1 = new JLabel();
        JLabel it2 = new JLabel();
        JLabel soul = new JLabel();
        JLabel bttn = new JLabel();
        mergeScreen.add(mBg);
        mergeScreen.add(it1);
        mergeScreen.add(it2);
        mergeScreen.add(soul);
        mergeScreen.add(bttn);
        it1.setIcon(new ImageIcon(item1.getImg().getScaledInstance(240, 240, Image.SCALE_DEFAULT)));
        it2.setIcon(new ImageIcon(item2.getImg().getScaledInstance(240, 240, Image.SCALE_DEFAULT)));
        for(JLabel j : mergeScreen){
            aeg.labelFactory(j, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
            destination.add(j, 0);
        }
        mBg.setBounds(100, 255, 1720, 570);
        it1.setBounds(490, 380, 240, 240);
        it2.setBounds(1170, 380, 240, 240);
        soul.setBounds(130, 280, 1660, 70);
        bttn.setBounds(730, 740, 460, 70);
        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(bttn.getText().equals("Merge")){
                    attemptMerge(item1);
                }else{
                    for(JLabel j : mergeScreen){
                        j.getParent().remove(j);
                    }
                    mergeScreen.clear();
                    destination.revalidate();
                    destination.repaint();
                    aeg.setOptions(true);
                }
            }
        });
        int price = (item1.getRarityTier()+1) * 5000;
        if(game.getSouls() >= price){
            soul.setText(String.valueOf(price));
            bttn.setText("Merge");
        }else{
            soul.setForeground(new Color(200,0,0));
            soul.setText(String.valueOf(price));
            bttn.setText("Insufficient Souls");
            inv.add(item1);
            inv.add(item2);
        }
        
        
        for(JLabel j : inventoryMenu){
            j.setVisible(false);
        }
        inv.remove(item1);
        inv.remove(item2);
        game.setInventory(inv);
        
        mBg.getParent().revalidate();
        mBg.getParent().repaint();
    }
    
    //This logic drives the item merging process
    public void attemptMerge(Item item1){
        Random rnd = new Random();
        int chance = 25 + game.getGenericStat("ItemsMerged");
        mergeScreen.get(2).setVisible(false);
        mergeScreen.get(1).setBounds(800, 350, 330, 320);
        if(rnd.nextInt(100) <= chance){
            //Success
            game.setGenericStat("ItemsMerged", game.getGenericStat("ItemsMerged")+1);
            item1.boostRarity();
            mergeScreen.get(4).setText("Success!");
        }else{
            //Failure
            mergeScreen.get(4).setText("Failure.");
        }
        ArrayList<Item> inv = game.getInv();
        inv.add(item1);
        game.setInventory(inv);
    }
    
    //Removes any item from the player inventory and adds souls for compensation
    public Boolean destroyItem(int itemIdx, JPanel destination){
        ArrayList<Item> inv = game.getInv();
        game.calcBurn(inv.get(itemIdx));
        int before = inv.size();
        inv.remove(itemIdx);
        hideInventory(destination);
        showInventory(destination, mode);
        if(inv.size() < before){
            return true;
        }
        return false;
    }
    
    public void hideInventory(JPanel origin){
        pop = null;
        for(JLabel j : getInventoryMenu()){
            j.getParent().remove(j);
        }
        if(aeg.getLocationOptions().size() > 0){
            aeg.setOptions(true);
        }else{
            aeg.setPOI(true);
        }
        getInventoryMenu().clear();
        origin.revalidate();
        origin.repaint();
    }
    
    //Returns the formal name of stats
    public String decodeStat(String st){
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
    
    public Boolean removeItem(Item gear, JPanel destination){
        ArrayList<Item> inv = game.getInv();
        int before = inv.size();
        inv.add(gear);
        Item[] gearSetup = game.getEquipped();
        for(int i = 0; i < gearSetup.length; i++){
            if(gearSetup[i] != null && gearSetup[i].equals(gear)){
                gearSetup[i] = null;
            }
        }
        game.setEquipped(gearSetup);
        hideInventory(destination);
        showInventory(destination, mode);
        if(before < inv.size()){
            return true;
        }
        return false;
    }
    
    public void equipItem(int slot, JPanel destination){
        Item x = game.getInv().get(slot);
        x.equip(slot);
        hideInventory(destination);
        showInventory(destination, mode);
    }
}
