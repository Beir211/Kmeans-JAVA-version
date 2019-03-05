package kmeans;

import org.junit.Test;
import kmeans.ClusterModel;
import kmeans.Kmeans;
/**
 * train���������֣�һ������Ҫ����Kֵ���㷨�ڲ��Զ�����Ϊ����ֵ����Ϊ��ϸ���Ⱦ��࣬
 * ��һ����Ҫ����Kֵ��kֵ��С���⣬��kֵ>�㷨�ڲ�����ֵʱ���Զ����� Ϊ����ֵ
 * ����modelԤ��ʱ��ֻ�贫��feature��ʶ 
 */
public class KmeansTest {  
    @Test
    public void test() throws Exception {      
        Kmeans kmeans = new Kmeans();
        String path = "C:\\Users\\Beir\\Desktop\\С������\\kmeans����\\TEST_NZA_KMEANS.txt";
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