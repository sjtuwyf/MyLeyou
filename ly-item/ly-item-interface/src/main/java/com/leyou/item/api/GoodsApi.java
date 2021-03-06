package com.leyou.item.api;

import com.leyou.commom.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author ssqswyf
 * @Date 2021/4/26
 */

public interface GoodsApi {

    /**
     * 根据spu的id查询详情detail
     * @param spuId spuId
     * @return SpuDetail
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id") Long spuId);

    /**
     * 根据spu查询下面的所有sku
     * @param spuId spuId
     * @return list
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);

    /**
     * 分页查询spu
     * @param page page
     * @param rows rows
     * @param saleable salable
     * @param key key
     * @return Spu
     */
    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    );

    /**
     * 根据spu的id查询spu
     * @param id
     * @return Spu
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);
}
