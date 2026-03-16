MotorPH Payroll System
Goroup - 44
Senvenizer I. Valles lr.svalles@mmdc.mcl.edu
Project Overview

The MotorPH Payroll System is a Java-based console application designed to automate payroll management for MotorPH employees. It allows employees to view their personal information and enables payroll staff to compute salaries efficiently based on attendance records. The system reads data from CSV files, making it easy to update employee details or attendance without modifying the program.

Features

User Login

Employee login: Employees can check their personal information such as employee number, full name, and birthday.

Payroll staff login: Payroll staff can process salaries for individual employees or all employees at once.

Employee Information

Displays the employee number, full name, and birthday.

Payroll Processing

Calculates total hours worked per cutoff period (1–15 and 16–30 of each month).

Computes gross and net salaries.

Deducts contributions for SSS, PhilHealth, Pag-IBIG, and applicable taxes.

Supports processing payroll for a single employee or all employees in the system.

Time Handling

Work hours are calculated only between 8:00 AM and 5:00 PM.

Lunch break of 1 hour is automatically subtracted.

Maximum work hours per day are limited to 8 hours.

Getting Started
Prerequisites

Java Development Kit (JDK) installed on your computer.

Setup

Place the following CSV files in the src folder:

MotorPH_Employee Data.csv

MotorPH_Employee Attendance Record.csv

Open a terminal or command prompt in the project folder.

Compile and run the program:

javac main.java
java main
Login Credentials

Employee:

Username: employee

Password: 12345

Payroll Staff:

Username: payroll_staff

Password: 12345

Once logged in, follow the on-screen menus to navigate through the system.

CSV File Format
Employee Data (MotorPH_Employee Data.csv)

The employee CSV should include at least the following columns:

Column	Description
0	Employee Number
1	Last Name
2	First Name
3	Birthday
18	Hourly Rate

Note: Hourly rate must be numeric and placed in column 18. Quotation marks or extra spaces may cause errors.

Attendance Record (MotorPH_Employee Attendance Record.csv)

The attendance CSV should include the following columns:

Column	Description
0	Employee Number
3	Date (MM/DD/YYYY)
4	Login Time (H:mm)
5	Logout Time (H:mm)

The system will ignore attendance records outside the current year and month being processed.

Payroll Calculation Notes

Cut-off periods: Salaries are computed in two halves per month: 1–15 and 16–30.

Deductions:

SSS: Based on the employee’s monthly gross salary using the official SSS table.

PhilHealth: 3% of monthly gross salary, capped between 300–1800 PHP, divided equally between employee and employer.

Pag-IBIG: 1–2% of monthly gross salary, capped at 100 PHP.

Tax: Computed based on taxable income after deductions, according to the simplified tax brackets.

Net Salary: Calculated by subtracting total deductions (SSS, PhilHealth, Pag-IBIG, and tax) from the gross salary of the second cutoff.

How It Works

User logs in as either an employee or payroll staff.

Employees can enter their employee number to view their personal info.

Payroll staff can:

Process payroll for a single employee by entering their employee number.

Process payroll for all employees automatically.

The system reads attendance data, computes hours, applies deductions, and displays the gross and net salaries for each cutoff period.

Notes and Tips

Make sure the CSV files are formatted correctly to avoid runtime errors.

Only the months June to December are currently processed in this version.

The program uses console input and output, so it runs entirely in a terminal or command prompt.

This system is suitable for small-scale payroll processing and educational purposes.
