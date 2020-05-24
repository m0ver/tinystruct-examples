package tinystruct.examples;

import org.tinystruct.AbstractApplication;

import java.util.HashMap;
import java.util.concurrent.*;

@SuppressWarnings("unchecked")
public class RequestMerger extends AbstractApplication {
    private static RequestMerger ourInstance = new RequestMerger();
    private LinkedBlockingQueue<AsyncRequest> queue = new LinkedBlockingQueue<AsyncRequest>();
    private ScheduledThreadPoolExecutor executorService;

    public static RequestMerger getInstance() {
        return ourInstance;
    }

    private RequestMerger() {
    }

    @Override
    public void init() {
        executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int size = queue.size();
                for(int i = 0 ; i < size; i++) {
                    AsyncRequest req = queue.poll();
                }



/*
                HashMap<String, Map<String,Result>> responses = new HashMap();
                for(Map response : responses) {
                    String requestId = response.get("requestId").toString();
                    responses.put(requestId, response);
                }
*/
                

            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public HashMap<String, Result> take(AsyncRequest request) throws InterruptedException, ExecutionException {
        request.future = new CompletableFuture();
        queue.put(request);

        return request.future.get();
    }

    private void workon(AsyncRequest request) {

    }

    @Override
    public String version() {
        return null;
    }
}

class AsyncRequest {
    public String requestId;
    public Future<HashMap<String, Result>> future;
}

interface Result<T> {
    T get();
}