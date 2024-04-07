package apitest;

class SharedObject {
    boolean isReady = false;
    
    synchronized void waitForReady() throws InterruptedException {
        while(!isReady) {
            wait(); // Wait until the object is ready
        }
        // Do something when the object is ready
    }
    
    synchronized void setReady() {
        isReady = true;
        notifyAll(); // Notify all waiting threads that the object is ready
    }
}	

class WaitingThread implements Runnable {
    SharedObject sharedObj;
    
    WaitingThread(SharedObject obj) {
        sharedObj = obj;
    }
    
    public void run() {
        try {
            sharedObj.waitForReady(); // Wait for the shared object to be ready
        } catch (InterruptedException e) {
            // Handle InterruptedException
        }
        // Do something when the shared object is ready
    }
}

