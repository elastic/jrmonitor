package com.purbon.jrmonitor.reports;

import com.purbon.jrmonitor.JRubyUtils;
import com.purbon.jrmonitor.monitors.MemoryMonitor;
import com.purbon.jrmonitor.monitors.MemoryMonitor.Report;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyHash;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.Map;

/**
 * Created by purbon on 12/12/15.
 */
@JRubyClass(name="Memory")
public class RubyMemoryReport extends RubyObject {

    private static final String NON_HEAP = "non_heap";
    private static final String HEAP = "heap";

    private static MemoryMonitor monitor = null;

    public RubyMemoryReport(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
        this.monitor = new MemoryMonitor();
    }

    /**
     * Build a report with current Memory information
     * @param context
     * @param self
     * @return
     */
    @JRubyMethod(name = "generate" )
    public static RubyHash generate(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;

        MemoryMonitor.Report report = generateReport(MemoryMonitor.Type.All);
        RubyHash container = new RubyHash(runtime);
        container.put(HEAP,     toHash(runtime, report.getHeapAsHash()));
        container.put(NON_HEAP, toHash(runtime, report.getNonHeapAsHash()));
        return container;
    }

    @JRubyMethod(name = "generate_with_heap")
    public static RubyHash generateHeap(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;

        MemoryMonitor.Report report = generateReport(MemoryMonitor.Type.Heap);
        return toHash(runtime, report.getHeapAsHash());
    }

    @JRubyMethod(name =  "generate_with_nonheap" )
    public static RubyHash generateNonHeap(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;

        MemoryMonitor.Report report = generateReport(MemoryMonitor.Type.NonHeap);
        return toHash(runtime, report.getNonHeapAsHash());
    }

    private static MemoryMonitor.Report generateReport(MemoryMonitor.Type type) {
        MemoryMonitor.Report report = monitor.detect(type);
        return report;
    }

    private static RubyHash toHash(Ruby runtime, Map<String, Map<String, Object>> info) {
        RubyHash hash = new RubyHash(runtime);
        for(String memoryPoolId : info.keySet()) {
            RubyHash poolHash = JRubyUtils.toRubyHash(runtime, info.get(memoryPoolId));
            hash.put(memoryPoolId, poolHash);
        }
        return hash;
    }
}

