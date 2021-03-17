package alanisia.rpc.core.future;

import alanisia.rpc.core.model.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class RPCFuture implements Future<Object> {
    private final long timeout = 10000;
    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();
    private Response response = null;

    @Override
    public boolean cancel(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    @Override
    public Object get() {
        return get(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Object get(long l, TimeUnit timeUnit) {
        lock.lock(); // be sure to synchronize
        long start = System.currentTimeMillis();
        log.info("Try to get future object...");
        try {
            // timeout control
            while (!isDone()) {
                // wait this thread until being done
                done.await(l, timeUnit);
                if (isDone() || System.currentTimeMillis() - start > l) break;
            }
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage());
        } finally {
            lock.unlock();
        }
        if (Objects.requireNonNull(response).getStatus() == 1) return response.getResult();
        else return null;
    }

    public void setResponse(Response response) {
        this.response = response;
        lock.lock();
        try {
            // send signal to waiting thread after getting response
            done.signal();
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
