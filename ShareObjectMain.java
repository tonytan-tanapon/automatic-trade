package apitest;

public class ShareObjectMain {

    public static void main(String[] args) {
        SharedObject sharedObj = new SharedObject();
        Thread waitingThread = new Thread(new WaitingThread(sharedObj));
        waitingThread.start(); // Start the waiting thread
        
        // Do some work...
        
        sharedObj.setReady(); // Signal that the shared object is ready
    }
}