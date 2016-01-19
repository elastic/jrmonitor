package com.purbon.jrmonitor.monitors;

import org.jruby.RubyProcess;

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
        private static final String THREAD_STACKTRACE = "thread.stacktrace";

        private Map<String, Object> map = new HashMap<String, Object>();

        public ThreadReport(ThreadInfo info, long cpuTime) {
            map.put(CPU_TIME, cpuTime);
            map.put(BLOCKED_COUNT, info.getBlockedCount());
            map.put(BLOCKED_TIME, info.getBlockedTime());
            map.put(WAITED_COUNT, info.getWaitedCount());
            map.put(WAITED_TIME, info.getWaitedTime());
            map.put(THREAD_NAME, info.getThreadName());
            map.put(THREAD_STATE, info.getThreadState().name().toLowerCase());
            map.put(THREAD_STACKTRACE, stackTraceAsString(info.getStackTrace()));
        }

        private List<String> stackTraceAsString(StackTraceElement [] elements) {
            List<String> lines = new ArrayList<String>();
            for(int i=0; i < elements.length; i++) {
                  lines.add(elements[i].toString());
            }
            return lines;
        }

        public Map<String, Object> toHash() {
            return map;
        }

        public String getThreadState() {
            return (String) map.get(THREAD_STATE);
        }

        public String getThreadName() {
            return (String) map.get(THREAD_NAME);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String key : map.keySet()) {
                if (i > 0)
                    sb.append(",");
                sb.append(key);
                sb.append(",");
                sb.append(map.get(key));
            }
            return sb.toString();
        }

        public Long getWaitedTime() {
            return (Long)map.get(WAITED_TIME);
        }

        public Long getBlockedTime() {
            return (Long)map.get(BLOCKED_TIME);
        }

        public Long getCpuTime() {
            return (Long) map.get(CPU_TIME);
        }

    }

    private List<String> VALID_TYPES = new ArrayList<String>();

    public HotThreadsMonitor() {
        VALID_TYPES.add("cpu");
        VALID_TYPES.add("wait");
        VALID_TYPES.add("block");
    }

    /**
     * Return the current hot threads information as provided by the JVM
     *
     * @return A list of ThreadReport including all selected threads
     */
    public List<ThreadReport> detect() {
        return detect("cpu");
    }

    /**
     * Return the current hot threads information as provided by the JVM
     *
     * @param type String selected type for sorting the threads information
     * @return A list of ThreadReport including all selected threads
     */
    public List<ThreadReport> detect(String type) {
        if (!isValidType(type))
            throw new IllegalArgumentException("Wrong search type");

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        enableCpuTime(threadMXBean);

        Map<Long, ThreadReport> reports = new HashMap<Long, ThreadReport>();

        for (long threadId : threadMXBean.getAllThreadIds()) {
            if (Thread.currentThread().getId() == threadId) {
                continue;
            }

            long cpuTime = threadMXBean.getThreadCpuTime(threadId);
            if (cpuTime == -1) {
                continue;
            }

            ThreadInfo info = threadMXBean.getThreadInfo(threadId, 3);
            reports.put(threadId, new ThreadReport(info, cpuTime));
        }
        return sort(new ArrayList(reports.values()), type);
     }

    public List<ThreadReport> sort(List<ThreadReport> reports, final String type) {
        Collections.sort(reports, new Comparator<ThreadReport>() {
            public int compare(ThreadReport a, ThreadReport b) {
               if ("block".equals(type)) {
                   return (int) (b.getBlockedTime()-a.getBlockedTime());
               } else if ("wait".equals(type)) {
                   return (int) (b.getWaitedTime()-a.getWaitedTime());
               } else {
                   return (int) (b.getCpuTime()-a.getCpuTime());
               }
            }
        });
        return reports;
    }

    private boolean isValidType(String type) {
        return VALID_TYPES.indexOf(type.toLowerCase()) != -1;
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
