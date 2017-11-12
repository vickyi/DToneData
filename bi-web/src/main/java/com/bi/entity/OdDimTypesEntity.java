package com.bi.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 状态汇总表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-28 14:27:02
 */
public class OdDimTypesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增型代理主键
	private Integer incId;
	//业务主键
	private Integer id;
	//分类
	private String name;
	//业务类型id，1:数据主题,2:数据更新频率,3:数据状态
	private Integer typeId;
	//业务类型名称，1:数据主题,2:数据更新频率,3:数据状态
	private String typeName;
	//是否删除，默认0表示未删除；1表示删除
	private Integer delFlag;
	//记录创建时间
	private Date createTime;
	//修改时间
	private Date lastModified;

	/**
	 * 设置：自增型代理主键
	 */
	public void setIncId(Integer incId) {
		this.incId = incId;
	}
	/**
	 * 获取：自增型代理主键
	 */
	public Integer getIncId() {
		return incId;
	}
	/**
	 * 设置：业务主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：业务主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：分类
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：分类
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：业务类型id，1:数据主题,2:数据更新频率,3:数据状态
	 */
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	/**
	 * 获取：业务类型id，1:数据主题,2:数据更新频率,3:数据状态
	 */
	public Integer getTypeId() {
		return typeId;
	}
	/**
	 * 设置：业务类型名称，1:数据主题,2:数据更新频率,3:数据状态
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * 获取：业务类型名称，1:数据主题,2:数据更新频率,3:数据状态
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * 设置：是否删除，默认0表示未删除；1表示删除
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：是否删除，默认0表示未删除；1表示删除
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：记录创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：记录创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getLastModified() {
		return lastModified;
	}
}
