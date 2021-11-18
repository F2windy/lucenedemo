package com.windy.lucenedemo.controller;

import com.windy.lucenedemo.pojo.ResultModel;
import com.windy.lucenedemo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @RequestMapping(value = "/list")
    public  String query(String queryString, String price, Integer page, Model model) throws Exception{
        if(ObjectUtils.isEmpty(page) ||page<=0){
            page=1;
        }
//        查询结果集
        ResultModel resultModel = searchService.query(queryString, price, page);
        model.addAttribute("result",resultModel);
        model.addAttribute("queryString",queryString);
        model.addAttribute("page",page);
        model.addAttribute("price",price);


        return "search";
    }

}
