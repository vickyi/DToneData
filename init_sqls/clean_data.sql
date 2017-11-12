delete from od_table where database_name <> "dw";
delete from od_table where table_name like "v%";
select * from od_table where (datas = "[]" or datas = "");
update od_table set STATUS = 0 where (datas = "[]" or datas = "");
update od_table set STATUS = 1 where table_id = 1033873;
update od_table set STATUS = 0 where (datas = "[]" or datas = "");
delete from od_field where table_id not in (select table_id from od_table a);
delete from od_field_detail where fid not in (select id from od_field a);
update od_index a set a.`database` = "dw" where a.`status` = 1 and index_type in ("基础指标", "衍生指标", "复合指标") and (where_string  is not null or where_string <> "");

-- 维表
update od_table set data_topic = 1 where table_name like 'fct_order%';
update od_table set data_topic = 4 where table_name like 'dim%';

-- 待处理的基础指标
SELECT * from od_index where index_type = "基础指标" and business_type not in("供应链", "品控", "供应链管理", "财务", "违规处罚");

-- 指标初始化
use oneData2;
truncate od_index;
insert into oneData2.od_index(
  id,
  index_name,
  business_type,
  index_type,
  index_desc,
  status,
  version,
  create_time,
  create_name)
select
a.id,
a.name,
a.category,
a.index_level,
a.index_define,
1 as status,
1 as version,
a.date,
"admin" as create_name
from rpt.rpt_onedata_index a;