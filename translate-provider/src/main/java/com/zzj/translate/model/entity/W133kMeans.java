package com.zzj.translate.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouzhijun
 * @since 2022-05-12
 */
@TableName("w133k_means")
@ApiModel(value = "W133kMeans对象", description = "")
public class W133kMeans implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer wordId;

    private Integer posId;

    private String means;

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }
    public Integer getPosId() {
        return posId;
    }

    public void setPosId(Integer posId) {
        this.posId = posId;
    }
    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    @Override
    public String toString() {
        return "W133kMeans{" +
            "wordId=" + wordId +
            ", posId=" + posId +
            ", means=" + means +
        "}";
    }
}
