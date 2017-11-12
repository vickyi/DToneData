package com.bi.service.impl;

import com.bi.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.bi.dao.OdIndexDao;
import com.bi.entity.OdIndexEntity;
import com.bi.service.OdIndexService;

import javax.servlet.http.HttpServletResponse;


@Service("odIndexService")
public class OdIndexServiceImpl implements OdIndexService {
	@Autowired
	private OdIndexDao odIndexDao;
	
	@Override
	public OdIndexEntity queryObject(Integer id){
		return odIndexDao.queryObject(id);
	}
	
	@Override
	public List<OdIndexEntity> queryList(Map<String, Object> map){
		return odIndexDao.queryList(map);
	}

	@Override
	public List<OdIndexEntity> queryListGroupByIndexName(Map<String, Object> map) {
		return odIndexDao.queryListGroupByIndexName(map);
	}

	@Override
	public int queryTotalGroupByIndexName(Map<String, Object> map) {
		return odIndexDao.queryTotalGroupByIndexName(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return odIndexDao.queryTotal(map);
	}

    @Override
    public int queryTotalByIndexName(Map<String, Object> map) {
        return odIndexDao.queryTotalByIndexName(map);
    }

    @Override
	public void save(OdIndexEntity odIndex){
		odIndexDao.save(odIndex);
	}
	
	@Override
	public void update(OdIndexEntity odIndex){
		odIndexDao.update(odIndex);
	}
	
	@Override
	public void delete(Integer id){
		odIndexDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		odIndexDao.deleteBatch(ids);
	}

    @Override
    public void exportOneDataIndex(List<OdIndexEntity> odIndexList, HttpServletResponse response) {
        try {
            ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(int i = 0; i < odIndexList.size(); i++){
                Map<String,Object> map =new HashMap<String,Object>();
                map.put("indexName",odIndexList.get(i).getIndexName());
                map.put("index",odIndexList.get(i).getIndex());
                map.put("businessType",odIndexList.get(i).getBusinessType());
                map.put("indexType",odIndexList.get(i).getIndexType());
                if(odIndexList.get(i).getStatus()!=null && odIndexList.get(i).getStatus() == 1){
                    map.put("status","有效");
                }else{
                    map.put("status","无效");
                }

                String index_desc = odIndexList.get(i).getIndexDesc();
                if(index_desc != null){
                    map.put("indexDesc",index_desc.replaceAll("[\\t\\n\\r]", "").replaceAll("[,]","，"));
                }else{
                    map.put("indexDesc",index_desc);
                }
                map.put("database",odIndexList.get(i).getDatabase());
                map.put("databaseTable",odIndexList.get(i).getDatabaseTable());
                map.put("indexCompute",odIndexList.get(i).getIndexCompute());
                map.put("olapNodes",odIndexList.get(i).getOlapNodes());
                map.put("gxtNodes",odIndexList.get(i).getGxtNodes());
                map.put("useScenes",odIndexList.get(i).getUseScenes());
                //map.put("version",odIndexList.get(i).getVersion());
                map.put("modifyTime",odIndexList.get(i).getModifyTime());
                //map.put("modifyDesc",odIndexList.get(i).getModifyDesc());
                list.add(map);
            }

            Map<String, String> exportHeader = new LinkedHashMap<>();
            exportHeader.put("indexName","指标名称");
            exportHeader.put("index","指标英文名称");
            exportHeader.put("businessType","业务类型");
            exportHeader.put("indexType","指标类型");
            exportHeader.put("status","状态");
            exportHeader.put("indexDesc","业务定义");
            exportHeader.put("database","技术定义-数据库");
            exportHeader.put("databaseTable","技术定义-依赖表");
            exportHeader.put("indexCompute","技术定义-计算方式");
            exportHeader.put("olapNodes","olap节点");
            exportHeader.put("gxtNodes","观星台节点");
            exportHeader.put("useScenes","应用场景");
            //exportHeader.put("version","版本");
            exportHeader.put("modifyTime","最后更改时间");
            //exportHeader.put("modifyDesc","更改说明");
            String exportFileName = String.format("onedata指标报表.csv");
            CSVUtils.exportFile(response, list, exportHeader, exportFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
