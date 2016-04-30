require "bundler/gem_tasks"
require "rspec/core/rake_task"
require "fileutils"

RSpec::Core::RakeTask.new(:spec)

task :default => :spec


namespace :build do
  desc "build the jruby extension classes"
  task :extension do
    FileUtils.rm_rf("jruby-ext/target")
    cd "jruby-ext" do
      `mvn compile`
    end
  end
end