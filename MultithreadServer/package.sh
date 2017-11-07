#!/bin/bash

javac Server.java MultiThreadedServer.java WorkerRunnable.java 

jar -cvfe Server.jar Server Server.class WorkerRunnable.class MultiThreadedServer.class 
