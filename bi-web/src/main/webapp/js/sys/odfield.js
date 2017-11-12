$(function () {
    $("#jqGrid").jqGrid({
        url: '../odfield/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '业务表id', name: 'tableId', index: 'table_id', width: 80 }, 			
			{ label: '表名', name: 'tableName', index: 'table_name', width: 80 }, 			
			{ label: '字段英文名称', name: 'field', index: 'field', width: 80 }, 			
			{ label: '字段英文中文名称', name: 'fieldName', index: 'field_name', width: 80 }, 			
			{ label: '字段类型', name: 'fieldType', index: 'field_type', width: 80 }, 			
			{ label: '时段长度', name: 'fieldLength', index: 'field_length', width: 80 }, 			
			{ label: '可否为空', name: 'isNull', index: 'is_null', width: 80 }, 			
			{ label: '是否主键', name: 'isKey', index: 'is_key', width: 80 }, 			
			{ label: '启用时间', name: 'enabledTime', index: 'enabled_time', width: 80 }, 			
			{ label: '失效时间', name: 'expiryTime', index: 'expiry_time', width: 80 }, 			
			{ label: '关联维表id', name: 'relationTableId', index: 'relation_table_id', width: 80 }, 			
			{ label: '关联维表', name: 'relationTable', index: 'relation_table', width: 80 }, 			
			{ label: '备注', name: 'remark', index: 'remark', width: 80 }, 			
			{ label: '版本', name: 'version', index: 'version', width: 80 }, 			
			{ label: '修改人', name: 'modifyName', index: 'modify_name', width: 80 }, 			
			{ label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
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
		odField: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.odField = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.odField.id == null ? "../odfield/save" : "../odfield/update";
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.odField),
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
				    url: "../odfield/delete",
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
		getInfo: function(id){
			$.get("../odfield/info/"+id, function(r){
                vm.odField = r.odField;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});