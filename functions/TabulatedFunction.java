package functions;

// Интерфейс TabulatedFunction, расширяющий базовый интерфейс Function

public interface TabulatedFunction extends Function {
    
    // getLeftDomainBorder, getRightDomainBorder, getFunctionValue от Function
    
    int getPointsCount();
    
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;
    
    void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;
    
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException;
}