package com.bi.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bi.entity.OdFieldDetailEntity;
import com.bi.service.OdFieldDetailService;
import com.bi.utils.PageUtils;
import com.bi.utils.Query;
import com.bi.utils.R;


/**
 * 表字段的详情（可更改）
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
@RestController
@RequestMapping("odfielddetail")
public class OdFieldDetailController {
	@Autowired
	private OdFieldDetailService odFieldDetailService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("odfielddetail:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<OdFieldDetailEntity> odFieldDetailList = odFieldDetailService.queryList(query);
		int total = odFieldDetailService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(odFieldDetailList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("odfielddetail:info")
	public R info(@PathVariable("id") Integer id){
		OdFieldDetailEntity odFieldDetail = odFieldDetailService.queryObject(id);
		
		return R.ok().put("odFieldDetail", odFieldDetail);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("odfielddetail:save")
	public R save(@RequestBody OdFieldDetailEntity odFieldDetail){
		odFieldDetailService.save(odFieldDetail);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("odfielddetail:update")
	public R update(@RequestBody OdFieldDetailEntity odFieldDetail){
		odFieldDetailService.update(odFieldDetail);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("odfielddetail:delete")
	public R delete(@RequestBody Integer[] ids){
		odFieldDetailService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
