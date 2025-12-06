package functions;

// Интерфейс, описывающий функции одной переменной
public interface Function {
    double getLeftDomainBorder();
    double getRightDomainBorder();
    double getFunctionValue(double x);
}