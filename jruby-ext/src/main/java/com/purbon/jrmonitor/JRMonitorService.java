package com.purbon.jrmonitor;

import com.purbon.jrmonitor.reports.MemoryReport;
import com.purbon.jrmonitor.reports.SystemReport;
import com.purbon.jrmonitor.reports.ThreadsReport;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.load.BasicLibraryService;

import java.io.IOException;

/**
 * Basic JVM monitoring class for threads
 * Created by purbon on 12/12/15.
 */
public class JRMonitorService implements BasicLibraryService {

    public boolean basicLoad(Ruby ruby) throws IOException {

        RubyModule rootModule = ruby.defineModule("JRMonitor");

        RubyClass reportClass = ruby.defineClassUnder("ThreadsReport", ruby.getObject(), new ObjectAllocator() {
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new ThreadsReport(ruby, rubyClass);
            }
        }, rootModule);

        reportClass.defineAnnotatedMethods(ThreadsReport.class);

        RubyClass systemClass = ruby.defineClassUnder("SystemReport", ruby.getObject(), new ObjectAllocator() {
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new SystemReport(ruby, rubyClass);
            }
        }, rootModule);
        systemClass.defineAnnotatedMethods(SystemReport.class);

        RubyClass memoryClass = ruby.defineClassUnder("MemoryReport", ruby.getObject(), new ObjectAllocator() {
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new MemoryReport(ruby, rubyClass);
            }
        }, rootModule);
        memoryClass.defineAnnotatedMethods(MemoryReport.class);


        return true;
    }
}
