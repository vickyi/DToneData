package com.bi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bi.dao.OdDimTypesDao;
import com.bi.entity.OdDimTypesEntity;
import com.bi.service.OdDimTypesService;



@Service("odDimTypesService")
public class OdDimTypesServiceImpl implements OdDimTypesService {
	@Autowired
	private OdDimTypesDao odDimTypesDao;
	
	private static final String ONEDATA_DIM_TYPES = "org.bi.manage.onedata.dimtypes";
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public OdDimTypesEntity queryObject(Integer incId){
		return odDimTypesDao.queryObject(incId);
	}
	
	@Override
	public List<OdDimTypesEntity> queryList(Map<String, Object> map){
		return odDimTypesDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return odDimTypesDao.queryTotal(map);
	}
	
	@Override
	public void save(OdDimTypesEntity odDimTypes){
		odDimTypesDao.save(odDimTypes);
	}
	
	@Override
	public void update(OdDimTypesEntity odDimTypes){
		odDimTypesDao.update(odDimTypes);
	}
	
	@Override
	public void delete(Integer incId){
		odDimTypesDao.delete(incId);
	}
	
	@Override
	public void deleteBatch(Integer[] incIds){
		odDimTypesDao.deleteBatch(incIds);
	}

    @Override
    public List<OdDimTypesEntity> queryListByTypeid(Integer typeId) {
        return odDimTypesDao.queryListByTypeId(typeId);
    }
    
    /**
     * 获取所有的枚举数据
     * 加入redis 缓存
     * 
     * @return
     */
    public HashMap<String, List<Map<String, String>>> getListAll(){

        HashMap<String, List<Map<String, String>>> keymap = null;
        
        // redis key值
        String rediskey = ONEDATA_DIM_TYPES;
        // 检测redis 中是否存在值
        String jsonstr = (String) redisTemplate.opsForValue().get(rediskey);
        if(jsonstr != null){
            // json解析为hashmap对象，建立hashmap结构
            JSONObject jsonb = JSON.parseObject(jsonstr);
            keymap = jsonb.parseObject(jsonstr, new TypeReference<HashMap<String, List<Map<String, String>>>>(){});
            
            if(keymap.size() > 0){
                return keymap;
            }
        }else {

            // 查询数据库
            List<OdDimTypesEntity> odDimTypesList = queryList(null);
            keymap = formatKeyMap(odDimTypesList);
            
            if(keymap.size() > 0){
                // 写入redis
                redisTemplate.opsForValue().set(rediskey, JSON.toJSONString(keymap));
                redisTemplate.expire(rediskey, 3600, TimeUnit.SECONDS);
            }
        }
        
        return keymap;
        
    }
	
    /**
     * 枚举数据结构转换
     * 将list数据转换为可用的hashmap
     * 
     * @param odDimList
     * @return
     */
    public HashMap<String, List<Map<String, String>>> formatKeyMap(List<OdDimTypesEntity> odDimList){
        
        HashMap<String, List<Map<String, String>>> keymap = new HashMap<String, List<Map<String, String>>>();
        if(odDimList.size() <= 0){
            return keymap;
        }
        
        String key;
        List<Map<String, String>> innerList = null;
        for (OdDimTypesEntity odDim : odDimList) {
            key = odDim.getTypeId().toString();
            if (keymap.containsKey(key)) {
                innerList = keymap.get(key);
            } else {
                innerList = new LinkedList<>();
                keymap.put(key, innerList);
            }
            
            HashMap<String, String> item = new HashMap<>();
            item.put("id", odDim.getId().toString() );
            item.put("name", odDim.getName());
            
            innerList.add(item);
        }
        
        return keymap;
    }
}
