package kmeans;

/*����k_means�����Ĳ���*/
public class Kmeans_param {
    public static final int K = 240;//ϵͳĬ�ϵ����������ĸ���
    public static final int MAX_ATTEMPTS = 4000;//����������
    public static final double MIN_CRITERIA = 0.1;
    public static final double MIN_EuclideanDistance = 0.8;
    public double criteria = MIN_CRITERIA; //��С��ֵ
    public int attempts = MAX_ATTEMPTS;    
    public boolean isDisplay = true;
    public double min_euclideanDistance = MIN_EuclideanDistance;
}