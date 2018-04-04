/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.sorting.algorithm.research;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.util.SplittableRandom;

/**
 *
 * @author osw4ld
 */
public class CCSortingAlgorithmResearch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int z, x;       //counter variables
        int IoverQ = 0; //counter for Insertion over Quick.
        int orig = 0;   //Variables for inclusinve origin and exclusive bound of random. 
        int bound = 1001;
        int len = 180;  //Size of array
    //Key for first number for array designation: 1 = quick sort, 2 = merge sort, 3 = insertion sort, 4 = selection sort
    //Key for second number for array designation: 1 = 10^3, 2 = 10^5, 3 = 10^7 
	int arr11[] = new int[len];
        int arr21[] = new int[len];
        int arr31[] = new int[len];
        int arr41[] = new int[len];
        int darray[] = new int[len]; //dummy array 
        
    //Delcaring start, end, and time variables
        double start, end;
        double quickTime, insertTime, mergeTime, heapTime;
    
    //Declaring averages and number of tests
        double quickAvg = 0, insertAvg = 0, mergeAvg = 0, heapAvg = 0;
        double tests = 100; //Number of instances taken into average
        double trials = 10; //Number of averages taken
        

    //Delcaring instance of splittable random for RNG.	
	SplittableRandom randNum = new SplittableRandom();

    //Setting proper formatting for nanotime: Sets to round to fourth decimal place, and rounds up. 
	DecimalFormat df = new DecimalFormat("###.####");
        df.setRoundingMode(RoundingMode.HALF_UP);
    
    //Setting up initial call to sorts to prevent over head of time
        for (z = 0; z < len; z++)
            darray[z] = randNum.nextInt(orig, bound);
        InsertionSort(darray); //Initial call to insertion Sort to prevent overhead 
        System.out.println("//Array Size: " + len + "\n"); //Prints out array size for data set
        
    //Testing for insertion over quick. If over half of averages are less than quick, will be considered breaking point    
    //Beginning of testing loop  
        for (int y = 0; y < trials; y++) {
            for (x = 0; x < tests; x++) {
            //Beginning of loops to fill arrays
            //For arrays size 10^3
                for(z = 0; z < len; z++) {
                    arr11[z] = randNum.nextInt(orig, bound);
                    arr21[z] = arr11[z];
                    arr31[z] = arr11[z];
                    arr41[z] = arr11[z];
                }

                //Quick sort initiated and timed

                start = System.nanoTime();
                quickSort(arr11, 0, (arr11.length -1));
                end = System.nanoTime();

                //Time acquired and converted from nanoseconds to milliseconds.
                quickTime = end - start;
                quickTime *= Math.pow(10, -6);

                quickAvg += quickTime;

                //Merge sort initiated and timed

                start = System.nanoTime();
                MergeSort(arr21);
                end = System.nanoTime();


                mergeTime = end - start;
                mergeTime *= Math.pow(10, -6);

                mergeAvg += mergeTime;

                //Insertion sort initiated and timed

                start = System.nanoTime();
                InsertionSort(arr31);
                end = System.nanoTime();

                insertTime = end - start;
                insertTime *= Math.pow(10, -6);

                insertAvg += insertTime;

                //Heap sort initiated and timed

                start = System.nanoTime();
                HeapSort(arr41);
                end = System.nanoTime();

                heapTime = end - start;
                heapTime *= Math.pow(10, -6);

                heapAvg += heapTime;
                
                if (insertAvg < quickAvg) 
                    IoverQ++;

            }

        // Averages found, then displayed
            quickAvg /= tests;
            mergeAvg /= tests;
            insertAvg /= tests;
            heapAvg /= tests;
            
            System.out.println("Average Insertion Sort Duration: " + df.format(insertAvg));
            System.out.println("Average Quick Sort Duration:     " + df.format(quickAvg));
            System.out.println("Average Merge Sort Duration:     " + df.format(mergeAvg));
            System.out.println("Average Heap Sort Duration:      " + df.format(heapAvg) + "\n");
            
            if (IoverQ >= (.9 * tests))
                System.out.println("Insertion beats Quick!\n\n");
                
            quickAvg = 0;
            mergeAvg = 0;
            insertAvg = 0;
            heapAvg = 0;
            IoverQ = 0;

        }
        
    }
    
    //Insertion Sort
    
     public static void InsertionSort(int[] list) {
        for (int i = 1; i < list.length; i++){
            int currentElement = list[i];
            int k;
            for (k = i - 1; k >= 0 && list[k] > currentElement; k--){
                list [k + 1] = list[k];
            }

            list[k + 1] = currentElement;
        }
    }

     //Merge Sort
     
     public static void MergeSort(int[] list) {
        if (list.length > 1) {
            // Merge sort the first half
            int[] firstHalf = new int[list.length / 2];
            System.arraycopy(list, 0, firstHalf, 0, list.length / 2);
            MergeSort(firstHalf);

            // Merge sort the second half
            int secondHalfLength = list.length - list.length / 2;
            int[] secondHalf = new int[secondHalfLength];
            System.arraycopy(list, list.length / 2, secondHalf, 0, secondHalfLength);
            MergeSort(secondHalf);

            // Merge firstHalf with secondHalf into list
            merge(firstHalf, secondHalf, list);
        }
    }

     
    public static void merge(int[] list1, int[] list2, int[] temp) {
        int current1 = 0; // Current index in list1
        int current2 = 0; // Current index in list2
        int current3 = 0; // Current index in temp

        while (current1 < list1.length && current2 < list2.length) {
            if (list1[current1] < list2[current2])
                temp[current3++] = list1[current1++];
            else
                temp[current3++] = list2[current2++];
        }

        while (current1 < list1.length)
            temp[current3++] = list1[current1++];

        while (current2 < list2.length)
            temp[current3++] = list2[current2++];
    }
    
    //Quick Sort
    
    public static int partition(int arr[], int left, int right) {
        int i = left, j = right;
        int tmp;
        int pivot = arr[(left + right) / 2];

        while (i <= j) {
            while (arr[i] < pivot)
                i++;
            while (arr[j] > pivot)
                j--;
            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }

    public static void quickSort(int arr[], int left, int right) {

        int index = partition(arr, left, right);
        if (left < index - 1)
            quickSort(arr, left, index - 1);
        if (index < right)
            quickSort(arr, index, right);

    }

    //Heap(Max) Sort
    public static void BuildHeap(int[] arr) {
        int len = (arr.length - 1);
        for (int i = len/2; i >= 0; i--)
            MaxHeapify(arr, i, arr.length - 1);
            
    }
    
    public static void MaxHeapify(int[] arr, int i, int size) {
        int left = 2 * i + 1;
        int right = (2 * i) + 2;
        int max;
        
        if(left <= size && arr[left] > arr[i])
            max = left;
        else
            max = i;
        
        if (right <= size && arr[right] > arr[max])
            max = right;
        
        if (max != i) {
            exchange(arr, i, max);
            MaxHeapify(arr, max, size);
        }
    }
    
    public static void exchange(int[] arr, int i, int max) {
        int temp = 0;
        temp = arr[i];
        arr[i] = arr[max];
        arr[max] = temp;
    }
    
    public static void HeapSort(int[] arr) {
        BuildHeap(arr);
        
        int SizeOfHeap = (arr.length - 1);
        for (int i = SizeOfHeap; i >= 1; i--) {
            exchange(arr, 0, i);
            SizeOfHeap--;
            MaxHeapify(arr, 0, SizeOfHeap);
        }
    }
    
}

