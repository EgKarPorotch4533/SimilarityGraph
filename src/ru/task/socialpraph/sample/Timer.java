package ru.task.socialpraph.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {

	public class TimeRange {
		
		private long start;
		private long end;
		
		public TimeRange() {
			this.start = System.currentTimeMillis();
		}
		
		public TimeRange(long ellapsed) {
			this.start = 0;
			this.end = ellapsed;
		}
		
		public TimeRange(TimeRange ...ranges) {
			this.start = 0;
			for (TimeRange range : ranges) {
				this.end += range.ellapsed();
			}
		}
		
		public void stop() {
			this.end = System.currentTimeMillis();
		}
		
		public long ellapsed() {
			return end - start;
		}
		
		public String toString() {
			return String.format("%d (mm:ss:SSS - %s)", ellapsed(),
					new SimpleDateFormat("mm:ss:SSS").format(new Date(ellapsed())));
		}
	}
	
	private TimeRange total;
	private TimeRange processing;
	private TimeRange mapping;
	private TimeRange reducing;
	private TimeRange other;
	private TimeRange io;
	private TimeRange io1;
	private TimeRange io2;
	
	public void startTotal() {
		total = new TimeRange();
	}
	
	public void stopTotal() {
		total.stop();
		io = new TimeRange(io1, io2);
		/*other = new TimeRange(
				total.ellapsed() - processing.ellapsed() - io.ellapsed());*/
	}
	
	public void startProcessing() {
		processing = new TimeRange();
	}
	
	public void stopProcessing() {
		processing.stop();;
	}
	
	public void startMapping() {
		mapping = new TimeRange();
	}
	
	public void stopMapping() {
		mapping.stop();;
	}
	
	public void startReducing() {
		reducing = new TimeRange();
	}
	
	public void stopReducing() {
		reducing.stop();;
	}
	
	public void startIo1() {
		io1 = new TimeRange();
	}
	
	public void stopIo1() {
		io1.stop();;
	}
	
	public void startIo2() {
		io2 = new TimeRange();
	}
	
	public void stopIo2() {
		io2.stop();;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Processing time: ").append(processing.toString());
		sb.append("\nMapping time: ").append(mapping.toString());
		sb.append("\nReducing time: ").append(reducing.toString());
		sb.append('\n');
		sb.append("\nTotal time: ").append(total.toString());
		sb.append("\nI/O: ").append(io.toString());
		//sb.append("\nRest: ").append(other.toString());
		return sb.toString();
	}
}
