package com.mjj.software_project;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Demo3Utils {
    static ResultData writeData2Fime(String path, int[][] data, ArrayList<Integer> list, int totalWeight, long time, String model) {
        File file = new File(path + ".txt");
        sortByWeight(data);
        if (!file.exists()) {
            try {
                //0,1,1,0,1,0,0,1,1,1,1
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(model + "算法解决0-1背包问题总耗时为" + time + "秒");
            bw.newLine();
            bw.write("商品信息如下：");
            bw.newLine();
            bw.write("重量：");
            for (int j = 0; j < data.length; j++) {
                bw.write(data[j][0] + "\t");
            }
            bw.newLine();
            bw.write("价值：");
            for (int j = 0; j < data.length; j++) {
                bw.write(data[j][1] + "\t");
            }
            bw.newLine();
            bw.write("放入背包的物品：");
            for (int j = 0; j < list.size(); j++) {
                bw.write(list.get(j) + "\t");
            }
            bw.newLine();
            bw.write("放入背包的物品重量：");
            int weight = 0;
            for (int j = 0; j < list.size(); j++) {
                bw.write(data[list.get(j) - 1][0] + "\t");
                weight += data[list.get(j) - 1][0];
            }
            bw.write("总重量：" + weight);
            bw.newLine();
            bw.write("放入背包的物品价值：");
            int value = 0;
            for (int j = 0; j < list.size(); j++) {
                bw.write(data[list.get(j) - 1][1] + "\t");
                value += data[list.get(j) - 1][1];
            }
            bw.write("总价值：" + value);
            bw.newLine();
            bw.write("背包剩余容量：" + (totalWeight - weight));
            bw.flush();
            bw.close();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("结果已写入" + file.getAbsolutePath() + "文件中");
            ResultData resultData = new ResultData();
            resultData.setBestValue(value);
            resultData.setTotalWeight(totalWeight);
            resultData.setBestWeight(weight);
            resultData.setList1(list);
            resultData.setTime((int) time);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            resultData.setDate(dateFormat.format(calendar.getTime()));
            return resultData;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void sortByWeight(int[][] data) {
        for (int i = 0; i < data.length; i++) {
            //选择排序
            //首先在未排序的数列中找到最小(or最大)元素，然后将其存放到数列的起始位置；
            //接着，再从剩余未排序的元素中继续寻找最小(or最大)元素，然后放到已排序序列的末尾。
            //以此类推，直到所有元素均排序完毕。
            for (int j = i + 1; j < data.length; j++) {
                if (data[j][0] > data[i][0]) {
                    int temp1 = data[j][0];
                    data[j][0] = data[i][0];
                    data[i][0] = temp1;
                    int temp2 = data[j][1];
                    data[j][1] = data[i][1];
                    data[i][1] = temp2;
                }
            }
        }
    }

    static ArrayList<FileBean> readDataFromFile(String root) {
        ArrayList<FileBean> fileLists = new ArrayList<FileBean>();
        File rootPath = new File(root);
        if (rootPath.exists()) {
            File files[] = rootPath.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("文件夹为空");
                return null;
            } else {
                String realName = null;
                Map realMap = null;
                for (File item : files
                ) {
                    if (item.isFile()) {
                        String name = item.getName();
                        String type = name.substring(name.lastIndexOf(".") + 1);
                        int totalWeight = 0;
                        if (type.equals("in")) {
                            try {
                                realName = name;
                                BufferedReader br = new BufferedReader(new FileReader(item));
                                String line;
                                List lists = new ArrayList<>();
                                while ((line = br.readLine()) != null) {
                                    lists.add(line);
                                }
                                br.close();
                                int i = 0;
                                int data[][] = new int[lists.size()][2];
                                Iterator<String> iterator = lists.iterator();
                                while (iterator.hasNext()) {
                                    String array[] = iterator.next().split(" ");
                                    data[i][0] = Integer.valueOf(array[0]);
                                    data[i][1] = Integer.valueOf(array[1]);
                                    i++;
                                }
                                Map map = new HashMap<>();
                                map.put(name, data);
                                realMap = map;
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                return null;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                        if (type.equals("out")) {
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(item));
                                String line;
                                while ((line = br.readLine()) != null) {
                                    totalWeight = Integer.valueOf(line.toString().trim());
                                }
                                br.close();
                                FileBean bean = new FileBean();
                                bean.setData(realMap);
                                bean.setName(realName);
                                bean.setTotalWeight(totalWeight);
                                fileLists.add(bean);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                return null;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    }
                }
                return fileLists;
            }
        }
        return null;
    }

    static class Picture extends JPanel {
        int DIS = 30;
        int data[][];

        public Picture(int data[][]) {
            this.data = data;
            JFrame frame = new JFrame("散点图");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(this);
            frame.setSize(300, 300);
            frame.setLocation(200, 200);
            frame.setVisible(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D draw = (Graphics2D) g;
            draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            draw.draw(new Line2D.Double(DIS, DIS, DIS, h - DIS));
            draw.drawString("价值", DIS - 10, DIS);
            draw.draw(new Line2D.Double(DIS, h - DIS, w - DIS, h - DIS));
            draw.drawString("重量", w - DIS, h - DIS);
            int ymin = (h - 2 * DIS) / data.length;
            int xmin = (w - 2 * DIS) / data.length;
            draw.setPaint(Color.black);
            int ydis = ymin;
            int xdis = xmin;
            for (int i = 0; i < data.length; i++) {
                int x = data[i][0];
                int y = data[i][1];
                if (i != data.length - 1) {
                    draw.drawString(xdis + "", xdis + DIS, h - DIS + 15);
                    draw.drawString(ydis + "", DIS - 20, h - (ydis + DIS));
                    xdis += xmin;
                    ydis += ymin;
                }
                draw.fill(new Ellipse2D.Double(x + DIS, h - (y + DIS), 4, 4));

            }

        }

    }

    static class DP {

        // 记录背包的总容量
        public int packageweight;
        // 记录商品总数
        public int productnum;
        // 记录每个商品的重量
        public ArrayList<Integer> weights;
        // 记录每个商品的价值
        public ArrayList<Integer> values;
        // 记录存放的物品
        public ArrayList<Integer> lists;

        public DP(int data[][], int weight) {
            packageweight = weight;
            productnum = data.length;
            weights = new ArrayList<Integer>();
            values = new ArrayList<Integer>();
            lists = new ArrayList<Integer>();
            for (int i = 0; i < productnum; i++) {
                weights.add(data[i][0]);
                values.add(data[i][1]);
            }
        }

        /**
         * 初始化背包问题（记录价值的表格）
         * m[i][0] = 0 :表示背包重量为0，不能装东西，因此价值全为0
         * m[0][j] = 0 :表示没有可以装的物品，因此价值为0
         */
        public int[][] initpkdata() {
            int[][] m = new int[this.productnum + 1][this.packageweight + 1];

            for (int i = 0; i <= this.productnum; i++) {
                m[i][0] = 0;
            }
            for (int j = 0; j <= this.packageweight; j++) {
                m[0][j] = 0;
            }

            return m;
        }

        /**
         * 计算背包问题
         *
         * @return 修改相应价值记录后的表
         */
        public int[][] calculate(int[][] arr) {

            for (int i = 1; i <= this.productnum; i++) {
                for (int j = 1; j <= this.packageweight; j++) {

                    // 当第i件物品重量大于当前包的容量 则放不进去
                    // 所以当前背包所含价值等于前i-1件商品的价值
                    if (this.weights.get(i - 1) > j) {
                        arr[i][j] = arr[i - 1][j];
                    }
                    // 当第i件商品能放进去时
                    // 1 放入商品，价值为：arr[i-1][j-(int)this.weights.get(i-1)] + (int)this.values.get(i-1)
                    // 2不放入商品，价值为前i-1件上篇价值和：arr[i][j] = arr[i-1][j];
                    // 此时最大价值为上述两种方案中最大的一个
                    else {
                        if (arr[i - 1][j] < arr[i - 1][j - this.weights.get(i - 1)] + this.values.get(i - 1)) {
                            arr[i][j] = arr[i - 1][j - this.weights.get(i - 1)] + this.values.get(i - 1);
                        } else {
                            arr[i][j] = arr[i - 1][j];
                        }
                    }
                }
            }

            return arr;
        }

        /**
         * 查找那些商品放在背包中
         *
         * @return
         */
        public ArrayList<Integer> findproducts(int[][] arr) {
            int j = this.packageweight;
            for (int i = this.productnum; i > 0; i--) {
                if (arr[i][j] > arr[i - 1][j]) {
                    lists.add(i);
                    j = j - this.weights.get(i - 1);
                    if (j < 0) {
                        break;
                    }
                }
            }
            return lists;
        }
    }

    static class Greedy {
        //记录背包容量
        private int packageWeight;
        // 记录商品
        private int[][] data;

        public Greedy(int data[][], int weight) {
            packageWeight = weight;
            this.data = data;
        }

        //计算背包问题
        public ArrayList<Integer> calculate() {
            //存放密度
            double[] performance = new double[data.length];
            //存放索引的数组
            int[] index = new int[data.length];
            for (int i = 0; i < performance.length; i++) {
                performance[i] = data[i][1] / data[i][0];
                index[i] = i;
            }
            //冒泡排序
            for (int i = 0; i < performance.length - 1; i++) {
                for (int j = 0; j < performance.length - 1 - i; j++) {
                    if (performance[j] < performance[j + 1]) {
                        double temp = performance[j];
                        performance[j] = performance[j + 1];
                        performance[j + 1] = temp;
                        //索引跟着交换
                        int temp1 = index[j];
                        index[j] = index[j + 1];
                        index[j + 1] = temp1;
                    }
                }
            }

            //计算最优解
            ArrayList<Integer> lists = new ArrayList<Integer>();
            for (int i = 0; i < index.length; i++) {
                if (data[index[i]][0] <= packageWeight) {
                    lists.add(index[i] + 1);
                    packageWeight -= data[index[i]][0];
                }
            }
            return lists;
        }

    }

    /*
     * 用回溯法实现0-1背包问题。
     * 在回溯开始之前，首先对于背包中的物品按照单位重量价值进行排序，方便于后面右子树的剪枝操作。
     * 在初始化物品的重量和价值时，已经按照单位重量的价值排好了序。
     * 一个典型的子集树问题，对于背包中的每一个物品，可以选择放入（左子树）或者不放入（右子树）。
     * 依次对每个节点进行搜索，得到最优解。
     * */
    static class Back {
        //物品数量
        int n;
        //背包容量
        int capacity;
        //物品重量数组
        int weight[];
        //物品价值数组
        int value[];
        //最大价值
        int maxValue = 0;
        //当前最大价值
        int currentValue;
        //当前重量
        int currentWeight;
        // 装入方法数组
        int[] way;
        //最佳装入方法数组
        int[] bestWay;

        public Back(int data[][], int totalWeight) {
            capacity = totalWeight;
            n = data.length;
            way = new int[n];
            bestWay = new int[n];
            int temp[] = new int[n];
            int temp1[] = new int[n];
            for (int i = 0; i < n; i++) {
                temp[i] = data[i][0];
                temp1[i] = data[i][1];
            }
            weight = temp;
            value = temp1;
        }

        /*
         * 回溯算法设计
         * */
        public void backTrack(int t) {
            // 已经搜索到根节点
            if (t > n - 1) {
                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    for (int i = 0; i < n; i++)
                        bestWay[i] = way[i];
                }
                return;
            }
            // 搜索左边节点
            if (currentWeight + weight[t] <= capacity) {
                currentWeight += weight[t];
                currentValue += value[t];
                way[t] = 1;
                //回溯
                backTrack(t + 1);
                currentWeight -= weight[t];
                currentValue -= value[t];
                way[t] = 0;
            }
            // 不装入这个物品，直接搜索右边的节点
            if (bound(t + 1) >= maxValue) {
                backTrack(t + 1);
            }
        }

        // 用于计算剩余物品的最高价值上界
        public double bound(int k) {
            double maxLeft = currentValue;
            int leftWeight = capacity - currentWeight;
            // 尽力依照单位重量价值次序装剩余的物品
            while (k <= n - 1 && leftWeight > weight[k]) {
                leftWeight -= weight[k];
                maxLeft += value[k];
                k++;
            }
            // 不能装时，用下一个物品的单位重量价值折算到剩余空间。
            if (k <= n - 1) {
                maxLeft += value[k] / weight[k] * leftWeight;
            }
            return maxLeft;
        }

        public int[] getBestWay() {
            return bestWay;
        }

        public ArrayList<Integer> getLists() {
            ArrayList<Integer> lists = new ArrayList<>();
            for (int i = 0; i < bestWay.length; i++) {
                if (bestWay[i] == 1) {
                    lists.add(i + 1);
                }
            }
            return lists;
        }
    }


    static class FileBean {
        private String name;
        private int totalWeight;
        private Map data;

        public void setData(Map data) {
            this.data = data;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTotalWeight(int totalWeight) {
            this.totalWeight = totalWeight;
        }

        public int getTotalWeight() {
            return totalWeight;
        }

        public Map getData() {
            return data;
        }

        public String getName() {
            return name;
        }
    }

    static class GA2 {

        private int[] value;// 物品价值
        private int[] weight;// 物品体积
        private int totalWeight;// 背包容积
        private int LL; // 染色体长度
        private int scale;// 种群规模
        private int MAX_GEN; // 运行代数

        private int bestT;// 最佳出现代数
        private int bestLength; // 最佳编码价值
        private int[] bestTour; // 最佳编码

        // 初始种群，父代种群，行数表示种群规模，一行代表一个个体，即染色体，列表示染色体基因片段
        private int[][] oldPopulation;
        private int[][] newPopulation;// 新的种群，子代种群
        private int[] fitness;// 种群适应度，表示种群中各个个体的适应度

        private float[] Pi;// 种群中各个个体的累计概率
        private float Pc;// 交叉概率
        private float Pm;// 变异概率
        private int t;// 当前代数
        private Random random;

        // 种群规模，染色体长度,最大代数，交叉率，变异率
        public GA2(int s, int g, float c, float m, int data[][], int totalWeight) {
            scale = s;
            LL = data.length;
            MAX_GEN = g;
            Pc = c;
            Pm = m;
            int temp[] = new int[LL];
            int temp1[] = new int[LL];
            for (int i = 0; i < LL; i++) {
                temp[i] = data[i][0];
                temp1[i] = data[i][1];
            }
            this.weight = temp;
            this.value = temp1;
            this.totalWeight = totalWeight;
        }

        private void init() {
            bestLength = 0;
            bestTour = new int[LL];
            bestT = 0;
            t = 0;
            newPopulation = new int[scale][LL];
            oldPopulation = new int[scale][LL];
            fitness = new int[scale];
            Pi = new float[scale];
            random = new Random(System.currentTimeMillis());
        }

        // 初始化种群
        void initGroup() {
            int k, i;
            for (k = 0; k < scale; k++)// 种群数
            {
                // 01编码
                for (i = 0; i < LL; i++) {
                    oldPopulation[k][i] = random.nextInt(65535) % 2;
                }
            }
        }

        public int evaluate(int[] chromosome) {
            // 010110
            int vv = 0;
            int bb = 0;
            // 染色体，起始城市,城市1,城市2...城市n
            for (int i = 0; i < LL; i++) {
                if (chromosome[i] == 1) {
                    vv += value[i];
                    bb += weight[i];
                }
            }
            if (bb > totalWeight) {
                // 超出背包体积
                return 0;
            } else {
                return vv;
            }
        }

        // 计算种群中各个个体的累积概率，前提是已经计算出各个个体的适应度fitness[max]，作为赌轮选择策略一部分，Pi[max]
        void countRate() {
            int k;
            double sumFitness = 0;// 适应度总和

            int[] tempf = new int[scale];

            for (k = 0; k < scale; k++) {
                tempf[k] = fitness[k];
                sumFitness += tempf[k];
            }

            Pi[0] = (float) (tempf[0] / sumFitness);
            for (k = 1; k < scale; k++) {
                Pi[k] = (float) (tempf[k] / sumFitness + Pi[k - 1]);
            }
        }

        // 挑选某代种群中适应度最高的个体，直接复制到子代中
        // 前提是已经计算出各个个体的适应度Fitness[max]
        public void selectBestGh() {
            int k, i, maxid;
            int maxevaluation;

            maxid = 0;
            maxevaluation = fitness[0];
            for (k = 1; k < scale; k++) {
                if (maxevaluation < fitness[k]) {
                    maxevaluation = fitness[k];
                    maxid = k;
                }
            }

            if (bestLength < maxevaluation) {
                bestLength = maxevaluation;
                bestT = t;// 最好的染色体出现的代数;
                for (i = 0; i < LL; i++) {
                    bestTour[i] = oldPopulation[maxid][i];
                }
            }

            // 复制染色体，k表示新染色体在种群中的位置，kk表示旧的染色体在种群中的位置
            copyGh(0, maxid);// 将当代种群中适应度最高的染色体k复制到新种群中，排在第一位0
        }

        // 复制染色体，k表示新染色体在种群中的位置，kk表示旧的染色体在种群中的位置
        public void copyGh(int k, int kk) {
            int i;
            for (i = 0; i < LL; i++) {
                newPopulation[k][i] = oldPopulation[kk][i];
            }
        }

        // 赌轮选择策略挑选
        public void select() {
            int k, i, selectId;
            float ran1;
            for (k = 1; k < scale; k++) {
                ran1 = (float) (random.nextInt(65535) % 1000 / 1000.0);
                // System.out.println("概率"+ran1);
                // 产生方式
                for (i = 0; i < scale; i++) {
                    if (ran1 <= Pi[i]) {
                        break;
                    }
                }
                selectId = i;
                copyGh(k, selectId);
            }
        }

        public void evolution() {
            int k;
            // 挑选某代种群中适应度最高的个体
            selectBestGh();
            // 赌轮选择策略挑选scale-1个下一代个体
            select();
            float r;

            // 交叉方法
            for (k = 0; k < scale; k = k + 2) {
                r = random.nextFloat();// /产生概率
                // System.out.println("交叉率..." + r);
                if (r < Pc) {
                    // System.out.println(k + "与" + k + 1 + "进行交叉...");
                    OXCross(k, k + 1);// 进行交叉
                } else {
                    r = random.nextFloat();// /产生概率
                    // System.out.println("变异率1..." + r);
                    // 变异
                    if (r < Pm) {
                        // System.out.println(k + "变异...");
                        OnCVariation(k);
                    }
                    r = random.nextFloat();// /产生概率
                    // System.out.println("变异率2..." + r);
                    // 变异
                    if (r < Pm) {
                        // System.out.println(k + 1 + "变异...");
                        OnCVariation(k + 1);
                    }
                }

            }

        }


        // 两点交叉算子
        void OXCross(int k1, int k2) {
            int i, j, flag;
            int ran1, ran2, temp = 0;

            ran1 = random.nextInt(65535) % LL;
            ran2 = random.nextInt(65535) % LL;

            while (ran1 == ran2) {
                ran2 = random.nextInt(65535) % LL;
            }
            if (ran1 > ran2)// 确保ran1<ran2
            {
                temp = ran1;
                ran1 = ran2;
                ran2 = temp;
            }
            flag = ran2 - ran1 + 1;// 个数
            for (i = 0, j = ran1; i < flag; i++, j++) {
                temp = newPopulation[k1][j];
                newPopulation[k1][j] = newPopulation[k2][j];
                newPopulation[k2][j] = temp;
            }

        }

        // 多次对换变异算子
        public void OnCVariation(int k) {
            int ran1, ran2, temp;
            int count;// 对换次数
            count = random.nextInt(65535) % LL;

            for (int i = 0; i < count; i++) {

                ran1 = random.nextInt(65535) % LL;
                ran2 = random.nextInt(65535) % LL;
                while (ran1 == ran2) {
                    ran2 = random.nextInt(65535) % LL;
                }
                temp = newPopulation[k][ran1];
                newPopulation[k][ran1] = newPopulation[k][ran2];
                newPopulation[k][ran2] = temp;
            }
        }

        public void solve() {
            init();
            int i;
            int k;
            // 初始化种群
            initGroup();
            // 计算初始化种群适应度，Fitness[max]
            for (k = 0; k < scale; k++) {
                fitness[k] = evaluate(oldPopulation[k]);
                // System.out.println(fitness[k]);
            }

            // 计算初始化种群中各个个体的累积概率，Pi[max]
            countRate();

            for (t = 0; t < MAX_GEN; t++) {
                evolution();
                // 将新种群newGroup复制到旧种群oldGroup中，准备下一代进化
                for (k = 0; k < scale; k++) {
                    for (i = 0; i < LL; i++) {
                        oldPopulation[k][i] = newPopulation[k][i];
                    }
                }
                // 计算种群适应度
                for (k = 0; k < scale; k++) {
                    fitness[k] = evaluate(oldPopulation[k]);
                }
                // 计算种群中各个个体的累积概率
                countRate();
            }


        }

        public ArrayList<Integer> getLists() {
            ArrayList<Integer> lists = new ArrayList<>();
            for (int i = 0; i < LL; i++) {
                if (bestTour[i] == 1)
                    lists.add(i + 1);
            }
            return lists;
        }
    }
}



