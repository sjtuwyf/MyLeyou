package com.leyou.item.web;

import com.leyou.commom.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ssqswyf
 * @Date 2021/4/20
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    /**
     * 分页查询品牌
     *
     * @param page   page
     * @param rows   rows
     * @param sortBy sortBy
     * @param desc   desc
     * @param key    key
     * @return ResponseEntity<PageResult < Brand>>
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key
    ) {
        return ResponseEntity.ok(brandService.queryBrandByPage(page, rows, sortBy, desc, key));

    }

    /**
     * 新增品牌
     *
     * @param brand brand
     * @param cids  cids
     * @return ResponseEntity<Void>
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据cid查询品牌
     *
     * @param cid cid
     * @return list
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(brandService.queryBrandByCid(cid));
    }

    /**
     * 根据id查询品牌
     * @param id id
     * @return brand
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(brandService.queryById(id));
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(brandService.queryByIds(ids));
    }


}
