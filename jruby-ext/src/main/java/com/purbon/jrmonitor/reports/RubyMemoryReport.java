package com.purbon.jrmonitor.reports;

import com.purbon.jrmonitor.JRubyUtils;
import com.purbon.jrmonitor.monitors.Memory;
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

    public RubyMemoryReport(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * Build a report with current Memory information
     * @param context
     * @param self
     * @return
     */
    @JRubyMethod(module = true, name = { "build" })
    public static RubyHash build(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;
        Memory monitor  = new Memory();

        Memory.Report report = monitor.detect(Memory.Type.All);
        RubyHash container = new RubyHash(runtime);
        container.put(HEAP,     toHash(runtime, report.getHeapAsHash()));
        container.put(NON_HEAP, toHash(runtime, report.getNonHeapAsHash()));
        return container;
    }

    @JRubyMethod(module = true, name = { "build_heap" })
    public static RubyHash buildHeap(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;
        Memory monitor  = new Memory();

        Memory.Report report = monitor.detect(Memory.Type.Heap);

        RubyHash container = new RubyHash(runtime);
        container.put(HEAP, toHash(runtime, report.getHeapAsHash()));

        return container;
    }

    @JRubyMethod(module = true, name = { "build_nonheap" })
    public static RubyHash buildNonHeap(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;
        Memory monitor  = new Memory();

        Memory.Report report = monitor.detect(Memory.Type.NonHeap);

        RubyHash container = new RubyHash(runtime);
        container.put(NON_HEAP, toHash(runtime, report.getNonHeapAsHash()));

        return container;
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

