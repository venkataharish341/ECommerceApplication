Running:
--------
Use any IDE and just run the CustomerOrderApp.java class.

Improvements :
--------------

1. When multiple orders are created at the same time, race condition occurs and product quantity will not be correctly deducted. 
We can use synchronized method for deductStock()

2. We can use multi-threading to reduce the latency where ever possible

3. Secure the end points and use role based authorization

4. Unit and integration tests to be written.

5. We can add logger configuration.

6. We can write end points to see contents of cache.