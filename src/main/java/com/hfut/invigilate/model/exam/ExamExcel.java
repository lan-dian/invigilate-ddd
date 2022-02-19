package com.hfut.invigilate.model.exam;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.hfut.invigilate.model.exception.BusinessException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

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

    /**
     * excel行数
     */
    @ExcelIgnore
    private Integer line;

    public LocalDate getDate(){
        int dateIndex = examTime.lastIndexOf("日");
        if(dateIndex<0){
            throw new BusinessException("第"+line+"行"+"日期格式不合法:"+examTime);
        }
        return LocalDate.parse(examTime.substring(0, dateIndex), DateTimeFormatter.ofPattern("yyyy年MM月dd"));
    }

}
