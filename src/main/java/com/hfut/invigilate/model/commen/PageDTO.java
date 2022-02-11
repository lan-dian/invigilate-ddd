package com.hfut.invigilate.model.commen;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 分页包装类
 */
@Data
@AllArgsConstructor
public class PageDTO<T> {

    @ApiModelProperty("数据总条数")
    private Long n;

    @ApiModelProperty("数据列表")
    private List<T> data;

    public static <T> PageDTO<T> build(Long n,List<T> data){
        return new PageDTO<>(n,data);
    }

    public static <T> PageDTO <T> build(IPage<T> iPage){
        return new PageDTO<>(iPage.getTotal(),iPage.getRecords());
    }

}
