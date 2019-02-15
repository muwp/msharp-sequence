package com.ruijing.sequence;

import com.ruijing.sequence.service.Sequence;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by xuan on 2018/1/10.
 */
public class IdGeneratorTest extends BaseTest {

    private Sequence sequence;

    @Before
    public void setup() {
        sequence = getDbSequence();
    }

    @Test
    public void test() {
        test(sequence);
    }

}
