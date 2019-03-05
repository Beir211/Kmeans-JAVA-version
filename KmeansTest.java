package kmeans;

import org.junit.Test;
import kmeans.ClusterModel;
import kmeans.Kmeans;
/**
 * train方法有两种，一个不需要传递K值，算法内部自动处理为最优值，此为最细粒度聚类，
 * 另一个需要传递K值，k值大小任意，当k值>算法内部最优值时，自动调整 为最优值
 * 利用model预测时，只需传递feature标识 
 */
public class KmeansTest {  
    @Test
    public void test() throws Exception {      
        Kmeans kmeans = new Kmeans();
        String path = "C:\\Users\\Beir\\Desktop\\小创材料\\kmeans程序\\TEST_NZA_KMEANS.txt";
        ClusterModel model = kmeans.train(path);   
        model.centers();
        System.out.println("China is in class " + (model.predict("China")+1));
        model.outputAllResult();
        System.out.println("---------------------------------When the value of k is specified---------------------------------");
        model = kmeans.train(path,200);
        model.centers();
        System.out.println("China is in class " + (model.predict("China")+1));
        model.outputAllResult();       
    }  
}