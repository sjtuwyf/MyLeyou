package com.leyou.commom.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author ssqswyf
 * @Date 2021/4/19
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    /**
     * 400
     */
    PRICE_CANNOT_BE_NULL(400, "价格不能为空!"),

    /**
     * 404
     */
    CATEGORY_NOT_FOUND(404,"商品分类没查到"),

    /**
     * 404
     */
    SPEC_GROUP_NOT_FOUND(404,"商品规格组不存在"),

    /**
     * 404
     */
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数不存在"),

    /**
     * 404
     */
    GOODS_NOT_FOUND(404,"商品不存在"),

    /**
     * 404
     */
    GOODS_DETAIL_NOT_FOUND(404,"商品详情不存在"),

    /**
     * 404
     */
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在"),

    /**
     * 404
     */
    GOODS_STOCK_NOT_FOUND(404,"商品库存不存在"),

    /**
     * 404
     */
    BRAND_NOT_FOUND(404,"品牌不存在"),

    /**
     * 500
     */
    BRAND_SAVE_ERROR(500,"新增品牌失败"),

    /**
     * 500
     */
    UPLOAD_FILE_ERROR(500,"文件上传失败"),

    /**
     * 400
     */
    INVALID_FILE_TYPE(400,"无效的文件类型"),

    /**
     * 500
     */
    GOODS_SAVE_ERROR(500,"新增商品失败"),

    /**
     * 500
     */
    GOODS_UPDATE_ERROR(500,"更新商品失败"),

    /**
     * 400
     */
    GOODS_ID_CANNOT_BE_NULL(400,"商品id不能为空"),
    ;

    private int code;
    private String msg;
}
