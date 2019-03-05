package kmeans;
/* @param data 原始矩阵
* @param labels 样本所属类别
* @param centers 聚类中心*/
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