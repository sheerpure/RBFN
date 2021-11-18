package hw2;

import java.io.FileWriter;
import java.io.IOException;

public class traindata {
    int traintime = 0;
    float[][] traindata = new float[3000][4];
    float [][]weight = new float[50000][2];
    float [][]center = new float[20][3];

    float []sita = new float[50000];
    int testtime = 0;
    float [][]testdata = new float[3000][3];
    float []varx = new float[20];
    float []vary = new float[20];
    double min=100;
    double max = 0;
    double correctrate;
    float[] finalweight = new float[2];
    float finalsita;
    int n1=100;
    int n2;
    int kmean;
    float corr;
    float []s = new float[2];
    float learnrate ;
    public traindata(int t, float td[][],float c[][], int t1, float td2[][],float learn,float typec,int k){
        weight[0][0] = -1;
        weight[0][1] = 1;
        sita[0] = 1;
        traintime = t;
        learnrate = learn;
        corr = typec;
        kmean = k;
        for(int i=0;i<traintime;i++){
            for(int j= 0;j<4;j++){
                traindata[i][j] = td[i][j];
            }
        }

        testtime =t1;
        for(int i=0;i<testtime;i++){
            for(int j=0;j<3;j++){
                testdata[i][j] = td2[i][j];
            }
        }

        for(int i=0;i<traintime;i++){
            if(traindata[i][2]<n1){
                n1 = (int)traindata[i][2];
            }

        }
        n2 = n1+1;
        for(int i=0;i<kmean;i++){
            for(int j=0;j<3;j++){
                center[i][j] = c[i][j];
            }
        }



        train();
    }
    public void calvarx(){
        int[]num = new int[20];
        double[]sumvax = new double[20];
        for(int i=0;i<20;i++){
            num[i] = 0;
            sumvax[i] = 0;
        }


        for(int i=0;i<traintime;i++){
            for(int j=0;j<kmean;j++){
                if(traindata[i][3] == j){
                    sumvax[j] = sumvax[j]+Math.pow(traindata[i][0]-center[j][0],2);
                    num[j]++;
                }
            }
        }

        for(int i=0;i<kmean;i++){
            varx[i] = (float)sumvax[i]/num[i];
        }

    }
    public void calvary(){
        float[] sumy = new float[20];
        int []num = new int[20];
        double[] sumvay = new double[20];

        for(int i=0;i<traintime;i++){
            for(int j=0;j<kmean;j++){
                if(traindata[i][3] == j){
                    sumvay[j] = sumvay[j]+Math.pow(traindata[i][0]-center[j][0],2);
                    num[j]++;
                }
            }

        }
        for(int i=0;i<kmean;i++){
            vary[i] = (float)sumvay[i]/num[i];
        }
    }

    public float output(int time, int time1){
        float ans = 0;
        for(int i = 0;i<2;i++){
            ans = ans + weight[time1][i]*traindata[time][i];
        }
        ans = ans+sita[time1];
        return ans;
    }
    public void sgn(int time){
        int group = (int)traindata[time][3];
        double dis1 = 0;
        double dis2 = 0;
        double value1 = 0;
        double value2 = 0;
        for(int i=0;i<kmean;i++){
            if(group == i){
                dis1 = Math.pow(traindata[time][0]-center[i][0],2);
                dis2 = Math.pow(traindata[time][1]-center[i][1],2);
                value1 = Math.exp(-dis1/(2*varx[i]));
                value2 = Math.exp(-dis2/(2*vary[i]));
            }
        }
        s[0] = (float)value1;
        s[1] = (float)value2;
    }

    public double correct(int time1){
        double sum = 0;
        int data = 0;
        int cor = 0;
        for(int i=0;i<traintime;i++){
            for(int j=0;j<2;j++){
                sum = sum +(traindata[i][j]*weight[time1][j]+sita[time1]);
            }
            if(sum<((n1+n2)/2)){
                data= n1;
            }
            else
                data = n2;
            if(data == (int)traindata[i][2]){
                cor++;
            }

        }
        System.out.println(traintime);
        System.out.println(" "+cor+" ");
        double yes = (cor*100/traintime);

        return yes;
    }
    public void train(){
        int time = 0;
        int time1 = 0;
        calvarx();
        calvary();
        while(time1<10000){

            if(time>=traintime){
                time= time-traintime;
            }
            sgn(time);
            if(correct(time1)>max){
                max = correct(time1);
                correctrate = correct(time1);
                for(int i=0;i<2;i++){
                    finalweight[i] = weight[time1][i];
                }
                finalsita = sita[time1];
            }
            if(max>=corr){
                break;
            }
            for(int i=0;i<2;i++){
                weight[time1+1][i] = weight[time1][i]+learnrate*(traindata[time][2]-output(time,time1))*s[i];
            }

            sita[time1+1] = sita[time1]+learnrate*(traindata[time][2]-output(time,time1));
            time1++;
            time++;
        }

        try{
            int corre = 0;
            FileWriter fw = new FileWriter("test1.txt");
            for(int i=0;i<traintime;i++){
                double data = traindata[i][0]*finalweight[0]+traindata[i][1]*finalweight[1]+finalsita;
                if(data<((n1+n2)/2)){
                    fw.write(String.valueOf(traindata[i][0])+" "+String.valueOf(traindata[i][1])+" "+String.valueOf(n1)+ System.getProperty("line.separator"));
                    traindata[i][3] = n1;
                    if(traindata[i][3] == traindata[i][2]){
                        corre++;
                    }
                }
                else{
                    fw.write(String.valueOf(traindata[i][0])+" "+String.valueOf(traindata[i][1])+" "+String.valueOf(n2)+ System.getProperty("line.separator"));
                    traindata[i][3] = n2;
                    if(traindata[i][3] == traindata[i][2]){
                        corre++;
                    }
                }
            }
            correctrate = corre*100/traintime;
            FileWriter fc = new FileWriter("traincorrect.txt");
            fc.write(String.valueOf(correctrate));
            fc.flush();
            fc.close();
            System.out.println("correct"+" "+correctrate);
            fw.flush();
            fw.close();
        }catch (IOException e) {System.out.println(e);}

        correctness ct = new correctness(traintime,traindata,finalweight,finalsita);
        if(traintime>10){
            testdata tdd = new testdata(testtime,testdata,finalweight,finalsita,n1,n2);
        }


    }
}
