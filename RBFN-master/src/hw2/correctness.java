package hw2;

import java.io.FileWriter;
import java.io.IOException;

public class correctness {
    int traintime;
    float [][] traindata = new float[3000][4];
    float [] finalweight = new float[2];
    float finalsita;
    double rmse;
    public correctness(int traint,float traind[][], float finalw[],float finals){
        traintime = traint;
        for(int i=0;i<traintime;i++){
            for(int j=0;j<4;j++){
                traindata[i][j] = traind[i][j];
            }
        }
        for(int i=0;i<2;i++){
            finalweight[i] = finalw[i];
        }
        finalsita = finals;
        RMSE();
    }
    public void RMSE(){
        double sum = 0;
        for(int i=0;i<traintime;i++){
            sum = sum+Math.pow(traindata[i][2]-traindata[i][3],2);
        }
        rmse = Math.sqrt(sum/traintime);
        try{
            FileWriter fw = new FileWriter("rmse.txt");
            fw.write(String.valueOf(rmse));
            fw.flush();
            fw.close();
        }catch (IOException e) {System.out.println(e);}
        System.out.println("rmse"+rmse);
    }
}
