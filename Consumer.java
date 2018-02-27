// Consumer.java
// Author: Jose Fraga
// UAID: 010738487
// Created November 13, 2017 5:52PM


import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer implements Runnable
{
    public void run()
    {
        // consumer will 100 times go through the cycle of sleeping a random
        // amount of time, consuming (aka removing) an item from the bounded
        // buffer, and then printing the random integer.
        for (int i = 0; i < 100; i++)
        {
            try 
            {
                // counting semaphore wait(full)
                ProducerConsumer.full.acquire();

                // binary semaphore (aka mutex) wait(mutex)
                // acquire lock before entering critical section
                ProducerConsumer.mutex.acquire(0);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }            

            try
            {
                // sleep a random amount of time
                Thread.sleep(ProducerConsumer.sleepTime);
            }
            catch(InterruptedException exception)
            {
                exception.printStackTrace();
            }

            if (ProducerConsumer.q.peek() != null)
            {
                // consume an item
                // printing the random integer
                System.out.printf("        Consumer consumed %d\n", ProducerConsumer.q.remove()); 
            }
            
            // binary semaphore (aka mutex) signal(mutex)
            // release the lock, exiting the critical section
            ProducerConsumer.mutex.release(1);

            // counting semaphore signal(empty)
            ProducerConsumer.empty.release();
        } 
    }
}  