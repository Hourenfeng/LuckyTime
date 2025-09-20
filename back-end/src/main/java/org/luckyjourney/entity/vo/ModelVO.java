package org.luckyjourney.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ModelVO {

    private Long userId;
    // 兴趣视频标签
    private List<String> labels;
}
