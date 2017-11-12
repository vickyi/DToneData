package com.bi.controller;

import static org.apache.shiro.SecurityUtils.getSubject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bi.entity.OdFieldEntity;
import com.bi.entity.OdTableEntity;
import com.bi.entity.SysUserEntity;
import com.bi.service.OdFieldService;
import com.bi.service.OdTableService;
import com.bi.utils.PageUtils;
import com.bi.utils.Query;
import com.bi.utils.R;


/**
 * 表对应的字段表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
@RestController
@RequestMapping("odfield")
public class OdFieldController {
	@Autowired
	private OdFieldService odFieldService;
	
	@Autowired
    private OdTableService odTableService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("odfield:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<OdFieldEntity> odFieldList = odFieldService.queryList(query);
		int total = odFieldService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(odFieldList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("odfield:info")
	public R info(@PathVariable("id") Integer id){
		OdFieldEntity odField = odFieldService.queryObject(id);
		
		return R.ok().put("odField", odField);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("odfield:save")
	public R save(@RequestBody OdFieldEntity odField){
		odFieldService.save(odField);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("odfield:update")
	public R update(@RequestBody OdFieldEntity odField){
		odFieldService.update(odField);
		
		return R.ok();
	}
	
	/**
     * 修改列表数据
     */
    @RequestMapping("/updatelist")
    // @RequiresPermissions("odfield:updatelist")
    public R updateList(@RequestBody Map<String, Map<String, String>> params){
        
        // 遍历传递过来的数据
        Iterator<Entry<String, Map<String, String>>> param =  params.entrySet().iterator();
        
        // 获取登录用户信息
        SysUserEntity sysUserEntity = (SysUserEntity)getSubject().getPrincipal();
        
        // tableid
        Integer tableid = 0;
        
        while (param.hasNext()) {
            Entry<String, Map<String, String>> map = param.next();
            
            Map<String, String> item = map.getValue();
            // 组装对象，更新字段
            OdFieldEntity odFieldEntity = new OdFieldEntity();
            // 字段表主键ID
            Integer fId = Integer.valueOf(item.get("id").toString());
            odFieldEntity.setId(fId);
            odFieldEntity.setFieldName(item.get("fieldName"));

            // 启用时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date etime;
            try {
                etime = simpleDateFormat.parse(item.get("enabledTime"));
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }
            odFieldEntity.setEnabledTime( etime);
            
            Date extime;
            try {
                extime = simpleDateFormat.parse(item.get("expiryTime"));
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }
            odFieldEntity.setExpiryTime(extime);
            // 关联维表 TODO 关联维表id从哪里获取？？手动维护或者查询相关表
            odFieldEntity.setRelationTable(item.get("relationTable"));
            
            // 写入备注
            odFieldEntity.setRemark(item.get("remark"));
            
            // 设置修改人
            odFieldEntity.setModifyName(sysUserEntity.getUsername());
            // 设置修改时间
            odFieldEntity.setModifyTime(new Date());
            odFieldService.update(odFieldEntity);
            
            // 设置tableid值
            if(tableid != null && tableid == 0){
                OdFieldEntity odfieldObj = odFieldService.queryObject(fId);
                tableid = odfieldObj.getTableId();
            }
        }
        
        // 写入表管理数据，新增一个小版本记录
        OdTableEntity odTableEntity = odTableService.queryObject(tableid);
        odTableEntity = odTableService.checkVersion(odTableEntity, 1);
        odTableEntity.setId(null);
        odTableEntity.setModifyDesc("表字段变更");
        // 设置修改人
        odTableEntity.setModifyName(sysUserEntity.getUsername());
        // 设置修改时间
        odTableEntity.setModifyTime(new Date());
        odTableService.save(odTableEntity);
         
        return R.ok();
    }
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("odfield:delete")
	public R delete(@RequestBody Integer[] ids){
		odFieldService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
