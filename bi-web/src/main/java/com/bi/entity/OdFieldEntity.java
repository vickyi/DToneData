package com.bi.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 表对应的字段表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
public class OdFieldEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//业务表id
	private Integer tableId;
	//表名
	private String tableName;
	//字段英文名称
	private String field;
	//字段英文中文名称
	private String fieldName;
	//字段类型
	private String fieldType;
	//时段长度
	private Integer fieldLength;
	//可否为空
	private Integer isNull;
	//是否主键
	private Integer isKey;
	//启用时间
	private Date enabledTime;
	//失效时间
	private Date expiryTime;
	//关联维表id
	private Integer relationTableId;
	//关联维表
	private String relationTable;
	//备注
	private String remark;
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
	 * 设置：业务表id
	 */
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	/**
	 * 获取：业务表id
	 */
	public Integer getTableId() {
		return tableId;
	}
	/**
	 * 设置：表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * 获取：表名
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * 设置：字段英文名称
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * 获取：字段英文名称
	 */
	public String getField() {
		return field;
	}
	/**
	 * 设置：字段英文中文名称
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * 获取：字段英文中文名称
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * 设置：字段类型
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	/**
	 * 获取：字段类型
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * 设置：时段长度
	 */
	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}
	/**
	 * 获取：时段长度
	 */
	public Integer getFieldLength() {
		return fieldLength;
	}
	/**
	 * 设置：可否为空
	 */
	public void setIsNull(Integer isNull) {
		this.isNull = isNull;
	}
	/**
	 * 获取：可否为空
	 */
	public Integer getIsNull() {
		return isNull;
	}
	/**
	 * 设置：是否主键
	 */
	public void setIsKey(Integer isKey) {
		this.isKey = isKey;
	}
	/**
	 * 获取：是否主键
	 */
	public Integer getIsKey() {
		return isKey;
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
	 * 设置：关联维表id
	 */
	public void setRelationTableId(Integer relationTableId) {
		this.relationTableId = relationTableId;
	}
	/**
	 * 获取：关联维表id
	 */
	public Integer getRelationTableId() {
		return relationTableId;
	}
	/**
	 * 设置：关联维表
	 */
	public void setRelationTable(String relationTable) {
		this.relationTable = relationTable;
	}
	/**
	 * 获取：关联维表
	 */
	public String getRelationTable() {
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
