package com.purbon.jrmonitor.reports;

import com.purbon.jrmonitor.JRubyUtils;
import com.purbon.jrmonitor.monitors.SystemMonitor;
import org.jruby.*;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * Created by purbon on 12/12/15.
 */
@JRubyClass(name="System")
 public class RubySystemReport extends RubyObject {

    public RubySystemReport(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * Build a report with current System information
     * @param context
     * @param self
     * @return a RubyHash with the current system report
     */
    @JRubyMethod(module = true, name = { "generate" })
    public static RubyHash generate(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;
        SystemMonitor monitor  = new SystemMonitor();
        return JRubyUtils.toRubyHash(runtime, monitor.detect().toHash());
    }
}

