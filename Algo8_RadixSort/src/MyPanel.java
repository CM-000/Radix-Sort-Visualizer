import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyPanel extends JPanel {

    ArrayList<Integer> intList = new ArrayList<Integer>();
    Random random = new Random();
    int rand;
    int temp;
    boolean sorted = false;
    private int linesDrawn = 0;
    int size = 300;
    private long startTime;
    private long elapsedTime;
    private double elapsedSeconds;
    int opCount;


    public MyPanel() {
    	
        this.setPreferredSize(new Dimension(size, size));

        for (int i = 0; i < size; i++) {
            rand = random.nextInt(0, size);
            intList.add(rand);
        }
        
        startTime = System.currentTimeMillis(); // Record the start time

        // Start a thread to sort the list after a short delay
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Delay for 500 milliseconds (adjust as needed)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            radixSort(intList);
            sorted = true;
            repaint(); // Trigger repaint after sorting
            
            long endTime = System.currentTimeMillis(); // Record the end time
            elapsedTime = endTime - startTime;
            elapsedSeconds = (double) elapsedTime / 1000.0;
            System.out.println("Sorting completed in " + elapsedSeconds + " seconds");
            System.out.println(opCount + " operations");
            
        }).start();
        
    }

    private void radixSort(ArrayList<Integer> list) {
        int maxVal = list.stream().max(Integer::compare).orElse(0);

        for (int exp = 1; maxVal / exp > 0; exp *= 10) {
            countingSortByDigit(list, exp);
        }
    }

    private void countingSortByDigit(ArrayList<Integer> list, int exp) {
        int[] output = new int[list.size()];
        int[] count = new int[10];

        // Count occurrences of each digit
        for (int num : list) {
            int digit = (num / exp) % 10;
            count[digit]++;
        }

        // Update the count array to have cumulative counts
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Build the output array
        for (int i = list.size() - 1; i >= 0; i--) {
            int num = list.get(i);
            int digit = (num / exp) % 10;
            output[count[digit] - 1] = num;
            count[digit]--;
        }

        // Copy the output array back to the original list
        for (int i = 0; i < list.size(); i++) {
            list.set(i, output[i]);

            // Pause to show the swap
            repaint(); // Trigger repaint to update the display
            try {
                Thread.sleep(4); // Delay for 1 millisecond (adjust as needed)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.opCount++; // Increment the counter for each operation
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(Color.BLACK); // Fill the panel with black
        g2D.fillRect(0, 0, getWidth(), getHeight());
        
        g2D.setStroke(new BasicStroke(1));
        
        g2D.setColor(Color.YELLOW);
        if(!sorted) {
        	g2D.drawString("Time: (running)", 10, 20);
        } else {
            g2D.drawString("Time: " + elapsedSeconds + " s", 10, 20);
        }
        g2D.drawString("Operations: " + opCount, 10, 40);

        if (!sorted) {
            g2D.setColor(Color.WHITE);
            // Draw the unsorted white lines
            for (int i = 0; i < intList.size(); i++) {
                g2D.drawLine(i, getHeight(), i, intList.size() - intList.get(i));
            }
        } else {
            g2D.setColor(Color.WHITE);
            // Draw the sorted white lines
            for (int i = 0; i < intList.size(); i++) {
                g2D.drawLine(i, getHeight(), i, intList.size() - intList.get(i));
            }

            g2D.setColor(Color.RED);
            // Draw the sorted red lines one by one, creating a growing effect
            for (int i = 0; i < linesDrawn; i++) {
                g2D.drawLine(i, getHeight() - intList.get(i), i, getHeight());
            }
            
            
            if (linesDrawn < intList.size()) {
                linesDrawn++; // Increase the number of lines drawn
                repaint(); // Trigger repaint to continue the animation
            }
        }
        
    }
}
