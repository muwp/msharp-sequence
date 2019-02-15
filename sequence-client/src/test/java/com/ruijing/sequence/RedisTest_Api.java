package com.ruijing.sequence;

import com.ruijing.sequence.service.Sequence;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by xuan on 2018/1/10.
 */
public class RedisTest_Api extends BaseTest {

    private Sequence sequence;

    @Before
    public void setup() {
        sequence = getRedisSequence();
    }

    @Test
    public void test() {
        test(sequence);
    }

}
