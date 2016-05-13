package com.purbon.jrmonitor;

import com.purbon.jrmonitor.reports.RubyMemoryReport;
import com.purbon.jrmonitor.reports.RubyProcessReport;
import com.purbon.jrmonitor.reports.RubySystemReport;
import com.purbon.jrmonitor.reports.RubyThreadsReport;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.load.BasicLibraryService;

import java.io.IOException;

/**
 * Basic JVM monitoring class for Threads, System and memory, basically all
 * metadata provided as MxBeans.
 * Created by purbon on 12/12/15.
 */
public class JRMonitorService implements BasicLibraryService {

    public boolean basicLoad(Ruby ruby) throws IOException {

        RubyModule rootModule    = ruby.defineModule("JRMonitor");
        RubyModule reportsModule = ruby.defineModuleUnder("Report", rootModule);

        RubyClass reportClass = ruby.defineClassUnder("Threads", ruby.getObject(), new ObjectAllocator() {
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyThreadsReport(ruby, rubyClass);
            }
        }, reportsModule);

        reportClass.defineAnnotatedMethods(RubyThreadsReport.class);

        RubyClass systemClass = ruby.defineClassUnder("System", ruby.getObject(), new ObjectAllocator() {
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubySystemReport(ruby, rubyClass);
            }
        }, reportsModule);
        systemClass.defineAnnotatedMethods(RubySystemReport.class);

        RubyClass memoryClass = ruby.defineClassUnder("Memory", ruby.getObject(), new ObjectAllocator() {
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyMemoryReport(ruby, rubyClass);
            }
        }, reportsModule);
        memoryClass.defineAnnotatedMethods(RubyMemoryReport.class);

        RubyClass processClass = ruby.defineClassUnder("Process", ruby.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyProcessReport(ruby, rubyClass);
            }
        }, reportsModule);

        processClass.defineAnnotatedMethods(RubyProcessReport.class);

        return true;
    }
}
