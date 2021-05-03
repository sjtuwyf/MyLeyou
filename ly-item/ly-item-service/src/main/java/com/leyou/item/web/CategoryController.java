package com.leyou.item.web;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ssqswyf
 * @Date 2021/4/20
 */

@RestController
@RequestMapping("category")
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 根据父节点id查询商品分类
     * @param pid pid
     * @return list
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid) {
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }

    /**
     * 根据id查询商品分类
     * @param ids ids
     * @return list
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }


    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return ResponseEntity.notFound().build();
        }
        // 响应200
        return ResponseEntity.ok(names);
    }
}
