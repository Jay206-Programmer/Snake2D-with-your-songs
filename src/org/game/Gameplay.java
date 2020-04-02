package org.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Random;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
    private int[] snakexlength = new int[750];
    private int[] snakeylength = new int[750];
    private int countx = (850-25)/25;
    private int county = (625-75)/25;
    private int[] enemyxpos = new int[countx];
    private int[] enemyypos = new int[county];

    private int lengthofsnake =3;
    private int moves=0;
    private int score = 0;
    private int highscore = 0;

    private boolean gamefin = false;

    private FileReader fr;
    private FileWriter fw;

    {
        try {
            fw = new FileWriter("High Score.txt",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader br;
    private BufferedWriter bw = new BufferedWriter(fw);

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    private ImageIcon rightmouth;
    private ImageIcon leftmouth;
    private ImageIcon upmouth;
    private ImageIcon downmouth;
    private ImageIcon titleImage;
    private ImageIcon snakeimage;
    private ImageIcon enemy;

    private Timer timer;
    private int delay=60;

    private Random r = new Random();
    private int xpos = r.nextInt(countx);
    private int ypos = r.nextInt(county);

    public void enemycollide() { //snake ni body ma enemy nathi place thato te chack karva.
        boolean temp = false;
        for (int i = 1; i < lengthofsnake; i++) {
            if ((enemyxpos[xpos] == snakexlength[i]) && (enemyypos[ypos] == snakeylength[i])) {
                xpos = r.nextInt(countx);
                ypos = r.nextInt(county);
                temp = true;
                enemycollide();
                break;
            }
        }
    }

    private JFrame obj; //jaroor nathi but snake right to left aave chhe tyaare background ma change rahi jaay chhe...tena maate.
    private Sound s = new Sound();

    public Gameplay(JFrame obj){
        this.obj = obj;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        createenemypos();
        timer = new Timer(delay, this);
        timer.start();//it will implicitely call actionPerformed method once.

    }

    public void createenemypos(){
        int cnt=25;
        for(int i = 0;i<countx;i++){

            enemyxpos[i] = cnt;
            cnt+=25;
        }
        cnt=75;
        for(int i = 0;i<county;i++){

            enemyypos[i] = cnt;
            cnt+=25;
        }
    }

    public void paint(Graphics g){  //starting ma paint method ek vaar call thase pachhi fari call karva repaint() use thase
        if(moves==0){               //initially snake mouth and body parts ni position chhe aa.
            snakexlength[2] = 350;
            snakexlength[1] = 375;
            snakexlength[0] = 400;

            snakeylength[2] = 300;
            snakeylength[1] = 300;
            snakeylength[0] = 300;

            right=true;
            left=false;
            up=false;
            down=false;
        }
        //set background again
        obj.setBackground(Color.YELLOW);

        //Draw title image border
        g.setColor(Color.BLACK);
        g.drawRect(20,10,851,55);

        //Draw the title image
        titleImage = new ImageIcon("snaketitle.jpg");
        titleImage.paintIcon(this,g,21,11);

        //Border for play area
        g.setColor(Color.BLACK);
        g.drawRect(20,74,851,576);

        //Draw Background for gameplay
        g.setColor(Color.BLACK);
        g.fillRect(21,75,850,575);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Segoe Print", Font.BOLD,15));
        g.drawString("Score: "+ score,780,31);
        g.setColor(Color.white);
        g.setFont(new Font("Segoe Print", Font.BOLD,15));
        g.drawString("Score: "+ score,778,31);

        if(score>highscore){
            highscore=score;
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Segoe Print", Font.BOLD,15));
        g.drawString("High Score: "+ highscore,740,55);
        g.setColor(Color.white);
        g.setFont(new Font("Segoe Print", Font.BOLD,15));
        g.drawString("High Score: "+ highscore,738,55);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Segoe Print", Font.BOLD,16));
        g.drawString("Press N for next song",43,44);
        g.setColor(Color.white);
        g.setFont(new Font("Segoe Print", Font.BOLD,16));
        g.drawString("Press N for next song",41,44);

        rightmouth = new ImageIcon("rightmouth.png");      //snake ni starting position initialize karie chhie.
        rightmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);   //snakexlength and snakeylength ae snake na body circles ni location hase
                                                                    //array ni 0th location par head hase.. baaki body hase.. head currently kai taraf point kare chhe ae maate ni condition aapde niche loop ma darshaavyu chhe.
        //deciding snake mouth direction
        for(int i =0;i<lengthofsnake;i++){
            if(i==0 && right){
                rightmouth = new ImageIcon("rightmouth.png");
                rightmouth.paintIcon(this,g,snakexlength[i],snakeylength[i]);
            }
            else if(i==0 && left){
                leftmouth = new ImageIcon("leftmouth.png");
                leftmouth.paintIcon(this,g,snakexlength[i],snakeylength[i]);
            }
            else if(i==0 && down){
                downmouth = new ImageIcon("downmouth.png");
                downmouth.paintIcon(this,g,snakexlength[i],snakeylength[i]);
            }
            else if(i==0 && up){
                upmouth = new ImageIcon("upmouth.png");
                upmouth.paintIcon(this,g,snakexlength[i],snakeylength[i]);
            }

            if(i!=0){
                    snakeimage = new ImageIcon("snakeimage.png");
                    snakeimage.paintIcon(this,g,snakexlength[i],snakeylength[i]);
            }
        }

        //optional changing colour on keypress
        backchange();

        enemy = new ImageIcon("enemy.png");
        enemycollide();

        if((enemyxpos[xpos]==snakexlength[0]) && (enemyypos[ypos]==snakeylength[0])){
            lengthofsnake++;
            xpos= r.nextInt(countx);
            ypos= r.nextInt(county);
            score +=9;
            //s.enemyhit();
        }

        enemy.paintIcon(this,g,enemyxpos[xpos],enemyypos[ypos]);

        for(int i = 1;i<lengthofsnake;i++){
            if((snakexlength[0]==snakexlength[i]) &&(snakeylength[0]==snakeylength[i])){
                g.setColor(Color.DARK_GRAY); //creating highlight
                g.setFont(new Font("verdana", Font.BOLD,17));
                g.drawString("GAME OVER",((24+850)/2)-48,(74+625)/2);
                g.drawString("Final Score: "+ score,((24+850)/2)-59,((74+625)/2)+25);
                g.drawString("Press Enter To Restart the Game!!",((24+850)/2)-148,((74+625)/2)+50);
                g.setColor(Color.LIGHT_GRAY);
                g.setFont(new Font("verdana", Font.BOLD,17));
                g.drawString("GAME OVER",((24+850)/2)-50,(74+625)/2);
                g.drawString("Final Score: "+ score,((24+850)/2)-61,((74+625)/2)+25);
                g.drawString("Press Enter To Restart the Game!!",((24+850)/2)-150,((74+625)/2)+50);
                obj.setBackground(Color.RED);
                timer.stop();
                gamefin = true;
            }
        }

        g.dispose();
    }

    private void backchange(){
        if(right){
            obj.setBackground(Color.YELLOW);
        }
        else if(up){
            obj.setBackground(Color.GREEN);
        }
        else if(down){
            obj.setBackground(Color.BLUE);
        }
        else if(left){
            obj.setBackground(Color.LIGHT_GRAY);
        }

    }

    private void reset(){
        timer.start();
        moves=0;
        lengthofsnake=3;
        score=0;
        repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start(); //creating recursion
        if(right){
            for(int r = lengthofsnake-1;r>=0;r--){

                snakeylength[r+1] = snakeylength[r];
            }
            for(int r = lengthofsnake-1;r>=0;r--){
                if(r==0){
                    snakexlength[r] += 25;
                }
                else{
                    snakexlength[r] = snakexlength[r-1];
                }
                if(snakexlength[r]>850){
                    snakexlength[r]=25;
                }
                repaint();  //will execute paint method again
            }
        }
        if(left){
            for(int r = lengthofsnake-1;r>=0;r--){

                snakeylength[r+1] = snakeylength[r];
            }
            for(int r = lengthofsnake-1;r>=0;r--){
                if(r==0){
                    snakexlength[r] -= 25;
                }
                else{
                    snakexlength[r] = snakexlength[r-1];
                }
                if(snakexlength[r]<25){
                    snakexlength[r]=850;
                }
                repaint();  //will execute paint method again
            }
        }
        if(up){
            for(int r = lengthofsnake-1;r>=0;r--){

                snakexlength[r+1] = snakexlength[r];
            }
            for(int r = lengthofsnake-1;r>=0;r--){
                if(r==0){
                    snakeylength[r] -= 25;
                }
                else{
                    snakeylength[r] = snakeylength[r-1];
                }
                if(snakeylength[r]<75){
                    snakeylength[r]=625;
                }
                repaint();  //will execute paint method again
            }
        }
        if(down){
            for(int r = lengthofsnake-1;r>=0;r--){

                snakexlength[r+1] = snakexlength[r];
            }
            for(int r = lengthofsnake-1;r>=0;r--){
                if(r==0){
                    snakeylength[r] += 25;
                }
                else{
                    snakeylength[r] = snakeylength[r-1];
                }
                if(snakeylength[r]>625){
                    snakeylength[r]=75;
                }
                repaint();  //will execute paint method again
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        moves++;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {//jo right key press karie toh..
            if (!left) {   //snake already left ma jato hoy and right maaro toh direct pachhal na javo joie.
                right = true;
            } else {
                right = false;
                left = true;
            }
            up = false;
            down = false;

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) { //jo right key press karie toh..

            if (!right) {   //snake already left ma jato hoy and right maaro toh direct pachhal na javo joie.
                right = false;
                left = true;
                up = false;
                down = false;
            } else {
                right = true;
                left = false;
                up = false;
                down = false;

            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) { //jo right key press karie toh..

            if (!down) {   //snake already left ma jato hoy and right maaro toh direct pachhal na javo joie.
                right = false;
                left = false;
                up = true;
                down = false;
            } else {
                right = false;
                left = false;
                up = false;
                down = true;

            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) { //jo right key press karie toh..

            if (!up) {   //snake already left ma jato hoy and right maaro toh direct pachhal na javo joie.
                right = false;
                left = false;
                up = false;
                down = true;
            } else {
                right = false;
                left = false;
                up = true;
                down = false;

            }
        }
        if (e.getKeyCode()== KeyEvent.VK_ENTER && gamefin){
            reset();
        }
        if(e.getKeyCode()==KeyEvent.VK_N){
            s.playnext();
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}



