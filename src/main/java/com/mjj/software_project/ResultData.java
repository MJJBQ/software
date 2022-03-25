package com.mjj.software_project;

import java.util.ArrayList;

/**
 * @author 孟姣姣
 * @desc  求解结果封装类
 * @date 2022年03月24日 2022/3/24
 */
public class ResultData {
    private ArrayList<Integer> list1=new ArrayList<>();
    private int totalWeight;
    private int bestWeight;
    private int bestValue;
    private String date;
    private int time;

    public ArrayList<Integer> getList1() {
        return list1;
    }

    public void setList1(ArrayList<Integer> list1) {
        this.list1 = list1;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getBestWeight() {
        return bestWeight;
    }

    public void setBestWeight(int bestWeight) {
        this.bestWeight = bestWeight;
    }

    public int getBestValue() {
        return bestValue;
    }

    public void setBestValue(int bestValue) {
        this.bestValue = bestValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
