package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ssqswyf
 * @Date 2021/4/27
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
