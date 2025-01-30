import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortingVisualizer extends JPanel {
    private static final int NUM_BARS = 50;  // Number of bars
    private int[] array = new int[NUM_BARS]; // Array to be sorted
    private String selectedAlgorithm = "Bubble Sort"; // Default sorting algorithm
    private int sortingSpeed = 50; // Default speed (delay in ms)

    public SortingVisualizer() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.BLACK);
        generateRandomArray();

        // UI Setup
        JFrame frame = new JFrame("Sorting Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setBackground(Color.DARK_GRAY);

        // Algorithm Selector
        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Quick Sort", "Merge Sort"};
        JComboBox<String> algorithmBox = new JComboBox<>(algorithms);
        algorithmBox.addActionListener(e -> selectedAlgorithm = (String) algorithmBox.getSelectedItem());

        // Buttons
        JButton startButton = new JButton("Start");
        JButton resetButton = new JButton("Reset");

        startButton.addActionListener(e -> startSorting());
        resetButton.addActionListener(e -> generateRandomArray());

        // Speed Slider
        JSlider speedSlider = new JSlider(1, 200, sortingSpeed);
        speedSlider.addChangeListener(e -> sortingSpeed = speedSlider.getValue());
        JLabel speedLabel = new JLabel("Speed:");

        controls.add(new JLabel("Algorithm:"));
        controls.add(algorithmBox);
        controls.add(startButton);
        controls.add(resetButton);
        controls.add(speedLabel);
        controls.add(speedSlider);

        frame.add(controls, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    // Generates a new random array
    private void generateRandomArray() {
        Random rand = new Random();
        for (int i = 0; i < NUM_BARS; i++) {
            array[i] = rand.nextInt(200) + 50; // Random height between 50-350


        }
        repaint();
    }

    // Starts sorting based on selected algorithm
    private void startSorting() {
        new Thread(() -> {
            switch (selectedAlgorithm) {
                case "Bubble Sort": bubbleSort(); break;
                case "Selection Sort": selectionSort(); break;
                case "Insertion Sort": insertionSort(); break;
                case "Quick Sort": quickSort(0, array.length - 1); repaint(); break;
                case "Merge Sort": mergeSort(0, array.length - 1); repaint(); break;
            }
        }).start();
    }

    // Bubble Sort Algorithm
    private void bubbleSort() {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    swap(j, j + 1);
                    repaint();
                    sleep();
                }
            }
        }
    }

    // Selection Sort Algorithm
    private void selectionSort() {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            swap(i, minIndex);
            repaint();
            sleep();
        }
    }

    // Insertion Sort Algorithm
    private void insertionSort() {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
                repaint();
                sleep();
            }
            array[j + 1] = key;
            repaint();
            sleep();
        }
    }

    // Quick Sort Algorithm
    private void quickSort(int low, int high) {
        if (low < high) {
            int pivotIndex = partition(low, high);
            quickSort(low, pivotIndex - 1);
            quickSort(pivotIndex + 1, high);
            repaint();
            sleep();
        }
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                swap(i, j);
                repaint();
                sleep();
            }
        }
        swap(i + 1, high);
        repaint();
        sleep();
        return i + 1;
    }

    // Merge Sort Algorithm
    private void mergeSort(int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(left, mid);
            mergeSort(mid + 1, right);
            merge(left, mid, right);
            repaint();
            sleep();
        }
    }

    private void merge(int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i++];
            } else {
                array[k] = rightArray[j++];
            }
            repaint();
            sleep();
            k++;
        }

        while (i < n1) {
            array[k++] = leftArray[i++];
            repaint();
            sleep();
        }

        while (j < n2) {
            array[k++] = rightArray[j++];
            repaint();
            sleep();
        }
    }

    // Swap two elements in the array
    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Sleep to slow down sorting for visualization
    private void sleep() {
        try {
            Thread.sleep(sortingSpeed); // Adjust speed dynamically
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Paint method to draw bars and display numbers on top of them
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() / NUM_BARS;
        for (int i = 0; i < NUM_BARS; i++) {
            // Draw bars
            g.setColor(Color.CYAN);
            g.fillRect(i * width, getHeight() - array[i], width - 2, array[i]);

            // Draw numbers above each bar
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(array[i]), i * width + width / 4, getHeight() - array[i] - 5);
        }
    }

    // Main method to launch GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortingVisualizer::new);
    }
}
