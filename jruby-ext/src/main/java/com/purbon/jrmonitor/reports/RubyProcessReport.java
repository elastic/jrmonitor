package com.purbon.jrmonitor.reports;

import com.purbon.jrmonitor.JRubyUtils;
import com.purbon.jrmonitor.monitors.ProcessMonitor;
import org.jruby.*;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * Created by andrewvc on 5/13/16.
 */
@JRubyClass(name="Process")
public class RubyProcessReport extends RubyObject {
    public RubyProcessReport(Ruby ruby, RubyClass metaclass) { super(ruby, metaclass); }

    /**
     * Build a report with current Process information
     * @param context
     * @param self
     * @return a RubyHash with the current process report
     */
    @JRubyMethod(module=true, name = { "generate" })
    public static RubyHash generate(ThreadContext context, IRubyObject self) {
        ProcessMonitor monitor = new ProcessMonitor();
        return JRubyUtils.toRubyHash(context.runtime, monitor.detect().toHash() );
    }
}
