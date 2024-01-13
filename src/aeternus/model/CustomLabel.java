/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

/**
 *
 * @author User
 */
public class CustomLabel extends JLabel {

   public CustomLabel() {
   }

   @Override
   public JToolTip createToolTip() {
      return new MyCustomToolTip(this);
   }
   
   @Override
   public Point getToolTipLocation(MouseEvent e) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        p.x -= e.getComponent().getLocationOnScreen().x;
        p.y -= e.getComponent().getLocationOnScreen().y;
        return p;
   }
}

class MyCustomToolTip extends JToolTip {
   public MyCustomToolTip(JComponent component) {
        super();
        setComponent(component);
        setBackground(Color.darkGray);
        setForeground(Color.lightGray);
        setFont(new java.awt.Font("Agency FB", 0, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ToolTipManager.sharedInstance().setInitialDelay(0);
   }
}
