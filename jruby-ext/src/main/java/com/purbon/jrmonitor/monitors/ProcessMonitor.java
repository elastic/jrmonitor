package com.purbon.jrmonitor.monitors;

import com.sun.management.UnixOperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrewvc on 5/12/16.
 */
public class ProcessMonitor {
    private static final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();

    class Reporter {
        public Long getOpenFileDescriptorCount() {
            return Long.valueOf(-1);
        }
        public Long getMaxFileDescriptorCount() {
            return Long.valueOf(-1);
        }
        public Long getProcessCpuTime() {
            return Long.valueOf(-1);
        }
        public double getProcessCpuLoad() {
            return Long.valueOf(-1);
        }
        public double getSystemCpuLoad() {
            return Long.valueOf(-1);
        }

        public Long getCommittedVirtualMemorySize() {
            return Long.valueOf(-1);
        }
    }

    class UnixReporter extends Reporter {
        private final UnixOperatingSystemMXBean osMxBean;

        public UnixReporter(UnixOperatingSystemMXBean osMxBean) {
            this.osMxBean = osMxBean;
        }

        public Long getOpenFileDescriptorCount() {
            try {
                return this.osMxBean.getOpenFileDescriptorCount();
            } catch(Throwable e) {
                return -1L;
            }
        }
        public Long getMaxFileDescriptorCount() {
            try {
                return this.osMxBean.getMaxFileDescriptorCount();
            } catch(Throwable e) {
                return -1L;
            }
        }
        public Long getProcessCpuTime() {
            try {
                return this.osMxBean.getProcessCpuTime();
            } catch(Throwable e) {
                return -1L;
            }
        }
        public double getProcessCpuLoad() {
            try {
                return this.osMxBean.getProcessCpuLoad();
            } catch(Throwable e) {
                return -1L;
            }
        }
        public double getSystemCpuLoad() {
            try {
                return this.osMxBean.getSystemCpuLoad();
            } catch(Throwable e) {
                return -1L;
            }
        }

        public Long getCommittedVirtualMemorySize() {
            try {
                return this.osMxBean.getCommittedVirtualMemorySize();
            } catch(Throwable e) {
                return -1L;
            }
        }
    }

    public class Report {
        private final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
        private final Reporter reporter;

        private Map<String, Object> map = new HashMap<>();

        public Report() {
            this.reporter = getReporter();
        }

        private Reporter getReporter() {
            if(isUnix())
                return new UnixReporter(((UnixOperatingSystemMXBean) this.osMxBean));
            else return new Reporter();
        }

        public Map<String, Object> toHash() {
            map.put("open_file_descriptors", this.reporter.getOpenFileDescriptorCount());
            map.put("max_file_descriptors", this.reporter.getMaxFileDescriptorCount());
            map.put("is_unix", isUnix());

            Map<String, Object> cpuMap = new HashMap<>();
            map.put("cpu", cpuMap);
            cpuMap.put("total_in_millis", this.reporter.getProcessCpuTime());
            cpuMap.put("process_percent", scaleLoadToPercent(this.reporter.getProcessCpuLoad()));
            cpuMap.put("system_percent", scaleLoadToPercent(this.reporter.getSystemCpuLoad()));

            Map<String, Object> memoryMap = new HashMap<>();
            map.put("mem", memoryMap);
            memoryMap.put("total_virtual_in_bytes", this.reporter.getCommittedVirtualMemorySize());

            return map;
        }

        private short scaleLoadToPercent(double load) {
            if (isUnix()) {
                if (load >= 0) {
                    return (short) (load * 100);
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        private boolean isUnix() {
            return this.osMxBean instanceof UnixOperatingSystemMXBean;
        }
    }

    public Report detect() {
        return new Report();
    }
}
