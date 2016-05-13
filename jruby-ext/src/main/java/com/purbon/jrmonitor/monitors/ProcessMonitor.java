package com.purbon.jrmonitor.monitors;

import com.sun.management.UnixOperatingSystemMXBean;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by andrewvc on 5/12/16.
 */
public class ProcessMonitor {
    private static final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
    private static final MBeanServer platformMxBean = ManagementFactory.getPlatformMBeanServer();

    public class Report {
        private Map<String, Object> map = new HashMap<>();

        public Report() {
            if (osMxBean instanceof UnixOperatingSystemMXBean) {
                UnixOperatingSystemMXBean unixOsBean = (UnixOperatingSystemMXBean) osMxBean;;
                map.put("open_file_descriptors", unixOsBean.getOpenFileDescriptorCount());
                map.put("max_file_descriptors", unixOsBean.getMaxFileDescriptorCount());

                Map<String, Object> cpuMap = new HashMap<>();
                map.put("cpu", cpuMap);
                cpuMap.put("total_in_millis", unixOsBean.getProcessCpuTime());
                cpuMap.put("process_percent", scaleLoadToPercent(unixOsBean.getProcessCpuLoad()));
                cpuMap.put("system_percent", scaleLoadToPercent(unixOsBean.getSystemCpuLoad()));

                Map<String, Object> memoryMap = new HashMap<>();
                map.put("mem", memoryMap);
                memoryMap.put("total_virtual_in_bytes", unixOsBean.getCommittedVirtualMemorySize());
            }
        }

        public Map<String, Object> toHash() {
            return map;
        }

        public short scaleLoadToPercent(double load) {
            if (osMxBean instanceof UnixOperatingSystemMXBean) {
                UnixOperatingSystemMXBean unixOsBean = (UnixOperatingSystemMXBean) osMxBean;

                if (load >= 0) {
                    return (short) (load * 100);
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }
    }

    public Report detect() {
        Report report = new Report();
        return report;
    }
}
