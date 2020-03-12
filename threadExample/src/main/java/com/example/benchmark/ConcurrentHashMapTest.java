package com.example.benchmark;


import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

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
public class ConcurrentHashMapTest {

    //设置Map中元素个数
    private int elementSize = 10;

    //ConcurrentHashMap
    ConcurrentHashMap<String,LongAdder> freqs = new ConcurrentHashMap<>(elementSize);

    Logger logger = LoggerFactory.getLogger(ConcurrentHashMapTest.class);


    /**
     * 测试ConcurrentHashMao中的ComputeIfAbsent方法
     */
    @Benchmark
    @Group("ComputeIfAbsent")
    public void testComputeIfAbsent(){
        String key = "item" + ThreadLocalRandom.current().nextInt(elementSize);
        freqs.computeIfAbsent(key,k -> new LongAdder()).increment();
    }

    /**
     * 使用synchronized同步Map的get
     */
    @Benchmark
    @Group("NormalCompate")
    public void testNormalCompate(){
        String key = "item" + ThreadLocalRandom.current().nextInt(elementSize);
        synchronized (freqs) {
            if (freqs.containsKey(key)) {
                //Key存在则+1
                LongAdder l = freqs.get(key);
                l.increment();
                freqs.put(key, l);
            } else {
                //Key不存在则初始化为1
                freqs.put(key, new LongAdder());
            }
        }
    }

    @TearDown
    public void tearDown(){
        Map<String,Long> m = freqs.entrySet().stream() .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().longValue()) );
        logger.info(m.toString());
    }
}
