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
		this.setAction("unlock", "unlock");
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

		Watcher w = Watcher.getInstance();
		w.setListener(new org.tinystruct.valve.Watcher.EventListener() {

			@Override
			public void onCreate(String lockId) {
				// TODO Auto-generated method stub
				System.out.println(String.format("Created %s", lockId));
			}

			@Override
			public void onUpdate() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDelete(String lockId) {
				// TODO Auto-generated method stub
				System.out.println(String.format("Deleted %s", lockId));
			}
		});
		new Thread(w).start();
	}

}