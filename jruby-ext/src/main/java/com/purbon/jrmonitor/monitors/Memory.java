package com.purbon.jrmonitor.monitors;

import java.lang.management.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by purbon on 13/12/15.
 */
public class Memory {

    public enum Type {
        All, Heap, NonHeap
    }

    public class Report {

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
                heapMap.get(getCurrentName()).put("usage.init", usage.getInit());
                heapMap.get(getCurrentName()).put("usage.committed", usage.getCommitted());
                heapMap.get(getCurrentName()).put("usage.used", usage.getUsed());
                heapMap.get(getCurrentName()).put("usage.max", usage.getMax());
            } else {
                nonHeapMap.get(currentName).put("usage.init", usage.getInit());
                nonHeapMap.get(currentName).put("usage.committed", usage.getCommitted());
                nonHeapMap.get(currentName).put("usage.used", usage.getUsed());
                nonHeapMap.get(currentName).put("usage.max", usage.getMax());
            }
        }

        public void addPeak(MemoryType type, MemoryUsage peak) {
            if (type == MemoryType.HEAP) {
                heapMap.get(getCurrentName()).put("peak.init", peak.getInit());
                heapMap.get(getCurrentName()).put("peak.committed", peak.getCommitted());
                heapMap.get(getCurrentName()).put("peak.used", peak.getUsed());
                heapMap.get(getCurrentName()).put("peak.max", peak.getMax());
            } else {
                nonHeapMap.get(currentName).put("peak.init", peak.getInit());
                nonHeapMap.get(currentName).put("peak.committed", peak.getCommitted());
                nonHeapMap.get(currentName).put("peak.used", peak.getUsed());
                nonHeapMap.get(currentName).put("peak.max", peak.getMax());
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


    public Memory.Report detect(Type selectType) {
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
