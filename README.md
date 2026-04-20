A Java-based Train Reservation Management System with both Console and Swing GUI interfaces. This project uses JDBC with MySQL to manage train operations like trains, passengers, and reservations.

📌 Features
🔹 Console Application
View Trains
View Passengers
View Reservations
Add Train
Update Train Price
Delete Train

🔹 GUI Application (Swing)
Tab-based interface:
Trains
Passengers
Reservations
Perform CRUD operations:
Add
Update
Delete
Load Data
User-friendly dialog input using JOptionPane

🛠️ Technologies Used
Java (Core + Swing)
JDBC (Java Database Connectivity)
MySQL Database
AWT & Swing for GUI

🗄️ Database Setup

Create Database
CREATE DATABASE train_reservation;
USE train_reservation;

Create Tables

Train Table
CREATE TABLE train (
train_id VARCHAR(10) PRIMARY KEY,
train_name VARCHAR(50),
source VARCHAR(50),
destination VARCHAR(50),
price INT
);

Passenger Table
CREATE TABLE passenger (
passenger_id VARCHAR(10) PRIMARY KEY,
name VARCHAR(50),
contact VARCHAR(15)
);

Reservation Table
CREATE TABLE reservation (
reservation_id VARCHAR(10) PRIMARY KEY,
train_id VARCHAR(10),
passenger_id VARCHAR(10),
travel_date DATE
);

⚙️ Configuration

Update your database credentials in both files:

static final String DB_URL = "jdbc:mysql://localhost:3306/train_reservation";
static final String USER = "root";
static final String PASS = "dbms";

▶️ How to Run

Compile
javac Train.java
javac TrainReservationGUI.java

Run Console Version
java train.Train

Run GUI Version
java TrainReservationGUI

💡 Key Concepts Used
JDBC Connection
PreparedStatement
ResultSet Handling
Swing Components (JFrame, JTable, JButton)
Event Handling
MVC-like structure

🚀 Future Enhancements
Login Authentication System
Online Ticket Booking
Seat Availability Check
Search & Filter Options
Reports & Analytics

👩‍💻 Author
Thirisha
