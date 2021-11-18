package hw2;

import javax.swing.*;

public class startframe extends JFrame{
    public startframe(){
        startup su = new startup();
        su.setBounds(0,0,700,700);
        add(su);

        
        setSize(700, 700);
        setLayout(null);
        setVisible(true);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
