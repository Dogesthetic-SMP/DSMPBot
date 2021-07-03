package me.elJoa.dsmpbot.utilities;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class PerfHandler {
    private static Spark spark = null;

    public PerfHandler(Spark spark) {
        PerfHandler.spark = spark;
    }

    public static String getMemoryUsage() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();

        long maxMemory = heapUsage.getMax();
        long usedMemory = heapUsage.getUsed();
        double percentTemp = (usedMemory * 100d) / maxMemory;
        String percentUsage = (int) percentTemp + "%";

        return humanReadableByteCountBin(usedMemory) + " / " + humanReadableByteCountBin(maxMemory) + " (" + percentUsage + ")";
    }

    public static String getTPS() {
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();

        assert tps != null;
        double tpsReturn = tps.poll(StatisticWindow.TicksPerSecond.SECONDS_5);

        if (tpsReturn < 19.9) {
            return String.valueOf(tpsReturn);
        } else {
            return "20.00";
        }
    }

    public static String getMSPT() {
        GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt = spark.mspt();

        assert mspt != null;
        DoubleAverageInfo msptReturn = mspt.poll(StatisticWindow.MillisPerTick.SECONDS_10);
        double msptMean = msptReturn.mean();

        return String.valueOf(truncateDecimalToX(2, msptMean));
    }

    public static String getCPUUsage() {
        DoubleStatistic<StatisticWindow.CpuUsage> cpuUsage = spark.cpuProcess();

        double cpu = cpuUsage.poll(StatisticWindow.CpuUsage.SECONDS_10);
        return (int)(cpu * 100d) + "%";
    }

    private static BigDecimal truncateDecimalToX(int x, double number) {
        if (number > 0) {
            return new BigDecimal(number).setScale(x, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(number).setScale(x, BigDecimal.ROUND_CEILING);
        }
    }

    // https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java - The most copied Java snippet on StackOverflow lol
    private static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }
}