# **Tetris Game Project Report**

## **1. Introduction**

The **Tetris Game** is a classic falling-block puzzle game that demonstrates the implementation of **Object-Oriented Programming (OOP)** principles using **Java**.
The project is designed to apply key OOP concepts such as **abstraction**, **inheritance**, **encapsulation**, and **polymorphism** to build a fully functional and interactive graphical application.

The game consists of differently shaped blocks (**Tetrominoes**) that fall from the top of the screen.
The player must move and rotate these blocks to fill horizontal lines without gaps.
When a line is completely filled, it disappears, and the player earns points.
The game ends when the blocks stack up to the top of the board.

### **Motivation**

The main motivation behind this project is to apply theoretical OOP knowledge in a creative and engaging way.
Developing a game like Tetris enhances logical thinking, event-driven programming skills, and real-time GUI handling using **Swing** and **AWT**.
Additionally, this project helps in understanding how OOP concepts improve **modularity**, **reusability**, and **maintainability** in Java-based applications.

---

## **2. Objectives**

* To implement core OOP concepts in Java.
* To design and develop an interactive graphical game using Swing.
* To understand the use of inheritance, polymorphism, and abstraction in game design.
* To provide persistent score storage using Java File I/O.
* To develop an efficient and user-friendly Tetris Game interface.

---

## **3. System Requirements**

### **Hardware Requirements**

* **Processor:** Intel i3 or higher
* **RAM:** 4 GB or higher
* **Hard Disk:** 500 MB free space
* **Display:** 1280x720 resolution or higher

### **Software Requirements**

* **Operating System:** Windows / Linux / macOS
* **Programming Language:** Java (JDK 17 or above)
* **IDE:** Eclipse / IntelliJ IDEA / NetBeans
* **Libraries Used:** Java Swing, AWT

---

## **4. Implementation**

This section describes the logic, structure, and use of Object-Oriented Programming in the Tetris Game.
The game is divided into several classes, each performing a specific function, following **modular** and **reusable** design principles.

---

### **Class Design and Description**

#### **1. Shape (Abstract Class)**

The **Shape** class acts as a base class for all Tetris block shapes.
It defines common attributes like position (**x**, **y**), color, and block structure matrix.
It also provides common methods such as movement (**moveLeft()**, **moveRight()**, **moveDown()**) and an abstract **rotate()** method implemented by subclasses.

* **Key OOP Concept Used:** Abstraction and Encapsulation
* **Purpose:** To define a common interface and shared properties for all shape types.

---

#### **2. Derived Shape Classes (IShape, OShape, TShape, SShape, ZShape, JShape, LShape)**

Each shape class extends the abstract **Shape** class and represents a unique Tetromino.
They define the block patterns and implement the rotation logic differently for each shape.

* **Key OOP Concept Used:** Inheritance and Polymorphism
* **Purpose:** Each subclass provides its specific behavior while maintaining a common interface defined by the abstract superclass.

---

#### **3. ScoreManager Class**

This class handles the loading and saving of high scores using file handling (**FileReader**, **FileWriter**).
It maintains persistent score data in a local file named **score.txt**.

* **Key Concepts Used:** File I/O and Exception Handling

**Methods:**

* **loadHighScore()** → Reads player name and score from the file.
* **saveHighScore()** → Writes updated high score to the file.

---

#### **4. Board Class**

The **Board** class is the core component of the game.
It extends **JPanel** and is responsible for game rendering, shape movement, collision detection, and scoring logic.

**Major Functions:**

* Generates random shapes using the **Random** class.

* Manages the current and next shape.

* Handles real-time updates using **javax.swing.Timer**.

* Detects collisions and line clearing.

* Displays player name, score, and high score.

* **Key OOP Concepts:** Composition, Event Handling, Encapsulation

* **GUI Framework:** Java Swing (**JPanel**, **Graphics**, **KeyListener**)

---

#### **5. Main Class**

The **Main** class serves as the entry point of the program.
It creates the main game window using **JFrame**, takes player input for name using **JOptionPane**, and adds the **Board** component to start the game.

**Key Responsibilities:**

* Initialize game window and user interface.
* Start the main event loop.

---

## **5. Use of OOP Concepts**

| **OOP Concept**   | **Implementation in Code**                                                               |
| ----------------- | ---------------------------------------------------------------------------------------- |
| **Abstraction**   | Abstract class **Shape** hides shape details and provides common interface.              |
| **Inheritance**   | **IShape**, **OShape**, etc., inherit from **Shape**.                                    |
| **Polymorphism**  | Each shape overrides **rotate()** method differently.                                    |
| **Encapsulation** | Data members like **x**, **y**, and **blocks** are protected within the **Shape** class. |
| **File Handling** | **ScoreManager** reads and writes scores to text files.                                  |
| **GUI Handling**  | Implemented using Swing’s **JPanel**, **JFrame**, and event listeners.                   |

---
