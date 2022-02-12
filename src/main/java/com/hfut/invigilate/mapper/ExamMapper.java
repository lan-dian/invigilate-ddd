package com.hfut.invigilate.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hfut.invigilate.entity.Exam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfut.invigilate.model.exam.ExamAssignVO;
import com.hfut.invigilate.model.exam.ExamPageQueryDTO;
import com.hfut.invigilate.model.exam.ExamTeachersVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 考试 Mapper 接口
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface ExamMapper extends BaseMapper<Exam> {

    List<ExamTeachersVO> page(@Param("pos") Integer pos, @Param("limit") Integer limit, @Param("query") ExamPageQueryDTO query);

    Long count(@Param("query") ExamPageQueryDTO query);

    IPage<ExamAssignVO> listAssignVO(IPage<ExamAssignVO> iPage, @Param("departmentId") Integer departmentId);

    List<Integer> listWorkId(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    List<Exam> listConflictExam(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime, @Param("workId") Integer workId);

}
