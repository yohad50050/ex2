import java.util.ArrayList;
//ssss

public class ThreadCheckArray implements Runnable {
    private boolean flag;
    private boolean[] winArray;
    private final SharedData sd;
    private ArrayList<Integer> array;
    private Integer b;

    public ThreadCheckArray(SharedData sd) {
        this.sd = sd;
        synchronized (sd) {
            // Convert int[] → ArrayList<Integer>
            int[] tempArray = sd.getArray();
            array = new ArrayList<Integer>();
            for (int value : tempArray) {
                array.add(value);
            }

            b = sd.getB();
        }
        winArray = new boolean[array.size()];
    }

    private void rec(int n, Integer b) {
        synchronized (sd) {
            if (sd.getFlag())
                return;
        }

        if (n == 1) {
            Integer last = array.get(n - 1);
            if (b == 0 || b.equals(last)) {
                flag = true;
                synchronized (sd) {
                    sd.setFlag(true);
                }
            }
            if (b.equals(last))
                winArray[n - 1] = true;
            return;
        }

        rec(n - 1, b - array.get(n - 1));
        if (flag)
            winArray[n - 1] = true;

        synchronized (sd) {
            if (sd.getFlag())
                return;
        }

        rec(n - 1, b);
    }

    @Override
    public void run() {
        int size = array.size();
        if (size != 1) {
            if (Thread.currentThread().getName().equals("thread1"))
                rec(size - 1, b - array.get(size - 1));
            else
                rec(size - 1, b);
        } else {
            if (b.equals(array.get(0)) && !flag) {
                winArray[0] = true;
                flag = true;
                synchronized (sd) {
                    sd.setFlag(true);
                }
            }
        }

        if (flag) {
            if (Thread.currentThread().getName().equals("thread1"))
                winArray[array.size() - 1] = true;

            synchronized (sd) {
                sd.setWinArray(winArray);
            }
        }
    }
}
