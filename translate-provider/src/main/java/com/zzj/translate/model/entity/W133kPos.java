package com.zzj.translate.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@TableName("w133k_pos")
@Data
@ToString
public class W133kPos implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String means;
}