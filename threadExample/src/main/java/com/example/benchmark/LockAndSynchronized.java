package com.example.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

//类中共享的区域
@State(Scope.Benchmark)
//测试输出，可以为吞吐量，平均时间
@BenchmarkMode(Mode.Throughput)
//输出时间单位
@OutputTimeUnit(TimeUnit.SECONDS)
//预热0次，5秒预热一次
@Warmup(iterations = 0, time = 5, timeUnit = TimeUnit.SECONDS)
//执行测试10次
@Measurement(iterations = 10)
//分配多少个进程进行测试
@Fork(1)
//分配多少线程进行测试
@Threads(8)
public class LockAndSynchronized {

    Logger logger = LoggerFactory.getLogger(LockAndSynchronized.class);

    //随机值
    private final  int loopCount = 100;

    List copyOnWriteArrayList = new CopyOnWriteArrayList();
    List synchronizedList = Collections.synchronizedList(new ArrayList<>());

    @Benchmark
    @Group("Lock")
    public void testCopyOnWriteArrayList(){
        copyOnWriteArrayList.add(ThreadLocalRandom.current().nextInt(loopCount));
    }

    @Benchmark
    @Group("Synchronized")
    public void testSynchronized(){
        synchronizedList.add(ThreadLocalRandom.current().nextInt(loopCount));
    }

    @TearDown
    public void TearDown(){
        logger.info("copyOnWriteArrayList List Size:"+copyOnWriteArrayList.size() + " synchronizedList List Size:"+synchronizedList.size());
    }


}
