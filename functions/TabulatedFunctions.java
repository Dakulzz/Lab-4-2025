package functions;

import java.io.*;

// класс со статическими методами для работы с TabulatedFunctions
public final class TabulatedFunctions {
    
    // запрет создания объектов класса извне
    private TabulatedFunctions() {
        throw new UnsupportedOperationException("Нельзя создать экземпляр класса TabulatedFunctions");
    }
    
    // табулирует функцию на заданном отрезке с заданным количеством точек
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                "Указанные границы [" + leftX + ", " + rightX + "] выходят за область определения функции [" 
                + function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]"
            );
        }
        
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            values[i] = function.getFunctionValue(leftX + i * step);
        }
        
        // возвращает ArrayTabulatedFunction 
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }
    
    // выводит TabulatedFunction в байтовый поток

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        
        int count = function.getPointsCount();
        dataOut.writeInt(count);
        
        for (int i = 0; i < count; i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        
        dataOut.flush();
        /* 
        поток НЕ закрывает - это задача вызывающего кода,
        который открыл поток и знает, нужно ли его дальше применять
        */
    }
    
    // читает TabulatedFunction из байтового потока

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        
        int count = dataIn.readInt();
        
        if (count < 2) {
            throw new IOException("Некорректное количество точек: " + count);
        }
        
        FunctionPoint[] points = new FunctionPoint[count];
        
        for (int i = 0; i < count; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
 
        // поток НЕ закрывает
        return new ArrayTabulatedFunction(points);
    }
    
    // Записывает TabulatedFunction в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        
        int count = function.getPointsCount();
        writer.print(count);
        
        for (int i = 0; i < count; i++) {
            writer.print(" " + function.getPointX(i));
            writer.print(" " + function.getPointY(i));
        }
        
        writer.flush();
        // поток НЕ закрывает
    }
    
    // читает TabulatedFunction из символьного потока

        public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        
        // Читаем количество точек
        tokenizer.nextToken();
        int count = (int) tokenizer.nval;
        
        FunctionPoint[] points = new FunctionPoint[count];
        
        for (int i = 0; i < count; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            
            tokenizer.nextToken();
            double y = tokenizer.nval;
            
            points[i] = new FunctionPoint(x, y);
        }
        
        // Поток НЕ закрываем
        return new ArrayTabulatedFunction(points);
    }
}