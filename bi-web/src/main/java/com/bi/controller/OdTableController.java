package com.bi.controller;

import static org.apache.shiro.SecurityUtils.getSubject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.bi.entity.OdFieldEntity;
import com.bi.entity.OdIndexEntity;
import com.bi.entity.OdTableEntity;
import com.bi.entity.SysUserEntity;
import com.bi.service.OdFieldService;
import com.bi.service.OdTableService;
import com.bi.utils.PageUtils;
import com.bi.utils.Query;
import com.bi.utils.R;



/**
 * table表
 * 
 * @author bi
 * @email bi.com
 * @date 2017-09-27 17:17:20
 */
@RestController
@RequestMapping("odtable")
public class OdTableController {
	@Autowired
	private OdTableService odTableService;
	
	@Autowired
    private OdFieldService odFieldService;
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("odtable:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<OdTableEntity> odTableList = odTableService.queryList(query);
		int total = odTableService.queryTotal(query);
		
		// datas数据设置为空，避免传输到客户端
		for(OdTableEntity odTableEntity: odTableList){
		    odTableEntity.setDatas("");
		}
		PageUtils pageUtil = new PageUtils(odTableList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 表管理导出
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/export")
    public void exportIndex(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,Object> params = new HashMap<String,Object>();
            List<OdIndexEntity> odIndexList = null;
            params.put("type",request.getParameter("type"));
            params.put("status",request.getParameter("status"));
            params.put("tbname",request.getParameter("tbname"));
            
            List<OdTableEntity> odTableList = odTableService.queryList(params);
            odTableService.exportOdtable(odTableList, response);

        } catch (Exception e) {
            LOGGER.error("数据导出异常,ex:{}", e);
        }
    }
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("odtable:info")
	public R info(@PathVariable("id") Integer id){
		OdTableEntity odTable = odTableService.queryObject(id);
		
		return R.ok().put("odTable", odTable);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("odtable:save")
	public R save(@RequestBody OdTableEntity odTable){
		odTableService.save(odTable);
		
		return R.ok();
	}
	 
	/**
	 * 修改表数据内容
	 * 
	 * @param odTable
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("odtable:update")
	public R update(@RequestBody OdTableEntity odTable){
	    // 检测版本，用于区别更新或新增
	    String version = odTable.getVersion();
	    Integer id = odTable.getId();
        OdTableEntity odTableEntity = odTableService.queryObjectById(id);
        String verinfo = odTableEntity.getVersion();

        // 获取登录用户信息
        SysUserEntity sysUserEntity = (SysUserEntity)getSubject().getPrincipal();
        // 设置修改人
        odTable.setModifyName(sysUserEntity.getUsername());
        // 设置修改时间
        odTable.setModifyTime(new Date());
        // 赋值真实的版本号
        odTable.setVersion(verinfo);
        
	    // 1小版本，2大版本，0 不更新版本
	    if(version.equals("0")){
	        odTableService.update(odTable);
	    }else{
	        
	        odTable = odTableService.checkVersion(odTable, Integer.parseInt(version));
	        odTable.setId(null);
	        odTableService.save(odTable);
	    }
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("odtable:delete")
	public R delete(@RequestBody Integer[] ids){
		odTableService.deleteBatch(ids);
		
		return R.ok();
	}
	
	
	/**
     * 数据字典查询
     * 
     */
    @RequestMapping("/querylist")
    //@RequiresPermissions("odtable:querylist")
    public R queryList(@RequestParam Map<String, Object> params){
        if(params.size() > 0){
            // 搜索类型
            String searchtype = (String) params.get("search_type");
            // 搜索内容
            String searchtxt = (String) params.get("search_txt");
            
            if(searchtype != null && searchtxt != null){
                // 字段搜索
                if(searchtype.equals("0")){
                    // 根据表字段搜索表名
                    Map<String, Object> fieldMap = new HashMap<String, Object>();
                    fieldMap.put("field", searchtxt);
                    List<OdFieldEntity> odFieldList = odFieldService.queryList(fieldMap);
                    // 存在数据
                    if(odFieldList.size() > 0){
                        ArrayList<Integer> tableids = new ArrayList<>();
                        for(OdFieldEntity odFieldEntity: odFieldList){
                            tableids.add(odFieldEntity.getTableId());
                        }
                        // 去除重复的元素
                        HashSet<Integer> set = new HashSet<Integer>(tableids);
                        ArrayList<Integer> tbids = new ArrayList<>(set);
                        
                        params.put("tbids", tbids);
                    }
                    
                }else if(searchtype.equals("1")){
                    // 表搜索
                    params.put("tbname", searchtxt);
                }
            }
        }
        
        //查询列表数据
        Query query = new Query(params);

        List<OdTableEntity> odTableList = odTableService.queryList(query);
        int total = odTableService.queryTotal(query);
        
        PageUtils pageUtil = new PageUtils(odTableList, total, query.getLimit(), query.getPage());
        
        return R.ok().put("page", pageUtil);
    }
    
    /**
     * 字典查询获取详细数据
     * 获取的内容有3部分
     * 1.表详细数据内容
     * 2.表字段列表
     * 3.表的历史版本列表
     * 
     */
    @RequestMapping("/queryinfo/{id}")
    //@RequiresPermissions("odtable:queryinfo")
    public R queryInfo(@PathVariable("id") Integer id){
        
        // 获取表数据内容
        OdTableEntity odTableEntity = odTableService.queryObject(id);
        HashMap<String, Object> tableinfo = new HashMap<>();
        tableinfo.put("dbname", odTableEntity.getDatabaseName());
        tableinfo.put("tablename", odTableEntity.getTableName());
        tableinfo.put("topic", odTableEntity.getDataTopic());

        // json数据实例，解析
        String datas = odTableEntity.getDatas();
        if(datas != null){
            // json字符串解析为 Linkedlist，保持json key的顺序
            LinkedList<LinkedHashMap<String, Object>> datamap = JSON.parseObject(datas, 
                    new TypeReference<LinkedList<LinkedHashMap<String, Object>>>() {
                    }, Feature.OrderedField);
            
            if(datamap.size() > 0){
                // 依次迭代，获取所有的字段集合，保持json 字段的顺序
                List<String> fieldList = new LinkedList<>();
                Iterator iterator = datamap.get(0).entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> item =  (Map.Entry<String, Object>) iterator.next();
                    // 依次写入
                    fieldList.add(item.getKey());
                }
                
                tableinfo.put("datas", datamap);
                tableinfo.put("fieldList", fieldList);
            }
            
        }
        
        // 获取表字段数据
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("table_id", id);
        List<OdFieldEntity> odFieldList = odFieldService.queryList(params);
        
        // 获取表数据的历史版本
        List<OdTableEntity> odTableList = odTableService.queryVersionList(params);
        // 遍历清除datas 数据，避免传输到客户端
        for(OdTableEntity odTable: odTableList){
            odTable.setDatas("");
        }
        
        // 组装数据
        HashMap<String, Object> odTable = new HashMap<>();
        odTable.put("tableinfo", tableinfo);
        odTable.put("fields", odFieldList);
        odTable.put("tablelist", odTableList);
        
        return R.ok().put("odTable", odTable);
    }
	
}
