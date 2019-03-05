package kmeans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
/* kMeans�����㷨*/
public class Kmeans {
    private DecimalFormat df = new DecimalFormat("#####.00");
    public Kmeans_data data = null;
    // feature,�������ƺ�����ӳ��
    private Map<String, Integer> identifier = new HashMap<String, Integer>();
    private Map<Integer, String> iden0 = new HashMap<Integer, String>();
    private ClusterModel model = new ClusterModel();
 
    /**
     * �ļ��������ӳ��
     * @param path
     * @return
     * @throws Exception
     */
    public double[][] fileToMatrix(String path) throws Exception {
        List<String> contents = new ArrayList<String>();
        model.identifier = identifier;
        model.iden0 = iden0;
         
        FileInputStream file = null;
        InputStreamReader inputFileReader = null;
        BufferedReader reader = null;
        String str = null;
        int rows = 0;
        int dim = 0;
         
        try {
            file = new FileInputStream(path);
            inputFileReader = new InputStreamReader(file, "utf-8");
            reader = new BufferedReader(inputFileReader);
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((str = reader.readLine()) != null) {
                contents.add(str);
                ++rows;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
 
        String[] strs = contents.get(0).split("	:");
        dim = strs[0].split("	").length;
 
        double[][] da = new double[rows][dim];
 
        for (int j = 0; j < contents.size(); j++) {
            strs = contents.get(j).split(":"); ////////:        
            identifier.put(strs[1], j);
            iden0.put(j, strs[1]);
            String[] feature = strs[0].split(" ");
            for (int i = 0; i < dim; i++) {             
                da[j][i] = Double.parseDouble(feature[i]);
            }
        }      
        return da;
    }
 
    /**
     * �������
     * @param matrix
     * @param highDim
     * @param lowDim
     */
    private void setDouble2Zero(double[][] matrix, int highDim, int lowDim) {
        for (int i = 0; i < highDim; i++) {
            for (int j = 0; j < lowDim; j++) {
                matrix[i][j] = 0;
            }
        }
    }
 
    /**
     * �������Ŀ���
     * @param dests
     * @param sources
     * @param highDim
     * @param lowDim
     */
    private void copyCenters(double[][] dests, double[][] sources, int highDim, int lowDim) {
        for (int i = 0; i < highDim; i++) {
            for (int j = 0; j < lowDim; j++) {
                dests[i][j] = sources[i][j];
            }
        }
    }
 
    /**
     * ���¾�������
     * @param k
     * @param data
     */
    private void updateCenters(int k, Kmeans_data data) {
        double[][] centers = data.centers;
        setDouble2Zero(centers, k, data.dim);
        int[] labels = model.labels;
        int[] centerCounts = model.centerCounts;
        for (int i = 0; i < data.dim; i++) {
            for (int j = 0; j < data.length; j++) {
                centers[labels[j]][i] += data.data[j][i];
            }
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < data.dim; j++) {
                centers[i][j] = centers[i][j] / centerCounts[i];               
            }
        }
    }
 
    /**
     * ����ŷ�Ͼ���
     * @param pa
     * @param pb
     * @param dim
     * @return
     */
    public double dist(double[] pa, double[] pb, int dim) {
        double rv = 0;
        for (int i = 0; i < dim; i++) {
            double temp = pa[i] - pb[i];
            temp = temp * temp;
            rv += temp;
        }
        return Math.sqrt(rv);
    }
 
    /**
     * ����ѵ��,��Ҫ��Ϊ�趨kֵ(����������Ŀ)
     * @param k
     * @param data
     * @return
     * @throws Exception
     */
    public ClusterModel train(String path, int k) throws Exception {
        double[][] matrix = fileToMatrix(path);
        data = new Kmeans_data(matrix, matrix.length, matrix[0].length);
        return train(k, new Kmeans_param());
    }
 
    /**
     * ����ѵ��(ϵͳĬ�����ž���������Ŀ)
     * @param data
     * @return
     * @throws Exception
     */
    public ClusterModel train(String path) throws Exception {
        double[][] matrix = fileToMatrix(path);
        data = new Kmeans_data(matrix, matrix.length, matrix[0].length);
        return train(new Kmeans_param());
    }
    /*
     * 
     * ����Ҫ��Ϊ�趨Kֵ
     * 
     */
    private ClusterModel train(Kmeans_param param) {
        int k = Kmeans_param.K;
        // ���Ƚ������ݹ�һ������
        normalize(data);
        // �����һ�������ͺ��������������ŷ�Ͼ��룬����list��Ȼ������ֵ����Ϊ��������ѡȡ������
        List<Double> dists = new ArrayList<Double>();
        for (int i = 1; i < data.length; i++) {
            dists.add(dist(data.data[0], data.data[i], data.dim));
        }
        param.min_euclideanDistance = Double.valueOf(df.format((Collections.max(dists) + Collections.min(dists)) / 2));
        double euclideanDistance = param.min_euclideanDistance > 0 ? param.min_euclideanDistance
                : Kmeans_param.MIN_EuclideanDistance;
         
        int centerIndexes[] = new int[k];// �ռ�������������������
        int countCenter = 0;// ��̬��ʾ���ĵ���Ŀ
        int count = 0;// ������
        centerIndexes[0] = 0;
        countCenter++;
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < countCenter; j++) {
                if (dist(data.data[i], data.data[centerIndexes[j]], data.dim) > euclideanDistance) {
                    count++;
                }
            }
            if (count == countCenter) {
                centerIndexes[countCenter++] = i;
            }
            count = 0;
        }
         
        double[][] centers = new double[countCenter][data.dim]; // ��������
        data.centers = centers;
        int[] centerCounts = new int[countCenter]; // �������ĵ���������
        model.centerCounts = centerCounts;
        Arrays.fill(centerCounts, 0);
        int[] labels = new int[data.length]; // ���������
        model.labels = labels;
        double[][] oldCenters = new double[countCenter][data.dim]; // �洢�ɵľ�������
 
        // ���������ĸ�ֵ
        for (int i = 0; i < countCenter; i++) {
            int m = centerIndexes[i];
            for (int j = 0; j < data.dim; j++) {
                centers[i][j] = data.data[m][j];
            }
        }
 
        // �����ʼ�ľ������ĸ�ֵ
        model.originalCenters = new double[countCenter][data.dim];
        for (int i = 0; i < countCenter; i++) {
            for (int j = 0; j < data.dim; j++) {
                model.originalCenters[i][j] = centers[i][j];
            }
        }
 
        //��ʼ����
        for (int i = 0; i < data.length; i++) {
            double minDist = dist(data.data[i], centers[0], data.dim);
            int label = 0;
            for (int j = 1; j < countCenter; j++) {
                double tempDist = dist(data.data[i], centers[j], data.dim);
                if (tempDist < minDist) {
                    minDist = tempDist;
                    label = j;
                }
            }
            labels[i] = label;
            centerCounts[label]++;
        }
        updateCenters(countCenter, data);
        copyCenters(oldCenters, centers, countCenter, data.dim);
 
        // ����Ԥ����
        int maxAttempts = param.attempts > 0 ? param.attempts : Kmeans_param.MAX_ATTEMPTS;
        int attempts = 1;
        double criteria = param.criteria > 0 ? param.criteria : Kmeans_param.MIN_CRITERIA;
        double criteriaBreakCondition = 0;
        boolean[] flags = new boolean[k]; // ������ʾ���������Ƿ����仯
 
        // ����
        iterate: while (attempts < maxAttempts) { // �����������������ֵ��������ĸı�����������ֵ
            for (int i = 0; i < countCenter; i++) { //  ��ʼ�����ĵ�"�Ƿ��޸Ĺ�"���
                flags[i] = false;
            }
            for (int i = 0; i < data.length; i++) {
                double minDist = dist(data.data[i], centers[0], data.dim);
                int label = 0;
                for (int j = 1; j < countCenter; j++) {
                    double tempDist = dist(data.data[i], centers[j], data.dim);
                    if (tempDist < minDist) {
                        minDist = tempDist;
                        label = j;
                    }
                }
                if (label != labels[i]) { // �����ǰ�㱻���ൽ�µ������������
                    int oldLabel = labels[i];
                    labels[i] = label;
                    centerCounts[oldLabel]--;
                    centerCounts[label]++;
                    flags[oldLabel] = true;
                    flags[label] = true;
                }
            }
            updateCenters(countCenter, data);
            attempts++;
 
            // ���㱻�޸Ĺ������ĵ�����޸����Ƿ񳬹���ֵ
            double maxDist = 0;
            for (int i = 0; i < countCenter; i++) {
                if (flags[i]) {
                    double tempDist = dist(centers[i], oldCenters[i], data.dim);
                    if (maxDist < tempDist) {
                        maxDist = tempDist;
                    }
                    for (int j = 0; j < data.dim; j++) { // ����oldCenter
                        oldCenters[i][j] = centers[i][j];
                        oldCenters[i][j] = Double.valueOf(df.format(oldCenters[i][j]));
                    }
                }
            }
            if (maxDist < criteria) {
                criteriaBreakCondition = maxDist;
                break iterate;
            }
        }
        // �ѽ���洢��ClusterModel��
        ClusterModel rvInfo = outputClusterInfo(criteriaBreakCondition, countCenter, attempts, param, centerCounts);
        return rvInfo;
    }
    /*
     * 
     * ��Ҫ��Ϊ�趨Kֵ
     * 
     */
    private ClusterModel train(int k, Kmeans_param param) {
        // ���Ƚ������ݹ�һ������
        normalize(data);
         
        List<Double> dists = new ArrayList<Double>();
        for (int i = 1; i < data.length; i++) {
            dists.add(dist(data.data[0], data.data[i], data.dim));
        }
 
        param.min_euclideanDistance = Double.valueOf(df.format((Collections.max(dists) + Collections.min(dists)) / 2));
        double euclideanDistance = param.min_euclideanDistance > 0 ? param.min_euclideanDistance
                : Kmeans_param.MIN_EuclideanDistance;
 
         
        double[][] centers = new double[k][data.dim];
        data.centers = centers;
        int[] centerCounts = new int[k];
        model.centerCounts = centerCounts;
        Arrays.fill(centerCounts, 0);
        int[] labels = new int[data.length];
        model.labels = labels;
        double[][] oldCenters = new double[k][data.dim];
 
         
        int centerIndexes[] = new int[k];
        int countCenter = 0;
        int count = 0;
        centerIndexes[0] = 0;
        countCenter++;
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < countCenter; j++) {
                if (dist(data.data[i], data.data[centerIndexes[j]], data.dim) > euclideanDistance) {
                    count++;
                }
            }
            if (count == countCenter) {
                centerIndexes[countCenter++] = i;
            }
            count = 0;
             
            if (countCenter == k) {
                break;
            }
             
            if (countCenter < k && i == data.length - 1) {
                k = countCenter;
                break;
            }
        }
         
        for (int i = 0; i < k; i++) {
            int m = centerIndexes[i];
            for (int j = 0; j < data.dim; j++) {
                centers[i][j] = data.data[m][j];
            }
        }
 
         
        model.originalCenters = new double[k][data.dim];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < data.dim; j++) {
                model.originalCenters[i][j] = centers[i][j];
            }
        }
 
         
        for (int i = 0; i < data.length; i++) {
            double minDist = dist(data.data[i], centers[0], data.dim);
            int label = 0;
            for (int j = 1; j < k; j++) {
                double tempDist = dist(data.data[i], centers[j], data.dim);
                if (tempDist < minDist) {
                    minDist = tempDist;
                    label = j;
                }
            }
            labels[i] = label;
            centerCounts[label]++;
        }
        updateCenters(k, data);
        copyCenters(oldCenters, centers, k, data.dim);
         
        int maxAttempts = param.attempts > 0 ? param.attempts : Kmeans_param.MAX_ATTEMPTS;
        int attempts = 1;
        double criteria = param.criteria > 0 ? param.criteria : Kmeans_param.MIN_CRITERIA;
        double criteriaBreakCondition = 0;
        boolean[] flags = new boolean[k];
         
        iterate: while (attempts < maxAttempts) {
            for (int i = 0; i < k; i++) {
                flags[i] = false;
            }
            for (int i = 0; i < data.length; i++) {
                double minDist = dist(data.data[i], centers[0], data.dim);
                int label = 0;
                for (int j = 1; j < k; j++) {
                    double tempDist = dist(data.data[i], centers[j], data.dim);
                    if (tempDist < minDist) {
                        minDist = tempDist;
                        label = j;
                    }
                }
                if (label != labels[i]) {
                    int oldLabel = labels[i];
                    labels[i] = label;
                    centerCounts[oldLabel]--;
                    centerCounts[label]++;
                    flags[oldLabel] = true;
                    flags[label] = true;
                }
            }
            updateCenters(k, data);
            attempts++;
             
            double maxDist = 0;
            for (int i = 0; i < k; i++) {
                if (flags[i]) {
                    double tempDist = dist(centers[i], oldCenters[i], data.dim);
                    if (maxDist < tempDist) {
                        maxDist = tempDist;
                    }
                    for (int j = 0; j < data.dim; j++) { // ����oldCenter
                        oldCenters[i][j] = centers[i][j];
                        oldCenters[i][j] = Double.valueOf(df.format(oldCenters[i][j]));
                    }
                }
            }
            if (maxDist < criteria) {
                criteriaBreakCondition = maxDist;
                break iterate;
            }
        }
     
        ClusterModel rvInfo = outputClusterInfo(criteriaBreakCondition, k, attempts, param, centerCounts);
        return rvInfo;
    }
 
    /**
     * �Ѿ������洢��Model��
     * @param criteriaBreakCondition
     * @param k
     * @param attempts
     * @param param
     * @param centerCounts
     * @return
     */
    private ClusterModel outputClusterInfo(double criteriaBreakCondition, int k, int attempts, Kmeans_param param,
            int[] centerCounts) {      
        model.data = data;
        model.k = k;
        int perm[] = new int[data.length];
        model.perm = perm;
        int start[] = new int[k];
        model.start = start;
        group_class(perm, start, k, data);
        return model;
    }
 
    /**
     * �Ѿ���������������������洢
     * @param perm
     * @param start
     * @param k
     * @param data
     */
    private void group_class(int perm[], int start[], int k, Kmeans_data data) {
         
        start[0] = 0;
        for (int i = 1; i < k; i++) {
            start[i] = start[i - 1] + model.centerCounts[i - 1];
        }      
         
        for (int i = 0; i < data.length; i++) {         
            perm[start[model.labels[i]]++] = i;
        }
 
        start[0] = 0;
        for (int i = 1; i < k; i++) {
            start[i] = start[i - 1] + model.centerCounts[i - 1];
        }
    }
 
    /**
     * ���ݹ�һ������
     * @param data
     */
    private void normalize(Kmeans_data data) {     
        Map<Integer, Double[]> minAndMax = new HashMap<Integer, Double[]>();
        for (int i = 0; i < data.dim; i++) {
            Double[] nums = new Double[2];
            double max = data.data[0][i];
            double min = data.data[data.length - 1][i];
            for (int j = 0; j < data.length; j++) {
                if (data.data[j][i] > max) {
                    max = data.data[j][i];
                }
                if (data.data[j][i] < min) {
                    min = data.data[j][i];
                }
            }
            nums[0] = min;
            nums[1] = max;
            minAndMax.put(i, nums);
        }      
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.dim; j++) {
                double minValue = minAndMax.get(j)[0];
                double maxValue = minAndMax.get(j)[1];
                data.data[i][j] = (data.data[i][j] - minValue) / (maxValue - minValue);
                data.data[i][j] = Double.valueOf(df.format(data.data[i][j]));
            }
        }
    }
}
