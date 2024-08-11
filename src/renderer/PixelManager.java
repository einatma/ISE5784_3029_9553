package renderer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PixelManager is a helper class. It is used for multi-threading in the
 * renderer and for follow up its progress.
 * A Camera uses one pixel manager object and several Pixel objects - one in
 * each thread.
 *
 * @author Dan Zilberstein
 */
class PixelManager {
    /**
     * Immutable record for object containing allocated pixel (with its row and
     * column numbers).
     */
    record Pixel(int col, int row) {
    }

    /** The maximum number of rows of pixels */
    private final int maxRows;
    /** The maximum number of columns of pixels */
    private final int maxCols;
    /** The total number of pixels in the generated image */
    private final long totalPixels;

    /** The current row of pixels being processed */
    private final AtomicInteger currentRow = new AtomicInteger(0);
    /** The current column of pixels being processed */
    private final AtomicInteger currentCol = new AtomicInteger(-1);
    /** The number of pixels that have been processed */
    private final AtomicLong processedPixels = new AtomicLong(0);
    /** The last printed progress update percentage */
    private volatile int lastPrintedPercentage = 0;

    /** A flag indicating whether progress should be printed */
    private final boolean printProgress;
    /** The interval in milliseconds between progress prints */
    private final long printIntervalMillis;
    /** The format string for printing progress */
    private static final String PRINT_FORMAT = "%5.1f%%\r";

    /** The mutex object for synchronizing next pixel allocation between threads */
    private final Object nextPixelLock = new Object();
    /** The mutex object for synchronizing progress printing between threads */
    private final Object progressPrintLock = new Object();

    /**
     * Initialize pixel manager data for multi-threading.
     *
     * @param maxRows         the amount of pixel rows
     * @param maxCols         the amount of pixel columns
     * @param intervalSeconds print time interval in seconds, 0 if printing is not
     *                        required
     */
    PixelManager(int maxRows, int maxCols, double intervalSeconds) {
        this.maxRows = maxRows;
        this.maxCols = maxCols;
        this.totalPixels = (long) maxRows * maxCols;
        this.printIntervalMillis = (long) (intervalSeconds * 1000);
        this.printProgress = (printIntervalMillis != 0);
        if (printProgress) {
            System.out.printf(PRINT_FORMAT, 0d);
        }
    }

    /**
     * Function for thread-safe manipulating of main follow up Pixel object. This
     * function is a critical section for all the threads, and the pixel manager
     * data is the shared data of this critical section.
     * The function provides the next available pixel number each call.
     *
     * @return the next available Pixel, or null if there are no more pixels
     */
    Pixel nextPixel() {
        synchronized (nextPixelLock) {
            if (currentRow.get() == maxRows) {
                return null;
            }

            int col = currentCol.incrementAndGet();
            if (col < maxCols) {
                return new Pixel(col, currentRow.get());
            }

            currentCol.set(0);
            currentRow.incrementAndGet();
            if (currentRow.get() < maxRows) {
                return new Pixel(currentCol.get(), currentRow.get());
            }
        }
        return null;
    }

    /**
     * Finish pixel processing by updating and printing the progress percentage.
     */
    void pixelDone() {
        boolean shouldPrint = false;
        int percentage = 0;
        synchronized (progressPrintLock) {
            long processedCount = processedPixels.incrementAndGet();
            if (printProgress) {
                percentage = (int) (1000L * processedCount / totalPixels);
                if (percentage - lastPrintedPercentage >= printIntervalMillis) {
                    lastPrintedPercentage = percentage;
                    shouldPrint = true;
                }
            }
            if (shouldPrint) {
                System.out.printf(PRINT_FORMAT, percentage / 10.0);
            }
        }
    }
}