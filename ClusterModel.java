package kmeans;

import java.util.Map;

/* 聚类模型*/
public class ClusterModel {
    public double originalCenters[][];
    public int centerCounts[];
    public int attempts; //最大迭代次数
    public double criteriaBreakCondition; // 迭代结束时的最小阈值
    public int[] labels;
    public int k;
    public int perm[];//连续存放的样本
    public int start[];//每个中心开始的位置
    public Map<String,Integer> identifier;   
    public Kmeans_data data;
    public Map<Integer, String> iden0;   
     
    public void centers(){
        System.out.println("Cluster center:");
        for (int i = 0; i < originalCenters.length; i++) {
            for (int j = 0; j < originalCenters[0].length; j++) {
                System.out.print(originalCenters[i][j] + " ");
            }
            System.out.print("\t"+"the " + (i+1)+"th sort：" + "\t" +"the number of samples：" + centerCounts[i]);
            System.out.println();
        }
    }
     
    public int predict(String iden){ 
    	System.out.println(identifier);
        int label = labels[identifier.get(iden)];
        return label;
    }  
     
    public void outputAllResult(){
        System.out.println("\n----------The final clusting result----------");
 
        int originalCount = 0;
        for (int i = 0; i < k; i++) {
            int index = labels[perm[start[i]]];
            int counts = centerCounts[index];
            originalCount += counts;
            System.out.println("the "+(index+1)+"th class members：");
            for (int j = start[i]; j < originalCount; j++) {
                for (double num : data.data[perm[j]]) {
                    System.out.print(num + " ");
                }
                System.out.print(":"+iden0.get(perm[j]));
                System.out.println();              
            }
        }
    }
}