package hw2;

import java.io.FileWriter;
import java.io.IOException;

public class testdata {
    int testtime;
    float [][]testdata = new float[3000][4];
    float []weight = new float[2];
    float sita;
    int n1,n2;
    double correctrate;
    public testdata(int t,float d[][],float w[],float fs,int n,int nn){
        testtime = t;
        for(int i=0;i<testtime;i++){
            for(int j=0;j<3;j++){
                testdata[i][j] = d[i][j];
            }
        }
        for(int i=0;i<2;i++){
            weight[i] = w[i];
        }
        sita = fs;
        n1 = n;
        n2 = nn;
        test();
    }
    public void test(){
        for(int i=0;i<testtime;i++){
            float sum = 0;
            for(int j=0;j<2;j++){
                sum = sum+weight[j]*testdata[i][j];
            }
            sum = sum+sita;
            if(sum>((n1+n2)/2)){
                testdata[i][3] = n2;
            }
            else
                testdata[i][3] = n1;
        }

        try{
            int corre = 0;
            FileWriter fw = new FileWriter("test2.txt");
            for(int i=0;i<testtime;i++){
                if(testdata[i][3] == testdata[i][2]){
                    corre++;
                }
                fw.write(String.valueOf(testdata[i][0])+" "+String.valueOf(testdata[i][1])+" "+String.valueOf(testdata[i][3])+ System.getProperty("line.separator"));
            }
            correctrate = corre*100/testtime;
            FileWriter fc = new FileWriter("testcorrect.txt");
            fc.write(String.valueOf(correctrate));
            fc.flush();
            fc.close();
            System.out.println("test"+correctrate);
            fw.flush();
            fw.close();
        }catch (IOException e) {System.out.println(e);}
    }


}
