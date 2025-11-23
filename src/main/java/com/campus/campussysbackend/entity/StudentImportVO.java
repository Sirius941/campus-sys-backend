package com.campus.campussysbackend.entity; // 或者 vo 包

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentImportVO {
    // 对应 Excel 第一列 "学生姓名"
    @ExcelProperty("学生姓名")
    private String stuName;

    // 对应 Excel 第二列 "来源地址"
    @ExcelProperty("来源地址")
    private String fromPlace;

    // 对应 Excel 第三列 "监护人电话"
    @ExcelProperty("监护人电话")
    private String parentPhone;

    // 对应 Excel 第四列 "当前年级"
    @ExcelProperty("当前年级")
    private String currentGrade;

    // 对应 Excel 第五列 "是否新生" (填 1 或 0)
    @ExcelProperty("是否新生")
    private String isFirst;

    // 对应 Excel 第六列 "备注"
    @ExcelProperty("备注")
    private String comment;
}