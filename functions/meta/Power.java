package functions.meta;

import functions.Function;

// Класс функции, являющейся степенью другой (Function.point(n)^power)

public class Power implements Function {
    
    private Function base;
    private double power;
    
    public Power(Function base, double power) {
        this.base = base;
        this.power = power;
    }
    // ОО та же, что и у функции
    @Override
    public double getLeftDomainBorder() {
        return base.getLeftDomainBorder();
    }
    @Override
    public double getRightDomainBorder() {
        return base.getRightDomainBorder();
    }
    @Override
    public double getFunctionValue(double x) {
        return Math.pow(base.getFunctionValue(x), power);
    }
}