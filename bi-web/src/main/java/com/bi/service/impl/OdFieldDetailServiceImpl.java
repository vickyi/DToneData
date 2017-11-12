package com.bi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.bi.dao.OdFieldDetailDao;
import com.bi.entity.OdFieldDetailEntity;
import com.bi.service.OdFieldDetailService;



@Service("odFieldDetailService")
public class OdFieldDetailServiceImpl implements OdFieldDetailService {
	@Autowired
	private OdFieldDetailDao odFieldDetailDao;
	
	@Override
	public OdFieldDetailEntity queryObject(Integer id){
		return odFieldDetailDao.queryObject(id);
	}
	
	@Override
	public List<OdFieldDetailEntity> queryList(Map<String, Object> map){
		return odFieldDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return odFieldDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(OdFieldDetailEntity odFieldDetail){
		odFieldDetailDao.save(odFieldDetail);
	}
	
	@Override
	public void update(OdFieldDetailEntity odFieldDetail){
		odFieldDetailDao.update(odFieldDetail);
	}
	
	@Override
	public void delete(Integer id){
		odFieldDetailDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		odFieldDetailDao.deleteBatch(ids);
	}
	
}
