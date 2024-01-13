/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.view.AeternusGUI;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author User
 */
public class LabyrinthEngineTest {
    AeternusGUI gui = new AeternusGUI();
    GameEngine game = new GameEngine(gui, 73915, false);
    LabyrinthEngine lab = new LabyrinthEngine(game, gui);
    
    @Test
    public void monsterSpawnTest(){
        assertTrue(lab.spawnMonster());
    }
    
    @Test
    public void viewPortMoverTest(){
        assertFalse(lab.moveViewport());
    }
    
    @Test
    public void playerSpawnTest(){
        assertNotNull(lab.spawnPlayer(null, 0, 0));
    }
}
