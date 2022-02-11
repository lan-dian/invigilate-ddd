package com.hfut.invigilate.service;

import com.hfut.invigilate.entity.Invigilate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hfut.invigilate.model.invigilate.TeacherInvigilateVO;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 监考 服务类
 * </p>
 *
 * @author 常珂洁
 * @since 2022-02-10
 */
public interface IInvigilateService extends IService<Invigilate> {

    List<TeacherInvigilateVO> listInvigilate(Integer workId, LocalDate startDate, LocalDate endDate);

}
