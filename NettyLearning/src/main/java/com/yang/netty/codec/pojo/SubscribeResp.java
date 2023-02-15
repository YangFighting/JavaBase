package com.yang.netty.codec.pojo;

import java.io.Serializable;

/**
 * @Description:
 * @Author zhangyang03
 * @Date 2023/2/15 17:26
 */
public class SubscribeResp implements Serializable {
    private static final long serialVersionUID = -8225217966836907066L;
    private int subReqID;

    private int respCode;

    private String desc;

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
