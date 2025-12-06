package functions.basic;

import functions.Function;


// Класс логарифмической функции с заданным основанием

public class Log implements Function {
    
    private double base;
    // основание логарифма (должно быть > 0 и != 1)
    public Log(double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть положительным и не равным 1");
        }
        this.base = base;
    }
    @Override
    public double getLeftDomainBorder() {
        return 0; // определен на (0, +infinity)
    }
    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    @Override
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        // log_base(x) = ln(x) / ln(base)
        return Math.log(x) / Math.log(base);
    }
}
