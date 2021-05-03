package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author ssqswyf
 * @Date 2021/4/27
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
