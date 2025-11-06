import java.util.Scanner;

public class TestThreadCheckArray {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Thread thread1, thread2;

            System.out.println("Enter array size:");
            int num = input.nextInt();
            int[] array = new int[num];

            System.out.println("Enter numbers for array:");
            for (int index = 0; index < num; index++) {
                array[index] = input.nextInt();
            }

            System.out.println("Enter target number (b):");
            int b = input.nextInt();

            // Shared data object
            SharedData sd = new SharedData(array, b);

            // Create and start threads
            thread1 = new Thread(new ThreadCheckArray(sd), "thread1");
            thread2 = new Thread(new ThreadCheckArray(sd), "thread2");
            thread1.start();
            thread2.start();

            // Wait for both threads to finish
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Print results
            if (!sd.getFlag()) {
                System.out.println("Sorry, no subset found.");
                return;
            }

            System.out.println("Solution for b = " + sd.getB() + ", n = " + sd.getArray().length);

            // Print indexes
            System.out.print("I:    ");
            for (int index = 0; index < sd.getArray().length; index++) {
                System.out.print(index + "    ");
            }
            System.out.println();

            // Print array values
            System.out.print("A:    ");
            for (int value : sd.getArray()) {
                System.out.print(value);
                int counter = 5;
                int temp = value;
                while (true) {
                    temp = temp / 10;
                    counter--;
                    if (temp == 0)
                        break;
                }
                for (int i = 0; i < counter; i++)
                    System.out.print(" ");
            }

            System.out.println();

            // Print win array (C)
            System.out.print("C:    ");
            for (boolean w : sd.getWinArray()) {
                if (w)
                    System.out.print("1    ");
                else
                    System.out.print("0    ");
            }
        }
    }
}
