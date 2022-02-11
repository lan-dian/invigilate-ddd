package com.hfut.invigilate.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.business.ExamService;
import com.hfut.invigilate.entity.Department;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.exam.ExamConflict;
import com.hfut.invigilate.model.exam.ExamExcel;
import com.hfut.invigilate.model.exam.ExamPageQueryDTO;
import com.hfut.invigilate.model.exam.ExamTeachersVO;
import com.hfut.invigilate.service.IDepartmentService;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "考试管理")
@RestController
@RequestMapping("/admin/exam")
public class ExamController {

    /*@Resource
    IDepartmentService iDepartmentService;*/

    @Resource
    ExamService examService;

    @RequiredRole(RoleConst.admin)
    @PostMapping("/upload/excel")
    @ApiOperation(value = "管理员上传考试文件",notes = "返回code:-1,需要展示错误列表")
    // @ApiImplicitParam(name = "file", value = "上传的文件（excel）", required = true, dataType = "__File")
    public CommonResult<List<ExamConflict>> uploadExcel(@RequestParam MultipartFile file) {
        CommonResult<List<ExamConflict>> result=new CommonResult<>();
        if (file.isEmpty()) {
            return result.err("文件为空");
        }

        ImportParams importParams = new ImportParams();

        List<ExamExcel> examExcels=null;
        try {
            examExcels = ExcelImportUtil.importExcel(file.getInputStream(), ExamExcel.class,importParams);
        } catch (Exception e) {
            return result.err("文件读取失败");
        }

        List<ExamConflict> conflicts = examService.loadExam(examExcels);
        if(conflicts.isEmpty()){
            return result.ok("成功导入"+examExcels.size()+"条考试信息");
        }else {
            return result.err("成功导入"+(examExcels.size()-conflicts.size())+"条考试信息").setData(conflicts);
        }

        /*List<Department> departments = examExcels.stream()
                .map(ExamExcel::getCollege)
                .distinct()
                .map(Department::convert)
                .collect(Collectors.toList());

        iDepartmentService.saveBatch(departments);*/
    }

    @PostMapping("/page")
    @ApiOperation("考试信息,分页查询")
    public CommonResult<PageDTO<ExamTeachersVO>> page(@RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "15") Integer limit,
                                                      @RequestBody(required = false) ExamPageQueryDTO query){
        CommonResult<PageDTO<ExamTeachersVO>> result=new CommonResult<>();

        if(query!=null){
            if(query.getStartDate()!=null && query.getEndDate()!=null
                    && query.getStartDate().isAfter(query.getEndDate())){
                return result.err("开始日期不能晚与结束日期");
            }
        }

        PageDTO<ExamTeachersVO> iPage = examService.page(page, limit, query);
        return result.body(iPage);

    }


}
