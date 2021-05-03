package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author ssqswyf
 * @Date 2021/4/27
 */
public interface CategoryApi {


    @GetMapping("category/list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("category")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);


}
