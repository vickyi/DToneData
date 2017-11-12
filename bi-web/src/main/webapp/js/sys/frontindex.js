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
        url: '../odindex/frontlist',
        datatype: "json",
        colModel: [
			{ label: '指标名称', name: 'indexName', index: 'index_name', width: 100 ,align:"left",
				formatter:function(value, options, row) {
					return "<strong>"+value+"</strong>";
				}
			},
			{ label: '业务类型', name: 'businessType', index: 'business_type', width: 70 },
			{ label: '指标类型', name: 'indexType', index: 'index_type', width: 70 },
			{ label: '状态', name: 'status', index: 'status', width: 60 ,
				formatter:function(value, options, row){
					if (value === 0) {
						return '无效';
					} else if (value === 1) {
						return '有效';
					} else {
						return '未知';
					}
				}
			}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		//rowList : [10,30,50],
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
        },
		onSelectRow: function (id){
            var a = vm.getSelectedRows();
			return a;
		}
	});
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		showSingle:false,
		showDetail:false,
		title: null,
		odIndex: {},
		hisOdIndexs:{},
		odIndexs:{},
		singleIndex:{},
		q:{business_type:'',index_type:'',status:'1'},
		map:{
			'id':'id',
			'indexName':'指标名称',
			'index':'指标英文名',
			'businessType':'业务类型',
			'indexType':'指标类型',
			'status':'状态',
			'indexDesc':'业务定义',
			'define':'技术定义',
			'database':'数据库',
			'databaseTable':'依赖表',
			'indexCompute':'计算方式',
			'whereString':'限制条件',
			'useScenes':'应用场景',
			'version':'版本号',
			'modifyTime':'最后更改时间',
			/*'createName':'',
			'createTime':'',
			'gxtNodes':'',
			'modifyDesc':'',
			'modifyName':'',
			'olapNodes':'',*/
		}
	},
	methods: {
		//选择多条记录
		getSelectedRows :function() {
			var grid = $("#jqGrid");
			var rowKey = grid.getGridParam("selrow");
			var ids = grid.getGridParam("selarrrow");
			if(ids.length > 3){
				alert("只能选中三个对比指标！");
				$("#jqGrid").setSelection(rowKey, false);
				return false;
			}
			vm.getInfos(ids);
			return true;
		},
		query: function () {
			vm.reload();
		},
		jumpSingle:function(val,allids){
			var ids = [];
			ids.push(val);
			for (var i in allids){
				if(i>1){
					var id = allids[i];
					$("#jqGrid").setSelection(id, false);
				}
			}
			$("#jqGrid").setSelection(val, true);
			vm.getInfos(ids);
			return true;
		},
        exportCsv:function(){
            var postData = {'business_type': vm.q.business_type,'index_type':vm.q.index_type,'status':vm.q.status,'index_name':vm.q.index_name,'front':1};
            window.location.href ="../odindex/export?"+$.param(postData);
        },
		getInfos: function(ids){
			if(ids.length == 0){
				vm.odIndex = {};
				vm.showDetail = false;
			}else if(ids.length == 1){
				vm.showDetail = true;
				var id = ids[0];
				var that = this;
				$.get("../odindex/info/"+id, function(r){
					r.odIndex.indexDesc = that.escape2Html(r.odIndex.indexDesc);
					r.odIndex.whereString = that.escape2Html(r.odIndex.whereString);
					vm.odIndex = r.odIndex;
					var odIndexLists = [];
					for(var index in r.odIndexLists){
						var item = r.odIndexLists[index];
						item.indexDesc =  that.escape2Html(item.indexDesc);
						item.whereString =  that.escape2Html(item.whereString);
						odIndexLists.push(item);
					}
					vm.hisOdIndexs = odIndexLists;
					vm.showSingle = true;
				});
			}else if(ids.length > 1 && ids.length < 4){
				var that = this;
				$.ajax({
					type: "POST",
					url: "../odindex/infos",
					contentType: "application/json",
					data: JSON.stringify(ids),
					success: function(r){
						if(r.code == 0){
							var odIndexLists = r.odIndexLists;
							var data = [];
							for(var key in vm.map){
								var name = vm.map[key];
								var a = [];
								a.push(key);
								a.push(name);
								for(var index in odIndexLists){
									var item = odIndexLists[index];
									if(key=="indexDesc" || key=="whereString" ){
										a.push(that.escape2Html(item[key]));
									}else{
										a.push(item[key]);
									}

								}
								data.push(a);
							}
							vm.odIndexs = data;
						}else{
							alert(r.msg);
						}
					}
				});
				vm.showDetail = true;
				vm.showSingle = false;
			}else{
				//alert("只能选中三个对比指标！");
			}
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
		reload: function (event) {
			var index_name = $("input[name=index_name]").val();
			vm.showList = true;
			//var page = $("#jqGrid").jqGrid('getGridParam','page');
			var page = 1;
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'business_type': vm.q.business_type,'index_type':vm.q.index_type,'status':vm.q.status,'index_name':index_name},
                page:page
            }).trigger("reloadGrid");
		}
	}
});