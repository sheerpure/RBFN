package hw2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class kmeans {
    float [][]traindata = new float[3000][4];
    int traintime = 0;
    int testtime;
    int kmean;
    float [][]testdata= new float[3000][4];
    double[][][]weight = new double[20][3000][2];
    int n1;
    int n2;
    float learnrate;
    float typec;

    float[][] center = new float[20][3];
    public kmeans(float data[][],int time,float data1[][], int time1,float learn,float typecor,int k){
        traintime = time;
        learnrate = learn;
        typec = typecor;
        kmean = k;
        for(int i=0;i<traintime;i++){
            for(int j=0;j<3;j++){
                traindata[i][j] = data[i][j];
            }
        }
        testtime = time1;
        for(int i=0;i<testtime;i++){
            for(int j=0;j<3;j++){
                testdata[i][j] = data1[i][j];
            }
        }
        int min = 100;
        for(int i=0;i<traintime;i++){
            if((int)traindata[i][2]<min){
                min = (int)traindata[i][2];
            }
        }
        n1 = min;
        n2 = min+1;

        inicenter();
    }

    public void inicenter(){
        boolean[] find = new boolean[20];
        for(int i=0;i<kmean;i++){
            find[i] = false;
        }
        Random ran = new Random();
        int start1 = kmean/2;
        for(int i=0;i<start1;i++){ //�����
            while(find[i] == false){
                int a = ran.nextInt(traintime);
                if(traindata[a][2] == n1){
                    for(int j = 0;j<3;j++){
                        center[i][j] = traindata[a][j];
                    }
                    find[i] = true;
                }
                else
                    find[i] = false;
            }
        }
        for(int i=start1;i<kmean;i++){
            while(find[i] == false){
                int a = ran.nextInt(traintime);
                if(traindata[a][2] == n2){
                    for(int j = 0;j<3;j++){
                        center[i][j] = traindata[a][j];
                    }
                    find[i] = true;
                }
                else
                    find[i] = false;
            }
        }

        classify();
    }

    public void classify(){
        double mu = 1E-10;
        double difference = 1000;
        int time = 10;//�葆甈⊥
        int t = 0;
        for(int i=0;i<kmean;i++){
            for(int j=0;j<2;j++){
                weight[i][0][j] = center[i][j];
            }
        }
        while(difference >mu &&t <time){
            int []index = new int[20];
            double []sumx = new double[20];
            double []sumy = new double[20];
            double []meanx = new double[20];
            double []meany = new double[20];
            double []differencee = new double[20];
            int [][]cen = new int[20][3000];
            double []distance = new double[20];
            double min = 100;

            for(int i=0;i<kmean;i++){
                index[i] = 0;
            }
            for(int i=0;i<traintime;i++){
                for(int j=0;j<kmean;j++){
                    double xdifference = weight[j][t][0]-traindata[i][0];
                    double ydifference = weight[j][t][1]-traindata[i][1];
                    distance[j] = Math.sqrt(Math.pow(xdifference,2)+Math.pow(ydifference,2));
                    if(distance[j]<min){
                        min = distance[j];
                        traindata[i][3] = j;
                    }
                }
                int a = (int)(traindata[i][3]);
                cen[a][index[a]] = i;
                index[a]++;
            }

            for(int i=0;i<kmean;i++){
                for(int j=0;j<index[i];j++){
                    sumx[i] = sumx[i]+traindata[(cen[i][j])][0];
                    sumy[i] = sumy[i]+traindata[(cen[i][j])][1];
                }
                meanx[i] = sumx[i]/(index[i]+1);
                meany[i] = sumy[i]/(index[i]+1);
                weight[i][t+1][0] = meanx[i];
                weight[i][t+1][1] = meany[i];
                differencee[i] =  Math.sqrt(Math.pow((weight[i][t+1][0]-weight[i][t][0]),2)+Math.pow(weight[i][t+1][1]-weight[i][t][1],2));
            }
            double max = 0;
            for(int i=0;i<kmean;i++){
                if(differencee[i]>max){
                    max = differencee[i];
                }
            }
            difference = max;

            t++;

        }
        float [][]c = new float[20][3];
        for(int i= 0;i<kmean;i++){
            for(int j=0;j<2;j++){
                c[i][j] = (float)weight[i][t][j];
            }
            c[i][2] = 0;
        }

        try{
            FileWriter fw = new FileWriter("test.txt");
            for(int i=0;i<kmean;i++){
                fw.write(String.valueOf(c[i][0])+" "+String.valueOf(c[i][1])+ System.getProperty("line.separator"));
            }
            fw.flush();
            fw.close();
        }catch (IOException e) {System.out.println(e);}
        traindata dt = new traindata(traintime,  traindata,c,testtime,testdata,learnrate,typec,kmean);

    }

}
