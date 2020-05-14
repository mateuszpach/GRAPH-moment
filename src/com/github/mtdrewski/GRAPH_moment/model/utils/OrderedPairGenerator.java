package com.github.mtdrewski.GRAPH_moment.model.utils;

import javafx.util.Pair;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface OrderedPairGenerator {

    public static List<Pair<Integer, Integer>> lexicographicalPairs(int upTo) {
        List<Pair<Integer, Integer>> pairs = Stream.iterate(new Pair<>(1, 2), p -> {
            if (p.getValue() == upTo)
                return new Pair<>(p.getKey() + 1, p.getKey() + 2);
            else
                return new Pair<>(p.getKey(), p.getValue() + 1);
        }).limit(upTo * (upTo - 1) / 2).collect(Collectors.toList());
        return pairs;
    }

    public static List<Pair<Integer, Integer>> allPairs(int upTo) {
        List<Pair<Integer, Integer>> pairs = Stream.iterate(new Pair<>(1, 1), p -> {
            if (p.getValue() == upTo)
                return new Pair<>(p.getKey() + 1, 1);
            else
                return new Pair<>(p.getKey(), p.getValue() + 1);
        }).limit(upTo * upTo).collect(Collectors.toList());
        return pairs;
    }
}
