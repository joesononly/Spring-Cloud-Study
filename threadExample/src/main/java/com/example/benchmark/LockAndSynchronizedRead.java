package com.example.benchmark;

import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
public class LockAndSynchronizedRead {
    Logger logger = LoggerFactory.getLogger(LockAndSynchronized.class);

    //随机值
    private final  int loopCount = 1000000;

    List copyOnWriteArrayList = new CopyOnWriteArrayList();
    List synchronizedList = Collections.synchronizedList(new ArrayList<>());

    private void addAll(List<Integer> list){
        list.addAll(IntStream.rangeClosed(1,loopCount).boxed().collect(Collectors.toList()));
    }

    @Setup
    public void startUp(){
        addAll(copyOnWriteArrayList);
        addAll(synchronizedList);
    }

    /**
     * 压测copyOnWriteArrayList的读取方法
     */
    @Benchmark
    @Group("Lock")
    public void testCopyOnWriteArrayList(){
        //logger.info("testCopyOnWriteArrayList");
        copyOnWriteArrayList.get(ThreadLocalRandom.current().nextInt(loopCount));
    }

    /**
     * 压测synchronizedList的读取方法，读取get方法，可以看到使用了synchronized关键字
     */
    @Benchmark
    @Group("Synchronized")
    public void testSynchronized(){
        synchronizedList.get(ThreadLocalRandom.current().nextInt(loopCount));
    }

    @TearDown
    public void TearDown(){
        logger.info("copyOnWriteArrayList List Size:"+copyOnWriteArrayList.size() + " synchronizedList List Size:"+synchronizedList.size());
    }


}
