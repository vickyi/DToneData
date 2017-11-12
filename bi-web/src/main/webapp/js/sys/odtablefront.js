$(function () {
	
    $("#jqGrid").jqGrid({
        url: '../odtable/querylist',
        datatype: "json",
        colModel: [			
        	{ label: '表编号', name: 'id', index: 'id', width: 50, key: true },
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
			{ label: '表名', name: 'tableName', index: 'table_name', width: 80,
				
				formatter:function(value,options,row){
					var tbid = row.tableId;
					var detailJob = "vm.detail('" + tbid + "')";
					value = '<a href="javascript:;" onclick="' + detailJob + '">'+value+'</a>';
					return value;
				}
			}, 			
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
		showDetail: false,
		title: null,
		tableData: {},
		tableinfo: {},
		q:{
			type: -1,
			status:-1,
			search_type: 1, // 搜索类型
			search_txt: null, // 搜索内容
		},
		// 所有的枚举值
		keymap: {},
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
		 
		// 展示表的详细信息
		detail: function(tbid){
			
			vm.showList = false;
			vm.showDetail = true;
			
			// 页面模板上for循环，展示
			var url = '../odtable/queryinfo/'+tbid;
			$.get(url, function(r){
                if(r.code == 0){
                	var data = r.odTable;
                	
                	// 枚举数据替换
                	// 数据主题
                	var keyrs = vm.keymap[1];
                	// 数据库类型
                	var dbtypes = vm.keymap[3];
                	// 更新频率
                	var frequency = vm.keymap[2];
					
                	var tblist = data['tablelist'];
                	for(j in tblist){
                		// 数据主题替换
                		for(i in keyrs){
    						if(keyrs[i].id == tblist[j].dataTopic){
    							tblist[j]['dataTopicStr'] = keyrs[i].name;
    							break;
    						}
    					}
                		// 数据类型替换
                		for(i in dbtypes){
    						if(dbtypes[i].id == tblist[j].databaseType){
    							tblist[j]['databaseTypeStr'] = dbtypes[i].name;
    							break;
    						}
    					}
                		// 更新频率
                		for(i in frequency){
    						if(frequency[i].id == tblist[j].updateFrequency){
    							tblist[j]['updateFrequencyStr'] = frequency[i].name;
    							break;
    						}
    					}
                		
                	}
                	
                	data['tablelist'] = tblist;
                	
                	// 数据主题替换
                	var tableinfo = data['tableinfo'];
                	// 数据主题替换
            		for(i in keyrs){
						if(keyrs[i].id == tableinfo.topic){
							tableinfo['topicStr'] = keyrs[i].name;
							break;
						}
					}
            		// 设置表信息和数据实例
            		vm.tableinfo = tableinfo;
            		
            		// 设置表字段，历史版本的值
		    		vm.tableData = data;
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			vm.showDetail = false;
			
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'type': vm.q.type,'status':vm.q.status, 'search_type':vm.q.search_type, 'search_txt':vm.q.search_txt},
                page:page
            }).trigger("reloadGrid");
		},
		 
		 
		
	}
});

