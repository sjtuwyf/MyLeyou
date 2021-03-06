package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.commom.enums.ExceptionEnum;
import com.leyou.commom.exception.LyException;
import com.leyou.commom.utils.JsonUtils;
import com.leyou.commom.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author ssqswyf
 * @Date 2021/4/27
 */

@Slf4j
@Service
public class SearchService {

    private final GoodsRepository repository;

    private final CategoryClient categoryClient;

    private final BrandClient brandClient;

    private final GoodsClient goodsClient;

    private final SpecificationClient specClient;

    private final ElasticsearchTemplate template;

    public SearchService(CategoryClient categoryClient, BrandClient brandClient, GoodsClient goodsClient, SpecificationClient specClient, GoodsRepository repository, ElasticsearchTemplate template) {
        this.categoryClient = categoryClient;
        this.brandClient = brandClient;
        this.goodsClient = goodsClient;
        this.specClient = specClient;
        this.repository = repository;
        this.template = template;
    }

    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();
        // ????????????
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());

        // ????????????
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        // ????????????
        String all = spu.getTitle() + StringUtils.join(names, " ") + brand.getName();

        // ??????sku
        List<Sku> skuList = goodsClient.querySkuBySpuId(spuId);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        // ???sku????????????
        List<Map<String,Object>> skus = new ArrayList<>();

        // ????????????
        Set<Long> priceList = new HashSet<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("image",StringUtils.substringBefore(sku.getImages(),","));
            skus.add(map);
            // ????????????
            priceList.add(sku.getPrice());
        }

        // ??????????????????
        List<SpecParam> params = specClient.queryParamList(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(params)) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        // ??????????????????
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);
        // ????????????????????????
        Map<Long, String> genericSpec = JsonUtils.toMap(spuDetail.getGenericSpec(), Long.class, String.class);
        // ????????????????????????
        Map<Long, List<String>> specialSpec = JsonUtils
                .nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {});

        // ????????????
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            // ????????????
            String key = param.getName();
            Object value = "";
            // ???????????????????????????
            if (param.getGeneric()) {
                value = genericSpec.get(param.getId());
                // ???????????????????????????
                if (param.getNumeric()) {
                    // ????????????
                    value = chooseSegment(value.toString(), param);
                }
            } else {
                value = specialSpec.get(param.getId());
            }
            // ??????map
            specs.put(key, value);
        }

        // ??????goods??????
        Goods goods = new Goods();

        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spuId);
        // ?????????????????????????????????????????????????????????
        goods.setAll(all);
        // ??????sku???????????????
        goods.setPrice(priceList);
        // ??????sku????????????json??????
        goods.setSkus(JsonUtils.toString(skus));
        // ?????????????????????????????????
        goods.setSpecs(specs);
        goods.setSubTitle(spu.getSubTitle());

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "??????";
        // ???????????????
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // ??????????????????
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // ????????????????????????
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "??????";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "??????";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }

        int page = request.getPage() - 1;
        int size = request.getSize();
        // ?????????????????????
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 0 ????????????
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"}, null));
        // 1 ??????
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 2 ??????
//        QueryBuilder basicQuery = QueryBuilders.matchQuery("all", key);
        QueryBuilder basicQuery = buildBasicQuery(request);
        queryBuilder.withQuery(basicQuery);

        // 2-3 ?????????????????????
        // .1 ????????????
        String categoryAggName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // .2 ????????????
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));


        // 3 ??????
//        Page<Goods> result = repository.search(queryBuilder.build());
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        // 4 ????????????
        // .1 ????????????
        long total = result.getTotalElements();
//        int totalPages = result.getTotalPages();
        long totalPages = (total + size - 1) / size;
        List<Goods> goodsList = result.getContent();

        // .2 ????????????
        Aggregations aggs = result.getAggregations();
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));

        // 5 ????????????????????????
        List<Map<String, Object>> specs = null;
        if (categories != null && categories.size() == 1) {
            specs = buildSpecificationAgg(categories.get(0).getId(),basicQuery);
        }

        return new SearchResult(total,totalPages,goodsList,categories,brands,specs);
    }

    private QueryBuilder buildBasicQuery(SearchRequest request) {
        // ??????????????????
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // ????????????
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()));
        // ????????????
        Map<String, String> map = request.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            queryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }

        return queryBuilder;
    }

    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        // 1 ?????????????????????????????????
        List<SpecParam> params = specClient.queryParamList(null, cid, true);
        // 2 ??????
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 2.1 ??????????????????
        queryBuilder.withQuery(basicQuery);
        // 2.2 ??????
        for (SpecParam param : params) {
            String name = param.getName();
            queryBuilder.addAggregation(
                    AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        }
        // 3 ????????????
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        // 4 ????????????
        Aggregations aggs = result.getAggregations();
        for (SpecParam param : params) {
            // ???????????????
            String name = param.getName();
            StringTerms terms = aggs.get(name);

            // ??????map
            Map<String, Object> map = new HashMap<>();
            map.put("k",name);
            map.put("options", terms.getBuckets()
                    .stream().map(b -> b.getKeyAsString()).collect(Collectors.toList()));

            specs.add(map);
        }

       return specs;
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByIds(ids);
            return brands;
        } catch (Exception e) {
            log.error("[????????????]??????????????????",e);
            return null;
        }
    }

    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryByIds(ids);
            return categories;
        } catch (Exception e) {
            log.error("[????????????]??????????????????",e);
            return null;
        }
    }

    public void createOrUpdateIndex(Long spuId) {
        // ??????spu
        Spu spu = goodsClient.querySpuById(spuId);
        // ??????goods
        Goods goods = buildGoods(spu);
        // ???????????????
        repository.save(goods);
    }

    public void deleteIndex(Long spuId) {
        repository.deleteById(spuId);
    }
}
