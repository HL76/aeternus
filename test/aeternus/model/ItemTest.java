/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import aeternus.controller.GameEngine;
import aeternus.view.AeternusGUI;
import java.util.ArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author User
 */
public class ItemTest {
    AeternusGUI gui = new AeternusGUI();
    GameEngine game = new GameEngine(gui, 73915, false);
    Item e;
    ArrayList<Item> inv;
    
    @Before
    public void setUp(){
        e = game.createItem(new String[]{"004", "rare"});
        inv = game.getInv();
        inv.add(e);
        game.setInventory(inv);
    }
    
    @Test
    public void stockItemEquipTest(){
        assertTrue(e.equip(inv.size()-1));
    }
    
    @Test
    public void charmEquipTest(){
        e = game.createItem(new String[]{"029", "epic"});
        inv = game.getInv();
        inv.add(e);
        game.setInventory(inv);
        assertTrue(e.equip(inv.size()-1));
    }
}
