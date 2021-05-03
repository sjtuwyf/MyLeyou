package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @Author ssqswyf
 * @Date 2021/4/26
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
