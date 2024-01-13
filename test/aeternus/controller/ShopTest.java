/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.view.AeternusGUI;
import javax.swing.JPanel;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author User
 */
public class ShopTest {
    AeternusGUI gui;
    GameEngine game;
    ShopManager s;
    
    @Before
    public void setUp(){
        gui = new AeternusGUI();
        game = new GameEngine(gui, 73915, false);
        s = new ShopManager(gui, game, "MAGICSHOP");
        game.alterSouls(100000000);
        gui.createNewPanel("subMenu", 0);
    }
    
    @Test
    public void testLocation(){
        assertNotNull(GameEngine.locations.SQUARE);
        assertNotNull(GameEngine.locations.SQUARE.getPOI());
        assertEquals("/images/Locations/menuBackground.png", GameEngine.locations.SQUARE.getPath());
    };
    
    @Test
    public void purchaseTest(){
        assertTrue(s.purchaseItem(0, "MAGICSHOP"));
    }
}
