package utils;

public class MathUtils {

    public static float taxCalculator(float numberRange, float balance){
        float tax = 0;
        if(numberRange == 0 )
            tax = 0;

        if(numberRange == 1)
            tax = (balance) * Float.valueOf("0.10");

        if(numberRange == 2)
            tax = (balance) * Float.valueOf("0.11");

        if(numberRange == 3)
            tax = (balance) * Float.valueOf("0.12");

        if(numberRange >= 4)
            tax = (balance) * Float.valueOf("0.15");

    return tax;
    }

}
