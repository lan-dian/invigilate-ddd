package com.hfut.invigilate.model.invigilate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TeacherInvigilateVO {

    @ApiModelProperty("考试名称")
    private String name;

    @ApiModelProperty("考试日期")
    private LocalDate date;

    @ApiModelProperty("开始时间")
    private LocalTime startTime;

    @ApiModelProperty("结束时间")
    private LocalTime endTime;

    @ApiModelProperty("考试地点")
    private String address;

    @ApiModelProperty("学生人数")
    private Integer studentNum;

    @ApiModelProperty("考试编码")
    private String examCode;

    @ApiModelProperty("监考状态")
    private ExamStateEnum examStateEnum;

    @ApiModelProperty("监考编码")
    private String invigilateCode;

    public static TeacherInvigilateVO convert(InvigilateBO invigilateBO,LocalDate now){
        TeacherInvigilateVO teacherInvigilateVO = new TeacherInvigilateVO();
        teacherInvigilateVO.setName(invigilateBO.getName());
        teacherInvigilateVO.setDate(invigilateBO.getDate());
        teacherInvigilateVO.setStartTime(invigilateBO.getStartTime());
        teacherInvigilateVO.setEndTime(invigilateBO.getEndTime());
        teacherInvigilateVO.setAddress(invigilateBO.getAddress());
        teacherInvigilateVO.setStudentNum(invigilateBO.getStudentNum());
        teacherInvigilateVO.setExamCode(invigilateBO.getExamCode());
        teacherInvigilateVO.setExamStateEnum(getStateEnum(invigilateBO.getExchangeNum(),invigilateBO.getDate(),now));
        teacherInvigilateVO.setInvigilateCode(invigilateBO.getInvigilateCode());
        return teacherInvigilateVO;
    }

    @JsonIgnore
    private static ExamStateEnum getStateEnum(Integer exchangeNum,LocalDate date,LocalDate now){
        if(date.isBefore(now)){
            return ExamStateEnum.FINISHED;
        }
        if(exchangeNum>0){
            return ExamStateEnum.TO_BE_CONFIRMED;
        }
        if(exchangeNum==0){
            return ExamStateEnum.TO_BE_REPLACED;
        }
        if(date.isEqual(now)){
            return ExamStateEnum.TODAY;
        }
        return ExamStateEnum.UNFINISHED;
    }

}
