package functions.meta;

import functions.Function;

// Класс функции, являющейся композицией двух функций
// f(g(x))

public class Composition implements Function {
    
    private Function outer; // внешняя функция f
    private Function inner; // внутренняя функция g
    
    public Composition(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }
    
    @Override
    public double getLeftDomainBorder() {
        return inner.getLeftDomainBorder();
    }
    
    @Override
    public double getRightDomainBorder() {
        return inner.getRightDomainBorder();
    }
    
    @Override
    public double getFunctionValue(double x) {
        return outer.getFunctionValue(inner.getFunctionValue(x));
    }
}