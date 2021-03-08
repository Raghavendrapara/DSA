/*input
*/
class QuickSort{

  // Sort interval [lo, hi] inplace recursively
  private static void quicksort(int[] ar, int lo, int hi) {
    if (lo < hi) {
      int splitPoint = partition(ar, lo, hi);
      quicksort(ar, lo, splitPoint);
      quicksort(ar, splitPoint + 1, hi);
    }
  }

  // Performs Hoare partition algorithm for quicksort
  private static int partition(int[] ar, int lo, int hi) {
    int pivot = ar[lo];
    int i = lo - 1, j = hi + 1;
    while (true) {
      do {
        i++;
      } while (ar[i] < pivot);
      do {
        j--;
      } while (ar[j] > pivot);
      if (i < j) swap(ar, i, j);
      else return j;
    }
  }

  // Swap two elements
  private static void swap(int[] ar, int i, int j) {
    int tmp = ar[i];
    ar[i] = ar[j];
    ar[j] = tmp;
  }

  public static void main(String[] args) {
    int[] array = {10, 0, 0, 0, 0, 0, 0, 0};
    quicksort(array,0,7);
    // Prints:
    // [-13, 2, 3, 4, 4, 6, 8, 10]
    System.out.println(java.util.Arrays.toString(array));
  }
}