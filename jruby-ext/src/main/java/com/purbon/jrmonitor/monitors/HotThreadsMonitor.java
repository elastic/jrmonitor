package com.purbon.jrmonitor.monitors;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;

/**
 * Hot threads monitoring class. This class pulls information out of the JVM #
 * provided beans and lest the different consumers query it.
 * Created by purbon on 12/12/15.
 */
public class HotThreadsMonitor {

    /**
     * Placeholder for a given thread report
     */
    public class ThreadReport {

        private static final String CPU_TIME = "cpu.time";
        private static final String BLOCKED_COUNT = "blocked.count";
        private static final String BLOCKED_TIME = "blocked.time";
        private static final String WAITED_COUNT = "waited.count";
        private static final String WAITED_TIME = "waited.time";
        private static final String THREAD_NAME = "thread.name";
        private static final String THREAD_STATE = "thread.state";

        private Map<String, Object> map = new HashMap<String, Object>();

        public ThreadReport(ThreadInfo info, long cpuTime) {
            map.put(CPU_TIME, cpuTime);
            map.put(BLOCKED_COUNT, info.getBlockedCount());
            map.put(BLOCKED_TIME, info.getBlockedTime());
            map.put(WAITED_COUNT, info.getWaitedCount());
            map.put(WAITED_TIME, info.getWaitedTime());
            map.put(THREAD_NAME, info.getThreadName());
            map.put(THREAD_STATE, info.getThreadState().name().toLowerCase());
        }

        public Map<String, Object> toHash() {
            return map;
        }

        public Long getCpuTime() {
            return (Long)map.get(CPU_TIME);
        }

        public String getThreadState() {
            return (String)map.get(THREAD_STATE);
        }

        public String getThreadName() {
            return (String)map.get(THREAD_NAME);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            int i=0;
            for(String key : map.keySet()) {
                if (i > 0)
                    sb.append(",");
                sb.append(key);
                sb.append(",");
                sb.append(map.get(key));
            }
            return sb.toString();
        }
    }

    /**
     * Return the current hot threads information as provided by the JVM
     * @return A list of ThreadReport including all selected threads
     */
    public List<ThreadReport> detect() {
        return detect(new HashMap<String, String>());
    }

    /**
     * Return the current hot threads information as provided by the JVM
     * @param options Map<String, String> holding a set of options used to filter the selected information
     * @return A list of ThreadReport including all selected threads
     */
    public List<ThreadReport> detect(Map<String, String> options) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        enableCpuTime(threadMXBean);

        String selectThreads = "";
         if (options.containsKey("select"))
           selectThreads = options.get("select");


        Map<Long, ThreadReport> reports = new HashMap<Long, ThreadReport>();

        for(long threadId : threadMXBean.getAllThreadIds()) {
            if (Thread.currentThread().getId() == threadId) {
                continue;
            }

            long cpuTime = threadMXBean.getThreadCpuTime(threadId);
            if (cpuTime == -1) {
                continue;
            }

            ThreadInfo info = threadMXBean.getThreadInfo(threadId, 0);
            if (selectThreads.isEmpty() || info.getThreadState().toString().equalsIgnoreCase(selectThreads)) {
                reports.put(threadId, new ThreadReport(info, cpuTime));
            }
        }
        List<ThreadReport> list = Arrays.asList(reports.values().toArray(new ThreadReport[reports.size()]));
        return sort(list);
    }

    public List<ThreadReport> sort(List<ThreadReport> reports) {
        Collections.sort(reports, new Comparator<ThreadReport>() {
            public int compare(ThreadReport a, ThreadReport b) {

                if (a.getCpuTime() > b.getCpuTime()) {
                    return -1;
                } else if (a.getCpuTime() < b.getCpuTime()) {
                    return 1;
                } else {
                    return a.getThreadState().compareTo(b.getThreadState());
                }
            }
        });
        return reports;
    }

    public String buildReport(Map<Long, ThreadReport> reports) {
        StringBuilder sb = new StringBuilder();

        for(ThreadReport report : reports.values()) {
            sb.append(report);
            sb.append("\n");
        }

        return sb.toString();
    }

    private void enableCpuTime(ThreadMXBean threadMXBean) {
        try {
            if (threadMXBean.isThreadCpuTimeSupported()) {
                if (!threadMXBean.isThreadCpuTimeEnabled()) {
                    threadMXBean.setThreadCpuTimeEnabled(true);
                }
            }
        } catch (SecurityException ex) {

        }
    }
}
