/**
 * Litus is a project by a group of students from the KU Leuven. The goal is to create
 * various applications to support the IT needs of student unions.
 *
 * @author Niels Avonds <niels.avonds@litus.cc>
 * @author Karsten Daemen <karsten.daemen@litus.cc>
 * @author Koen Certyn <koen.certyn@litus.cc>
 * @author Bram Gotink <bram.gotink@litus.cc>
 * @author Dario Incalza <dario.incalza@litus.cc>
 * @author Pieter Maene <pieter.maene@litus.cc>
 * @author Kristof Mariën <kristof.marien@litus.cc>
 * @author Lars Vierbergen <lars.vierbergen@litus.cc>
 * @author Daan Wendelen <daan.wendelen@litus.cc>
 *
 * @license http://litus.cc/LICENSE
 */

import java.util.LinkedList;
import java.util.Queue;


public class PrintQueue {

	private Queue<Job> jobs = new LinkedList<Job>();
	private static PrintQueue instance;

	private PrintQueue () {
	}

	public static PrintQueue getInstance() {
		if (instance == null)
			instance = new PrintQueue();

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
