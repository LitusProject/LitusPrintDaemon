import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.management.InstanceAlreadyExistsException;


public class PrintQueu {
	
	private Queue<Job> jobs = new LinkedList<Job>();
	private static PrintQueu instance;
	
	private PrintQueu () {
		
	}
	
	public static PrintQueu getInstance() {
		if (instance == null) {
			instance = new PrintQueu();
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
