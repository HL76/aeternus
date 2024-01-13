/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class PopupFloatingText {
    private JPanel jp;
    private ArrayList<JLabel> effects = new ArrayList<JLabel>();
    private ArrayList<Integer> effectLifespan = new ArrayList<Integer>();
    
    public PopupFloatingText(JPanel jp, int speed){
        this.jp = jp;
        effectThread(speed);
    }
    
    //Renders floating effect
    public Boolean spawnEffect(String s, Boolean miss, Point p, int zone){
        JLabel j = new JLabel();
        Color c = ((miss) ? new Color(200,200,200) : new Color(200,0,0));
        j.setVisible(true);
        j.setHorizontalAlignment(SwingConstants.LEFT);
        j.setVerticalAlignment(SwingConstants.CENTER);
        j.setForeground(c);
        j.setText(s);
        j.setFont(new Font("Agency FB", 1, 30));
        Random rnd = new Random();
        j.setBounds(p.x + rnd.nextInt(zone)-(zone/2), p.y - rnd.nextInt(zone), 300, 50);
        jp.add(j);
        jp.setComponentZOrder(j, 0);
        effects.add(j);
        effectLifespan.add(40);
        if(jp.isAncestorOf(j)){
            return true;
        }
        return false;
    }
    
    //Starts thread in charge of handling effects
    private void effectThread(int speed){
        Runnable newThread = () -> {
            for(int i = 0; i < effects.size(); i++){
                effects.get(i).setLocation(effects.get(i).getLocation().x, effects.get(i).getLocation().y-1);
                if(effectLifespan.get(i) > 1){
                    effectLifespan.set(i, effectLifespan.get(i)-1);
                }else{
                    effects.get(i).getParent().remove(effects.get(i));
                    effects.remove(i);
                    effectLifespan.remove(i);
                }
                
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(newThread, 0, speed, TimeUnit.MILLISECONDS);
    }
}
