package com.example.benchmark.example2;

import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jodd.util.concurrent.ThreadFactoryBuilder;

/**
 * @Comment 校验Excetor
 */
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
@Threads(1)
public class ThreadPoolBenchMark {

    Logger logger = LoggerFactory.getLogger(ThreadPoolBenchMark.class);

    /**
     * @Comment 测试ThreadPoolExecutor线程词工具类，验证当线程并发量较大时，任务处理效率较低，任务队列堆积，导致发生内存溢出（OOM）
     */
    public void testThreadPoolExecutor() throws InterruptedException{
        int loopCount = 100000000;
        //定义线程池，定义线程池中初始化一个线程
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

        //打印线程
        printStatus(executor);
        //循环执行任务，在服务
        for(int i = 0;i<loopCount;i++){
            executor.execute(()->{
                logger.info(Thread.currentThread().getName()+":Start");
                String payLoad = IntStream.rangeClosed(1,1000000).mapToObj(o -> "a").collect(Collectors.joining("")) + UUID.randomUUID().toString();

                try{TimeUnit.HOURS.sleep(1);}catch (InterruptedException e){};

                logger.info(payLoad);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1,TimeUnit.HOURS);
    }

    /**
     * @Comment 每次间隔 1 秒向线程池提交任务，循环 20 次，每个任务需要 10 秒才能执行完成,
     *          函数运行的目标为论证当线程池队列满了之后任务是否还能全部执行完成
     *          <ul>
     *              <li>不会初始化 corePoolSize 个线程，有任务来了才创建工作线程；</li>
     *              <li>当核心线程满了之后不会立即扩容线程池，而是把任务堆积到工作队列中；</li>
     *              <li>当工作队列满了后扩容线程池，一直到线程个数达到 maximumPoolSize 为止；</li>
     *              <li>如果队列已满且达到了最大线程后还有任务进来，按照拒绝策略处理；</li>
     *              <li>当线程数大于核心线程数时，线程等待 keepAliveTime 后还是没有任务需要处理的话，收缩线程到核心线程数。</li>
     *          </ul>
     */
    public void testThreadPoolPolicy() throws InterruptedException{
        //统计类
        AtomicInteger atomicInteger = new AtomicInteger();
        //创建核心线程数为2，最大数为5，队列数为10的线程池,
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,
                5,
                5,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").get(),
                new ThreadPoolExecutor.AbortPolicy());

        //每秒打印线程池信息
        printStatus(executor);
        IntStream.rangeClosed(1,20).forEach(i ->{
            //睡眠1秒
            try{TimeUnit.SECONDS.sleep(1);}catch (InterruptedException e){e.printStackTrace();}

            //定义每次循环的Id
            int id = atomicInteger.incrementAndGet();

            try{
                executor.submit(() ->{
                    //输出线程运行Id
                    logger.info("Start Thread id:{}",id);
                    try{TimeUnit.SECONDS.sleep(20);}catch (InterruptedException e){e.printStackTrace();}
                    logger.info("End Thread id:{}",id);
                });
            }catch (Exception e){
                logger.error("error thread id:{}",id,e);
                e.printStackTrace();
            }

        });

        TimeUnit.SECONDS.sleep(60);
        logger.info("{}",atomicInteger.intValue());
    }

    /**
     * @Comment 测试ThreadPoolHelper创建线程池调用Executors.newFixedThreadPool每次都创建一个新的线程池
     */
    public void testWrongThreadPool(){
        ThreadPoolExecutor executor = ThreadPoolHelper.getThreadPool();
        IntStream.rangeClosed(1,20).forEach(i->{
            executor.submit(()->{
                String payload = IntStream.rangeClosed(1, 1000000)
                        .mapToObj(__ -> "a")
                        .collect(Collectors.joining("")) + UUID.randomUUID().toString();
                try{TimeUnit.SECONDS.sleep(1);}catch (InterruptedException e){e.printStackTrace();}

                logger.debug(payload);
            });
        });
    }

    @Benchmark
    public void testWorngThreadPoolSynchronized(){
        ThreadPoolExecutor executor = ThreadPoolHelper.getThreadPoolRight();
        executor.submit(()->{
            try{TimeUnit.MILLISECONDS.sleep(10);}catch (InterruptedException e){e.printStackTrace();}
        });
    }

    static class ThreadPoolHelper{

        static ThreadPoolExecutor executor = new ThreadPoolExecutor(2,
                5,
                5,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").get(),
                new ThreadPoolExecutor.AbortPolicy());
        /**
         * @Comment 此方法为错误方法，方法钟使用Executors.newCachedThreadPool每次调用都会生成一个新的线程池
         * @return
         */
        public static ThreadPoolExecutor getThreadPool(){
            return (ThreadPoolExecutor) Executors.newCachedThreadPool();
        }

        /**
         * @Comment 此方法为正确方法，每次调用时返回在对象实例化已经初始的对象
         * @return
         */
        public static ThreadPoolExecutor getThreadPoolRight(){
            return executor;
        }
    }

    /**
     * @Comment 打印线程信息
     * @param executor
     */
    public void printStatus(ThreadPoolExecutor executor){
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->{
            logger.info("============================");
            logger.info("Pool Size:{}",executor.getPoolSize());
            logger.info("Actives Thread:{}",executor.getActiveCount());
            logger.info("Number of Tasks Completed:{}",executor.getCompletedTaskCount());
            logger.info("Number of Tasks in Queue:{}",executor.getQueue().size());
            logger.info("============================");
        },0,1,TimeUnit.SECONDS);
    }

//    public static void main(String[] args) throws InterruptedException {
//        //ThreadPoolBenchMark mark = new ThreadPoolBenchMark();
//        //mark.testThreadPoolExecutor();
//        //mark.testThreadPoolPolicy();
//        //mark.testWrongThreadPool();
//    }
}
