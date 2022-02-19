package com.hfut.invigilate.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.hfut.invigilate.author.RoleConst;
import com.hfut.invigilate.author.UserAuthorService;
import com.hfut.invigilate.business.ExamLoadAssignService;
import com.hfut.invigilate.model.commen.CommonResult;
import com.hfut.invigilate.model.commen.PageDTO;
import com.hfut.invigilate.model.exam.*;
import com.hfut.invigilate.model.exam.err.ConflictList;
import com.hfut.invigilate.model.exam.err.ExamConflict;
import com.hfut.invigilate.service.ExamService;
import com.landao.guardian.annotations.author.RequiredRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "考试管理")
@RestController
@RequestMapping("/admin/exam")
public class ExamController {

    @Resource
    ExamService examService;

    @Resource
    ExamLoadAssignService examLoadAssignService;

    @Resource
    UserAuthorService userAuthorService;

    @RequiredRole(RoleConst.manager)
    @ApiOperation("二级管理分配教师")
    @PostMapping("assigned")
    public CommonResult<Void> assignedTeacher(@RequestBody List<Integer> workerIds) {
        CommonResult<Void> result=new CommonResult<>();

        Integer departmentId = userAuthorService.getDepartmentId();

        examLoadAssignService.assignWorkIds(workerIds,departmentId);

        return result.ok();
    }


    @ApiOperation("获取未分配人员的考试")
    @RequiredRole(RoleConst.admin)
    @GetMapping("/un_assigned")
    public CommonResult<PageDTO<ExamAssignVO>> listUnAssigned(@RequestParam(defaultValue = "1") Integer page,
                                                              @RequestParam(defaultValue = "15") Integer limit){
        CommonResult<PageDTO<ExamAssignVO>> result=new CommonResult<>();


        PageDTO<ExamAssignVO> examAssignDTOPageDTO = examService.listRequiredAssignExam(page, limit, null);

        return result.body(examAssignDTOPageDTO);
    }

    @RequiredRole(RoleConst.manager)
    @ApiOperation(value = "获取自己部门未分配的考试",notes = "二级管理员使用,获取自己部门成员的接口在/用户管理中")
    @GetMapping("/un_assigned/department")
    public CommonResult<DepartmentExamAssignVO> getDepartmentUnAssignedExam(){
        CommonResult<DepartmentExamAssignVO> result=new CommonResult<>();

        Integer departmentId = userAuthorService.getDepartmentId();

        DepartmentExamAssignVO departmentUnAssignedExam = examService.getDepartmentUnAssignedExam(departmentId);

        return result.body(departmentUnAssignedExam);
    }


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

        ConflictList conflicts = examLoadAssignService.loadExam(examExcels);
        if(conflicts.size()==0){
            return result.ok("成功导入"+conflicts.getSuccessCount()+"条考试信息");
        }else {
            return result.err("成功导入"+conflicts.getSuccessCount()+"条考试信息").setData(conflicts);
        }

        /*List<Department> departments = examExcels.stream()
                .map(ExamExcel::getCollege)
                .distinct()
                .map(Department::convert)
                .collect(Collectors.toList());

        iDepartmentService.saveBatch(departments);*/
    }

    // @RequiredLogin
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
