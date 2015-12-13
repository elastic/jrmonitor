import com.purbon.jruby.monitor.*;
import com.purbon.jruby.monitor.System;
import org.jruby.*;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.List;
import java.util.Map;

/**
 * Created by purbon on 12/12/15.
 */
public class SystemReport extends RubyObject {

    public SystemReport(Ruby ruby, RubyClass metaclass) {
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

