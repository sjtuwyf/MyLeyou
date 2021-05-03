package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ssqswyf
 * @Date 2021/4/23
 */

@RestController
@RequestMapping("spec")
public class SpecificationController {

    private final SpecificationService specService;

    public SpecificationController(SpecificationService specService) {
        this.specService = specService;
    }

    /**
     * 根据分类id查询规格组
     * @param cid cid
     * @return list
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specService.queryGroupByCid(cid));

    }

    /**
     * 查询参数集合
     * @param gid gid
     * @param cid cid
     * @param searching searching
     * @return list
     */
    @RequestMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamList(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching
            ) {
        return ResponseEntity.ok(specService.queryParamList(gid,cid,searching));

    }

    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(specService.queryListByCid(cid));
    }


}
