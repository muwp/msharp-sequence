package com.ruijing.sequence;

import com.ruijing.sequence.service.Sequence;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuan on 2018/5/9.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(8)
@State(Scope.Benchmark)
public class SnowflakeTestJmh extends BaseTest {

    private Sequence sequence;

    @Setup
    public void setup() {
        sequence = getSnowflakeSequence();
    }

    @Benchmark
    public void test() {
        sequence.nextId("d");
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(SnowflakeTestJmh.class.getSimpleName()).output(
                "/Users/xuan/Documents/Code/xuan/gitee/xsequence/doc/jmh/SnowflakeTestJmh.log").forks(4).build();
        new Runner(options).run();
    }
}
