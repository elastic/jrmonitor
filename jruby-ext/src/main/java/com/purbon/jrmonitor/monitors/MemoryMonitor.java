package com.purbon.jrmonitor.monitors;

import java.lang.management.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by purbon on 13/12/15.
 */
public class MemoryMonitor {

    public enum Type {
        All, Heap, NonHeap
    }

    public class Report {

        private static final String USAGE_INIT = "usage.init";
        private static final String USAGE_COMMITTED = "usage.committed";
        private static final String USAGE_USED = "usage.used";
        private static final String USAGE_MAX = "usage.max";
        private static final String PEAK_INIT = "peak.init";
        private static final String PEAK_COMMITTED = "peak.committed";
        private static final String PEAK_USED = "peak.used";
        private static final String PEAK_MAX = "peak.max";

        private Map<String, Map<String, Object>> heapMap = new HashMap<String, Map<String, Object>>();
        private Map<String, Map<String, Object>> nonHeapMap = new HashMap<String, Map<String, Object>>();

        private String currentName;

        public Report() {
            currentName = "";
        }

        public Map<String, Map<String, Object>> getHeapAsHash() {
            return heapMap;
        }

        public Map<String, Map<String, Object>> getNonHeapAsHash() {
            return nonHeapMap;
        }

        public void addUsage(MemoryType type, MemoryUsage usage) {
            if (type == MemoryType.HEAP) {
                heapMap.get(getCurrentName()).put(USAGE_INIT, usage.getInit());
                heapMap.get(getCurrentName()).put(USAGE_COMMITTED, usage.getCommitted());
                heapMap.get(getCurrentName()).put(USAGE_USED, usage.getUsed());
                heapMap.get(getCurrentName()).put(USAGE_MAX, usage.getMax());
            } else {
                nonHeapMap.get(currentName).put(USAGE_INIT, usage.getInit());
                nonHeapMap.get(currentName).put(USAGE_COMMITTED, usage.getCommitted());
                nonHeapMap.get(currentName).put(USAGE_USED, usage.getUsed());
                nonHeapMap.get(currentName).put(USAGE_MAX, usage.getMax());
            }
        }

        public void addPeak(MemoryType type, MemoryUsage peak) {
            if (type == MemoryType.HEAP) {
                heapMap.get(getCurrentName()).put(PEAK_INIT, peak.getInit());
                heapMap.get(getCurrentName()).put(PEAK_COMMITTED, peak.getCommitted());
                heapMap.get(getCurrentName()).put(PEAK_USED, peak.getUsed());
                heapMap.get(getCurrentName()).put(PEAK_MAX, peak.getMax());
            } else {
                nonHeapMap.get(currentName).put(PEAK_INIT, peak.getInit());
                nonHeapMap.get(currentName).put(PEAK_COMMITTED, peak.getCommitted());
                nonHeapMap.get(currentName).put(PEAK_USED, peak.getUsed());
                nonHeapMap.get(currentName).put(PEAK_MAX, peak.getMax());
            }
        }

        public void addName(MemoryType type, String name) {
            if (type == MemoryType.HEAP) {
                setName(heapMap, name);
            } else {
                setName(nonHeapMap, name);
            }
            setCurrentName(name);
        }

        public void setCurrentName(String name) {
            currentName = name;
        }

        public String getCurrentName() {
            return currentName;
        }

        private void setName(Map<String, Map<String, Object>> map, String name) {
            if (map.get(name) == null) {
                map.put(name, new HashMap<String, Object>());
            }
        }
     }


    public Report detect(Type selectType) {
        List<MemoryPoolMXBean> beans = ManagementFactory.getMemoryPoolMXBeans();

        Report report = new Report();

        for(MemoryPoolMXBean bean : beans) {
            MemoryType  type  = bean.getType();

            if (selectType.equals(Type.All) || !filterPool(type, selectType) ) {
                report.addName(type, bean.getName());
                report.addUsage(type, bean.getUsage());
                report.addPeak(type, bean.getPeakUsage());
            }
        }

        return report;
    }

    private boolean filterPool(MemoryType type, Type selectType) {
       return ((selectType.equals(Type.NonHeap) && type.equals(MemoryType.HEAP))
               ||
               (selectType.equals(Type.Heap) && type.equals(MemoryType.NON_HEAP)));
    }
}
