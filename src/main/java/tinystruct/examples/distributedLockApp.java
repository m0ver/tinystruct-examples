package tinystruct.examples;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.valve.Lock;
import org.tinystruct.valve.Watcher;

public class distributedLockApp extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("lock", "lock");
		this.setAction("lock/true", "lock");
		this.setAction("unlock", "unlock");
		this.setAction("close", "close");
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	public void lock() throws ApplicationException {
		Lock lock = Watcher.getInstance().acquire(true);

		if (lock != null) {
			System.out.println("Lock Id:" + lock.id());
			lock.lock();
		}
	}

	public void unlock() throws ApplicationException {
		Lock lock = Watcher.getInstance().acquire();
		if (lock != null) {
			System.out.println("UnLock Id:" + lock.id());
			lock.unlock();
		} else {
			System.out.println("No task is locked.");
		}
	}

	public void lock(boolean autoclose) throws ApplicationException {
		this.lock();
		if (autoclose)
			this.close();
	}

	public void close() {
		Watcher.getInstance().stop();
	}

	public static void main(String[] args) throws ApplicationException, InterruptedException {
		distributedLockApp lock = new distributedLockApp();
		ApplicationManager.init();
		ApplicationManager.install(lock);

		System.out.println("Lock 1 started...");
		ApplicationManager.call("lock", null);
		System.out.println("Hello, I executed this printing after lock 1 released.");

		System.out.println("Lock 2 started...");
		ApplicationManager.call("lock", null);
		System.out.println("Hello, I executed this printing after lock 2 released.");

		System.out.println("Lock 3 started...");
		ApplicationManager.call("lock", null);
		System.out.println("Hello, I executed this printing after lock 3 released.");

		ApplicationManager.call("close", null);

	}

}