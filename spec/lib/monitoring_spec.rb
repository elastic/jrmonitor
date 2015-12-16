require 'spec_helper'

describe JRMonitor::ThreadsReport do

  let(:object) { subject.build }

  it 'pull running threads' do
    expect(object.keys.count).to be > 0
  end

  it 'fetch foreach threads information about cpu time' do
    object.each_pair do |_, values|
       expect(values).to include('cpu.time')
    end
  end

  it 'fetch foreach threads information about thread state' do
    object.each_pair do |_, values|
      expect(values).to include('thread.state')
    end
  end

  it 'fetch foreach threads information about blocked information' do
    object.each_pair do |_, values|
      expect(values).to include('blocked.count')
      expect(values).to include('blocked.time')
    end
  end

  it 'fetch foreach threads information about waited information' do
    object.each_pair do |_, values|
      expect(values).to include('waited.count')
      expect(values).to include('waited.time')
    end
  end

end