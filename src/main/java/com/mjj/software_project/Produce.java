package com.mjj.software_project;


import javax.persistence.*;

/**
 * @author 孟姣姣
 * @desc  产品封装类
 * @date 2022年03月25日 2022/3/25
 */
@Entity
@Table(name = "produce")
public class Produce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //物品重量
    @Column(name = "weight")
    private int weight;
    //物品价值
    @Column(name = "value")
    private int value;
    //数据属于哪个类（总共9类）
    @Column(name = "type")
    private String type;
    //每一个数据携带背包总量量
    @Column(name = "capacity")
    private int capacity;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
}
