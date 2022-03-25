package com.mjj.software_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class Demo3Controller {
    @Autowired
    private ProduceService produceService;
    String root = "C:\\Users\\lenovo\\Desktop\\demo";
    @CrossOrigin
    @RequestMapping("login")
    public DataBean logo() {
        //读文件
        ArrayList<Demo3Utils.FileBean> files = Demo3Utils.readDataFromFile(root);
        int code = 0;
        if (files.size() > 0 || files != null) {
            System.out.println("执行");
            for (Demo3Utils.FileBean item: files) {
                ArrayList<Produce> list = new ArrayList<>();
                String type = item.getName();
                int total=item.getTotalWeight();
                Map map = item.getData();
                int data[][] = (int[][]) map.get(type);
                for (int i = 0; i < data.length; i++) {
                    Produce produce = new Produce();
                    produce.setCapacity(total);
                    produce.setWeight(data[i][0]);
                    produce.setValue(data[i][1]);
                    produce.setType(type);
                    list.add(produce);
                }
                List<Produce> produces = produceService.addData(list);
                System.out.println(produces.toArray().toString());
            }
            code = 200;
        }
        DataBean bean = new DataBean();
        bean.setCode(code);
        bean.setData(files);
        return bean;
    }
    @CrossOrigin
    @RequestMapping("calculate")
    public DataBean calculate(HttpServletRequest request) {
        int op = Integer.valueOf(request.getParameter("op"));
        int index = Integer.valueOf(request.getParameter("index"));
        System.out.println(op);
        System.out.println(index);
        int code = 0;
        Object data = null;
        ArrayList<Demo3Utils.FileBean> files = Demo3Utils.readDataFromFile(root);
        switch (op) {
            case 1: {
                System.out.println(files);
                int original[][] = (int[][]) files.get(index).getData().get(files.get(index).getName());
                //按重量比排序
                code=200;
                data=original;
                break;
            }
            case 2: {
                int original[][] = (int[][]) files.get(index).getData().get(files.get(index).getName());
                //按重量比排序
                Demo3Utils.sortByWeight(original);
                code=200;
                data=original;
                break;
            }
            case 3: {
                int original[][] = (int[][]) files.get(index).getData().get(files.get(index).getName());
                //动态规划
                Demo3Utils.sortByWeight(original);
                long start = System.currentTimeMillis();
                Demo3Utils.DP dp = new Demo3Utils.DP(original, files.get(index).getTotalWeight());
                //初始化背包
                int[][] initpkdata = dp.initpkdata();
                int[][] result = dp.calculate(initpkdata);
                ArrayList<Integer> list = dp.findproducts(result);
                long end = System.currentTimeMillis();
                ResultData result1=Demo3Utils.writeData2Fime(root + "\\" + files.get(index).getName(), original, list, files.get(index).getTotalWeight(), (end - start), "动态规划");
                code=200;
                data=result1;
                break;
            }
            case 4: {

                int original[][] = (int[][]) files.get(index).getData().get(files.get(index).getName());
                Demo3Utils.sortByWeight(original);
                long start = System.currentTimeMillis();
                Demo3Utils.Greedy greedy = new Demo3Utils.Greedy(original, files.get(index).getTotalWeight());
                ArrayList<Integer> list = greedy.calculate();
                long end = System.currentTimeMillis();
                ResultData result=Demo3Utils.writeData2Fime(root + "\\" + files.get(index).getName(), original, list, files.get(index).getTotalWeight(), (end - start), "贪心算法");
                code=200;
                data=result;
                break;
            }
            case 5: {

                int original[][] = (int[][]) files.get(index).getData().get(files.get(index).getName());
                Demo3Utils.sortByWeight(original);
                long start = System.currentTimeMillis();
                Demo3Utils.Back back = new Demo3Utils.Back(original, files.get(index).getTotalWeight());
                back.backTrack(0);
                long end = System.currentTimeMillis();
                ArrayList<Integer> list = back.getLists();
                ResultData result=Demo3Utils.writeData2Fime(root + "\\" + files.get(index).getName(), original, list, files.get(index).getTotalWeight(), (end - start), "回溯算法");
                code=200;
                data=result;
                break;
            }
            case 6: {

                int original[][] = (int[][]) files.get(index).getData().get(files.get(index).getName());
                Demo3Utils.sortByWeight(original);
                long start = System.currentTimeMillis();
                Demo3Utils.GA2 ga = new Demo3Utils.GA2(20, 500, 0.8f, 0.9f, original, files.get(index).getTotalWeight());
                ga.solve();
                long end = System.currentTimeMillis();
                ArrayList<Integer> list = ga.getLists();
                ResultData result=Demo3Utils.writeData2Fime(root + "\\" + files.get(index).getName(), original, list, files.get(index).getTotalWeight(), (end - start), "遗传算法");
                code=200;
                data=result;
                break;
            }
            default:
                break;

        }
        DataBean bean = new DataBean();
        bean.setCode(code);
        bean.setData(data);
        return bean;
    }


}
