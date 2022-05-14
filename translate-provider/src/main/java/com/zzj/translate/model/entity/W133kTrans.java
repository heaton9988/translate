package com.zzj.translate.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@TableName("w133k_trans")
@Data
@ToString
public class W133kTrans implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String word;
    private String mean;
    private String exchange;
    private String voice;
    private Integer times;
}