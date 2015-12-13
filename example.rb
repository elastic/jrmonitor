require "jruby-monitoring"

threads_report = JRMonitor.threads.build
threads_report.each_pair do |report_name, fields|
  puts "#{report_name} #{fields['cpu.time']}"
end

puts

system_report = JRMonitor.system.build
system_report.each_pair do |key, value|
  puts "#{key} #{value}"
end

puts

memory_report = JRMonitor.memory.build
memory_report.each_pair do |type, data|
  puts type
  data.each_pair do |pool, values|
    puts pool
    values.each_pair do |k,v|
      puts "#{k} #{v}"
    end
    puts
  end
  puts "---"
end
puts "****"