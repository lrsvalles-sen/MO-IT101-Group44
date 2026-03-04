import java.util.Scanner;
public class main {
    Scanner userInput = new Scanner(System.in);

    public static void main(String[] args)
    {   

        Scanner userInput = new Scanner(System.in); //Create a scanner for user inputs
        System.out.println("==========/Log-in/==========");
        System.out.print("Enter Username: "); //ask the user for username
        String userName = userInput.nextLine().toLowerCase();//This code will get the user's username and convert it to lowercase

        System.out.print("Enter Password: ");//ask the user for password
        String password = userInput.nextLine().toLowerCase();//This code will get the user's password and convert it to lowercase
        System.out.println("============================");


        if (userName.equals("admin") && password.equals("admin123")) {//Check if the the name and password is correct
            System.out.println("Welcome admin");
            admin(); //opens the admin interface
        }
        //This will be updated para soon we can use yung csv file para magllog-in si employee using their name and employee number
        else if (userName.equals("employee")&& password.equals("employee123")) {//Check if the the name and password is correct
            // employee();
            employee();//opens the employee interface
        }
        else{
            System.out.println("User Not found");// if the username and password are wrong
            main(args);
        }
        userInput.close();
    } 

    public static void admin(){
        System.out.println("this is the admin panel");
        System.out.println("Working");
    }

    public static void employee(){
        Scanner userInput = new Scanner(System.in);
        boolean isChoosing = false;


        System.out.println("=====Welcome Employee=====" );
        while (!isChoosing) {
            System.out.println("What can I do for you?");
            System.out.print("1 for paycheck, 2 for time-in/out: ");
            int choice = userInput.nextInt();
            if (choice == 1) {
                isChoosing = true;
                payCheck();
            }
            else if (choice == 2){
                isChoosing = true;
                timeInOut();
            }
            else{
                System.out.println("NOT A VALID INPUT");
            }
        }
        System.out.println("===========================" );
        

        userInput.close();
    }

    public static void timeInOut(){
        Scanner userInput = new Scanner(System.in);
        boolean timeinout = false;

        while (!timeinout) {
            System.out.println("*****Employee time-in/time-out*****");
            System.out.print("Enter employee number: ");
            String employeeNumber = userInput.nextLine();

            if (!employeeNumber.equals("123456")) {
                System.out.println("No Account Found");
                continue;
            }

            System.out.print("Salary per hour: ");
            String salaryPerHourInput = userInput.nextLine();
            double salaryPerHour = Double.parseDouble(salaryPerHourInput);

            System.out.print("Enter time-in (HH:mm): ");
            double timeIn = toDecimalHours(userInput.nextLine());

            System.out.print("Enter time-out (HH:mm): ");
            double timeOut = toDecimalHours(userInput.nextLine());

            double adjustedTimeOut = timeOut;
            if (adjustedTimeOut < timeIn) {
                adjustedTimeOut += 24;
            }

            double hoursWorked = adjustedTimeOut - timeIn;
            double totalSalary = salaryPerHour * hoursWorked;

            System.out.println("***********************************");
            System.out.println("=====Calculated Salary=====");
            System.out.println("Employee Number: " + employeeNumber);
            System.out.println("Salary per hour: " + salaryPerHour);
            System.out.println("Time-In: " + timeIn);
            System.out.println("Time-Out: " + timeOut);
            System.out.println("Total Hours Worked: " + hoursWorked);
            System.out.println("---------------------------");
            System.out.println("Total Salary: " + totalSalary);
            System.out.println("===========================");

            employee();
            timeinout = true;
        }
    }

    public static double toDecimalHours(String time) {
    String[] parts = time.split(":");
    int hour = Integer.parseInt(parts[0]);
    int minute = Integer.parseInt(parts[1]);
    return hour + (minute / 60.0);
}


    public static void payCheck(){
        Scanner userInput = new Scanner(System.in);
        boolean paycheck = false;

        

        while (!paycheck) {
            System.out.print("Enter your name: ");
            String employeeName = userInput.nextLine().toLowerCase();

            System.out.print("Enter your employee number: ");
            String employeeNumber = userInput.nextLine();
            
            if (employeeName.equals("jhon") && employeeNumber.equals("123456")) {
                System.out.println("paycheck Working");
                paycheck = true;
            }
            else{
                System.out.println("No Account Found");
            }
        }


        userInput.close();
        
    }

}

