package functions.meta;

import functions.Function;

// Класс функции, являющейся суммой двух других

public class Sum implements Function {
    
    private Function f1;
    private Function f2;
    
    public Sum(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }
    
    @Override
    public double getLeftDomainBorder() {
        // берем максимум левых границ(самое правое значение)
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }
    @Override
    public double getRightDomainBorder() {
        // берем минимум правых границ(самое левое значение)
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }
    @Override
    public double getFunctionValue(double x) {
        return f1.getFunctionValue(x) + f2.getFunctionValue(x);
    }
}