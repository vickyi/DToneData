package com.bi.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * table表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
public class OdTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//业务表id（唯一）
	private Integer tableId;
	//表
	private String tableName;
	//数据主题
	private Integer dataTopic;
	
	// hive表类型
    private String tableType;
    
    // 数据库id
    private Long databaseId;

    // 数据库名
    private String databaseName;
    
    // 数据在hdfs上存储路径id
    private Long sdId;
    
    //数据库类型
	private Integer databaseType;
	//表描述
	private String tableDesc;
	//更新逻辑
	private String updateLogic;
	//更新频率
	private Integer updateFrequency;
	//数据范围
	private String dataRange;
	//使用说明
	private String instructions;
	//负责人
	private String owner;
	//状态(1 有效 0 无效)
	private Integer status;
	//版本
	private String version;
	//表的数据实例
	private String datas;
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
	 * 设置：业务表id（唯一）
	 */
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	/**
	 * 获取：业务表id（唯一）
	 */
	public Integer getTableId() {
		return tableId;
	}
	/**
	 * 设置：表
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * 获取：表
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * 设置：数据主题
	 */
	public void setDataTopic(Integer dataTopic) {
		this.dataTopic = dataTopic;
	}
	/**
	 * 获取：数据主题
	 */
	public Integer getDataTopic() {
		return dataTopic;
	}
	/**
	 * 设置：数据库类型
	 */
	public void setDatabaseType(Integer databaseType) {
		this.databaseType = databaseType;
	}
	/**
	 * 获取：数据库类型
	 */
	public Integer getDatabaseType() {
		return databaseType;
	}
	/**
	 * 设置：表描述
	 */
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	/**
	 * 获取：表描述
	 */
	public String getTableDesc() {
		return tableDesc;
	}
	/**
	 * 设置：更新逻辑
	 */
	public void setUpdateLogic(String updateLogic) {
		this.updateLogic = updateLogic;
	}
	/**
	 * 获取：更新逻辑
	 */
	public String getUpdateLogic() {
		return updateLogic;
	}
	/**
	 * 设置：更新频率
	 */
	public void setUpdateFrequency(Integer updateFrequency) {
		this.updateFrequency = updateFrequency;
	}
	/**
	 * 获取：更新频率
	 */
	public Integer getUpdateFrequency() {
		return updateFrequency;
	}
	/**
	 * 设置：数据范围
	 */
	public void setDataRange(String dataRange) {
		this.dataRange = dataRange;
	}
	/**
	 * 获取：数据范围
	 */
	public String getDataRange() {
		return dataRange;
	}
	/**
	 * 设置：使用说明
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	/**
	 * 获取：使用说明
	 */
	public String getInstructions() {
		return instructions;
	}
	/**
	 * 设置：负责人
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * 获取：负责人
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * 设置：状态(1 有效 0 无效)
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态(1 有效 0 无效)
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：版本
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * 获取：版本
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * 设置：表的数据实例
	 */
	public void setDatas(String datas) {
		this.datas = datas;
	}
	/**
	 * 获取：表的数据实例
	 */
	public String getDatas() {
		return datas;
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
	
	public String getTableType() {
        return tableType;
    }
    public void setTableType(String tableType) {
        this.tableType = tableType;
    }
    public Long getDatabaseId() {
        return databaseId;
    }
    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }
    public String getDatabaseName() {
        return databaseName;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    public Long getSdId() {
        return sdId;
    }
    public void setSdId(Long sdId) {
        this.sdId = sdId;
    }
}
