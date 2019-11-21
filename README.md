# Signly - American Sign Language Interpretor

![Signly](https://github.com/MonikaCat/Signly/blob/master/photo/Signly_.PNG)

Signly is a desktop application written in Java language that can recognise and translate static American Sign Language gestures into English text and audio output. It captures the gestures using a small device Leap Motion Controller. The application aims to help hearing impaired people to communicate with the rest of the public by providing software that acts as an automated computer interpreter. 

![Signly](https://github.com/MonikaCat/Signly/blob/master/photo/signly.png)

## Getting Started

The application has been developed using the following tools: 

1) Java Enterprise Edition 8.0 (Java EE) 

2) NetBeans IDE  8.2  

3) Neuroph Artificial Neural Network Framework  

4) Leap Motion Controller & SDK (Leap Motion Orion 4.0.0) 

5) FreeTTS 1.2.3  

## Prerequisites

Before cloning the repo, make sure you have the following software installed on your computer: 

```
Leap Motion Controller SDK (Leap Motion Orion 4.0.0) 
```


## Getting Started

1. Plug in Leap Motion Controller to your computer
```
2. git clone https://github.com/MonikaCat/Signly
```
```
3. cd Signly/src/signly
```
4. Run SignlyMain.java


## Class Diagram

![Signly Class Diagram](https://github.com/MonikaCat/Signly/blob/master/photo/signly_diagram.png)

## Tests Results

The system achieved high accuracy of 84.80 %. Several letters including A, D, E, K, N, R accomplished lower value than anticipated due to high similarity between them and controller inability to correctly recognise gestures that involve placing finger on top of another.

![Signly](https://github.com/MonikaCat/Signly/blob/master/photo/signly_table.png)


## Authors

* **Monika Pusz** 


## Acknowledgments

* The application integrates the hand visualizer developed by Github user RuZman (https://github.com/RuZman/LeapFX). The developer had been contacted and his approval obtained to integrate it with this project.
* For the purpose of this project the dynamic gestures J and Z were replaced with the temporary static representation. 
