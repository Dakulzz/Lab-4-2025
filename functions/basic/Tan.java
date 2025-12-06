package functions.basic;

//Класс функции тангенса

public class Tan extends TrigonometricFunction {
    
    @Override
    public double getFunctionValue(double x) {
        return Math.tan(x);
    }
}