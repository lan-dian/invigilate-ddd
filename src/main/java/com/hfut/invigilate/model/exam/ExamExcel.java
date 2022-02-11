package com.hfut.invigilate.model.exam;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExamExcel {

    @Excel(name = "开课院系")
    private String college;

    @Excel(name = "课程名称")
    private String name;

    @Excel(name="教学班描述")
    private String classDescription;

    @Excel(name = "人数")
    private Integer studentNum;

    @Excel(name = "教师")
    private String teacherName;

    @Excel(name = "考试时间")
    private String examTime;

    @Excel(name = "考试地点(人数)")
    private String addressWithNum;

}
