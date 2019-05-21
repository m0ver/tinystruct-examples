package tinystruct.examples;

import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.valve.DistributedLock;
import org.tinystruct.valve.Lock;

public class distributedUnlockTesting {
	public static void main(String[] args) throws ApplicationException {
		// TODO Auto-generated method stub
		ApplicationManager.init();
		ApplicationManager.install(new distributedLockApp());
		ApplicationManager.call("unlock", null);
		
		// TODO Auto-generated method stub
		Lock mlock = new DistributedLock("400000000000000000000000000000000000".getBytes());
		mlock.unlock();
		
		// TODO Auto-generated method stub
		int i = 0;
		while (i < 100) {
			Lock lock = new DistributedLock();
			lock.lock();
			System.out.println(i++ + ":Hello!");
			lock.unlock();
		}
	}
}