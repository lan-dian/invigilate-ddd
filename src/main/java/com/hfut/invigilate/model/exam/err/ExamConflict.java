package com.hfut.invigilate.model.exam.err;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamConflict {

    @ApiModelProperty("考试名称")
    private String name;

    @ApiModelProperty("excel行号")
    private Integer line;

    @ApiModelProperty("错误原因")
    private String reason;

}
