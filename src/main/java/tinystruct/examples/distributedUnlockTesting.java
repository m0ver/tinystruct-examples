package tinystruct.examples;

import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;

public class distributedUnlockTesting {
	public static void main(String[] args) throws ApplicationException {
		// TODO Auto-generated method stub
		ApplicationManager.init();
		ApplicationManager.install(new distributedLockApp());

		ApplicationManager.call("unlock", null);
		
//		Lock lock = new DistributedLock("400000000000000000000000000000000000".getBytes());
//		lock.unlock();
	}
}