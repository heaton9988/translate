package com.zzj.translate.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@TableName("w133k_means")
@Data
@ToString
public class W133kMeans implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer wordId;

    private Integer posId;

    private String means;
}