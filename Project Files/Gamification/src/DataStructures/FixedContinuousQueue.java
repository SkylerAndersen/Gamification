package DataStructures;

/**
 * Queue with fixed size, O(1) enqueue, O(1) dequeue
 * */
public class FixedContinuousQueue<T> {
    private int dequeuePoint;
    private int enqueuePoint;
    private T[] data;

    /**
     * Default constructor
     * @param size fixed size of the queue.
     * */
    public FixedContinuousQueue (int size) {
        enqueuePoint = 0;
        dequeuePoint = 0;
        data = (T[]) (new Object[size]);
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
    }

    /**
     * Enqueue an object if space is available, throw exeption otherwise.
     * @param entry object to be added to the queue.
     * */
    public void enqueue (T entry) throws Exception {
        if (entry == null)
            return;
        boolean spaceAvailable = data[enqueuePoint] == null;
        if (!spaceAvailable)
            throw new Exception("Unable to queue more tasks. Space unavailable.");

        data[enqueuePoint] = entry;
        enqueuePoint = (enqueuePoint == data.length-1) ? 0 : (enqueuePoint + 1);
    }

    /**
     * Dequeue object from queue if there exists objects in the queue, throw an exception otherwise.
     * @return object dequeued
     * */
    public T dequeue () throws Exception {
        boolean dataPresent = data[dequeuePoint] != null;
        if (!dataPresent)
            throw new Exception("Unable to retrieve data that is not present.");

        T retrieved = data[dequeuePoint];
        dequeuePoint = (dequeuePoint == data.length-1) ? 0 : (dequeuePoint + 1);
        return retrieved;
    }

    /**
     * Check if there are objects in the queue
     * @return a boolean if the queue has any elements in it.
     * */
    public boolean hasNext () {
        return data[dequeuePoint] != null;
    }
}
