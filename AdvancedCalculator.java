import java.util.Scanner;

public class AdvancedCalculator {

    // Basic Arithmetic Methods
    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static double divide(double a, double b) {
        if (b == 0) {
            System.out.println("Error: Division by zero is not allowed.");
            return 0;
        }
        return a / b;
    }

    // Scientific Calculations
    public static double squareRoot(double num) {
        if (num < 0) {
            System.out.println("Error: Cannot find square root of negative number.");
            return 0;
        }
        return Math.sqrt(num);
    }

    public static double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    // Temperature Conversion
    public static double celsiusToFahrenheit(double c) {
        return (c * 9 / 5) + 32;
    }

    public static double fahrenheitToCelsius(double f) {
        return (f - 32) * 5 / 9;
    }

    // Currency Conversion (Example rate)
    public static double inrToUsd(double inr) {
        double rate = 0.012; // Example rate
        return inr * rate;
    }

    public static double usdToInr(double usd) {
        double rate = 83.0; // Example rate
        return usd * rate;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ADVANCED CALCULATOR =====");
            System.out.println("1. Addition");
            System.out.println("2. Subtraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Division");
            System.out.println("5. Square Root");
            System.out.println("6. Power");
            System.out.println("7. Temperature Conversion");
            System.out.println("8. Currency Conversion");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            double a, b;

            switch (choice) {

                case 1:
                    System.out.print("Enter first number: ");
                    a = sc.nextDouble();
                    System.out.print("Enter second number: ");
                    b = sc.nextDouble();
                    System.out.println("Result: " + add(a, b));
                    break;

                case 2:
                    System.out.print("Enter first number: ");
                    a = sc.nextDouble();
                    System.out.print("Enter second number: ");
                    b = sc.nextDouble();
                    System.out.println("Result: " + subtract(a, b));
                    break;

                case 3:
                    System.out.print("Enter first number: ");
                    a = sc.nextDouble();
                    System.out.print("Enter second number: ");
                    b = sc.nextDouble();
                    System.out.println("Result: " + multiply(a, b));
                    break;

                case 4:
                    System.out.print("Enter first number: ");
                    a = sc.nextDouble();
                    System.out.print("Enter second number: ");
                    b = sc.nextDouble();
                    System.out.println("Result: " + divide(a, b));
                    break;

                case 5:
                    System.out.print("Enter number: ");
                    a = sc.nextDouble();
                    System.out.println("Square Root: " + squareRoot(a));
                    break;

                case 6:
                    System.out.print("Enter base: ");
                    a = sc.nextDouble();
                    System.out.print("Enter exponent: ");
                    b = sc.nextDouble();
                    System.out.println("Result: " + power(a, b));
                    break;

                case 7:
                    System.out.println("1. Celsius to Fahrenheit");
                    System.out.println("2. Fahrenheit to Celsius");
                    int tempChoice = sc.nextInt();

                    if (tempChoice == 1) {
                        System.out.print("Enter Celsius: ");
                        double c = sc.nextDouble();
                        System.out.println("Fahrenheit: " + celsiusToFahrenheit(c));
                    } else {
                        System.out.print("Enter Fahrenheit: ");
                        double f = sc.nextDouble();
                        System.out.println("Celsius: " + fahrenheitToCelsius(f));
                    }
                    break;

                case 8:
                    System.out.println("1. INR to USD");
                    System.out.println("2. USD to INR");
                    int currChoice = sc.nextInt();

                    if (currChoice == 1) {
                        System.out.print("Enter INR: ");
                        double inr = sc.nextDouble();
                        System.out.println("USD: " + inrToUsd(inr));
                    } else {
                        System.out.print("Enter USD: ");
                        double usd = sc.nextDouble();
                        System.out.println("INR: " + usdToInr(usd));
                    }
                    break;

                case 9:
                    System.out.println("Calculator Closed.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}