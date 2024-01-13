/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class InfoText {
    private JLabel infoText = new javax.swing.JLabel();
    private JPanel destination;
    public InfoText(JPanel destination) throws InterruptedException{
        this.destination = destination;
    }
    
    public void createText(String x, int length) throws InterruptedException{
        infoText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoText.setForeground(new java.awt.Color(204, 204, 204));
        infoText.setOpaque(true);
        infoText.setVisible(true);
        infoText.setText(x);
        infoText.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        infoText.setFont(new java.awt.Font("Agency FB", 0, 48));
        infoText.setBackground(new Color(30, 30, 30));
        destination.add(infoText);
        destination.setComponentZOrder(infoText, 0);
        infoText.setBounds(610, 465, 700, 150);
        Thread.sleep(length);
        destination.remove(infoText);
        destination.revalidate();
        destination.repaint();
    }    
    
}
