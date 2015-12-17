require "java"
$CLASSPATH << File.join(File.dirname(__FILE__), "..", "jruby-ext", "target", "classes")

require "monitoring/version"
require "com/purbon/jrmonitor/JRMonitor"

module JRMonitor

  def self.threads
    ThreadsReport.new
  end

  def self.system
    SystemReport.new
  end

  def self.memory
    MemoryReport.new
  end
end

if $0 == __FILE__
  puts "--"
  puts JRMonitor.threads.build(select: :waiting).each{ |k,v| puts v["thread.state"] }
  puts "--"
end