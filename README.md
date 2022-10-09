# CS6650-Assignment1
<h1>LiftServer/LiftSpringServer</h1>

The *LiftServer* contains a *SkiersServlet* which validates the URL for both post and get requests, and it also validates whether a JSON file is sent with the correct attributes.

LiftSpringServer operates similarly as the LiftServer except it uses SpringBoot.

<h1>LiftRecord</h1>


DataGeneration
	SkiersDataGeneration uses an Abstract Factory design pattern to generate random data for the JSON file.


Model
	SkiersRunner is a POJO that stores information regarding the generated data.


Threads
	The producer class puts the SkiersRunner into a BlockingQueue and the consumer runnable then takes the SkiersRunner object to run the POST requests each phase. There’s one producer thread producing and multiple consumer threads consuming. The SkiersClient runs 32, 64, and 128 threads in three different phases, and each phase displays the number of successful and unsuccessful posts, Wall Time, and the actual/expected throughput in the Phase class. Thread execution occurs in the Phase class and it’s where most of the thread creation, termination, and printing of the statistics of each phase happens. A thread-pool is used in this class to manage the consumer thread and ensure that threads are always running. Concurrency tools, such as AtomicIntegers and Collections.synchronizedList, were also used to ensure that the variables were thread-safe during the entire process.


Client
	SkiersClient manages calling the threads, initializing the ApiClient, and handling the print state

    
Calculations
	A calculations class is created in part2 of the assignment, and it is used to compute the mean, median, 99th percentile, min, and max.

<h2>Little’s Law Prediction:</h2>
Little’s Law is defined as N = λ * L. This means that the amount of concurrency = throughput of requests processed per unit time * the average response time a request spends in the system. The actual throughput is calculated as the total number of requests/wall time. For the expected throughput, I divided the total number of threads by the average latency of a single thread to calculate the requests per second for a single thread. It was better than the expected throughput mainly due to multithreading– more requests are being handled at the same time. This is also shown when the number of threads doubles when the number of posts only increments by a smaller percentage.
