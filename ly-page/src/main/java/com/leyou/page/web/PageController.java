package com.leyou.page.web;

import com.leyou.page.service.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @Author ssqswyf
 * @Date 2021/4/28
 */

@Controller
public class  PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    /**
     * 跳转到商品详情页
     * @param spuId spuId
     * @param model model
     * @return string
     */
    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long spuId, Model model) {
        // 查询模型数据
//        Map<String,Object> attributes = pageService.loadmodel(spuId);
        Map<String,Object> modelMap = pageService.loadData(spuId);
        // 准备模型数据
//        model.addAllAttributes(attributes);
        model.addAllAttributes(modelMap);

        // 返回视图
        return "item";
    }
}
