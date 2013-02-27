import java.util.LinkedList;
import java.util.Queue;

public class PrintQueue {
	
	private Queue<Job> jobs = new LinkedList<Job>();
	private static PrintQueue instance;
	
	private PrintQueue () {
		
	}
	
	public static PrintQueue getInstance() {
		if (instance == null) {
			instance = new PrintQueue();
		}
		return instance;
	}
	
	public synchronized boolean hasJob() {
		return !jobs.isEmpty();
	}
	
	public synchronized void addJob(Ticket ticket, int type) {
		jobs.add(new Job(ticket,type));
	}
	
	public synchronized Job getJob() {
		return jobs.poll();
	}
	
	
	public static class Job {
		
		private Ticket ticket;
		private int layoutType;
		
		public Job(Ticket ticket, int layoutType) {
			this.ticket = ticket;
			this.layoutType =layoutType;
		}

		public Ticket getTicket() {
			return ticket;
		}

		public void setTicket(Ticket ticket) {
			this.ticket = ticket;
		}

		public int getLayoutType() {
			return layoutType;
		}

		public void setLayoutType(int layoutType) {
			this.layoutType = layoutType;
		}
		
	}

}
