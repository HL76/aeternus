/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import java.awt.Point;
import javax.swing.JPanel;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author User
 */
public class PopUpTest {
    JPanel dummy = new JPanel();
    PopupFloatingText p = new PopupFloatingText(dummy, 30);
    
    @Test
    public void popUpCreationTest(){
        assertTrue(p.spawnEffect("text", Boolean.TRUE, new Point(10,10), 10));
    };

}
