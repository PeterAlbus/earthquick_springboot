#include <iostream>
#include <iomanip>
#include <cmath>
using namespace std;

int main()
{
    clock_t start, finish;
    double  duration;
    start = clock();
    double x[10][4]={{6.962645,67101,730.3979,1.3},
                     {5.643831,50732,170.8517,1.7},
                     {7.775079,39755,89.2054,1},
                     {6.568763,67101,294.3379,1},
                     {6.552100,67101,281.1741,1.9},
                     {6.669364,43274,41.5144,1.2},
                     {7.774892,39755,89.2054,1.4},
                     {6.511145,67101,518.4596,1.4},
                     {6.966898,67101,382.3772,1.7},
                     {5.894564,35641,374.3219,1.2}};
    double max[4];
    double min[4];
    double sum[4];
    for(int i=0;i<4;i++)
    {
        max[i]=x[0][i];
        min[i]=x[0][i];
        sum[i]=x[0][i];
        for(int j=1;j<10;j++)
        {
            if(x[j][i]>max[i])
            {
                max[i]=x[j][i];
            }
            if(x[j][i]<min[i])
            {
                min[i]=x[j][i];
            }
            sum[i]+=x[j][i];
        }
    }
    double x_normalize[10][4];
    for(int i=0;i<10;i++)
    {
        for(int j=0;j<4;j++)
        {
            if(j==1)
            {
                x_normalize[i][j]=(max[j]-x[i][j])/(max[j]-min[j]);
            }
            else
            {
                x_normalize[i][j]=(x[i][j]-min[j])/(max[j]-min[j]);
            }
            cout<<setw(15)<<x_normalize[i][j]<<",";
        }
        cout<<endl;
    }
    cout<<endl;
    cout<<endl;
    double p[10][4];
    for(int i=0;i<10;i++)
    {
        for(int j=0;j<4;j++)
        {
            p[i][j]=x_normalize[i][j]/sum[j]+0.0000001;
            cout<<setw(15)<<p[i][j]<<",";
        }
        cout<<endl;
    }
    cout<<endl;
    cout<<endl;
    double e[4];
    double sumG=0;
    for(int j=0;j<4;j++)
    {
        double sumJ=0;
        for(int i=0;i<10;i++)
        {
            sumJ+=p[i][j]*log(p[i][j]);
        }
        e[j]=(-1/log(10))*sumJ;
        sumG+=(1-e[j]);
        cout<<setw(15)<<e[j]<<",";
    }
    cout<<endl;
    cout<<endl;
    double a[4];
    for(int i=0;i<4;i++)
    {
        a[i]=(1-e[i])/sumG;
        cout<<setw(15)<<a[i]<<",";
    }
    cout<<endl;
    cout<<endl;
    double weights[10];
    double sumW=0;
    for(int i=0;i<10;i++)
    {
        double weight=0;
        for(int j=0;j<4;j++)
        {
            weight+=(a[j]*p[i][j]);
        }
        weights[i]=weight;
        sumW+=weight;
    }
    for(int i=0;i<10;i++)
    {
        cout<<weights[i]/sumW<<",";
    }
    cout<<endl;
    finish   = clock();
    duration = (double)(finish - start) / CLOCKS_PER_SEC;
    printf("%f seconds\n", duration);
    return 0;
}
