package bam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Bam {
    private static final Logger logger = LoggerFactory.getLogger(Bam.class);
    private static final String fileName = "/Users/parag/work/workspaces/PriceProcessor/src/main/resources/inputData.csv";
    private static final long maxEquiFreq = 6;
    private static final int dataSetSize = 20;

    public static void main(String[] args) {
        try {
            findBestEquiFreq();
        } catch (IOException e) {
            logger.error("Error Error Error", e);
        }
    }

    private static void findBestEquiFreq() throws IOException {
        List<String> ticketList;
        List<String> numberList = new ArrayList<>();
        Stream<String> lines = Files.lines(Paths.get(fileName));
        ticketList = lines.limit(dataSetSize + 1).collect(Collectors.toList());
        List<String> testTicket = Arrays.asList(ticketList.get(0).split(","));

        List<String> luckyNumbers = new ArrayList<>();

        for (Long equiFreq = 0L; equiFreq < maxEquiFreq; equiFreq++) {
            numberList.clear();
            luckyNumbers.clear();

            ticketList.subList(1, ticketList.size() - Math.toIntExact(maxEquiFreq - equiFreq)).forEach(line -> {
                numberList.addAll(Arrays.asList(line.split(",")));
            });
            Map<String, Long> numberFreqMap = numberList.stream().collect(Collectors.groupingBy(item -> item, Collectors.counting()));
            final Long equiFreqFinal = equiFreq;
            numberFreqMap.forEach((ticketNumber, numberFreq) -> {
                if (numberFreq.equals(equiFreqFinal)) luckyNumbers.add(ticketNumber);
            });

            //calculating the equi Freq efficiency
            long positiveCounter = 0, efficiency;
            if (luckyNumbers.size() == 0)
                efficiency = 0;
            else {
                positiveCounter = luckyNumbers.stream().filter(testTicket::contains).count();
                efficiency = positiveCounter * 100 / luckyNumbers.size();
            }

            logger.debug("equiFreq = {}, positiveCounter = {}, luckyNumbers.size() = {}, efficiency = {}", equiFreq, positiveCounter, luckyNumbers.size(), efficiency);
        }
        logger.debug("testing");
    }
}
