package com.ruijing.sequence;

import com.ruijing.sequence.service.Sequence;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by xuan on 2018/5/9.
 */
public class SnowflakeTestApi extends BaseTest {

    private Sequence sequence;

    @Before
    public void setup() {
        sequence = getSnowflakeSequence();
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            System.out.println("++++++++++snowflake:" + sequence.nextId("d"));
        }
        System.out.println("interval time:" + (System.currentTimeMillis() - start));
    }
}
