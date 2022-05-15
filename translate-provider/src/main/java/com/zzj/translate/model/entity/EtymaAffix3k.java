package com.zzj.translate.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouzhijun
 * @since 2022-05-15
 */
@TableName("etyma_affix_3k")
@Data
@Builder
@ToString
public class EtymaAffix3k implements Serializable {
    private Integer id;

    private String affix;

    private String mean;

    private String example;
}