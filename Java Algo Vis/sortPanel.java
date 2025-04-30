import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class sortPanel extends JPanel{
    private int[] arr;
    private Color[] barColors;
    private int currIndex = -1;
    private Thread currentSortThread = null;
    private int sleepTime;

    public sortPanel(int size, int speed){
        this.sleepTime = speed;
        this.arr = new int[size];
        this.barColors = new Color[size];
        generateRandomArray(size);
        setBackground(Color.BLACK);
    }

    public void setSleepTime(int speed){
        this.sleepTime = speed;
    }

    public void generateRandomArray(int size){
        Random rand = new Random();
        for(int i = 0; i < size - 1; i++){
            this.arr[i] = rand.nextInt(470) + 10;
            this.barColors[i] = Color.WHITE;
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBars(g);
    }

    public void drawBars(Graphics g){
        int width = getWidth();
        int height = getHeight();
        for(int i = 0; i < arr.length - 1; i++){
            if(i == currIndex){
                g.setColor(Color.RED);
            }else{
                g.setColor(barColors[i]);
            }
            int barHeight = arr[i];
            int x = (int)((i * (double)width) / arr.length);
            int nextX = (int)(((i + 1) * (double)width) / arr.length);
            int actualBarWidth = nextX - x;
            g.fillRect(x, height - barHeight, actualBarWidth, barHeight);
        }
    }

    private void startSort(Runnable sortLogic){
        if(currentSortThread != null && currentSortThread.isAlive()){
            currentSortThread.interrupt();
        }
        currentSortThread = new Thread(sortLogic);
        currentSortThread.start();
    }

    private void highlightSorted(){
        new Thread(() -> {
            try{
                for(int i = 0; i < arr.length; i++){
                    barColors[i] = Color.GREEN;
                    currIndex = i;
                    repaint();
                    Thread.sleep(sleepTime);
                }
                currIndex = -1;
                repaint();
            }catch(InterruptedException e){}
        }).start();
    }

    public void selectionSort(){
        startSort(() -> {
            try{
                for(int i = 0; i < arr.length - 1; i++){
                    int minIndex = i;
                    for(int j = i + 1; j < arr.length; j++){
                        if(Thread.interrupted()) return;
                        currIndex = j;
                        repaint();
                        Thread.sleep(sleepTime);
                        if(arr[j] < arr[minIndex]){
                            minIndex = j;
                        }
                    }
                    if(minIndex != i){
                        int temp = arr[i];
                        arr[i] = arr[minIndex];
                        arr[minIndex] = temp;
                    }
                }
                currIndex = -1;
                repaint();
                highlightSorted();
            }catch(InterruptedException e){}
        });
    }

    public void bubbleSort(){
        startSort(() -> {
            try{
                for(int i = 0; i < arr.length - 1; i++){
                    for(int j = 0; j < arr.length - 1; j++){
                        if(Thread.interrupted()) return;
                        currIndex = j;
                        repaint();
                        Thread.sleep(sleepTime);
                        if(arr[j] > arr[j + 1]){
                            int temp = arr[j];
                            arr[j] = arr[j + 1];
                            arr[j + 1] = temp;
                        }
                    }
                }
                currIndex = -1;
                repaint();
                highlightSorted();
            }catch(InterruptedException e){}
        });
    }

    public void mergeSort(){
        startSort(() -> {
            try{
                mergeSortHelper(0, arr.length - 1);
                currIndex = -1;
                repaint();
                highlightSorted();
            }catch(InterruptedException e){}
        });
    }

    private void mergeSortHelper(int left, int right) throws InterruptedException{
        if(Thread.interrupted()) throw new InterruptedException();
        if(left < right){
            int mid = (left + right) / 2;
            mergeSortHelper(left, mid);
            mergeSortHelper(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) throws InterruptedException{
        if(Thread.interrupted()) throw new InterruptedException();
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while(i <= mid && j <= right){
            if(Thread.interrupted()) throw new InterruptedException();
            currIndex = i;
            repaint();
            Thread.sleep(sleepTime);
            if(arr[i] <= arr[j]){
                temp[k++] = arr[i++];
            }else{
                temp[k++] = arr[j++];
            }
        }
        while(i <= mid){
            if(Thread.interrupted()) throw new InterruptedException();
            currIndex = i;
            repaint();
            Thread.sleep(sleepTime);
            temp[k++] = arr[i++];
        }
        while(j <= right){
            if(Thread.interrupted()) throw new InterruptedException();
            currIndex = j;
            repaint();
            Thread.sleep(sleepTime);
            temp[k++] = arr[j++];
        }
        for(i = left, k = 0; i <= right; i++, k++){
            if(Thread.interrupted()) throw new InterruptedException();
            arr[i] = temp[k];
            currIndex = i;
            repaint();
            Thread.sleep(sleepTime);
        }
    }

        public void quickSort(){
            startSort(() -> {
                try{
                    qSort(arr, 0, arr.length - 1);
                    currIndex = -1;
                    repaint();
                    highlightSorted();
                }catch(InterruptedException e){}
            });
        }

        private int[] qSort(int[] arr, int start, int end) throws InterruptedException{
            if(Thread.interrupted()) throw new InterruptedException();
            if(start >= end) return arr;
            int pivot = partition(arr, start, end);
            qSort(arr, start, pivot - 1);
            qSort(arr, pivot + 1, end);
            return arr;
        }

        private int partition(int[] arr, int start, int end) throws InterruptedException{
            if(Thread.interrupted()) throw new InterruptedException();
            int pivot = arr[end];
            int i = start - 1;
            for(int j = start; j < end; j++){
                if(Thread.interrupted()) throw new InterruptedException();
                currIndex = j;
                repaint();
                Thread.sleep(sleepTime);
                if(arr[j] < pivot){
                    i++;
                    swap(arr, i, j);
                }
            }
            swap(arr, i + 1, end);
            return i + 1;
        }

        private void swap(int[] arr, int i, int j) throws InterruptedException{
            if(Thread.interrupted()) throw new InterruptedException();
            currIndex = i;
            repaint();
            Thread.sleep(sleepTime);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
