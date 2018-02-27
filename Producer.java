// Producer.java
// Author: Jose Fraga
// UAID: 010738487
// Created November 13, 2017 5:52PM


import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer implements Runnable
{
    // producer thread and consumer thread
    public void run()
    {                           
        // producer will 100 times go through the cycle of sleeping a random
        // amount of time, creating a random integer, inserting it into the
        // bounded buffer of size 5, and then printing the random integer            
        for (int i = 0; i < 100; i++)
        {
            try 
            {
                // counting semaphore wait(empty)
                ProducerConsumer.empty.acquire();

                // binary semaphore (aka mutex) wait(mutex)
                // acquire lock before entering critical section
                ProducerConsumer.mutex.acquire(0);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }

            // sleep a random amount of time
            try
            {
                // sleep
                Thread.sleep(ProducerConsumer.sleepTime);
            }
            catch(InterruptedException exception)
            {
                exception.printStackTrace();
            }                
            
            // creating a random integer
            Random randomItem = new Random();
            int randomInt = randomItem.nextInt();            
            ProducerConsumer.item = randomInt;
                    
            // inserting the random integer             
            ProducerConsumer.q.add(ProducerConsumer.item);
            
            // printing the head of the queue
            System.out.printf("Producer produced %d\n", ProducerConsumer.item);

            // binary semaphore (aka mutex) signal(mutex)
            // release the lock, exiting the critical section
            ProducerConsumer.mutex.release(1);

            // counting semaphore signal(full)
            ProducerConsumer.full.release();    
        }    
    }
}