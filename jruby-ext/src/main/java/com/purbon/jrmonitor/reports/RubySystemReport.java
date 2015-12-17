package com.purbon.jrmonitor.reports;

import com.purbon.jrmonitor.JRubyUtils;
import com.purbon.jrmonitor.monitors.System;
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
     * @return
     */
    @JRubyMethod(module = true, name = { "build" })
    public static RubyHash build(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;
        System monitor  = new System();
        return JRubyUtils.toRubyHash(runtime, monitor.detect().toHash());
    }
}

