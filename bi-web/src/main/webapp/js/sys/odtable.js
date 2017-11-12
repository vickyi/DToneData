$(function () {
	
    $("#jqGrid").jqGrid({
        url: '../odtable/list',
        datatype: "json",
        colModel: [			
        	{ label: '表编号', name: 'tableId', index: 'table_id', width: 50, key: true },
			{ label: '归属主题', name: 'dataTopic', index: 'data_topic', width: 80, 
        		formatter:function(value,options,row){
					var keyrs = vm.keymap[1];
					for(i in keyrs){
						if(keyrs[i].id == value){
							value = '<span class="label label-info" >'+keyrs[i].name+'</span>';
							break;
						}
					}
					return value;
				}
			}, 
			{ label: '表名', name: 'tableName', index: 'table_name', width: 80 }, 			
			{ label: '表描述', name: 'tableDesc', index: 'table_desc', width: 80 }, 
			// 类型值
			{ label: '数据库类型', name: 'databaseType', index: 'database_type', width: 80,
				formatter:function(value,options,row){
					var keyrs = vm.keymap[3];
					for(i in keyrs){
						if(keyrs[i].id == value){
							value = '<span class="label label-info" >'+keyrs[i].name+'</span>';
							break;
						}
					}
					return value;
				}
			},
			{ label: '更新逻辑', name: 'updateLogic', index: 'update_logic', width: 80 }, 			
			// 更新频率
			{ label: '更新频率', name: 'updateFrequency', index: 'update_frequency', width: 80, 
				formatter:function(value,options,row){
					var keyrs = vm.keymap[2];
					for(i in keyrs){
						if(keyrs[i].id == value){
							value = '<span class="label label-info" >'+keyrs[i].name+'</span>';
							break;
						}
					}
					return value;
				}
			}, 			
			{ label: '数据范围', name: 'dataRange', index: 'data_range', width: 80 }, 			
			{ label: '使用说明', name: 'instructions', index: 'instructions', width: 80 }, 			
			{ label: '负责人', name: 'owner', index: 'owner', width: 80 }, 			
			// 状态
			{ label: '状态', name: 'status', index: 'status', width: 80,
				formatter:function(value,options,row){
					switch (value){
						case 1:
							value = '<span class="label label-success" >有效</span>';
							break;
						case 0:
							value = '<span class="label label-danger">无效</span>';
							break;
						default:
							value= '<span class="label label-success" >有效</span>';
					}
					return value;
				}
			},
			
			{ label: '最后更改日期', name: 'modifyTime', index: 'modify_time', width: 80 },
			{ label: '更改人', name: 'modifyName', index: 'modify_name', width: 80 },
			{
                label: '操作', align: "center", width: 150,
                formatter: function (value, options, row) {
                    var df = row.tableId;
                    var detailJob = "vm.updateTb('" + df + "')";
                    var editfield = "vm.updateTbField('" + df + "')";
                    
                    return '<input id="yu' + df + '" type="button" name="Submit" value="编辑"  onclick="' + detailJob + '"/>'
                        + '&nbsp;'+ '<input id="edit' + df + '" type="button" name="Submit" value="编辑字段"  onclick="' + editfield + '"/>';
                }
            }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		showFields: false,
		odTable: {},
		q:{
			type: -1,
			status:-1,
			tbname: null
		},
		// 所有的枚举值
		keymap: {},
		// 字段编辑变量
		fields: {}
	},
	// 页面加载过程中进行请求
	created:function(){
		this.getKeyMap();
	},
	methods: {
		// 获取所有的枚举值
		getKeyMap: function (){
			var url = "../oddimtypes/getListAll/";
			$.get(url, function(r){
                if(r.code == 0){
		    		vm.keymap = r.listall;
				}
            });
			
		},
		query: function () {
			vm.reload();
		},
		exportCsv:function(){
			var postData = {'type': vm.q.type,'status':vm.q.status, 'tbname':vm.q.tbname};
			window.location.href ="../odtable/export?"+$.param(postData);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.odTable = {};
		},
		updateTb: function (tbid) {
			if(tbid == null){
				return ;
			}
			vm.showList = false;
			vm.showFields = false;
			
            vm.title = "编辑";
            
            vm.getInfo(tbid)
		},
		// 编辑表字段
		updateTbField: function (tbid) {
			if(tbid == null){
				return ;
			}
            vm.title = "编辑字段";
            vm.reloadField(tbid)
		},
		
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "编辑";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.odTable.id == null ? "../odtable/save" : "../odtable/update";
			
			var version = vm.odTable.version_select;
			if(version == null){
				alert('请选择此次修改的版本');
				return false;
			}else {
				// 设置到原版本值上
				vm.odTable.version = version;
			}
			
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.odTable),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../odtable/delete",
				    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		// 通过table id 获取表数据
		getInfo: function(id){
			$.get("../odtable/info/"+id, function(r){
                vm.odTable = r.odTable;
            });
		},
		reload: function (event) {
			vm.showList = true;
			vm.showFields = false;
			
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'type': vm.q.type,'status':vm.q.status, 'tbname':vm.q.tbname},
                page:page
            }).trigger("reloadGrid");
		},
		
		// 编辑字段数据
		reloadField: function (tbid) {
			vm.showList = false;
			vm.showFields = true;
			
			$("#jqGrid_field").jqGrid({
		        url: '../odfield/list',
		        datatype: "json",
		        postData:{'table_id': tbid},
		        colModel: [			
		        	{ label: 'id', name: 'id', index: 'id',key: true,
		        		formatter:function(value,options,row){
							value = value+'<input type="hidden" class="form-control"  attr-id="'+value+'" name="attrids[]" />';
							return value;
						}
		        	},
					{ label: '字段英文', name: 'field', index: 'field', }, 			
					{ label: '字段英文中文名', name: 'fieldName', index: 'field_name', 
						formatter:function(value,options,row){
							var id = row.id;
							value = '<input type="text" class="form-control"  name="fieldName['+id+']" placeholder="更改说明" value="'+value+'" />';
							return value;
						}
					}, 			
					{ label: '字段类型', name: 'fieldType', index: 'field_length',
						formatter:function(value,options,row){
							value = value+'('+row.fieldLength+')';
							return value;
						}
					},
					{ label: '可否为空', name: 'isNull', index: 'is_null',
						formatter:function(value,options,row){
							switch(value){
								// 可以为空	
								case 1:
									value = '<span class="label label-success" >Y</span>';
									break;
								case 0:
									value = '<span class="label label-info" >N</span>';
									break;
							}
							return value;
						}	
					}, 			
					{ label: '是否主键', name: 'isKey', index: 'is_key',
						formatter:function(value,options,row){
							switch(value){
								case 1:
									value = '<span class="label label-success" >Y</span>';
									break;
								case 0:
									value = '<span class="label label-info" >N</span>';
									break;
							}
							return value;
						}
					}, 			
					{ label: '启用时间', name: 'enabledTime', index: 'enabled_time',
						formatter:function(value,options,row){
							var id = row.id;
							var arr = value.split(" ");
							value = '<input type="text" class="form-control Wdate Wdateinput" name="enabledTime['+id+']" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\'})" value="'+arr[0]+'" />';
							return value;
						}
					}, 			
					{ label: '失效时间', name: 'expiryTime', index: 'expiry_time',
						formatter:function(value,options,row){
							var id = row.id;
							var arr = value.split(" ");
							value = '<input type="text" class="form-control Wdate Wdateinput" name="expiryTime['+id+']" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\'})" value="'+arr[0]+'" />';
							return value;
						}
					}, 			
					{ label: '关联维表', name: 'relationTable', index: 'relation_table',
						formatter:function(value,options,row){
							var id = row.id;
							value = '<input type="text" class="form-control"  name="relationTable['+id+']" placeholder="关联维表" value="'+value+'" />';
							return value;
						}
					}, 			
					{ label: '备注', name: 'remark', index: 'remark',
						formatter:function(value,options,row){
							var id = row.id;
							value = '<input type="text" class="form-control"  name="remark['+id+']" placeholder="备注信息" value="'+value+'" />';
							return value;
						}
					}, 			
		        ],
				viewrecords: true,
		        height: 385,
		        rowNum: 10,
				rowList : [10,30,50],
		        rownumbers: true, 
		        rownumWidth: 25, 
		        autowidth:true,
		        multiselect: false,
		        pager: "#jqGridPagerx",
		        jsonReader : {
		            root: "page.list",
		            page: "page.currPage",
		            total: "page.totalPage",
		            records: "page.totalCount"
		        },
		        prmNames : {
		            page:"page", 
		            rows:"limit", 
		            order: "order"
		        },
		        gridComplete:function(){
		        	//隐藏grid底部滚动条
		        	$("#jqGrid_field").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
		        	// 再次设置宽度
		        	$("#jqGrid_field").setGridWidth($(window).width() );
		        }
		    });
			
			$("#jqGrid_field").jqGrid('setGridParam',{ 
				postData:{'table_id': tbid},
            }).trigger("reloadGrid");
			
		},
		// 更新多列表字段信息
		updateFields: function (event) {
			
			var ids = [];
			var idslist = $('input[name^="attrids"]');
			idslist.each(function(i){
				ids.push($(this).attr('attr-id'));
			});
			
			var rsmap = {};
			// 获取表单数据进行提交
			var tb = ['fieldName', 'enabledTime', 'expiryTime', 'relationTable',  'remark'];
			
			for(i in ids){
				var id = ids[i];
				var tmp = {};
				
				for(x in tb){
					var f = tb[x];
					var obj = $('input[name="'+f+'['+id+']"]');
					tmp[f] = $(obj).val();
				}
				tmp['id'] = id;
				rsmap[id] = tmp;
			}
			
			var url = "../odfield/updatelist";
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data:JSON.stringify(rsmap),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reloadField();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		
	}
});

