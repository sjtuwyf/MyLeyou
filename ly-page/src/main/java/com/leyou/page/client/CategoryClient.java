package com.leyou.page.client;


import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @Author ssqswyf
 * @Date 2021/4/26
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {

}
