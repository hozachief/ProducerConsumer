// ProducerConsumer.java
// Author: Jose Fraga
// UAID: 010738487
// Created November 13, 2017 5:52PM


import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerConsumer
{    
    static int item;
    static Queue<Integer> q = new LinkedList<Integer>();
    static long sleepTime;
    // mutex (aka binary semaphore) to protect the buffer and counting semaphores
    static Semaphore mutex = new Semaphore(1);
    // counting semaphores 
    // to prevent a consumer from removing an item from an empty buffer(empty)
    static Semaphore empty = new Semaphore(5);
    // to prevent a producer from inserting into a full buffer(full)
    static Semaphore full = new Semaphore(0);
    
    // constructor
    public ProducerConsumer()
    {
        // initializing random number between 0 and 0.5
        Random randomNum = new Random();
        //              (rangeMin + (rangeMax - rangeMin) * r.nextDouble())
        double random = (0.0 + (0.5 - 0.0) * randomNum.nextDouble());
        this.sleepTime = (long)random;
    }         
    
    // main() function will initialize the buffer and create the separate
    // producer and consumer threads.
    // once created producer and consumer threads, main() function will sleep
    // for a period of time and, awaken and terminate the application.
    public static void main(String[] args)
    {
        // create Scanner to obtain input from command window
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter: sleep time, number of producer threads,"
            + " number of consumer threads\n");
        int runTime = input.nextInt();
        int producerNum = input.nextInt();
        int consumerNum = input.nextInt();
               
        // printing inputs
        System.out.printf("\nUsing arguments from the command line\nSleep time = %d\n"
                + "Producer threads = %d\nConsumer threads = %d\n\n", 
            runTime, producerNum, consumerNum);
        
        // need an array to store the Threads, to allow you to reference them
        Thread producerArray[] = new Thread[producerNum];        
        for (int i = 0; i < producerNum; i++)
        {          
            Thread Producer = new Thread(new Producer());            
            producerArray[i] = Producer;
            Producer.start();
        }

        Thread consumerArray[] = new Thread[consumerNum];        
        for (int i = 0; i < consumerNum; i++)
        {
            Thread Consumer = new Thread(new Consumer());
            consumerArray[i] = Consumer;
            Consumer.start();
        }
        
        // make main sleep for the runTime       
        try 
        {
            Thread.sleep(runTime * 1000);
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // stop the threads to prevent zombie threads in case the parent (aka main)
        // dies before the children
        for (int i = 0; i < producerNum; i++)
        {
            producerArray[i].stop();
            try 
            {
                producerArray[i].join();
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }

        for (int i = 0; i < consumerNum; i++)
        {
            consumerArray[i].stop();
            try 
            {
                consumerArray[i].join();
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}