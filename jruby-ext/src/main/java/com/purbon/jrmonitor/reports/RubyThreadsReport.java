package com.purbon.jrmonitor.reports;

import com.purbon.jrmonitor.JRubyUtils;
import com.purbon.jrmonitor.monitors.HotThreadsMonitor;
import org.jruby.*;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.List;
import java.util.Map;

/**
 * Created by purbon on 12/12/15.
 */
@JRubyClass(name = "Threads", parent = "Object")
public class RubyThreadsReport extends RubyObject {

    public RubyThreadsReport(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * Build a report with current Thread information
     * @param context
     * @param self
     * @return
     */
    @JRubyMethod(name = "generate" )
    public static RubyHash generate(ThreadContext context, IRubyObject self, IRubyObject params) {
        Ruby runtime = context.runtime;
        RubyHash hash = new RubyHash(runtime);

        Map<String, String> options = JRubyUtils.parseOptions(runtime, params);

        HotThreadsMonitor reporter = new HotThreadsMonitor();
        List<HotThreadsMonitor.ThreadReport> reports = reporter.detect(options);

        for(HotThreadsMonitor.ThreadReport report : reports) {
             RubyHash reportHash = JRubyUtils.toRubyHash(runtime, report.toHash());
            hash.put(report.getThreadName(), reportHash);
        }
        return hash;
    }

    @JRubyMethod(name = "generate" )
    public static RubyHash generate(ThreadContext context, IRubyObject self) {
        return generate(context, self, new RubyHash(context.runtime));
    }
    /**
     * Return the report as string
     * @param context
     * @param self
     * @return
     */
    @JRubyMethod(name = { "to_s" })
    public static RubyString to_string(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;

        StringBuilder sb = new StringBuilder();

        HotThreadsMonitor reporter = new HotThreadsMonitor();
        List<HotThreadsMonitor.ThreadReport> reports = reporter.detect();

        for (HotThreadsMonitor.ThreadReport report : reports) {
            sb.append(report);
            sb.append("\n");
        }
        return runtime.newString(sb.toString());
    }
}

