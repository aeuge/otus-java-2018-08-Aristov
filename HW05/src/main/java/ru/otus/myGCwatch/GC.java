package ru.otus.myGCwatch;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GC {
    static final Set<String> YOUNG_GC = new HashSet<String>(3);
    static final Set<String> OLD_GC = new HashSet<String>(3);

    static {
        // young generation GC names
        YOUNG_GC.add("PS Scavenge");
        YOUNG_GC.add("ParNew");
        YOUNG_GC.add("G1 Young Generation");

        // old generation GC names
        OLD_GC.add("PS MarkSweep");
        OLD_GC.add("ConcurrentMarkSweep");
        OLD_GC.add("G1 Old Generation");
    }

    public static void printGCMetrics() {

        long minorCount = 0;
        long minorTime = 0;
        long majorCount = 0;
        long majorTime = 0;
        long unknownCount = 0;
        long unknownTime = 0;

        List<GarbageCollectorMXBean> mxBeans
                = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : mxBeans) {
            long count = gc.getCollectionCount();
            if (count >= 0) {
                //System.out.println(gc.getName()+"--"+count);
                if (YOUNG_GC.contains(gc.getName())) {
                    minorCount += count;
                    minorTime += gc.getCollectionTime();
                } else if (OLD_GC.contains(gc.getName())) {
                    majorCount += count;
                    majorTime += gc.getCollectionTime();
                } else {
                    unknownCount += count;
                    unknownTime += gc.getCollectionTime();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" Count YOUNG Generation: ").append(minorCount)
                .append(", Time (ms): ").append(minorTime)
                .append(", Count OLD Generation: ").append(majorCount)
                .append(", Time (ms): ").append(majorTime);

        if (unknownCount > 0) {
            sb.append(", UnknownGC -> Count: ").append(unknownCount)
                    .append(", Time (ms): ").append(unknownTime);
        }

        System.out.println(sb);
    }
}
