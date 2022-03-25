package com.mjj.software_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 孟姣姣
 * @desc  Servicec层
 * @date 2022年03月25日 2022/3/25
 */
@Service
public class ProduceService {
    @Autowired
    private ProduceRepository produceRepository;

    public List<Produce> addData(ArrayList<Produce> lists) {
        return produceRepository.saveAllAndFlush(lists);
    }
}
