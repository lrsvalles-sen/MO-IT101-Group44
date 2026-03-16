import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class main {

    static Scanner input = new Scanner(System.in); // Scanner for user input

    static String employeeFile = "src/MotorPH_Employee Data.csv"; // CSV file with employee data
                                                                //You can change the file name/directory
    static String attendanceFile = "src/MotorPH_Employee Attendance Record.csv"; // CSV file with attendance records
                                                                                 //Change the file name/directory

    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm"); // time format, e.g., 8:00

    public static void devider(){
        System.out.println("======================================"); // simple line divider
    }

    public static void main(String[] args) {

        while(true){ // keep running until correct login

            System.out.println("\nUSER LOGIN");
            devider();
            System.out.print("Username: ");
            String username = input.nextLine();

            System.out.print("Password: ");
            String password = input.nextLine();
            devider();

            if(username.equals("employee") && password.equals("12345")){ // if employee login
                employeeMenu(); // go to employee menu
            }
            else if(username.equals("payroll_staff") && password.equals("12345")){ // if payroll staff login
                payrollMenu(); // go to payroll menu
            }
            else{
                System.out.println("Incorrect username and/or password"); // wrong credentials
            }
        }
    }

    // EMPLOYEE MENU
    public static void employeeMenu(){

        while(true){

            System.out.println("\nEMPLOYEE MENU");
            devider();
            System.out.println("1. Enter Employee Number");
            System.out.println("2. Exit");
            devider();

            System.out.print("Choice: ");
            String choice = input.nextLine();

            if(choice.equals("1")){ // if user chooses 1, ask employee number
                employeeInfo(); // display employee info
            }
            else if(choice.equals("2")){ // exit program
                System.exit(0);
            }
            else{
                System.out.println("Invalid input"); // wrong input
            }
        }
    }

    public static void employeeInfo(){ // print employee info
        devider();
        System.out.print("Enter Employee Number: ");
        String empNum = input.nextLine().trim(); // input employee number
        devider();

        boolean found = false; // flag to check if employee exists

        try{

            BufferedReader br = new BufferedReader(new FileReader(employeeFile)); // open CSV file
            br.readLine(); // skip header

            String line;

            while((line = br.readLine()) != null){ // read each line

                String[] data = line.split(","); // split CSV line

                if(data[0].trim().equals(empNum)){ // if employee number matches
                    devider();
                    System.out.println("\nEmployee Number: " + data[0]);
                    System.out.println("Employee Name: " + data[2] + " " + data[1]); // first name + last name
                    System.out.println("Birthday: " + data[3]);
                    devider();

                    found = true; // employee found
                    break;
                }
            }

            br.close(); // close file

        }catch(Exception e){
            e.printStackTrace(); // print error if something goes wrong
        }

        if(!found){
            System.out.println("**Employee number does not exist**"); // not found
        }
    }

    // PAYROLL STAFF MENU
    public static void payrollMenu(){

        while(true){
            devider();
            System.out.println("\nPAYROLL STAFF MENU");
            System.out.println("1. Process Payroll");
            System.out.println("2. Exit");
            devider();

            System.out.print("Choice: ");
            String choice = input.nextLine();

            if(choice.equals("1")){
                processPayrollMenu(); // go to process payroll menu
            }
            else if(choice.equals("2")){
                System.exit(0); // exit program
            }
            else{
                System.out.println("**Invalid input**"); // wrong input
            }
        }
    }

    public static void processPayrollMenu(){

        while(true){
            devider();
            System.out.println("\nPROCESS PAYROLL");
            System.out.println("1. One Employee");
            System.out.println("2. All Employees");
            System.out.println("3. Exit");
            devider();
            System.out.print("Choice: ");
            String choice = input.nextLine();

            if(choice.equals("1")){
                System.out.print("Enter Employee Number: ");
                processEmployeePayroll(input.nextLine().trim()); // process single employee
            }
            else if(choice.equals("2")){
                processAllEmployees(); // process all employees
            }
            else if(choice.equals("3")){
                return; // back to previous menu
            }
            else{
                System.out.println("**Invalid input**"); // wrong input
            }
        }
    }

    // COMPUTE WORK HOURS
    public static double computeHours(LocalTime in, LocalTime out){

        LocalTime start = LocalTime.of(8,0); // official start time
        LocalTime end = LocalTime.of(17,0); // official end time

        if(in.isBefore(start)) in = start; // adjust if clocked in earlier than 8
        if(out.isAfter(end)) out = end; // adjust if clocked out later than 5pm

        long minutes = Duration.between(in,out).toMinutes(); // total minutes worked

        if(minutes > 60) minutes -= 60; // subtract 1 hour lunch

        double hours = minutes / 60.0; // convert to hours

        if(hours > 8) hours = 8; // max 8 hours per day

        return hours;
    }

    // PROCESS SINGLE EMPLOYEE
    public static void processEmployeePayroll(String empNum){

        String first="",last="",birthday="";
        double hourlyRate=0;

        boolean found=false;

        try{

            BufferedReader br = new BufferedReader(new FileReader(employeeFile)); // open employee CSV
            br.readLine(); // skip header

            String line;

            while((line=br.readLine())!=null){

                String[] data=line.split(",");

                if(data[0].trim().equals(empNum)){ // check if employee number matches

                    last=data[1];
                    first=data[2];
                    birthday=data[3];
                    String rateText = data[18].replace("\"","").trim(); // get hourly rate
                    hourlyRate = Double.parseDouble(rateText);

                    found=true;
                    break;
                }
            }

            br.close();

        }catch(Exception e){
            e.printStackTrace(); // print error
        }

        if(!found){
            System.out.println("**Employee number does not exist**");
            return;
        }

        System.out.println("\n====================================");
        System.out.println("Employee #: "+empNum);
        System.out.println("Employee Name: "+first+" "+last);
        System.out.println("Birthday: "+birthday);

        for(int month=6; month<=12; month++){ // loop from June to December

            double firstHalf=0; // hours 1st half
            double secondHalf=0; // hours 2nd half

            try{

                BufferedReader br=new BufferedReader(new FileReader(attendanceFile)); // open attendance CSV
                br.readLine(); // skip header

                String line;

                while((line=br.readLine())!=null){

                    String[] data=line.split(",");

                    if(!data[0].trim().equals(empNum)) continue; // skip if not this employee

                    String[] date=data[3].split("/"); // split date

                    int m=Integer.parseInt(date[0]);
                    int day=Integer.parseInt(date[1]);
                    int year=Integer.parseInt(date[2]);

                    if(year!=2024 || m!=month) continue; // skip if not current month/year

                    LocalTime login=LocalTime.parse(data[4],timeFormat);
                    LocalTime logout=LocalTime.parse(data[5],timeFormat);

                    double hours=computeHours(login,logout); // compute hours

                    if(day<=15) firstHalf+=hours; // first half of month
                    else secondHalf+=hours; // second half
                }

                br.close();

            }catch(Exception e){
                e.printStackTrace(); // print error
            }

            String monthName=getMonth(month); // get month name

            double gross1=firstHalf*hourlyRate;
            double gross2=secondHalf*hourlyRate;
            double monthlyGross=gross1+gross2; // total monthly gross

            // compute deductions
            double sss=getSSS(monthlyGross); // SSS contribution
            double philhealth=getPhilHealth(monthlyGross); // PhilHealth contribution
            double pagibig=getPagIbig(monthlyGross); // Pag-IBIG contribution

            double deductionNoTax=sss+philhealth+pagibig; // deductions before tax
            double taxable=monthlyGross-deductionNoTax; // taxable income
            double tax=getTax(taxable); // compute tax
            double totalDeduction=sss+philhealth+pagibig+tax; // total deductions including tax

            double netSecond=gross2-totalDeduction; // net pay for 16-30 cutoff

            devider();
            System.out.println("\nCutoff Date: "+monthName+" 1 to 15");
            System.out.println("Total Hours Worked: "+firstHalf);
            System.out.println("Gross Salary: "+gross1);
            System.out.println("Net Salary: "+gross1); // first half net = gross, walang deductions

            System.out.println("\nCutoff Date: "+monthName+" 16 to 30");
            System.out.println("Total Hours Worked: "+secondHalf);
            System.out.println("Gross Salary: "+gross2);

            // show deductions
            System.out.println("SSS: "+sss);
            System.out.println("PhilHealth: "+philhealth);
            System.out.println("Pag-IBIG: "+pagibig);
            System.out.println("Tax: "+tax);
            System.out.println("Total Deductions: "+totalDeduction);

            System.out.println("Net Salary: "+netSecond);
            devider();
        }
    }

    // PROCESS ALL EMPLOYEES
    public static void processAllEmployees(){

        try{

            BufferedReader br=new BufferedReader(new FileReader(employeeFile)); // open employee CSV
            br.readLine(); // skip header

            String line;

            while((line=br.readLine())!=null){

                String[] data=line.split(",");

                processEmployeePayroll(data[0].trim()); // process payroll for each employee

                System.out.println("\n-------------------------------------"); // divider between employees
            }

            br.close();

        }catch(Exception e){
            e.printStackTrace(); // print error
        }
    }

    // GET MONTH NAME
    public static String getMonth(int m){

        if(m==6) return "June";
        if(m==7) return "July";
        if(m==8) return "August";
        if(m==9) return "September";
        if(m==10) return "October";
        if(m==11) return "November";
        if(m==12) return "December";

        return "";
    }

    // SSS CONTRIBUTION TABLE
    public static double getSSS(double salary){

        // basic fixed contributions based on salary range
        if(salary<3250) return 135;
        else if(salary<3750) return 157.5;
        else if(salary<4250) return 180;
        else if(salary<4750) return 202.5;
        else if(salary<5250) return 225;
        else if(salary<5750) return 247.5;
        else if(salary<6250) return 270;
        else if(salary<6750) return 292.5;
        else if(salary<7250) return 315;
        else if(salary<7750) return 337.5;
        else if(salary<8250) return 360;
        else if(salary<8750) return 382.5;
        else if(salary<9250) return 405;
        else if(salary<9750) return 427.5;
        else if(salary<10250) return 450;
        else if(salary<10750) return 472.5;
        else if(salary<11250) return 495;
        else if(salary<11750) return 517.5;
        else if(salary<12250) return 540;
        else if(salary<12750) return 562.5;
        else if(salary<13250) return 585;
        else if(salary<13750) return 607.5;
        else if(salary<14250) return 630;
        else if(salary<14750) return 652.5;
        else if(salary<15250) return 675;
        else if(salary<15750) return 697.5;
        else if(salary<16250) return 720;
        else if(salary<16750) return 742.5;
        else if(salary<17250) return 765;
        else if(salary<17750) return 787.5;
        else if(salary<18250) return 810;
        else if(salary<18750) return 832.5;
        else if(salary<19250) return 855;
        else if(salary<19750) return 877.5;
        else if(salary<20250) return 900;
        else if(salary<20750) return 922.5;
        else if(salary<21250) return 945;
        else if(salary<21750) return 967.5;
        else if(salary<22250) return 990;
        else if(salary<22750) return 1012.5;
        else if(salary<23250) return 1035;
        else if(salary<23750) return 1057.5;
        else if(salary<24250) return 1080;
        else if(salary<24750) return 1102.5;
        else return 1125;
    }

    public static double getPhilHealth(double salary){

        double premium=salary*0.03; // 3% contribution

        if(premium<300) premium=300; // min contribution
        if(premium>1800) premium=1800; // max contribution

        return premium/2; // employee share
    }

    public static double getPagIbig(double salary){

        double contrib;

        if(salary<=1500) contrib=salary*0.01; // 1% if <=1500
        else contrib=salary*0.02; // 2% if >1500

        if(contrib>100) contrib=100; // max 100

        return contrib; // return employee share
    }

    public static double getTax(double taxable){

        // simplified tax brackets
        if(taxable<=20832) return 0;
        else if(taxable<33333)
            return (taxable-20833)*0.20;
        else if(taxable<66667)
            return 2500+(taxable-33333)*0.25;
        else if(taxable<166667)
            return 10833+(taxable-66667)*0.30;
        else if(taxable<666667)
            return 40833.33+(taxable-166667)*0.32;
        else
            return 200833.33+(taxable-666667)*0.35;
    }
}
