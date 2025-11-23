package com.campus.campussysbackend.common; // 建议放在这里或专门的 listener 包

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.campus.campussysbackend.entity.Student;
import com.campus.campussysbackend.entity.StudentImportVO;
import com.campus.campussysbackend.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class StudentImportListener implements ReadListener<StudentImportVO> {

    // 因为 Listener 不能被 Spring 容器托管（每次读取都要 new），所以需要通过构造方法传入 Service
    private IStudentService studentService;

    public StudentImportListener(IStudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 每读取一行数据，都会调用一次这个方法
     */
    @Override
    public void invoke(StudentImportVO data, AnalysisContext context) {
        // 1. 数据转换：将 Excel 对象 (VO) 转为 数据库实体 (Entity)
        Student student = new Student();
        // 复制属性：注意属性名要一致，或者手动 set
        BeanUtils.copyProperties(data, student);

        // 2. 调用 Service 保存
        // 这里直接复用“新增学员”的逻辑，它会自动创建账号
        try {
            studentService.addStudentWithUser(student);
        } catch (Exception e) {
            // 实际项目中可以记录错误日志或收集错误行号，这里简单打印
            log.error("导入学员失败: {}，原因: {}", data.getStuName(), e.getMessage());
        }
    }

    /**
     * 所有数据解析完成都会调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("Excel 所有数据解析完成！");
    }
}