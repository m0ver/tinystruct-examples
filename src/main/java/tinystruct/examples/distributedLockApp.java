package tinystruct.examples;

import org.tinystruct.AbstractApplication;
import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.valve.DistributedLock;
import org.tinystruct.valve.Lock;
import org.tinystruct.valve.Watcher;

import java.util.concurrent.TimeUnit;

public class distributedLockApp extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.setAction("lock", "lock");
		this.setAction("unlock", "unlock");
		this.setAction("monitor", "monitor");
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	public void lock() throws ApplicationException {
		Lock lock = Watcher.getInstance().acquire();

		if (lock != null) {
			System.out.println("Lock Id:" + lock.id());
			lock.lock();
		}
	}

	public void unlock(String lockId) throws ApplicationException {
		Lock lock = new DistributedLock(lockId.getBytes());
		System.out.println("UnLock Id:" + lock.id());
		lock.unlock();
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

	public static void main(String[] args) throws ApplicationException, InterruptedException {
		ApplicationManager.init();
		ApplicationManager.install(new distributedLockApp());
		try {
			System.out.println("Lock started...");
			ApplicationManager.call("lock", null);
			Thread.sleep(5000);
			System.out.println("Hello, I executed this printing after lock released.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ApplicationManager.call("unlock", null);
		}
	}

}