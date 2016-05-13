require "java"
$CLASSPATH << File.join(File.dirname(__FILE__), "..", "jruby-ext", "target", "classes")

require "monitoring/version"
require "com/purbon/jrmonitor/JRMonitor"

module JRMonitor

  def self.threads
    Report::Threads.new
  end

  def self.system
    Report::System.new
  end

  def self.memory
    Report::Memory.new
  end

  def self.process
    Report::Processd.new
  end
end
