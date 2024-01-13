/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.view;

import aeternus.controller.GameEngine;
import aeternus.controller.GameEngine.interactables;
import aeternus.controller.GameEngine.locations;
import aeternus.model.Item;
import java.util.ArrayList;
import java.util.Random;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author User
 */
public class EngineTest {
    AeternusGUI aeg = new AeternusGUI();
    GameEngine game = new GameEngine(aeg, 73915, false);
    
    @Test
    public void testLocation(){
        assertNotNull(locations.SQUARE);
        assertNotNull(locations.SQUARE.getPOI());
        assertEquals("/images/Locations/menuBackground.png", locations.SQUARE.getPath());
    };
    
    @Test
    public void testInteractables(){
        assertNotNull(interactables.BASE);
        assertNotNull(interactables.BASE.getOptions());
        assertTrue(interactables.BASE.addOptions(0, new String[]{}));
        
    };
    
    @Test
    public void flagTest(){
        assertNull(game.getFlag("asd"));
        assertTrue(game.setFlag("Merge Items", "rnd"));
    };
    
    @Test
    public void listTranslatorTest(){
        ArrayList<Item> items = new ArrayList<>();
        items.add(game.genRandomItem("MAGICSHOP", new Random(20)));
        items.add(game.genRandomItem("WEAPONSHOP", new Random(20)));
        
        assertNotNull(game.itemToList(items));
        assertEquals(2, game.itemToList(items).size());
    };
    
    @Test
    public void getArmorTest(){
        Item[] gear = new Item[]{game.createItem(new String[]{"009","rare"}), game.createItem(new String[]{"014","uncommon"})};
        game.setGear(gear);
        assertEquals(15, game.getArmor());
    };
    
    @Test
    public void endReqTest(){
        game.alterSouls(100000000);
        Item[] gear = new Item[]{game.createItem(new String[]{"009","rare"}), game.createItem(new String[]{"014","uncommon"})};
        game.setGear(gear);
        assertFalse(game.endReq());
        gear = new Item[]{game.createItem(new String[]{"009","legendary"}), 
            game.createItem(new String[]{"014","legendary"}),
            game.createItem(new String[]{"002","legendary"}),
            game.createItem(new String[]{"026","legendary"}),
            game.createItem(new String[]{"027","legendary"})};
        game.setGear(gear);
        assertTrue(game.endReq());
    };
    
    @Test
    public void statTest(){
        assertEquals("10", game.getStat("asd"));
    };
    
    @Test
    public void randomItemTest(){
        Item x = game.genRandomItem("MAGICSHOP", new Random(20));
        assertNotNull(x);
    };
    
    @Test
    public void genericStatTest(){
        game.setGenericStat("LabyrinthsWon", 0);
        assertEquals(0, game.getGenericStat("LabyrinthsWon"));
    };
    
    @Test
    public void itemCreator(){
        Item x = game.createItem(new String[]{"000", "common"});
        assertNull(x);
        x = game.createItem(new String[]{"001", "common"});
        assertNotNull(x);
        assertEquals("common", x.getRarity());
    };
    
    @Test
    public void readTest(){
        ArrayList<Item> inv = game.readInv("saves/playerInventory");
        game.setInventory(inv);
        assertEquals(game.getInv(), inv);
    };
    
    
}
