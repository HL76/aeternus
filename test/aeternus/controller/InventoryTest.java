/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.Item;
import aeternus.view.AeternusGUI;
import java.util.ArrayList;
import javax.swing.JPanel;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author User
 */
public class InventoryTest {
    AeternusGUI gui = new AeternusGUI();
    GameEngine game = new GameEngine(gui, 73915, false);
    InventoryManager i;
    JPanel dummy = new JPanel();
    ArrayList<Item> inv;
    Item e;
    
    @Before
    public void setup(){
        inv = game.getInv();
        Item e = game.createItem(new String[]{"005", "rare"});
        inv.add(e);
        i = new InventoryManager(gui, game);
        i.showInventory(dummy, "");
    }
    
    @Test
    public void itemDestroyerTest(){
        assertTrue(i.destroyItem(inv.size()-1, dummy));
    };
    
    @Test
    public void itemRemoverTest(){
        Item e = game.createItem(new String[]{"005", "rare"});
        i.equipItem(inv.size()-1, dummy);
        assertTrue(i.removeItem(e, dummy));
    }
}
