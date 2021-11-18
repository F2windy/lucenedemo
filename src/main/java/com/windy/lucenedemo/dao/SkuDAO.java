package com.windy.lucenedemo.dao;

import com.windy.lucenedemo.pojo.Sku;

import java.util.List;
/**
 * @author windy
 * 查询所有sku数据
 */

public interface SkuDAO {
    public List<Sku> querySkuList();
}
