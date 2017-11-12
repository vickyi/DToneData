package com.bi.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 表字段的详情（可更改）
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
public class OdFieldDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//表id
	private Integer fid;
	//启用时间
	private Date enabledTime;
	//失效时间
	private Date expiryTime;
	//关联维表
	private String relationTableId;
	//关联维表ID
	private Integer relationTable;
	//备注
	private String remark;
	//版本号
	private String version;
	//修改人
	private String modifyName;
	//修改时间
	private Date modifyTime;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：表id
	 */
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	/**
	 * 获取：表id
	 */
	public Integer getFid() {
		return fid;
	}
	/**
	 * 设置：启用时间
	 */
	public void setEnabledTime(Date enabledTime) {
		this.enabledTime = enabledTime;
	}
	/**
	 * 获取：启用时间
	 */
	public Date getEnabledTime() {
		return enabledTime;
	}
	/**
	 * 设置：失效时间
	 */
	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
	/**
	 * 获取：失效时间
	 */
	public Date getExpiryTime() {
		return expiryTime;
	}
	/**
	 * 设置：关联维表
	 */
	public void setRelationTableId(String relationTableId) {
		this.relationTableId = relationTableId;
	}
	/**
	 * 获取：关联维表
	 */
	public String getRelationTableId() {
		return relationTableId;
	}
	/**
	 * 设置：关联维表ID
	 */
	public void setRelationTable(Integer relationTable) {
		this.relationTable = relationTable;
	}
	/**
	 * 获取：关联维表ID
	 */
	public Integer getRelationTable() {
		return relationTable;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * 获取：版本号
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * 设置：修改人
	 */
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	/**
	 * 获取：修改人
	 */
	public String getModifyName() {
		return modifyName;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
}
