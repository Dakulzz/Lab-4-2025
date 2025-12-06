// functions/ArrayTabulatedFunction.java
package functions;
import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {

    private static final double EPSILON = 1e-9;
    private FunctionPoint[] points;  // массив-буфер
    private int pointsCount;         // фактическое число точек

/*
    //конструктор без параметров для Externalizable
    public ArrayTabulatedFunction() {
        this.points = null;
        this.pointsCount = 0;
    }
        
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }
    
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }
*/
    //3.1  leftX-rightX + количество точек (y = 0)
    public ArrayTabulatedFunction(double leftX, double rightX, int count) {
        if(count<2){
			throw new IllegalArgumentException("Количество точек должно быть не менее 2");
		}
		if (leftX >= rightX) {
			throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        points=new FunctionPoint[count];
        pointsCount=count;

        double step=(rightX-leftX)/(count-1);
        for(int i=0;i<count;i++)
            points[i]=new FunctionPoint(leftX+i*step,0);
    }

    //3.2  leftX-rightX + массив значений функции
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if(values==null||values.length<2) {
            throw new IllegalArgumentException("Массив значений должен содержать не менее 2 элементов");
        }
		if (leftX>=rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        points=new FunctionPoint[values.length];
        pointsCount=values.length;

        double step=(rightX-leftX)/(values.length-1);
        for(int i=0;i<values.length;i++)
            points[i]=new FunctionPoint(leftX+i*step,values[i]);
    }

    // Конструктор из массива FunctionPoint
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Массив должен содержать не менее 2 элементов");
        }
        // Проверка упорядоченности по x
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию x");
            }
        }
        // Копирование с инкапсуляцией
        this.points = new FunctionPoint[points.length];
        this.pointsCount = points.length;
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    //4  (границы области + значение функции в x через линейную интерполяцию)
	@Override public double getLeftDomainBorder() {return points[0].getX();}
	@Override public double getRightDomainBorder() {return points[pointsCount-1].getX();}
	@Override public double getFunctionValue(double x) {

        if(x<getLeftDomainBorder()||x>getRightDomainBorder())
            return Double.NaN;

    // точное совпадение с узлом
	for (int i = 0; i < pointsCount; i++) {
		if (Math.abs(x - points[i].getX()) < EPSILON) {
			return points[i].getY();
		}
	}

        // поиск интервала [xi , xi+1]
        int i=0;
        while(i<pointsCount-2&&x>points[i+1].getX()) i++;

        double x1=points[i].getX(),y1=points[i].getY();
        double x2=points[i+1].getX(),y2=points[i+1].getY();

        // линейная интерполяция
        return y1+(y2-y1)*(x-x1)/(x2-x1);
    }

    //5  (работа с отдельными точками)
	@Override public int getPointsCount() {return pointsCount;}
	
	@Override public FunctionPoint getPoint(int index) {
        checkIndex(index);
        return new FunctionPoint(points[index]);
    }

    @Override public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        checkIndex(index);
        double newX=point.getX();
        if(index>0&&newX<=points[index-1].getX()){
            throw new InappropriateFunctionPointException("x должен быть больше x предыдущей точки");
        }
		if(index<pointsCount-1&&newX>=points[index+1].getX()){
            throw new InappropriateFunctionPointException("x должен быть меньше x следующей точки");
        }                                     	
        points[index]=new FunctionPoint(point); 
    }	

    @Override public double getPointX(int i) throws FunctionPointIndexOutOfBoundsException {
        checkIndex(i);
        return points[i].getX();
    }
    @Override public double getPointY(int i) throws FunctionPointIndexOutOfBoundsException {
        checkIndex(i);
        return points[i].getY();
    }

    @Override public void setPointX(int i, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        checkIndex(i);
        if(i>0&&x<=points[i-1].getX()){
            throw new InappropriateFunctionPointException("x должен быть не больше x следующей точки");
        }
		if(i<pointsCount-1&&x>=points[i+1].getX()) {
            throw new InappropriateFunctionPointException("x должен быть не меньше x следующей точки");
        }
        points[i].setX(x);
    }

    
    @Override public void setPointY(int i, double y) throws FunctionPointIndexOutOfBoundsException {
        checkIndex(i);
        points[i].setY(y);
    }
    //6  (изменение количества точек: удаление + добавление)
    @Override public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (pointsCount<=2) {
            throw new IllegalStateException("Нельзя удалить точку: минимальное количество точек - 2");
        }
        checkIndex(index);
		
        for (int i=index;i<pointsCount-1;i++) {
            points[i] = points[i + 1];
        }
        points[--pointsCount] = null;
    }

    @Override public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
            // проверка на совпадение x
            for (int i=0;i<pointsCount;i++) {
                if (Math.abs(point.getX() - points[i].getX()) < EPSILON) {
                    throw new InappropriateFunctionPointException("Точка с таким x уже существует");
                }
            }
            
            // расширение массива при необходимости
            if (pointsCount == points.length) {
                int newLen = points.length * 2;
                if (newLen == 0) newLen = 4;
                FunctionPoint[] tmp = new FunctionPoint[newLen];
                for (int i = 0; i < pointsCount; i++) {
                    tmp[i] = points[i];
                }
                points = tmp;
            }
            
            // поиск позиции вставки
            int pos = 0;
            while (pos < pointsCount && points[pos].getX() < point.getX()) {
                pos++;
            }
            
            // Сдвиг элементов
            for (int i = pointsCount; i > pos; i--) {
                points[i] = points[i - 1];
            }
            points[pos] = new FunctionPoint(point);
            pointsCount++;
        }
    //  ДОП
    private void checkIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне диапазона [0, " + (pointsCount - 1) + "]");
        }
    }
}
