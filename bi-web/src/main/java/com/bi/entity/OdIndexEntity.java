package com.bi.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 指标表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 14:26:52
 */
public class OdIndexEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//指标名称
	private String indexName;
	//指标英文名称
	private String index;
	//业务类型
	private String businessType;
	//指标类型
	private String indexType;
	//指标定义
	private String indexDesc;
	//数据库
	private String database;
	//表名
	private String databaseTable;
	//指标的计算公式
	private String indexCompute;
	//限制条件
	private String whereString;
	//使用场景
	private String useScenes;
	//olap 节点
	private String olapNodes;
	//观星台 节点
	private String gxtNodes;
	//1 有效 0 无效
	private Integer status;
	//大版本
	private String version;
	//更改说明
	private String modifyDesc;
	//创建时间
	private Date createTime;
	//创建人
	private String createName;
	//修改时间
	private Date modifyTime;
	//修改人
	private String modifyName;
	//版本选择 大版本 小版本 无修改
	private int versionSelect;
	//是否删除
	private int del;

	public String getWhereString() {
		return whereString;
	}

	public void setWhereString(String whereString) {
		this.whereString = whereString;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	public int getVersionSelect() {
		return versionSelect;
	}

	public void setVersionSelect(int versionSelect) {
		this.versionSelect = versionSelect;
	}

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
	 * 设置：指标名称
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	/**
	 * 获取：指标名称
	 */
	public String getIndexName() {
		return indexName;
	}
	/**
	 * 设置：指标英文名称
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * 获取：指标英文名称
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * 设置：业务类型
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	/**
	 * 获取：业务类型
	 */
	public String getBusinessType() {
		return businessType;
	}
	/**
	 * 设置：指标类型
	 */
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
	/**
	 * 获取：指标类型
	 */
	public String getIndexType() {
		return indexType;
	}
	/**
	 * 设置：指标定义
	 */
	public void setIndexDesc(String indexDesc) {
		this.indexDesc = indexDesc;
	}
	/**
	 * 获取：指标定义
	 */
	public String getIndexDesc() {
		return indexDesc;
	}
	/**
	 * 设置：数据库
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
	/**
	 * 获取：数据库
	 */
	public String getDatabase() {
		return database;
	}
	/**
	 * 设置：表名
	 */
	public void setDatabaseTable(String databaseTable) {
		this.databaseTable = databaseTable;
	}
	/**
	 * 获取：表名
	 */
	public String getDatabaseTable() {
		return databaseTable;
	}
	/**
	 * 设置：指标的计算公式
	 */
	public void setIndexCompute(String indexCompute) {
		this.indexCompute = indexCompute;
	}
	/**
	 * 获取：指标的计算公式
	 */
	public String getIndexCompute() {
		return indexCompute;
	}
	/**
	 * 设置：使用场景
	 */
	public void setUseScenes(String useScenes) {
		this.useScenes = useScenes;
	}
	/**
	 * 获取：使用场景
	 */
	public String getUseScenes() {
		return useScenes;
	}
	/**
	 * 设置：olap 节点
	 */
	public void setOlapNodes(String olapNodes) {
		this.olapNodes = olapNodes;
	}
	/**
	 * 获取：olap 节点
	 */
	public String getOlapNodes() {
		return olapNodes;
	}
	/**
	 * 设置：观星台 节点
	 */
	public void setGxtNodes(String gxtNodes) {
		this.gxtNodes = gxtNodes;
	}
	/**
	 * 获取：观星台 节点
	 */
	public String getGxtNodes() {
		return gxtNodes;
	}
	/**
	 * 设置：1 有效 0 无效
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：1 有效 0 无效
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：大版本
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * 获取：大版本
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * 设置：更改说明
	 */
	public void setModifyDesc(String modifyDesc) {
		this.modifyDesc = modifyDesc;
	}
	/**
	 * 获取：更改说明
	 */
	public String getModifyDesc() {
		return modifyDesc;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreateName() {
		return createName;
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

	@Override
	public String toString() {
		return "OdIndexEntity{" +
				"id=" + id +
				", indexName='" + indexName + '\'' +
				", index='" + index + '\'' +
				", businessType='" + businessType + '\'' +
				", indexType='" + indexType + '\'' +
				", indexDesc='" + indexDesc + '\'' +
				", database='" + database + '\'' +
				", databaseTable='" + databaseTable + '\'' +
				", indexCompute='" + indexCompute + '\'' +
				", useScenes='" + useScenes + '\'' +
				", olapNodes='" + olapNodes + '\'' +
				", gxtNodes='" + gxtNodes + '\'' +
				", status=" + status +
				", version='" + version + '\'' +
				", modifyDesc='" + modifyDesc + '\'' +
				", createTime=" + createTime +
				", createName='" + createName + '\'' +
				", modifyTime=" + modifyTime +
				", modifyName='" + modifyName + '\'' +
				", versionSelect=" + versionSelect +
				", del=" + del +
				'}';
	}
}
