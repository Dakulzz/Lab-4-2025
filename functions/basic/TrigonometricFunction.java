package functions.basic;

import functions.Function;

// Базовый класс для тригонометрических функций

public abstract class TrigonometricFunction implements Function {
/*
 Имело бы смысл сделать что-то вроде RealDomainFunction 
 с границами ОО(+-беск), а уже от него TrigonometricFunction
 наследующий RealDomainFunction с тригонометрическими особенностями
*/ 

@Override
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }
    
    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}