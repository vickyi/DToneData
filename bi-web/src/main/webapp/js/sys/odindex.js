$(function () {
	var url1 = '../odindex/frontlist?limit=10000&page=1&sidx=&order=asc';
	$.get(url1,{},function(res){
		var source = []
		for (var i in res.page.list){
			var item = res.page.list[i];
			if(item.indexName != null){
				source.push(item.indexName);
			}
			if(item.index != null) {
				source.push(item.index)
			}
		}
		$("#search-indexName").typeahead({
			source:source
		});
	});

    $("#jqGrid").jqGrid({
        url: '../odindex/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '指标名称', name: 'indexName', index: 'index_name', width: 80 }, 			
			{ label: '指标英文名称', name: 'index', index: 'index', width: 80 }, 			
			{ label: '业务类型', name: 'businessType', index: 'business_type', width: 80 }, 			
			{ label: '指标类型', name: 'indexType', index: 'index_type', width: 80 }, 			
			{ label: '指标定义', name: 'indexDesc', index: 'index_desc', width: 80 }, 			
			{ label: '数据库', name: 'database', index: 'database', width: 80 }, 			
			{ label: '表名', name: 'databaseTable', index: 'database_table', width: 80 }, 			
			{ label: '指标的计算公式', name: 'indexCompute', index: 'index_compute', width: 80 }, 			
			{ label: '使用场景', name: 'useScenes', index: 'use_scenes', width: 80 ,hidden:true},
			{ label: 'olap节点', name: 'olapNodes', index: 'olap_nodes', width: 80 ,hidden:true },
			{ label: '观星台节点', name: 'gxtNodes', index: 'gxt_nodes', width: 80 ,hidden:true},
			{ label: '状态', name: 'status', index: 'status', width: 80 ,
				formatter:function(value, options, row){
					var id = row.id;
					if (value === 0) {
						return '<span class="label label-danger" style="cursor:pointer" onclick="vm.updatStatus('+id+',1)">无效</span>';
					} else if (value === 1) {
						return '<span class="label label-success" style="cursor:pointer" onclick="vm.updatStatus('+id+',0)">有效</span>';
					} else {
						return '<span class="label label-warning">未知</span>';
					}
				}
			},
			{ label: '操作',
					formatter:function(value, options, row){
						var id = row.id;
						var str =  '<span class="label label-danger" style="cursor:pointer" onclick="vm.updatRow('+id+')">编辑</span>&nbsp;&nbsp;&nbsp;';
						str += '<span class="label label-danger" style="cursor:pointer" onclick="vm.deleteRow('+id+')">删除</span>';
						return str;
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
		showdatabase:true,
		odIndex: {},
		q:{business_type:'',index_type:'',status:1,version:1}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		exportCsv:function(){
			var postData = {'business_type': vm.q.business_type,'index_type':vm.q.index_type,'status':vm.q.status,'index_name':vm.q.index_name,'v':vm.q.version,'front':2};
			window.location.href ="../odindex/export?"+$.param(postData);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.odIndex = {businessType:'',indexType:'',status:"1"};
		},
		updateIndexType : function (item) {
			console.log(item.currentTarget.value)
			if(item.currentTarget.value == "复合指标"){
				vm.showdatabase = false;
			}else{
				vm.showdatabase = true;
			}
		},
		updatStatus:function(id,status){
			if(id == null){
				return ;
			}
			vm.odIndex.id = id;
			vm.odIndex.status = status;
			if(status == 0){
				status_name = "无效";
			}else if(status == 1){
				status_name = "有效";
			}
			confirm('是否要置为'+status_name+'？', function(){
				var url = "../odindex/update";
				var data = {
					"id":vm.odIndex.id,
					"status":vm.odIndex.status,
					"del":1
				}
				$.ajax({
					type: "POST",
					url: url,
					contentType: "application/json",
					data: JSON.stringify(data),
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
			})
		},
		updatRow :function(id){
			if(id == null){
				return ;
			}
			vm.showList = false;
			vm.getInfo(id)
		},
		deleteRow:function(id){
			if(id == null){
				return ;
			}
			vm.odIndex.id = id;
			vm.odIndex.del = 0;
			confirm('确定要删除选中的记录？', function(){
				var url = "../odindex/update";
				var data = {
					"id":vm.odIndex.id
				}
				$.ajax({
					type: "POST",
					url: url,
					contentType: "application/json",
					data: JSON.stringify(data),
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
			})
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
		isEmpty:function(){
			if(vm.odIndex.index == null || vm.odIndex.index == ''){
				alert("请输入指标英文名称！");
				return false;
			}
			if(vm.odIndex.businessType == null || vm.odIndex.businessType == ''){
				alert("请选择业务类型！");
				return false;
			}
			if(vm.odIndex.indexType == null || vm.odIndex.indexType == ''){
				alert("请选择指标类型！");
				return false;
			}

			if(vm.odIndex.indexType != "复合指标"){
				if(vm.odIndex.indexDesc == null || vm.odIndex.indexDesc == ''){
					alert("请输入指标定义！");
					return false;
				}
				if(vm.odIndex.database == null || vm.odIndex.database == ''){
					alert("请选择数据库类型！");
					return false;
				}
				if(vm.odIndex.databaseTable == null || vm.odIndex.databaseTable == ''){
					alert("请输入数据库表名！");
					return false;
				}
				if(vm.odIndex.indexCompute == null || vm.odIndex.indexCompute == ''){
					alert("请输入指标的计算公式！");
					return false;
				}
			}

			if(vm.odIndex.status == null || vm.odIndex.status == ''){
				alert("请选择状态！");
				return false;
			}
			return true;
		},
		saveOrUpdate: function (event) {
			//判断是否是编辑
			if(vm.odIndex.id == null){
				if(vm.odIndex.indexName == null || vm.odIndex.indexName == ''){
					alert("请输入指标名称！");
					return false;
				}
				if(!this.isEmpty()){
					return false;
				}
				var url = "../odindex/save" ;
			}else{
				if(!this.isEmpty()){
					return false;
				}
				//判断版本号 为 3 为编辑  其他的为新增
				if(vm.odIndex.versionSelect == 0){
					alert("请选择版本号");
					return false;
				}else {
					if (vm.odIndex.versionSelect == 3) {
						var url = "../odindex/update";
					} else {
						var url = "../odindex/save" ;
					}
				}
			}
			vm.odIndex.indexDesc = this.html2Escape(vm.odIndex.indexDesc);
			vm.odIndex.whereString = this.html2Escape(vm.odIndex.whereString);
			$.ajax({
				type: "POST",
				url: url,
				contentType: "application/json",
				data: JSON.stringify(vm.odIndex),
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
		html2Escape:function(sHtml) {
			return sHtml.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
		},
		escape2Html:function(str) {
			if(str != null){
				var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
				return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
			}else{
				return null;
			}
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../odindex/delete",
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
			var that = this;
			$.get("../odindex/info/"+id, function(r){
				r.odIndex.indexDesc = that.escape2Html(r.odIndex.indexDesc);
				r.odIndex.whereString = that.escape2Html(r.odIndex.whereString);
                vm.odIndex = r.odIndex;
            });
		},
		reload: function (event) {
			var index_name = $("input[name=index_name]").val();
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'business_type': vm.q.business_type,'index_type':vm.q.index_type,'status':vm.q.status,'index_name':index_name,'v':vm.q.version},
                page:page
            }).trigger("reloadGrid");
		}
	}
});