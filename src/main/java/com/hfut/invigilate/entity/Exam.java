package com.hfut.invigilate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hfut.invigilate.model.exam.ExamExcel;
import com.hfut.invigilate.utils.CodeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 考试
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Exam对象", description="考试")
@Slf4j
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "考试编码")
    private Long code;

    @ApiModelProperty(value = "考试名称")
    private String name;

    @ApiModelProperty(value = "考试日期")
    private LocalDate date;

    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "监考人数")
    private Integer teacherNum;

    @ApiModelProperty(value = "考试地点")
    private String address;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "部门id")
    private Integer departmentId;

    @ApiModelProperty(value = "教学班描述")
    private String classDescription;

    @ApiModelProperty(value = "学生人数")
    private Integer studentNum;

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    @ApiModelProperty(value = "工资")
    private Integer money;

    public static final Pattern dateTimePattern = Pattern.compile("(\\d+年\\d+月(\\d+)日) (\\d+:\\d+)~(\\d+:\\d+)");

    public static final Pattern addressPattern = Pattern.compile("(.*?) \\((\\d+)\\)");

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public void checkTimeConflict(Exam exam){
        if(startTime.isBefore(exam.endTime) && endTime.isAfter(exam.startTime)){
            throw new RuntimeException("与其他考试时间冲突!!! "+getSimpleDescription());
        }
    }

    @JsonIgnore
    public String getSimpleDescription(){
        return "考试编码:"+code+" "+"相关信息:"+ name +" "+address+" "+date+" "+startTime+"-"+endTime;
    }

    public static Exam convert(ExamExcel excel, Map<String, Integer> departments, Integer money) {
        Exam exam = new Exam();
        exam.setCode(CodeUtils.getId());
        exam.setName(excel.getName());
        Matcher dateTimeMatcher = dateTimePattern.matcher(excel.getExamTime());
        if (dateTimeMatcher.find()) {
            exam.setDate(LocalDate.parse(dateTimeMatcher.group(1),dateFormatter));
            exam.setStartTime(LocalTime.parse(dateTimeMatcher.group(3),timeFormatter));
            exam.setEndTime(LocalTime.parse(dateTimeMatcher.group(4),timeFormatter));
        } else {
            log.info("日期格式不合法:"+excel.getExamTime());
            throw new IllegalArgumentException("日期格式不合法");
        }

        Matcher addressMatcher = addressPattern.matcher(excel.getAddressWithNum());
        if(addressMatcher.find()){
            exam.setAddress(addressMatcher.group(1));
            exam.setStudentNum(Integer.valueOf(addressMatcher.group(2)));
        }else {
            log.info("考试地址格式不合法:"+excel.getAddressWithNum());
            throw new IllegalArgumentException("考试地点(人数)不合法");
        }
        exam.setTeacherNum(getTeacherNum(exam.studentNum));

        String college = excel.getCollege();
        if(!departments.containsKey(college)){
            throw new IllegalArgumentException("学院:"+college+"不存在,请先添加");
        }
        exam.setDepartmentId(departments.get(college));
        exam.setClassDescription(excel.getClassDescription());

        exam.setTeacherName(excel.getTeacherName());
        exam.setMoney(money);
        return exam;
    }

    private static Integer getTeacherNum(Integer studentNum){
        if(studentNum>=70){
            return 4;
        }else if(studentNum>=50){
            return 3;
        }else{
            return 2;
        }
    }


}
