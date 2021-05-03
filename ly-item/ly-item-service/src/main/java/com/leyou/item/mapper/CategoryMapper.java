package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author ssqswyf
 * @Date 2021/4/20
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
}
