package com.hfut.invigilate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hfut.invigilate.model.exception.BusinessException;
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

    @TableField(exist = false)
    private Integer line;

    public void margeStudentNum(Integer studentNum){
        if(this.studentNum==null){
            throw new RuntimeException("内部计算错误");
        }
        this.studentNum+=studentNum;
    }

    public void mergeClassDescription(String classDescription){
        if(this.classDescription==null){
            throw new RuntimeException("内部计算错误");
        }
        this.classDescription+=","+classDescription;
    }

    public void replaceAble(){
        if(!beforeStart()){
            throw new BusinessException("不能顶替已经开始或者过期的考试");
        }
    }

    public void startExchangeAble(Integer minute){
        if(!beforeStart()){
            throw new BusinessException("考试已经过期或处于进行状态!");
        }
        //可能正在考试或者处于考试前
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(minute);
        if(deadline.isAfter(LocalDateTime.of(date,startTime))) {
            throw new BusinessException("考试前"+minute+"分钟禁止交换考试");
        }
    }


    public boolean beforeStart(){
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.isAfter(today) || (date.isEqual(today) && startTime.isAfter(time));
    }

    /**
     * 是否过期(考试已经结束
     */
    @JsonIgnore
    public boolean isTimeOut(){
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.isBefore(today) || (date.isEqual(today) && endTime.isBefore(time));
    }

    @JsonIgnore
    public boolean isStarted(){
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date.isEqual(today) && startTime.isBefore(time) && endTime.isAfter(time);
    }

    public boolean isConflict(Exam exam){
        return date.isEqual(exam.date) && address.equals(exam.address)
                && (startTime.isBefore(exam.endTime) && endTime.isAfter(exam.startTime));
    }


    public void checkTimeConflict(Exam exam){
        if(startTime.isBefore(exam.endTime) && endTime.isAfter(exam.startTime)){
            throw new RuntimeException("与其他考试时间冲突!!! "+getSimpleDescription());
        }
    }

    @JsonIgnore
    public String getSimpleDescription(){
        return "考试编码:"+code+" "+"相关信息:"+ name +" "+address+" "+date+" "+startTime+"-"+endTime;
    }




}
