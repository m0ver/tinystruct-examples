package tinystruct.examples;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.tinystruct.ApplicationException;
import org.tinystruct.system.ApplicationManager;
import org.tinystruct.valve.DistributedLock;
import org.tinystruct.valve.Lock;

public class distributedUnlockTesting {
	public static void main(String[] args) throws ApplicationException {
		// TODO Auto-generated method stub
//		ApplicationManager.init();
//		ApplicationManager.install(new distributedLockApp());
//		ApplicationManager.call("unlock", null);
//
//		// TODO Auto-generated method stub
//		Lock mlock = new DistributedLock("400000000000000000000000000000000000".getBytes());
//		mlock.unlock();

		// TODO Auto-generated method stub
		int i = 1;
//		while (i <= 100) {
//			Lock lock = new DistributedLock();
//			lock.lock();
//			System.out.println(i++ + ":" + lock.id());
//			lock.unlock();
//		}

		int n = 1000;
		CyclicBarrier barrier = new CyclicBarrier(n);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					barrier.await();

					Lock lock = new DistributedLock();
					lock.lock();
					System.out.println(lock.id());
					lock.unlock();
				} catch (InterruptedException ex) {
					return;
				} catch (BrokenBarrierException ex) {
					return;
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};

		i = 1;
		while (i <= n) {
			new Thread(runnable).start();
			i++;
		}

	}
}