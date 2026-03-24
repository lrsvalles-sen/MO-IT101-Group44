import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class main {

    // Single Scanner instance to avoid closing System.in accidentally
    static Scanner input = new Scanner(System.in);

    // File paths for employee and attendance records
    static String employeeFile = "src/MotorPH_Employee Data.csv";
    static String attendanceFile = "src/MotorPH_Employee Attendance Record.csv";

    // Time format used in attendance file (e.g., 8:00)
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

    // Simple divider for cleaner console output
    public static void devider(){
        System.out.println("======================================");
    }

    public static void main(String[] args) {

        // Loop keeps login running without using recursion (avoids stack overflow)
        while(true){
            System.out.println("\nUSER LOGIN");
            devider();

            System.out.print("Username: ");
            String username = input.nextLine();

            System.out.print("Password: ");
            String password = input.nextLine();

            devider();

            // Validate user credentials and redirect to correct menu
            if(username.equals("employee") && password.equals("12345")){
                employeeMenu();
            }
            else if(username.equals("payroll_staff") && password.equals("12345")){
                payrollMenu();
            }
            else{
                System.out.println("Incorrect username and/or password");
            }
        }
    }

    // ================= EMPLOYEE MENU =================
    public static void employeeMenu(){
        while(true){
            System.out.println("\nEMPLOYEE MENU");
            devider();
            System.out.println("1. Enter Employee Number");
            System.out.println("2. Exit");
            devider();

            System.out.print("Choice: ");
            String choice = input.nextLine();

            if(choice.equals("1")){
                employeeInfo();
            }
            else if(choice.equals("2")){
                System.exit(0);
            }
            else{
                System.out.println("Invalid input");
            }
        }
    }

    // Displays basic employee information from CSV file
    public static void employeeInfo(){

        devider();
        System.out.print("Enter Employee Number: ");
        String empNum = input.nextLine().trim();
        devider();

        boolean found = false;

        try{
            // NOTE: File is opened every time this method runs.
            // For better performance, this should be loaded once into memory (ArrayList),
            // but kept simple here for beginner implementation.
            BufferedReader br = new BufferedReader(new FileReader(employeeFile));

            br.readLine(); // skip header
            String line;

            while((line = br.readLine()) != null){
                String[] data = line.split(",");

                // Check if this row belongs to the entered employee number
                if(data[0].trim().equals(empNum)){
                    devider();
                    System.out.println("\nEmployee Number: " + data[0]);
                    System.out.println("Employee Name: " + data[2] + " " + data[1]);
                    System.out.println("Birthday: " + data[3]);
                    devider();

                    found = true;
                    break;
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(!found){
            System.out.println("**Employee number does not exist**");
        }
    }

    // ================= PAYROLL MENU =================
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
                processPayrollMenu();
            }
            else if(choice.equals("2")){
                System.exit(0);
            }
            else{
                System.out.println("**Invalid input**");
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
                processEmployeePayroll(input.nextLine().trim());
            }
            else if(choice.equals("2")){
                processAllEmployees();
            }
            else if(choice.equals("3")){
                return; // return to previous menu (avoids stacking menus)
            }
            else{
                System.out.println("**Invalid input**");
            }
        }
    }

    // Computes total worked hours while enforcing company rules (8AM–5PM + lunch break)
    public static double computeHours(LocalTime in, LocalTime out){

        LocalTime start = LocalTime.of(8,0);
        LocalTime end = LocalTime.of(17,0);

        // Adjust time if employee logs in too early or logs out too late
        if(in.isBefore(start)) in = start;
        if(out.isAfter(end)) out = end;

        long minutes = Duration.between(in,out).toMinutes();

        // Deduct 1-hour lunch break if applicable
        if(minutes > 60) minutes -= 60;

        double hours = minutes / 60.0;

        // Maximum of 8 working hours per day
        if(hours > 8) hours = 8;

        return hours;
    }

    // ================= PAYROLL PROCESSING =================
    public static void processEmployeePayroll(String empNum){

        String first="", last="", birthday="";
        double hourlyRate=0;
        boolean found=false;

        try{
            BufferedReader br = new BufferedReader(new FileReader(employeeFile));
            br.readLine();

            String line;

            while((line=br.readLine())!=null){
                String[] data=line.split(",");

                if(data[0].trim().equals(empNum)){
                    last=data[1];
                    first=data[2];
                    birthday=data[3];

                    String rateText = data[18].replace("\"","").trim();
                    hourlyRate = Double.parseDouble(rateText);

                    found=true;
                    break;
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(!found){
            System.out.println("**Employee number does not exist**");
            return;
        }

        System.out.println("\n====================================");
        System.out.println("Employee #: "+empNum);
        System.out.println("Employee Name: "+first+" "+last);
        System.out.println("Birthday: "+birthday);

        for(int month=6; month<=12; month++){

            double firstHalf=0;
            double secondHalf=0;

            try{
                BufferedReader br=new BufferedReader(new FileReader(attendanceFile));
                br.readLine();

                String line;

                while((line=br.readLine())!=null){
                    String[] data=line.split(",");

                    if(!data[0].trim().equals(empNum)) continue;

                    String[] date=data[3].split("/");
                    int m=Integer.parseInt(date[0]);
                    int day=Integer.parseInt(date[1]);
                    int year=Integer.parseInt(date[2]);

                    if(year!=2024 || m!=month) continue;

                    LocalTime login=LocalTime.parse(data[4],timeFormat);
                    LocalTime logout=LocalTime.parse(data[5],timeFormat);

                    double hours=computeHours(login,logout);

                    if(day<=15) firstHalf+=hours;
                    else secondHalf+=hours;
                }
                br.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            String monthName=getMonth(month);

            double gross1=firstHalf*hourlyRate;
            double gross2=secondHalf*hourlyRate;
            double monthlyGross=gross1+gross2;

            double sss=getSSS(monthlyGross);
            double philhealth=getPhilHealth(monthlyGross);
            double pagibig=getPagIbig(monthlyGross);

            double deductionNoTax=sss+philhealth+pagibig;
            double taxable=monthlyGross-deductionNoTax;
            double tax=getTax(taxable);

            double totalDeduction=sss+philhealth+pagibig+tax;
            double netSecond=gross2-totalDeduction;

            devider();

            System.out.println("\nCutoff Date: "+monthName+" 1 to 15");
            System.out.println("Total Hours Worked: "+firstHalf);
            System.out.println("Gross Salary: "+gross1);
            System.out.println("Net Salary: "+gross1);

            System.out.println("\nCutoff Date: "+monthName+" 16 to 30");
            System.out.println("Total Hours Worked: "+secondHalf);
            System.out.println("Gross Salary: "+gross2);
            System.out.println("SSS: "+sss);
            System.out.println("PhilHealth: "+philhealth);
            System.out.println("Pag-IBIG: "+pagibig);
            System.out.println("Tax: "+tax);
            System.out.println("Total Deductions: "+totalDeduction);
            System.out.println("Net Salary: "+netSecond);

            devider();
        }
    }

    public static void processAllEmployees(){
        try{
            BufferedReader br=new BufferedReader(new FileReader(employeeFile));
            br.readLine();

            String line;

            while((line=br.readLine())!=null){
                String[] data=line.split(",");
                processEmployeePayroll(data[0].trim());

                System.out.println("\n-------------------------------------");
            }

            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

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

    public static double getSSS(double salary){
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
        double premium=salary*0.03;

        if(premium<300) premium=300;
        if(premium>1800) premium=1800;

        return premium/2;
    }

    public static double getPagIbig(double salary){
        double contrib;

        if(salary<=1500) contrib=salary*0.01;
        else contrib=salary*0.02;

        if(contrib>100) contrib=100;

        return contrib;
    }

    public static double getTax(double taxable){
        if(taxable<=20832) return 0;
        else if(taxable<33333) return (taxable-20833)*0.20;
        else if(taxable<66667) return 2500+(taxable-33333)*0.25;
        else if(taxable<166667) return 10833+(taxable-66667)*0.30;
        else if(taxable<666667) return 40833.33+(taxable-166667)*0.32;
        else return 200833.33+(taxable-666667)*0.35;
    }
}
