package org.game;

import javax.swing.*;
import java.awt.*;

public class Game {
    public static void main(String[] args) {
        JFrame obj = new JFrame();
        Gameplay gameplay = new Gameplay(obj);
        Sound s = new Sound();

        obj.setBounds(0,0,907,700);
        obj.setBackground(Color.YELLOW);
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gameplay);
        s.bgm();
    }
}
