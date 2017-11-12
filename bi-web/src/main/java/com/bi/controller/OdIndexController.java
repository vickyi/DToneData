package com.bi.controller;

import java.util.*;

import com.bi.entity.SysUserEntity;
import com.bi.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bi.entity.OdIndexEntity;
import com.bi.service.OdIndexService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.shiro.SecurityUtils.getSubject;


/**
 * 指标表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 14:26:52
 */
@RestController
@RequestMapping("odindex")
public class OdIndexController {
	@Autowired
	private OdIndexService odIndexService;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("odindex:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		if(params.get("v") == null){
			params.put("v","1");
		}
        Query query = new Query(params);
		if(!params.containsKey("status")){
			query.put("status","1");
		}
		query.put("del","1");
		List<OdIndexEntity> odIndexList = null;
		int total = 0;
		int v = Integer.parseInt((String) params.get("v")) ;
		if(v == 2){
			odIndexList = odIndexService.queryList(query);
			total = odIndexService.queryTotal(query);
		}else if(v == 1){
			odIndexList = odIndexService.queryListGroupByIndexName(query);
			total = odIndexService.queryTotalGroupByIndexName(query);
		}

		PageUtils pageUtil = new PageUtils(odIndexList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	@RequestMapping("/frontlist")
	public R frontlist(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		if(!params.containsKey("status")){
			query.put("status","1");
		}
		query.put("del","1");
		List<OdIndexEntity> odIndexList = odIndexService.queryListGroupByIndexName(query);
		int total = odIndexService.queryTotalGroupByIndexName(query);

		PageUtils pageUtil = new PageUtils(odIndexList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

    @RequestMapping("/export")
    public void exportIndex(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,Object> params = new HashMap<String,Object>();
            List<OdIndexEntity> odIndexList = null;
            params.put("business_type",request.getParameter("business_type"));
            params.put("index_type",request.getParameter("index_type"));
            params.put("status",request.getParameter("status"));
            params.put("index_name",request.getParameter("index_name"));
            params.put("del","1");
            if(request.getParameter("front") != null){
                Integer front = Integer.parseInt(request.getParameter("front"));
                if(front == 1){
                    odIndexList = odIndexService.queryListGroupByIndexName(params);
                }else if(front == 2){
					int v = Integer.parseInt(request.getParameter("v"));
					if(v == 1){
						odIndexList = odIndexService.queryListGroupByIndexName(params);
					}else if(v == 2){
						odIndexList = odIndexService.queryList(params);
					}
                }
            }else{
                odIndexList = odIndexService.queryListGroupByIndexName(params);
            }
            odIndexService.exportOneDataIndex(odIndexList, response);

        } catch (Exception e) {
            LOGGER.error("数据导出异常,ex:{}", e);
        }
    }

	/**
	 * 获取单个指标的信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		OdIndexEntity odIndex = odIndexService.queryObject(id);
		String indexName = odIndex.getIndexName();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("index_name_eq",indexName);
		List<OdIndexEntity> odIndexLists = odIndexService.queryList(map);
		return R.ok().put("odIndex", odIndex).put("odIndexLists",odIndexLists);
	}

	/**
	 * 获取多个指标的信息
	 */
	@RequestMapping("/infos")
	public R info(@RequestBody Integer[] ids){
		//String s = ArrayUtil.toString(ids, ",");
		//System.out.println(s);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids",ids);
		List<OdIndexEntity> odIndexLists = odIndexService.queryList(map);

		return R.ok().put("odIndexLists",odIndexLists);
	}

	private String computeVersion(String version,int versionSelect){
		String[] v = new String[2];
		if(version.indexOf(".") > 0){
			v =  version.split("\\.");
		}else{
			v[0] = "1";
			v[1] = "0";
		}

		int big = 1;
		int min = 0;

		if(versionSelect == 1){
			//判断大版本相加
			big = Integer.parseInt(v[0]) + 1;
			min = 0;
		}else if(versionSelect == 2){
			//判断小版本相加
			big = Integer.parseInt(v[0]);
			min = Integer.parseInt(v[1]) + 1;
		}

		return big +"."+ min;
	}
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("odindex:save")
	public R save(@RequestBody OdIndexEntity odIndex){
		int versionSelect = odIndex.getVersionSelect();
		if(versionSelect == 0){
			//判断没有选择大小版本的时候 为没有版本的添加操作
			odIndex.setVersion("1.0");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("index_name",odIndex.getIndexName());
			map.put("del","1");
			int total = getTotalByIndexName(map);
			if(total > 0){
				return R.error(1, "指标名称已存在！");
			}
		}else{
			//选择大小版本计算 有版本的添加操作
			String version = odIndex.getVersion();
			String newVersion = this.computeVersion(version,versionSelect);
			odIndex.setVersion(newVersion);

			//老版本置无效
			SysUserEntity sysUserEntity = (SysUserEntity)getSubject().getPrincipal();
			OdIndexEntity odIndexOld = new OdIndexEntity();
			odIndexOld.setModifyName(sysUserEntity.getUsername());
			odIndexOld.setModifyTime(new Date());
			odIndexOld.setId(odIndex.getId());
			odIndexOld.setStatus(0);
			odIndexService.update(odIndex);
		}

		SysUserEntity sysUserEntity = (SysUserEntity)getSubject().getPrincipal();
		odIndex.setCreateName(sysUserEntity.getUsername());
		odIndex.setCreateTime(new Date());
		odIndex.setDel(1);

		odIndexService.save(odIndex);
		
		return R.ok();
	}

	private int getTotalByIndexName(Map<String, Object>  map){
		int total = odIndexService.queryTotalByIndexName(map);
		return total;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("odindex:update")
	public R update(@RequestBody OdIndexEntity odIndex){
		SysUserEntity sysUserEntity = (SysUserEntity)getSubject().getPrincipal();
		odIndex.setModifyName(sysUserEntity.getUsername());
		odIndex.setModifyTime(new Date());
		odIndexService.update(odIndex);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("odindex:delete")
	public R delete(@RequestBody Integer[] ids){
		odIndexService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
