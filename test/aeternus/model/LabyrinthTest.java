/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import aeternus.controller.CombatEngine.enemy;
import java.util.Random;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author User
 */
public class LabyrinthTest {
    Labyrinth lab = new Labyrinth();
    
    @Test
    public void statTest(){
        Random rnd = new Random();
        Monster m = new Monster(0, 0, 50, 50, null, enemy.values()[rnd.nextInt(3)], null);
        assertTrue(lab.collides(m));
        m = new Monster(-100, -100, 50, 50, null, enemy.values()[rnd.nextInt(3)], null);
        assertFalse(lab.collides(m));
    };
}
