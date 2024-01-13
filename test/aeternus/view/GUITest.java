package aeternus.view;

import javax.swing.JPanel;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class GUITest {
    AeternusGUI aeg = new AeternusGUI();

    @Test
    public void testPanelFinder(){
        assertNull(aeg.findPanel("xyz"));
        assertNotNull(aeg.findPanel("Main"));
    };
    
    @Test
    public void testBackgroundCreator(){
        JPanel x = new JPanel();
        assertNotNull(aeg.createBackground(x, 0));
    };
    
    @Test
    public void testTransitionCreator(){
        JPanel x = new JPanel();
        assertNotNull(aeg.createTransition(x));
    };
}
