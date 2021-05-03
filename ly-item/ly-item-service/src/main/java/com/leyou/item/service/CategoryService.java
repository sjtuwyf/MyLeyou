package com.leyou.item.service;

import com.leyou.commom.enums.ExceptionEnum;
import com.leyou.commom.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ssqswyf
 * @Date 2021/4/20
 */

@Service
public class CategoryService {



    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> queryCategoryListByPid(Long pid) {
        Category t = new Category();
        t.setParentId(pid);
        List<Category> list = categoryMapper.select(t);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    /**
     * 根据多个分类id查询分类名称
     * @param ids
     * @return
     */
    public List<String> queryNamesByIds(List<Long> ids){
        List<Category> categories = this.categoryMapper.selectByIdList(ids);
        // 把List<Category> 转化成List<String>
        return categories.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
