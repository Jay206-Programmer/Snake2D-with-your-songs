package org.game;

import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

public class Sound {
    File f = new File("song");
    private String[] SNames ;
    private void createplaylist(){
        SNames = f.list();
    }
    private static Player p;
    private int temp1=-1,temp2=-1,temp3=-1,temp4=-1,var;

    public Sound(){
        createplaylist();
    }

    public void enemyhit(){
            try{
                FileInputStream fis = new FileInputStream("ball_by_ball.mp3");
                Player p1 = new Player(fis);
                p1.play();
            }catch(Exception e){
                e.printStackTrace();
            }
    }

    public void bgm(){
        try{
            FileInputStream fis = new FileInputStream("song/" + randsong());
            p = new Player(fis);
            p.play();
            bgm();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String randsong(){
        Random r = new Random();
        var = r.nextInt(SNames.length); //eknu ek song repeat na thaay etla maate
        while(var==temp1 || var==temp2 || var==temp3 || var==temp4){
            var = r.nextInt(SNames.length);
        }
        temp4=temp3;
        temp3=temp2;
        temp2=temp1;
        temp1=var;
        return SNames[var];
    }

    public void playnext(){
        p.close();

    }
}
