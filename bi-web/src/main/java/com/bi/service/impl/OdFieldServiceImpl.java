package com.bi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.bi.dao.OdFieldDao;
import com.bi.entity.OdFieldEntity;
import com.bi.service.OdFieldService;



@Service("odFieldService")
public class OdFieldServiceImpl implements OdFieldService {
	@Autowired
	private OdFieldDao odFieldDao;
	
	@Override
	public OdFieldEntity queryObject(Integer id){
		return odFieldDao.queryObject(id);
	}
	
	@Override
	public List<OdFieldEntity> queryList(Map<String, Object> map){
		return odFieldDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return odFieldDao.queryTotal(map);
	}
	
	@Override
	public void save(OdFieldEntity odField){
		odFieldDao.save(odField);
	}
	
	@Override
	public void update(OdFieldEntity odField){
		odFieldDao.update(odField);
	}
	
	@Override
	public void delete(Integer id){
		odFieldDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		odFieldDao.deleteBatch(ids);
	}
	
}
