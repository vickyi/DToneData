INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 'fa fa-cog', 0, 0);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'sys/user.html', NULL, 1, 'fa fa-user', 1, 0);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'sys/role.html', NULL, 1, 'fa fa-user-secret', 2, 0);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'sys/menu.html', NULL, 1, 'fa fa-th-list', 3, 0);
INSERT INTO `sys_menu` VALUES (5, 1, 'SQL监控', 'druid/sql.html', NULL, 1, 'fa fa-bug', 4, 0);
INSERT INTO `sys_menu` VALUES (6, 1, '定时任务', 'sys/schedule.html', NULL, 1, 'fa fa-tasks', 5, 0);
INSERT INTO `sys_menu` VALUES (7, 6, '查看', NULL, 'sys:schedule:list,sys:schedule:info', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (8, 6, '新增', NULL, 'sys:schedule:save', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (9, 6, '修改', NULL, 'sys:schedule:update', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (10, 6, '删除', NULL, 'sys:schedule:delete', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (11, 6, '暂停', NULL, 'sys:schedule:pause', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (12, 6, '恢复', NULL, 'sys:schedule:resume', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (13, 6, '立即执行', NULL, 'sys:schedule:run', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (14, 6, '日志列表', NULL, 'sys:schedule:log', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:perms', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:perms', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0, 0);
INSERT INTO `sys_menu` VALUES (27, 1, '参数管理', 'sys/config.html', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'fa fa-sun-o', 6, 0);
INSERT INTO `sys_menu` VALUES (28, 1, '代码生成器', 'sys/generator.html', 'sys:generator:list,sys:generator:code', 1, 'fa fa-rocket', 8, 0);
INSERT INTO `sys_menu` VALUES (29, 1, '系统日志', 'sys/log.html', 'sys:log:list', 1, 'fa fa-file-text-o', 7, 0);
INSERT INTO `sys_menu` VALUES (30, 1, '文件上传', 'sys/oss.html', 'sys:oss:all', 1, 'fa fa-file-image-o', 6, 0);
INSERT INTO `sys_menu` VALUES (74, 1, '指标管理', 'sys/odindex.html', NULL, 1, 'fa fa-file-code-o', 6, 0);
INSERT INTO `sys_menu` VALUES (75, 74, '查看', NULL, 'odindex:list,odindex:info', 2, NULL, 6, 0);
INSERT INTO `sys_menu` VALUES (76, 74, '新增', NULL, 'odindex:save', 2, NULL, 6, 0);
INSERT INTO `sys_menu` VALUES (77, 74, '修改', NULL, 'odindex:update', 2, NULL, 6, 0);
INSERT INTO `sys_menu` VALUES (78, 74, '删除', NULL, 'odindex:delete', 2, NULL, 6, 0);
INSERT INTO `sys_menu` VALUES (79, 1, '表管理', 'sys/odtable.html', NULL, 1, 'fa fa-file-code-o', 6, 0);
INSERT INTO `sys_menu` VALUES (80, 79, '查看', NULL, 'odtable:list,odtable:info', 2, NULL, 6, 0);
INSERT INTO `sys_menu` VALUES (81, 79, '新增', NULL, 'odtable:save', 2, NULL, 6, 0);
INSERT INTO `sys_menu` VALUES (82, 79, '修改', NULL, 'odtable:update', 2, NULL, 6, 0);
INSERT INTO `sys_menu` VALUES (83, 79, '删除', NULL, 'odtable:delete', 2, NULL, 6, 0);
INSERT INTO `sys_menu` VALUES (84, 0, '元数据查询', NULL, NULL, 0, 'fa fa-cog', 0, 0);
INSERT INTO `sys_menu` VALUES (85, 84, '指标查询', 'sys/frontindex.html', NULL, 1, 'fa fa-rocket', 0, 0);
INSERT INTO `sys_menu` VALUES (91, 84, '数据字典', 'sys/odtablefront.html', NULL, 1, 'fa fa-file-code-o', 0, 0);
INSERT INTO `sys_menu` VALUES (92, 91, '查看详细', NULL, 'odtable:frontinfo', 2, NULL, 0, 0);
