package com.ruijing.sequence;

import com.alibaba.druid.pool.DruidDataSource;
import com.ruijing.sequence.builder.DbSeqBuilder;
import com.ruijing.sequence.builder.RedisSeqBuilder;
import com.ruijing.sequence.builder.SnowflakeSeqBuilder;
import com.ruijing.sequence.service.Sequence;

import java.util.concurrent.CountDownLatch;

/**
 * Created by xuan on 2018/5/31.
 */
public class BaseTest {

    protected Sequence getRedisSequence() {
        /**
         * 参数说明如下：
         * ip：redis连接ip
         * port：redis连接port
         * auth：如果redis设置了密码权限需要设置，没有就可以不用设置
         * bizName：具体某中业务的序列号
         * step：[可选] 默认1000，即每次取redis获取步长值，根据具体业务吞吐量来设置，越大性能越好，但是序列号断层的风险也就越大
         */
        return RedisSeqBuilder
                .custom()
                .ip("xxx")
                .port(6379)
                .auth("xxx")
                .step(1000)
                .stepStart(
                        60000).bizName("redis_test8").build();
    }

    protected Sequence getSnowflakeSequence() {
        /**
         * 参数说明如下：
         * datacenterId: 一般可以设置机房标志，值只能设置[0-31]之间
         * workerId: 一般设置主机编号，值只能设置[0-31]之间
         * 只用这来那个值保证服务器唯一就可以
         */
        return SnowflakeSeqBuilder
                .custom()
                .datacenterId(1)
                .workerId(2)
                .build();
    }

    protected Sequence getDbSequence() {
        //数据源
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/pearl");
        dataSource.setUsername("root");
        dataSource.setPassword("122816");
        dataSource.setMaxActive(300);
        dataSource.setMinIdle(50);
        dataSource.setInitialSize(2);
        dataSource.setMaxWait(500);

        /**
         * 参数说明如下：
         * dataSource：数据库的数据源
         * bizName：具体某中业务的序列号
         * step：[可选] 默认1000，即每次取db【mysql】获取步长值，根据具体业务吞吐量来设置，越大性能越好，但是序列号断层的风险也就越大
         */
        return DbSeqBuilder
                .custom()
                .dataSource(dataSource)
                .build();
    }

    protected void test(Sequence sequence) {
        CountDownLatch countDownLatch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            // Thread thread = new Thread(() -> {
            for (int j = 0; j < 100; j++) {
                long start = System.currentTimeMillis();
                long seq = sequence.nextId("test");
                System.out.println("snowflake:" + seq + " ,cost_time:" + (System.currentTimeMillis() - start));
            }
            //   countDownLatch.countDown();
            //   });
            //  thread.setName("thread-" + i);
            // thread.start();
        }

        try {
            //    countDownLatch.await();
        } catch (Exception e) {

        }
    }

}
