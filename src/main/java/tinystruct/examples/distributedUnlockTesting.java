package tinystruct.examples;

import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;

public class distributedUnlockTesting {

	public static void main(String[] args) throws ApplicationException {
		// TODO Auto-generated method stub
		distributedLockApp lock = new distributedLockApp();
		ApplicationManager.init();
		ApplicationManager.install(lock);

		ApplicationManager.call("unlock", null);
	}

}