package kmeans;
/* @param data ԭʼ����
* @param labels �����������
* @param centers ��������*/
public class Kmeans_data {
    public double[][] data;
    public int length;
    public int dim;
    public double[][] centers; 
     
    public Kmeans_data(double[][] data, int length, int dim) {
        this.data = data;
        this.length = length;
        this.dim = dim;    
    }
}