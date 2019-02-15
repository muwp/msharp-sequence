## 创建数据库msharp_sequence
CREATE DATABASE if not exists msharp_sequence;

## 序列表(sequence)
CREATE TABLE IF NOT EXISTS `sequence_data`
(
  `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
    COMMENT '表id',
  `biz_name`    VARCHAR(100)    NOT NULL DEFAULT ''
    COMMENT '业务名称',
  `max_id`      BIGINT(22)      NOT NULL DEFAULT 1
    COMMENT '当前最大id值',
  `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP
    COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_biz_name` (`biz_name`),
  KEY `idx_updateTime` (`update_time`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '序列表';

## 系列号配制表(sequence_config)
CREATE TABLE IF NOT EXISTS `sequence_config`
(
  `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
    COMMENT '表id',
  `biz_name`    VARCHAR(50)     NOT NULL DEFAULT ''
    COMMENT '业务名称',
  `mode`        VARCHAR(15)     NOT NULL DEFAULT 'snowflake'
    COMMENT '序列表模式',
  `type`        int(11)         NOT NULL DEFAULT 0
    COMMENT '序列号重置策略 0表示不循环重置,1 按每分钟重置初始值,2 按小时重置初始值,3 按天重置初始值,4 按月重置初始值,5 按年重置初始值',
  `init_value`  BIGINT(11)      NOT NULL DEFAULT 0
    COMMENT '初始值',
  `step`        int(11)         NOT NULL DEFAULT 100
    COMMENT '步长',
  `retry_times` int(11)         NOT NULL DEFAULT 100
    COMMENT '重复次数',
  `token`       VARCHAR(100)    NOT NULL DEFAULT ''
    COMMENT '客户端监权token',
  `reset_time`  DATETIME        NOT NULL DEFAULT '1970-10-10 10:00:00'
    COMMENT '更新时间',
  `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP
    COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_biz_name` (`biz_name`),
  KEY `idx_updateTime` (`update_time`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '序列表配置';

