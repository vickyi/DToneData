package com.bi.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/7/31
 * Created by xianggu.
 *
 * @author xianggu
 */
public class BaseEntity implements Serializable {

    //添加用户名
    private Long createBy;
    //添加时间
    private Date createDate;
    //修改用户
    private Long updateBy;
    //修改时间
    private Date updateDate;

    /**
     * 设置：添加时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取：添加时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置：添加人
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取：添加人
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * 设置：修改时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取：修改时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置：修改人
     */
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取：修改人
     */
    public Long getUpdateBy() {
        return updateBy;
    }
}
