package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_manager_activity")
public class CampusActivity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;        // 活动名
    private String category;    // 活动类型
    private String title;       // 全称
    private String creator;     // 创建者
    private String abstractStr; // 摘要 (注意：abstract是Java关键字，这里改名为abstractStr，需配合配置文件开启驼峰映射)
    private String detailedDesc;// 详细内容
    private Integer status;     // 0已创建 1已发布 2已结束 3已删除
    private LocalDateTime createdTime;
    private LocalDateTime archivedTime;
    private String picUrls;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
