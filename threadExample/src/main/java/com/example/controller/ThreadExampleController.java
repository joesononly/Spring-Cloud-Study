package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/thread")
public class ThreadExampleController {

    Logger logger = LoggerFactory.getLogger(ThreadExampleController.class);

    //定义ThreadLocal用于存储线程私有变量
    private ThreadLocal<Integer> current = ThreadLocal.withInitial(() -> null);

    //定义锁
    final transient ReentrantLock lock = new ReentrantLock();


    /**********************************************************************************************************************/
    /**
     * @Comment 使用ThreadLocal存储线程共享信息，使用完成后不清除，数据将会遗留在线程中，当下次用户再次调用请求时，
     *          系统刚好分配到同一个线程，那么上一次数据将会被读取到
     * @param userId
     * @param session
     * @return
     */
    @RequestMapping("threadLocal/{userId}")
    public Map testThreadLoacl(@PathVariable(name="userId") Integer userId, HttpSession session){
        String before = Thread.currentThread().getName()+":"+current.get();
        logger.info(before);
        current.set(userId);
        String after = Thread.currentThread().getName()+":"+current.get();
        logger.info(after);

        Map result = new HashMap();
        result.put("before",before);
        result.put("after",after);
        return result;
    }


    /**
     * @Comment 比较CopyOnWriteArrayList与ArrayList的写入耗时
     *          由于CopyWriteArrayList的同步实现是通过Lock接口
     *          而SynchronizedList的同步实现是通过synchronized关键字
     * @return
     */
    @RequestMapping("write")
    public Map testWrite(){
        //线程安全的并发List类
        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        //普通的List
        //List<Integer> synchronizedList = new ArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        StopWatch stopWatch = new StopWatch();
        int loopCount = 1000;
        stopWatch.start("Write:CopyOnWriteArrayList");
        IntStream.rangeClosed(1,loopCount).parallel().forEach(i -> copyOnWriteArrayList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();
        stopWatch.start("Write:synchronizedList");
        IntStream.rangeClosed(1,loopCount).parallel().forEach(i -> synchronizedList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();

        logger.info(stopWatch.prettyPrint());
        Map result = new HashMap();
        result.put("copyOnWriteArrayList",copyOnWriteArrayList.size());
        result.put("synchronizedList",synchronizedList.size());
        return result;
    }

    /**
     * @Comment 为List创建10w个随机对象
     */
    private void addAll(List<Integer> list){
        list.addAll(IntStream.rangeClosed(1,1000000).boxed().collect(Collectors.toList()));
    }

    /**
     * @Comment 比较CopyOnWriteArrayList与ArrayList中的读取效率
     * @return
     */
    @RequestMapping("read")
    public Map testRead(){
        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        //填充数据
        addAll(copyOnWriteArrayList);
        addAll(synchronizedList);
        StopWatch stopWatch = new StopWatch();
        int loopCount = 100000;
        int size = copyOnWriteArrayList.size();

        //并发查询10w次
        stopWatch.start("Read:CopyOnWriteArrayList");
        IntStream.rangeClosed(1,loopCount).parallel().forEach(i->copyOnWriteArrayList.get(ThreadLocalRandom.current().nextInt(size)));
        stopWatch.stop();

        //并发查询10w次
        stopWatch.start("Read:SynchronizedList");
        IntStream.rangeClosed(1,loopCount).parallel().forEach(i->synchronizedList.get(ThreadLocalRandom.current().nextInt(size)));
        stopWatch.stop();

        logger.info(stopWatch.prettyPrint());

        Map result = new HashMap();
        result.put("copyOnWriteArrayList",copyOnWriteArrayList.size());
        result.put("synchronizedList",synchronizedList.size());
        return result;
    }

    /**
     * @Comment 比较Lock与Synchronized同步耗时
     */
    @RequestMapping("compare")
    public void compareLockAndSyn(){

        List<Integer> list = new ArrayList<>();
        addAll(list);

        StopWatch stopWatch = new StopWatch();
        int loopCount = 10;

        stopWatch.start("Lock");
        IntStream.rangeClosed(0,loopCount).parallel().forEach(i->testGetLock(list,i));
        stopWatch.stop();

        stopWatch.start("Synchronized");
        IntStream.rangeClosed(0,loopCount).parallel().forEach(i->testGetSynchronized(list,i));
        stopWatch.stop();

        logger.info(stopWatch.prettyPrint());

    }

    /**
     * @Comment 测试Lock同步耗时
     * @return
     */
    private void testGetLock(List<Integer> list,int i){
        try{
            lock.lock();
            logger.info("Lock:"+Thread.currentThread().getName()+":"+list.get(i));
        }finally {
            lock.unlock();
        }
    }

    /**
     * @Comment 测试Synchronized同步耗时
     * @param list
     * @param i
     */
    private void testGetSynchronized(List<Integer> list,int i){
        synchronized (list){
            logger.info("Synchronized:"+Thread.currentThread().getName()+":"+list.get(i));
        }
    }

}
