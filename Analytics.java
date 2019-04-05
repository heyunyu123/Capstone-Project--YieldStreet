import java.util.*;
//======================================================================================
// Class containing methods to perform analytics on an array of numbers
// Methods are:  compute(): perform all the computations
//               normalize(): removes Double.MIN_VALUESs (considered bad/no data)  
//               count(), sum(), avg(), median(), min(), max(), stdDev()
//               slice():     takes a 2dim array, and returns a row or col slice 
//               transpose(): takes a 2dim array, and transpose it. x/y becomes y/x 
//======================================================================================
public class Analytics
{
    private double[] array;             //incoming clean array

    private int      count;
    private double   sum;
    private double   avg;
    private double   median;
    private double   min;
    private double   max;
    private double   stdDev;
    private double   range;
    private double   mode;

//----------------------------------------------------------------------------------
// compute: Compute all analytics for a single dimension array
// Now changes to constructor
//----------------------------------------------------------------------------------
    Analytics(double[] dataArray)
    {
        array = normalize(dataArray);  //return an array without the Double.NIM_VALUEs   

        count  = count(array);  
        sum    = sum(array);
        avg    = avg(array);
        median = median(array);
        min    = min(array);
        max    = max(array);
        stdDev = stdDev(array);
        range  = range(array);
        mode   = mode(array);
    }

//--------------------------------------------------------------------------------
// instance getter methods
//--------------------------------------------------------------------------------
    int      getCount()  { return count; } 
    double   getSum()    { return sum;   } 
    double   getAvg()    { return avg;   } 
    double   getMedian() { return median;} 
    double   getMin()    { return min;   } 
    double   getMax()    { return max;   }
    double   getStdDev() { return stdDev;} 
    double   getRange()  { return range; }
    double   getMode()   { return mode;  }

//----------------------------------------------------------------------------------
// normalize: Returns a new array that has all the Double.MIN_VALUEs removed
//            All bad/no data was previously replaced with Double.MIN_VALUE 
//----------------------------------------------------------------------------------
    double[] normalize(double[] inArray)
    {
        int i     = 0;
        int nCols = 0;

        for (double col : inArray)                  //loop through all columns                  
            if (col != Double.MIN_VALUE)            //if not smallest double value (assumed bad/no data)
                nCols += 1;                         //add 1 to number of columns

        double[] outArray = new double[nCols];      //create a new array
        
        for (double col : inArray)                  //loop through incoming array                  
            if (col != Double.MIN_VALUE)            //if not bad data
                outArray[i++] = col;                //copy into new output array                        

        return outArray;                              
    }
    
//--------------------------------------------------------------------------------
// count: Count all elements of a 1 dim array
//--------------------------------------------------------------------------------
    int count(double[] array)
    {
        int count = array.length;                    //count = array.length
        return(count);                              
    }
//--------------------------------------------------------------------------------
// sum: Sum all elements of an array
//--------------------------------------------------------------------------------
    double sum(double[] array)
    {
        double sum = 0;
        for (double col : array)                     //loop through all columns                  
            sum += col;                              //add to total
        return(sum);
    }
//--------------------------------------------------------------------------------
// avg: Average all elements of an array
//--------------------------------------------------------------------------------
    double avg(double[] array)
    {
        double avg = sum/count;                       //perform the average
        return(avg);
    }
//--------------------------------------------------------------------------------
// median: Returns the median value on an array
//         if odd number of array elements, then return the median, 
//         if even number, then return the average of the 2 mid points 
//--------------------------------------------------------------------------------
    double median(double[] array)
    {        
        double[] array2 = Arrays.copyOf(array, array.length);   //copy the array into array2                   
        Arrays.sort(array2);                                    //sort array2
        
        int mid1 = count/2;
        int mid2 = (count-1)/2;
        double median = (count%2 == 1)              //if count is odd
            ?  array2[mid1]                         //median= mid point                                                               
            : (array2[mid1] + array2[mid2]) / 2;    //median= average of 2 mid points
        return(median);                                                                           
    }
//--------------------------------------------------------------------------------
// min: Returns the minimum value within an array
//--------------------------------------------------------------------------------
    double min(double[] array)
    {
        double minimum = Double.POSITIVE_INFINITY;   //start with largest possible value
        for (double col : array)                     //loop through all columns                  
            if (col < minimum) minimum = col;        //if col is less than minimum, save it in mimimum
        return(minimum);                              
    }
//--------------------------------------------------------------------------------
// max: Returns the maximum value within an array
//--------------------------------------------------------------------------------
    double max(double[] array)
    {
        double maximum = Double.NEGATIVE_INFINITY;   //start with lowest possible value
        for (double col : array)                     //loop through all columns                  
            if (col > maximum) maximum = col;        //if col is more than maximum, save it in maximum
        return(maximum);                              
    }
//--------------------------------------------------------------------------------
// stdDev: Returns the standard deviation of an array
//         It is a measure of the amount of variation of a set of data values
//         Low stdDev means the values are close to the average (or are tight) 
//         1. take average of array
//         2. take the difference (delta) of each element to the average
//         3. take the square of that delta
//         4. add all those square of deltas
//         5. divide the square of deltas by count of elements
//         6. take the square root of item 5.  
//--------------------------------------------------------------------------------
    double stdDev(double[] array)
    {
        int    count   = count(array);              //call count method
        double sum     = sum(array);                //call sum method
        double average = sum/count;

        double sqDelta = 0;                         //square of deltas      
        for (double col : array)                    //loop through all columns                  
            sqDelta += Math.pow(col-average,2);     //add to square of delta

        double std_dev = Math.sqrt(sqDelta/count);  //square root of average(square of deltas)
        return std_dev;                              
    }
    
//--------------------------------------------------------------------------------
// toString: Returns the array as well as all the analytic computations 
//--------------------------------------------------------------------------------
    String toString(double[] array)
    {
        double[] array2 = normalize(array);  //return an array without the Double.NIM_VALUEs   

        String str  = "Data points: "   + Arrays.toString(array2);
               str += "\nCount......: " + count(array2); 
               str += "\nSum........: " + sum(array2); 
               str += "\nAverage....: " + avg(array2); 
               str += "\nMedian.....: " + median(array2); 
               str += "\nMinimum....: " + min(array2); 
               str += "\nMaximum....: " + max(array2); 
               str += "\nStd.Dev....: " + stdDev(array2); 
               str += "\nMode.......: " + mode(array2);
               str += "\nRange......: " + range(array2);
        return str;                              
    }

//--------------------------------------------------------------------------------
// slice: Takes  a 2 dimensional array, slice type (row/col/all), and index
//        Return a single dimention array for that row or column or all cells
//--------------------------------------------------------------------------------
    static double[] slice(double[][] array2dim, String type, int idx)
    {
        int      size  = 0;
        double[] array = null;

        if (type.equals("row"))                             //ROW slice
        {
            size  = array2dim[idx].length;                      //determine the needed array size           
            array = Arrays.copyOf(array2dim[idx], size);        //copy that row into a 1dim array
        }
        if (type.equals("col"))                             //COL slice
        {           
            size  = array2dim.length;                           //determine the needed array size           
            array = new double[size];                           //create a new array of that size
            for (int i=0; i < size ; i++)                       //loop through all rows
                array[i] = array2dim[i][idx];                   //add cell into a 1dim array                     
        }
        if (type.equals("all"))                             //ALL slice (turn a 2dim array to 1 dim)
        {           
            for (double[] row : array2dim)                      //loop through all rows                     
                size += row.length;                             //compute the needed array size                              
            array = new double[size];                           //create a new array of that size
            int i = 0;
            for (double[] row : array2dim)                      //loop through all rows
                for (double col : row)                          //loop through all columns
                    array[i++] = col;                           //add cell into a 1dim array                         
        }

//      System.out.println(size + Arrays.toString(array));      //debug only
        return array;
    }
//--------------------------------------------------------------------------------
// transpose: Takes  a 2 dimensional array
//            Return a transposed 2 dimensional array
//--------------------------------------------------------------------------------
    static double[][] transpose(double[][] array2dim)
    {
        int rowNum  = array2dim.length;                     //compute number of rows
        int colNum  = 0;                                    //compute number of columns
            
        for (double[] row : array2dim)                      //loop through all rows                     
            if (row.length > colNum)                        //take the size of the longest row                              
                colNum = row.length;                        //this becomes the number of columns        
        
        double[][] newArray = new double[colNum][rowNum];   //create new array
                                                            //notice [row][col] dimensions are transposed
        int colT = 0;
        for (int row=0; row < array2dim.length; row++)              //loop thru original rows
        {
            int rowT = 0;                                           
            for (int col=0; col < array2dim[row].length; col++)     //loop thru original columns
            {
                newArray[rowT][colT] = array2dim[row][col];         //copy into new array           
                rowT++;                                             //add 1 to row of new array
            }
            colT++;                                                 //add 1 to col of new array
        }   
        return newArray;
    }
    
    
    
    
//--------------------------------------------------------------------------------
// range: Return max number minus min number of the array
//--------------------------------------------------------------------------------    
    double range(double[] array) {
      double range = max - min;
      return(range);
    }
    
    
    
//--------------------------------------------------------------------------------
// mode: Return the number that occurs most frequently within the array 
//       If no number occur more than once, no mode. 
//       If multiple numbers occur as most, you can simply return one of those modes 
//       Extra credit if you return multiple modes as an array of modes, if more than 1 mode exists.
//--------------------------------------------------------------------------------      
    double mode(double[] array) {
      double mode = array[0];
      int maxCount = 0;
      int count = 0;
      for (int i = 0; i < array.length; i++) {
        double value = array[i];
        for (int j = 0; j < array.length; j++) {
          if (array[j] == value) count++;
        }   
        if (count > maxCount) {
          mode = value;
          maxCount = count;
        }
        count = 0;
      }
      if (maxCount == 1) mode = Double.NaN;
      return mode;
    }
    
    
    
//--------------------------------------------------------------------------------    
}