import java.util.InputMismatchException;
import java.util.Scanner;
public class unitConversion{

    public static String toBinary(String num, int base){
        if(!isValidNumberForBase(num, base)){
            return "Number "+num+" is not valid for base "+base;
        }
        if(base == 2){
            return num;
        }

        if(num.length() == 1){
            return sub10ToBinary(num);
        }

        else if(base == 8){
            StringBuilder res = new StringBuilder();
            for (char c : num.toCharArray()) {
                switch (c) {
                    case '0' -> res.append("000");
                    case '1' -> res.append("001");
                    case '2' -> res.append("010");
                    case '3' -> res.append("011");
                    case '4' -> res.append("100");
                    case '5' -> res.append("101");
                    case '6' -> res.append("110");
                    case '7' -> res.append("111");
                    default ->{
                        System.out.println("error in toOctal switch logic");
                    }
                }
            }
            return res.toString().replaceFirst("^0+", "");
        } 

        else if(base == 10){

            StringBuilder res = new StringBuilder();
            int intvalue = Integer.parseInt(num);

            while(intvalue >= 2){ // calculates remainder then inserts it at index zero (beginning of binary string)
                int r = intvalue % 2;
                intvalue /= 2;
                res.insert(0, Integer.toString(r));
            }

            if(intvalue != 0){
                res.insert(0, intvalue);
            }

            return res.toString();
        }

        else if(base == 16){
            StringBuilder res = new StringBuilder();

            for(char c : num.toCharArray()){
                switch (c) {
                    case 'A' -> res.append("1010");
                    case 'B' -> res.append("1011");
                    case 'C' -> res.append("1100");
                    case 'D' -> res.append("1101");
                    case 'E' -> res.append("1110");
                    case 'F' -> res.append("1111");
                    default -> {
                        // Handle decimal digits (0-9)
                        String binary = sub10ToBinary(Character.toString(c));
                        res.append(String.format("%4s", binary).replace(' ', '0')); // Ensures the binary is always 4 bits long
                    }
                }
            }
            return res.toString().replaceFirst("^0+", "");
        }
        return "Error in Binary convertion";// shouldn't reach here
    }

    public static String toDecimal(String num, int base){
        if(!isValidNumberForBase(num, base)){
            return "Number "+num+" is not valid for base "+base;
        }

        return switch(base){
            case 10 -> num;
            case 2 -> toDecimalCalc(num, base);
            case 8 -> toDecimalCalc(num, base);
            case 16 -> toDecimal(toBinary(num, base), 2);
            default -> "error";// shouldn't reach here
        };
    }
    
    public static String toOctal(String num, int base){
        if(!isValidNumberForBase(num, base)){
            return "Number "+num+" is not valid for base "+base;
        }
        if(base == 8){
            return num;
        }
        String binary = binaryPadding(num, base, 8);//returns binary string with length cleanly divisable by 3
        StringBuilder res = new StringBuilder();
        int n = 3;
        for(int i = 0; i < binary.length(); i += n){
            String subString = binary.substring(i, Math.min(i + n, binary.length())); // handles index out of bounds exceptions
            switch(subString){
                case "000"-> res.append("0"); 
                case "001"-> res.append("1"); 
                case "010"-> res.append("2"); 
                case "011"-> res.append("3"); 
                case "100"-> res.append("4"); 
                case "101"-> res.append("5"); 
                case "110"-> res.append("6"); 
                case "111"-> res.append("7");
                default ->{
                    System.out.println("error in toOctal binary switch logic");
                } 
            }
        }
        return res.toString().replaceFirst("^0+", "");
    }
    
    public static String toHex(String num, int base){
        if(!isValidNumberForBase(num, base)){
            return "Number "+num+" is not valid for base "+base;
        }
        if(base == 16){
            return num;
        }
        String binary = binaryPadding(num, base, 16);//returns binary string with length cleanly divisable by 4
        StringBuilder res = new StringBuilder();
        int n = 4;
        for(int i = 0; i < binary.length(); i += n){
            String subString = binary.substring(i, Math.min(i + n, binary.length())); // handles index out of bounds exceptions
            switch(subString){
                case "0000"-> res.append("0"); 
                case "0001"-> res.append("1"); 
                case "0010"-> res.append("2"); 
                case "0011"-> res.append("3"); 
                case "0100"-> res.append("4"); 
                case "0101"-> res.append("5"); 
                case "0110"-> res.append("6"); 
                case "0111"-> res.append("7"); 
                case "1000"-> res.append("8");
                case "1001"-> res.append("9");
                case "1010"-> res.append("A");
                case "1011"-> res.append("B");
                case "1100"-> res.append("C");
                case "1101"-> res.append("D");
                case "1110"-> res.append("E");
                case "1111"-> res.append("F");
                default ->{
                    System.out.println("error in toHex binary switch logic");
                } 
            }
        }
        return res.toString().replaceFirst("^0+", "");
    }

    // helper methods below

    private static String binaryPadding(String num, int base, int convBase){// used in toOctal and toHex
        int padding = (convBase == 16) ? 4 : 3;
        String binary = toBinary(num, base);
        
        // adds needed amount of zeros to the front of the binary number
        // making the length of the binary number evenly divisable by 3 or 4 depending on convBase
        int totalLength = (padding - (binary.length() % padding)) + binary.length();
        return String.format("%"+totalLength+"s", binary).replace(' ', '0');
    }
    private static String toDecimalCalc(String num, int base){// used in toDecimal()
        int exp = num.length() - 1;
        int res = 0;

        for(char c : num.toCharArray()){// res += num * base ^ exp 
            res += Integer.parseInt(Character.toString(c)) * Math.pow(base, exp--);
        }

        return Integer.toString(res);
    }

    private static String sub10ToBinary(String num){// used in toBinary()
        return switch (num) {
            case "0" -> "0";
            case "1" -> "1";
            case "2" -> "10";
            case "3" -> "11";
            case "4" -> "100";
            case "5" -> "101";
            case "6" -> "110";
            case "7" -> "111";
            case "8" -> "1000";
            case "9" -> "1001";
            default -> "error in binary switch logic";
        };
    }

    private static boolean isValidNumberForBase(String num, int base) {
        if (!(base == 2 ||base == 8 ||base == 10 ||base == 16)){
            return false;
        }
        for (char c : num.toCharArray()) {
            if (base == 8 && !isValidOctalDigit(c)) {
                return false;
            } else if (base == 10 && !Character.isDigit(c)) {
                return false;
            } else if (base == 16 && !isValidHexDigit(c)) {
                return false;
            }
            else if(base == 2 && !isValidBinary(c)){
                return false;
            }
        }
        return true;
    }
    private static boolean isValidOctalDigit(char c) {
        return c >= '0' && c <= '7';
    }
    private static boolean isValidHexDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F');
    }
    private static boolean isValidBinary(char c){
        return (c == '1' || c == '0');
    }
    public static void main(String[] args) {

        Scanner scnr = new Scanner(System.in);
        int base = 0;
        String num;
        int conv = 0;

        // Loop until a valid base is entered
        while (true) {
            System.out.println("Enter a base (2, 8, 10, 16):");
            try {
                base = scnr.nextInt();
                if (base == 2 || base == 8 || base == 10 || base == 16) {
                    break; // Valid base
                } 
                else {
                    System.out.println("Invalid base. Please enter 2, 8, 10, or 16.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for the base.");
                scnr.next(); 
            }
        }

        System.out.println("Enter a number:");
        num = scnr.next();

        // Loop until a valid conversion option is entered
        while (true) {
            System.out.println("Convert to:\n1. Binary\n2. Decimal\n3. Octal\n4. Hexadecimal");
            try {
                conv = scnr.nextInt();
                if (conv >= 1 && conv <= 4) {
                    break; // Valid conversion option
                }
                else {
                    System.out.println("Invalid option. Please choose between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for the conversion option.");
                scnr.next();
            }
        }
        scnr.close();

        switch (conv) {
            case 1 -> System.out.println("Binary: " + toBinary(num, base));
            case 2 -> System.out.println("Decimal: " + toDecimal(num, base));
            case 3 -> System.out.println("Octal: " + toOctal(num, base));
            case 4 -> System.out.println("Hexadecimal: " + toHex(num, base));
        }
    }
}