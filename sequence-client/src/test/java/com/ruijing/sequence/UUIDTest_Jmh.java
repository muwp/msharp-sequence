package com.ruijing.sequence;

import com.ruijing.sequence.utils.UUIDUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuan on 2018/6/7.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(8)
@State(Scope.Benchmark)
public class UUIDTest_Jmh {

    @Benchmark
    public void test() {
        UUIDUtils.uuid();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(UUIDTest_Jmh.class.getSimpleName()).output(
                "/Users/xuan/Documents/Code/xuan/gitee/xsequence/doc/jmh/UUIDTest_Jmh.log").forks(4).build();
        new Runner(options).run();
    }

}
