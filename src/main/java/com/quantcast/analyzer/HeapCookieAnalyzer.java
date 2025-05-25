package com.quantcast.analyzer;

import com.quantcast.observer.EventObserver;
import com.quantcast.utils.CookieResult;

import java.util.*;

/**
 * Efficiently retrieves the top‑N most frequent cookies.
 */
public class HeapCookieAnalyzer implements CookieAnalyzer {
    private final EventObserver observer;

    /**
     * Constructor.
     *
     * @param observer the event observer
     */
    public HeapCookieAnalyzer(EventObserver observer) {
        this.observer = observer;
    }

    @Override
    public List<CookieResult> findMostActiveCookies(Map<String, Integer> cookiesFrequency, int topNRanks) {
        int numUniqueElements = cookiesFrequency.size();
        observer.analysisStarted(numUniqueElements);

        // 1. Convert map entries into a max‐heap in O(M) by using the
        // PriorityQueue constructor with a reversed comparator.
        PriorityQueue<Map.Entry<String, Integer>> maxHeap = getMaxHeap(cookiesFrequency);

        // 2. Extract the top N Rank elements: O(topNRanks * log M)
        // e.g. if highest frequencies are 10 and 11 and topNRanks is 2, then return all elements with these 10 and 11 frequencies
        List<CookieResult> top = getTopRankCookies(topNRanks, maxHeap);

        observer.analysisCompleted(numUniqueElements, top.size());

        return top;
    }


    private static PriorityQueue<Map.Entry<String, Integer>> getMaxHeap(Map<String, Integer> freqMap) {
        int initialCapacity = Math.max(1, freqMap.size());
        PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(
                initialCapacity,
                Comparator.comparing(Map.Entry<String, Integer>::getValue)
                        .reversed() // make it a max‐heap
        );
        maxHeap.addAll(freqMap.entrySet());  // O(M)

        return maxHeap;
    }

    private static List<CookieResult> getTopRankCookies(int topNRanks, PriorityQueue<Map.Entry<String, Integer>> maxHeap) {
        List<CookieResult> top = new ArrayList<>();
        int lastFrequency = -1;
        while (!maxHeap.isEmpty()) {
            Map.Entry<String, Integer> entry = maxHeap.poll();
            int frequency = entry.getValue();

            if (frequency != lastFrequency) {
                topNRanks--;
                if (topNRanks < 0) break;

                lastFrequency = frequency;
            }

            top.add(new CookieResult(entry.getKey()));
        }

        return top;
    }

}
