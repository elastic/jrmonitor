require 'spec_helper'

describe JRMonitor::Report::Threads do

  let(:threads) { subject.build }

  it 'pull running threads' do
    expect(threads.keys.count).to be > 0
  end

  it 'fetch foreach threads information about cpu time' do
    threads.each_pair do |_, values|
      expect(values).to include('cpu.time')
    end
  end

  it 'fetch foreach threads information about thread state' do
    threads.each_pair do |_, values|
      expect(values).to include('thread.state')
    end
  end

  it 'fetch foreach threads information about blocked information' do
    threads.each_pair do |_, values|
      expect(values).to include('blocked.count')
      expect(values).to include('blocked.time')
    end
  end

  it 'fetch foreach threads information about waited information' do
    threads.each_pair do |_, values|
      expect(values).to include('waited.count')
      expect(values).to include('waited.time')
    end
  end

  describe "#filter thread status" do

    let(:options) do
      {}
    end
    let(:threads) do
      subject.build(options)
    end

    context "when filtering waiting threads" do

      let(:options) do
        {select: :waiting}
      end

      it 'fetch only selected threads' do
        threads.each_pair do |_, values|
          expect(values['thread.state']).to eq("waiting")
        end
      end

      context "if filtering with string key" do

        let(:options) do
          { "select" => "waiting" }
        end

        it 'fetch only selected threads' do
          threads.each_pair do |_, values|
            expect(values['thread.state']).to eq("waiting")
          end
        end
      end
    end

    context "when filtering running threads" do
      let(:options) do
        {select: :running}
      end

      it 'fetch only selected threads' do
        threads.each_pair do |_, values|
          expect(values['thread.state']).to eq("running")
        end
      end
    end


  end
end