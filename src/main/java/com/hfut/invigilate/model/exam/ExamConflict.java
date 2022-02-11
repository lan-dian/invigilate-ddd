package com.hfut.invigilate.model.exam;

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

    @ApiModelProperty("错误原因")
    private String reason;

}
