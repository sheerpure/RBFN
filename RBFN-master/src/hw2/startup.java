package hw2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


import javax.swing.JFrame;


public class startup extends JPanel implements ActionListener{

        ImageIcon startbtn = new ImageIcon("38-512.png");
        ImageIcon filebtn = new ImageIcon("62319-open-file-button.png");
        JButton choose = new JButton(filebtn);
        JLabel learn = new JLabel("learning rate");
        JTextField typelearn = new JTextField();
        JLabel time = new JLabel("correct rate");
        JTextField typerate = new JTextField();
        JButton okbtn = new JButton(startbtn);
        JLabel rm = new JLabel("RMSE(train)");
        JTextField rms = new JTextField();
        JLabel trainr = new JLabel("train rate");
        JTextField trainra = new JTextField();
        JLabel testr = new JLabel("test rate");
        JTextField testra = new JTextField();
        JLabel km = new JLabel("k-means");
        JTextField kmean = new JTextField();
        String path = "";
        float learnrate;
        float correctrate;

        float[][] traindata = new float[3000][3];
        float[][] testdata = new float[3000][3];
        int testtime = 0;
        int traintime = 0;



    public startup(){
            gui();

            //add(new JScrollPane(new startup()));
            setLayout(null);
        }

        public void gui(){




            choose.setBounds(10,5,50,50);
            choose.addActionListener(this);
            add(choose);

            okbtn.setBounds(60,5,50,50);
            okbtn.addActionListener(this);
            add(okbtn);

            learn.setBounds(10,50,100,50);
            add(learn);

            typelearn.setBounds(10,100,100,50);
            add(typelearn);

            time.setBounds(10,150,120,50);
            add(time);

            typerate.setBounds(10,200,100,50);
            add(typerate);

            km.setBounds(10,260,70,50);
            add(km);

            kmean.setBounds(80,260,20,50);
            add(kmean);

            rm.setBounds(10,300,100,50);
            add(rm);

            rms.setBounds(10,350,100,50);
            add(rms);

            trainr.setBounds(10,400,100,50);
            add(trainr);

            trainra.setBounds(10,450,100,50);
            add(trainra);

            testr.setBounds(10,500,100,50);
            add(testr);

            testra.setBounds(10,550,100,50);
            add(testra);


        }

    public int sizeofdata() {
        int line = 0;
        try {
            File file = new File(path);

            FileReader fr = new FileReader(file);

            BufferedReader br = new BufferedReader(fr);
            while (br.readLine() != null) {
                line++;
            }
        }

        catch (IOException e) {System.out.println(e);}
        return line;
    }

    public void readfile(String file){

        int[] exp = new int[sizeofdata()];
        String line,tempstring;
        if(sizeofdata()>10){
            traintime = sizeofdata()*2/3;
            testtime = sizeofdata()-traintime;
        }
        else{
            traintime = sizeofdata();
            testtime = 0;
        }

        float[][] data = new float[sizeofdata()][3];
        int []test = new int[testtime];
        int []train = new int[traintime];

        String[] temparray = new String [3];
        int linenumber = 0;

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                tempstring = line;
                temparray = tempstring.split("\\s");
                int index = 0;
                for(int i=0;i<3;i++){
                    data[linenumber][i] = Float.parseFloat(temparray[index]);
                    index++;
                }

                linenumber++;
            }
        }
        catch (IOException e) {System.out.println(e);}

        rand(test,train,testtime,traintime);
        gettraindata(train,traindata,traintime,data);
        gettestdata(test,testdata,testtime,data);

    }

    public void gettraindata(int train[], float traindata[][], int traintime, float data[][]){
        for(int i=0;i<traintime;i++){
            for(int j=0;j<3;j++){
                int a = train[i];
                traindata[i][j] = data[a][j];

            }

        }
    }
    public void gettestdata(int test[],float testdata[][], int testtime, float data[][]){
        for(int i=0;i<testtime;i++){
            for(int j=0;j<3;j++){
                int a = test[i];
                testdata[i][j] = data[a][j];

            }
        }
    }

    public void rand(int test[], int train[], int testtime, int traintime){
        Random ran = new Random();
        int temp;
        int emp[] = new int[sizeofdata()];
        for(int i=0;i<sizeofdata();i++){
            emp[i] = i;
        }

        for(int i=0;i<200;i++){
            int n1 = ran.nextInt(sizeofdata());
            int n2 = ran.nextInt(sizeofdata());

            temp = emp[n1];
            emp[n1] = emp[n2];
            emp[n2] = temp;
        }

        for(int i=0;i<traintime;i++){
            train[i] = emp[i];
        }
        int k=traintime;
        for(int i=0;i<testtime;i++){
            test[i] = emp[k];
            k++;
        }

    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == choose){
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) 
            {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println(selectedFile.getAbsolutePath()); 
                path = selectedFile.getAbsolutePath();
                readfile(path);
            }
        }
        if(e.getSource() == okbtn){
            if(traintime>10){
                float learn1 = Float.parseFloat(typelearn.getText());
                float typecor = Float.parseFloat(typerate.getText());
                int k = Integer.parseInt(kmean.getText());
                kmeans km = new kmeans(traindata,traintime,testdata,testtime,learn1,typecor,k);
                drawtrain d = new drawtrain(traindata,traintime);
                d.setBounds(150,-300,700,600);

                add(d);



                drawtest dt = new drawtest(testdata,testtime);
                dt.setBounds(150,310,700,300);

                add(dt);

                try{
                    FileReader fr = new FileReader("traincorrect.txt");
                    BufferedReader br = new BufferedReader(fr);
                    String tr =  br.readLine();
                    trainra.setText(tr);
                    trainra.setEditable(false);
                    fr.close();

                    FileReader fr1 = new FileReader("testcorrect.txt");
                    BufferedReader br1 = new BufferedReader(fr1);
                    String tr1 = br1.readLine();
                    testra.setText(tr1);
                    testra.setEditable(false);
                    fr1.close();

                    FileReader fr2 = new FileReader("rmse.txt");
                    BufferedReader br2 = new BufferedReader(fr2);
                    String rm = br2.readLine();
                    rms.setText(rm);
                    rms.setEditable(false);
                    fr2.close();
                }catch (IOException ex) {System.out.println(ex);}

            }
            else{
                float learn1 = Float.parseFloat(typelearn.getText());
                float typecor = Float.parseFloat(typerate.getText());
                int k = Integer.parseInt(kmean.getText());
                kmeans km = new kmeans(traindata,traintime,testdata,0,learn1,typecor,k);
                drawtrain d = new drawtrain(traindata,traintime);
                d.setBounds(150,-300,700,600);

                add(d);

                try{
                    FileReader fr = new FileReader("traincorrect.txt");
                    BufferedReader br = new BufferedReader(fr);
                    String tr =  br.readLine();
                    trainra.setText(tr);
                    trainra.setEditable(false);
                    fr.close();

                    testra.setText("NA");
                    testra.setEditable(false);

                    rms.setText("NA");
                    rms.setEditable(false);
                }catch (IOException ex) {System.out.println(ex);}


            }


            repaint();
        }

    }



    public void keyPressed(KeyEvent event) {

    }
    public void keyReleased(KeyEvent event){

    }
    public void keyTyped(KeyEvent event) {


    }


}
