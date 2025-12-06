package functions.basic;

// Класс функции синуса

public class Sin extends TrigonometricFunction {
    
    @Override
    public double getFunctionValue(double x) {
        return Math.sin(x);
    }
}