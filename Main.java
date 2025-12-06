import functions.*;
import functions.basic.*;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        println("=== ТЕСТИРОВАНИЕ ===\n");
        
        testSinCos();
        testTabulatedSinCos();
        testSumOfSquares();
        testWriteReadExp();
        testOutputInputLog();
        testSerializable();
        testExternalizable();
    }
    
    // Тест 1: Sin и Cos на [0, π] с шагом 0.1

    private static void testSinCos() {
        println("--- Тест 1: Sin и Cos");
        
        Function sin = new Sin();
        Function cos = new Cos();
        
        println("x\t\tsin(x)\t\tcos(x)");
        println("-".repeat(50));
        
        for (double x = 0; x <= Math.PI + 0.001; x += 0.1) {
            System.out.printf("%.1f\t\t%.6f\t%.6f%n", 
                x, sin.getFunctionValue(x), cos.getFunctionValue(x));
        }
        println();
    }
    
    // Тест 2: Табулированные Sin и Cos

    private static void testTabulatedSinCos() {
        println("--- Тест 2: Табулированные Sin и Cos");
        
        Function sin = new Sin();
        Function cos = new Cos();
        
        TabulatedFunction tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);
        
        println("Сравнение оригинальных и табулированных функций:");
        println("x\t\tsin(x)\t\ttabSin(x)\tcos(x)\t\ttabCos(x)");
        println("-".repeat(80));
        
        for (double x = 0; x <= Math.PI + 0.001; x += 0.1) {
            System.out.printf("%.1f\t\t%.6f\t%.6f\t%.6f\t%.6f%n",
                x, 
                sin.getFunctionValue(x), tabSin.getFunctionValue(x),
                cos.getFunctionValue(x), tabCos.getFunctionValue(x));
        }
        println();
    }
    
    // Тест 3: Сумма квадратов sin^2(x) + cos^2(x) = 1

    private static void testSumOfSquares() {
        println("--- Тест 3: sin^2(x) + cos^2(x) с разным числом точек");
        
        int[] pointsCounts = {5, 50, 100};
        
        for (int points : pointsCounts) {
            println("\nКоличество точек: " + points);
            
            Function sin = new Sin();
            Function cos = new Cos();
            
            TabulatedFunction tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, points);
            TabulatedFunction tabCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, points);
            
            // sin^2(x) + cos^2(x)
            Function sin2 = Functions.power(tabSin, 2);
            Function cos2 = Functions.power(tabCos, 2);
            Function sum = Functions.sum(sin2, cos2);
            
            println("x\t\tsin^2+cos^2\tОтклонение от 1");
            
            double maxError = 0;
            for (double x = 0; x <= Math.PI + 0.001; x += 0.1) {
                double value = sum.getFunctionValue(x);
                double error = Math.abs(value - 1.0);
                maxError = Math.max(maxError, error);
                System.out.printf("%.1f\t\t%.6f\t%.6f%n", x, value, error);
            }
            System.out.printf("Максимальная ошибка: %.6f%n", maxError);
        }
        println();
    }
    
    // Тест 4: Запись/чтение экспоненты (символьный поток)
    private static void testWriteReadExp() {
        println("--- Тест 4: Write/Read экспоненты");
        
        String filename = "exp_tabulated.txt";
        
        try {
            // Создаем табулированную экспоненту
            Function exp = new Exp();
            TabulatedFunction tabExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);
            
            // Записываем в файл
            try (FileWriter writer = new FileWriter(filename)) {
                TabulatedFunctions.writeTabulatedFunction(tabExp, writer);
            }
            println("Функция записана в файл: " + filename);
            
            // Читаем из файла
            TabulatedFunction readExp;
            try (FileReader reader = new FileReader(filename)) {
                readExp = TabulatedFunctions.readTabulatedFunction(reader);
            }
            println("Функция прочитана из файла");
            
            // Сравниваем
            println("\nСравнение исходной и считанной функции:");
            println("x\tИсходная\tСчитанная\tРазница");
            
            for (int x = 0; x <= 10; x++) {
                double orig = tabExp.getFunctionValue(x);
                double read = readExp.getFunctionValue(x);
                System.out.printf("%d\t%.6f\t%.6f\t%.9f%n", x, orig, read, Math.abs(orig - read));
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
        println();
    }
    
    // Тест 5: Output/Input логарифма (байтовый поток)

    private static void testOutputInputLog() {
        println("--- Тест 5: Output/Input логарифма");
        
        String filename = "log_tabulated.bin";
        
        try {
            // Создаем табулированный натуральный логарифм
            Function log = new Log(Math.E); // ln(x)
            // Логарифм определен на (0, +∞), начинаем с небольшого положительного числа
            TabulatedFunction tabLog = TabulatedFunctions.tabulate(log, 1, 10, 10);
            
            // Записываем в файл
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                TabulatedFunctions.outputTabulatedFunction(tabLog, fos);
            }
            println("Функция записана в файл: " + filename);
            
            // Читаем из файла
            TabulatedFunction readLog;
            try (FileInputStream fis = new FileInputStream(filename)) {
                readLog = TabulatedFunctions.inputTabulatedFunction(fis);
            }
            println("Функция прочитана из файла");
            
            // Сравниваем
            println("\nСравнение исходной и считанной функции:");
            println("x\tИсходная\tСчитанная\tРазница");
            
            for (int x = 1; x <= 10; x++) {
                double orig = tabLog.getFunctionValue(x);
                double read = readLog.getFunctionValue(x);
                System.out.printf("%d\t%.6f\t%.6f\t%.9f%n", x, orig, read, Math.abs(orig - read));
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
        println();
    }
    
    // Тест 6: Serializable (ArrayTabulatedFunction)
    private static void testSerializable() {
        System.out.println("--- Тест 6: Serializable (Array) для ln(e^x) = x");
        
        String filename = "array_serializable.ser";
        
        try {
            Function composition = Functions.composition(new Log(Math.E), new Exp());
            TabulatedFunction original = TabulatedFunctions.tabulate(composition, 0, 10, 11);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(original);
            }
            
            TabulatedFunction restored;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                restored = (TabulatedFunction) ois.readObject();
            }
            
            System.out.println("x\tИсходная\tСчитанная");
            for (int x = 0; x <= 10; x++) {
                System.out.printf("%d\t%.6f\t%.6f%n", x, original.getFunctionValue(x), restored.getFunctionValue(x), x);
            }
            
            System.out.println("Размер файла: " + new File(filename).length() + " байт\n");
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    // Тест 7: Externalizable (LinkedListTabulatedFunction)
    private static void testExternalizable() {
        System.out.println("--- Тест 7: Externalizable (LinkedList) для ln(e^x) = x");
        
        String filename = "linked_externalizable.ser";
        
        try {
            Function composition = Functions.composition(new Log(Math.E), new Exp());
            
            // Externalizable LinkedList с теми же значениями
            double[] values = new double[11];
            for (int i = 0; i <= 10; i++) {
                values[i] = composition.getFunctionValue(i);
            }
            LinkedListTabulatedFunction original = new LinkedListTabulatedFunction(0, 10, values);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(original);
            }
            
            LinkedListTabulatedFunction restored;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                restored = (LinkedListTabulatedFunction) ois.readObject();
            }
            
            System.out.println("x\tИсходная\tСчитанная");
            for (int x = 0; x <= 10; x++) {
                System.out.printf("%d\t%.6f\t%.6f%n", x, original.getFunctionValue(x), restored.getFunctionValue(x), x);
            }
            
            println("Размер файла: " + new File(filename).length() + " байт\n");
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    public static void println(Object obj) {
    System.out.println(obj);
    }
    
    public static void println() {
    System.out.println();
    }
}