package com.zzj.translate.model.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouzhijun
 * @since 2022-05-14
 */
@ApiModel(value = "Stardict对象", description = "")
public class Stardict implements Serializable {

    private static final long serialVersionUID = 1L;

    private String word;

    private String phonetic;

    private String definition;

    private String translation;

    private String pos;

    private String collins;

    private String oxford;

    private String tag;

    private String bnc;

    private String frq;

    private String exchange;

    private String detail;

    private String audio;

    private String others;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }
    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
    public String getCollins() {
        return collins;
    }

    public void setCollins(String collins) {
        this.collins = collins;
    }
    public String getOxford() {
        return oxford;
    }

    public void setOxford(String oxford) {
        this.oxford = oxford;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getBnc() {
        return bnc;
    }

    public void setBnc(String bnc) {
        this.bnc = bnc;
    }
    public String getFrq() {
        return frq;
    }

    public void setFrq(String frq) {
        this.frq = frq;
    }
    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "Stardict{" +
            "word=" + word +
            ", phonetic=" + phonetic +
            ", definition=" + definition +
            ", translation=" + translation +
            ", pos=" + pos +
            ", collins=" + collins +
            ", oxford=" + oxford +
            ", tag=" + tag +
            ", bnc=" + bnc +
            ", frq=" + frq +
            ", exchange=" + exchange +
            ", detail=" + detail +
            ", audio=" + audio +
            ", others=" + others +
        "}";
    }
}
