package com.windy.lucenedemo.service;

import com.windy.lucenedemo.pojo.ResultModel;
import org.springframework.ui.Model;

public interface SearchService {
  ResultModel query(String queryString, String price, Integer page)throws Exception;
}
