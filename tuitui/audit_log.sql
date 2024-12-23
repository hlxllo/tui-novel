create table wuyan.audit_log
(
	id int auto_increment comment '主键'
		primary key,
	table_name varchar(50) not null comment '操作的表名',
	record_id int not null comment '操作的记录ID',
	operation tinyint(1) not null comment '操作类型的枚举值，如 INSERT, UPDATE, DELETE',
	old_data text null comment '操作前的数据（仅用于 UPDATE 和 DELETE）',
	new_data text null comment '操作后的数据（仅用于 INSERT 和 UPDATE）',
	operation_time datetime default CURRENT_TIMESTAMP not null comment '操作时间',
	status tinyint(1) default 1 not null comment '状态，0-删除，1-正常，其他待定',
	update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
	add_time datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
comment '流水表';

