require "java"
$CLASSPATH << File.join(File.dirname(__FILE__), "..", "jruby-ext", "target", "classes")

require "monitoring/version"
require "JRMonitor"

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

