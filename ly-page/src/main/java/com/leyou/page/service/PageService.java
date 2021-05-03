package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * @Author ssqswyf
 * @Date 2021/4/29
 */

@Service
@Slf4j
public class PageService {

    private final BrandClient brandClient;
    private final CategoryClient categoryClient;
    private final GoodsClient goodsClient;
    private final SpecificationClient specClient;
    private final TemplateEngine templateEngine;

    public PageService(BrandClient brandClient, CategoryClient categoryClient, GoodsClient goodsClient, SpecificationClient specClient, TemplateEngine templateEngine) {
        this.brandClient = brandClient;
        this.categoryClient = categoryClient;
        this.goodsClient = goodsClient;
        this.specClient = specClient;
        this.templateEngine = templateEngine;
    }


/*    public Map<String, Object> loadmodel(Long spuId) {

        Map<String, Object> model = new HashMap<>();
        // 查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        // 查询skus
        List<Sku> skus = spu.getSkus();
        // 查询详情
        SpuDetail detail = spu.getSpuDetail();
        // 查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        // 查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 查询规格参数
//        List<SpecGroup> specs = specClient.queryGroupByCid(spu.getCid3());
        List<SpecGroup> groups = specClient.queryGroupByCid(spu.getCid3());


        List<SpecParam> params = specClient.queryParamList(null, spu.getCid3(), null);
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param ->{
            paramMap.put(param.getId(), param.getName());
        });


        model.put("title",spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("skus",skus);
        model.put("detail",detail);
        model.put("brand",brand);
        model.put("categories",categories);
//        model.put("specs",specs);
        model.put("groups",groups);
        model.put("paramMap",paramMap);

        return model;
    }*/

    public Map<String, Object> loadData(Long spuId) {

        Map<String,Object> model = new HashMap<>();

        // 根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);
        // 根据分类cid1，cid2，cid3查询分类
        List<Map<String, Object>> categories = new ArrayList<>();
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }

        // 根据brandId查询brand
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        // 根据spuId查询skus
//        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);
        List<Sku> skus = spu.getSkus();

//        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        SpuDetail spuDetail = spu.getSpuDetail();

//        List<SpecGroup> groups = this.specClient.queryGroupWithParam(spu.getCid3());
        List<SpecGroup> groups = specClient.queryGroupByCid(spu.getCid3());

        // 查询特殊的规格参数
//        List<SpecParam> params = this.specClient.queryParams(null, spu.getCid3(), false, null);
        List<SpecParam> params = specClient.queryParamList(null, spu.getCid3(), false);

        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });

        model.put("categories", categories);
        model.put("brand", brand);
        model.put("spu", spu);
        model.put("skus", skus);
        model.put("spuDetail", spuDetail);
        model.put("groups", groups);
        model.put("paramMap", paramMap);
        return model;
    }

    public void createHtml(Long spuId) {
        // 上下文
        Context context = new Context();
        context.setVariables(loadData(spuId));

        // 输出流
        File dest = new File("D:\\IdeaProjects\\leyou\\upload", spuId + ".html");
        try (PrintWriter writer = new PrintWriter(dest, "UTF-8")) {
            // 生成HTML
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            log.error("[静态页服务] 生成静态页异常！", e);
        }




    }
}
