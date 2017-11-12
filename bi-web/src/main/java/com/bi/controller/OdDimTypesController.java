package com.bi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bi.entity.OdDimTypesEntity;
import com.bi.service.OdDimTypesService;
import com.bi.utils.PageUtils;
import com.bi.utils.Query;
import com.bi.utils.R;


/**
 * 状态汇总表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-28 14:27:02
 */
@RestController
@RequestMapping("oddimtypes")
public class OdDimTypesController {
	@Autowired
	private OdDimTypesService odDimTypesService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("oddimtypes:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<OdDimTypesEntity> odDimTypesList = odDimTypesService.queryList(query);
		int total = odDimTypesService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(odDimTypesList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{incId}")
	@RequiresPermissions("oddimtypes:info")
	public R info(@PathVariable("incId") Integer incId){
		OdDimTypesEntity odDimTypes = odDimTypesService.queryObject(incId);
		
		return R.ok().put("odDimTypes", odDimTypes);
	}
	
	/**
	 * 根据类型id获取枚举列表数据
	 * 
	 * @param typeId
	 * @return
	 */
    @RequestMapping("/getListByTypeid/{typeId}")
    public R getListByTypeid(@PathVariable("typeId") Integer typeId){
        List<OdDimTypesEntity> odDimTypesList = odDimTypesService.queryListByTypeid(typeId);
        HashMap<String, List<Map<String, String>>> list = odDimTypesService.formatKeyMap(odDimTypesList);
        
        return R.ok().put("odDimTypesList", list);
    }
    
    /**
     * 获取所有的枚举数据，将所有的数据写入redis 缓存
     * 
     * @param typeId
     * @return
     */
    @RequestMapping("/getListAll/")
    public R getListAll(){
        
        HashMap<String, List<Map<String, String>>> list = odDimTypesService.getListAll();
        return R.ok().put("listall", list);
    }
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("oddimtypes:save")
	public R save(@RequestBody OdDimTypesEntity odDimTypes){
		odDimTypesService.save(odDimTypes);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("oddimtypes:update")
	public R update(@RequestBody OdDimTypesEntity odDimTypes){
		odDimTypesService.update(odDimTypes);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("oddimtypes:delete")
	public R delete(@RequestBody Integer[] incIds){
		odDimTypesService.deleteBatch(incIds);
		
		return R.ok();
	}
	
}
