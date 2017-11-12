package com.bi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.bi.dao.OdTableDao;
import com.bi.entity.OdTableEntity;
import com.bi.service.OdDimTypesService;
import com.bi.service.OdTableService;
import com.bi.utils.CSVUtils;



@Service("odTableService")
public class OdTableServiceImpl implements OdTableService {
	@Autowired
	private OdTableDao odTableDao;
	
	@Autowired
    private OdDimTypesService odDimTypesService;
	
	@Override
	public OdTableEntity queryObject(Integer id){
		return odTableDao.queryObject(id);
	}
	
	@Override
	public List<OdTableEntity> queryList(Map<String, Object> map){
		return odTableDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return odTableDao.queryTotal(map);
	}
	
	@Override
	public void save(OdTableEntity odTable){
		odTableDao.save(odTable);
	}
	
	@Override
	public void update(OdTableEntity odTable){
		odTableDao.update(odTable);
	}
	
	@Override
	public void delete(Integer id){
		odTableDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		odTableDao.deleteBatch(ids);
	}

    @Override
    public OdTableEntity queryObjectById(Integer id) {
        return odTableDao.queryObjectById(id);
    }
    
    /**
     * 设置表的版本
     * 处理为大版本或者小版本的值，返回表实体
     * 
     * @param odTable
     * @param isversion 1小版本，2大版本
     * @return
     */
    public OdTableEntity checkVersion(OdTableEntity odTable, Integer isversion){
        String verinfo = odTable.getVersion();
        if(verinfo == null)
            return odTable;
        
        // 保持精度计算
        BigDecimal vernumobj = new BigDecimal(verinfo);
        BigDecimal verdb = new BigDecimal("0.1");
        Double vernum = Double.parseDouble(verinfo);
        // 小版本
        if(isversion.equals(1)){
            
            odTable.setVersion(vernumobj.add(verdb).toString());
        }else if(isversion.equals(2)){
            // 大版本
            Integer verintnum = vernum.intValue();
            verintnum += 1;
            vernum = verintnum.doubleValue();
            odTable.setVersion(vernum.toString());
        }
        
        return odTable;
    }
    
    
    /**
     * 根据条件查询所有历史版本的table list
     * @param map
     * @return
     */
    public List<OdTableEntity> queryVersionList(Map<String, Object> map){
        return odTableDao.queryVersionList(map);
    }
	
    
    /**
     * 表管理数据内容导出
     * 
     */
    @Override
    public void exportOdtable(List<OdTableEntity> odTableList, HttpServletResponse response) {
        try {
            
            // 获取所有的枚举数据
            HashMap<String, List<Map<String, String>>> typeslist = odDimTypesService.getListAll();
            
            // 主题枚举列表
            List<Map<String, String>> topics = typeslist.get("1");
            // 更新频率枚举列表
            List<Map<String, String>> frequencys = typeslist.get("2");
            // 数据库类型
            List<Map<String, String>> dbtypes = typeslist.get("3");
            
            ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(int i = 0; i < odTableList.size(); i++){
                Map<String,Object> map =new HashMap<String,Object>();
                map.put("tableId",odTableList.get(i).getTableId());
                // 所属主题
                String topic = String.valueOf(odTableList.get(i).getDataTopic());
                for(Map<String, String> topicmap: topics){
                    if(topicmap.get("id").equals(topic)){
                        map.put("dataTopic", topicmap.get("name"));
                        break;
                    }
                }
                
                
                map.put("tableName",odTableList.get(i).getTableName());
                map.put("tableDesc",odTableList.get(i).getTableDesc());
                // 数据库类型
                String dbtype = String.valueOf(odTableList.get(i).getDatabaseType());
                for(Map<String, String> typemap: dbtypes){
                    if(typemap.get("id").equals(dbtype)){
                        map.put("databaseType", typemap.get("name"));
                        break;
                    }
                }
                
                // 过滤逗号，避免csv导出列表影响
                String logicstr = odTableList.get(i).getUpdateLogic();
                if(logicstr != null){
                    logicstr = logicstr.replaceAll("[\\t\\n\\r]", "").replaceAll("[,]","，");
                }
                map.put("updateLogic", logicstr);
                // 更新频率
                String frequency = String.valueOf(odTableList.get(i).getUpdateFrequency());
                for(Map<String, String> fmap: frequencys){
                    if(fmap.get("id").equals(frequency)){
                        map.put("updateFrequency", fmap.get("name"));
                        break;
                    }
                }
                
                map.put("dataRange",odTableList.get(i).getDataRange());
                map.put("instructions",odTableList.get(i).getInstructions());
                map.put("owner",odTableList.get(i).getOwner());
                
                DateFormat dtf = DateFormat.getDateTimeInstance();
                map.put("modifyTime", "\t"+dtf.format(odTableList.get(i).getModifyTime()));
                map.put("modifyName",odTableList.get(i).getModifyName());

                if(odTableList.get(i).getStatus()!=null && odTableList.get(i).getStatus() == 1){
                    map.put("status","有效");
                }else{
                    map.put("status","无效");
                }
                
                list.add(map);
            }

            Map<String, String> exportHeader = new LinkedHashMap<>();
            exportHeader.put("tableId","表编号");
            exportHeader.put("dataTopic","归属主题");
            exportHeader.put("tableName","表名");
            exportHeader.put("tableDesc","表描述");
            exportHeader.put("databaseType","数据库类型");
            exportHeader.put("updateLogic","更新逻辑");
            exportHeader.put("updateFrequency","更新频率");
            exportHeader.put("dataRange","数据范围");
            exportHeader.put("instructions","使用说明");
            exportHeader.put("owner","负责人");
            exportHeader.put("status","状态");
            exportHeader.put("modifyTime","最后更改日期");
            exportHeader.put("modifyName","更改人");
            String exportFileName = String.format("onedata表管理数据.csv");
            CSVUtils.exportFile(response, list, exportHeader, exportFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
