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
    private static final int dataSetSize = 30;

    public static void main(String[] args) {
        try {
            findBestEquiFreq();
        } catch (IOException e) {
            logger.error("Error Error Error", e);
        }
    }

    private static void findBestEquiFreq() throws IOException {
        List<String> sampleTicketList, maxEquiFreqTicketList, allTicketsList, validationTicketNumbers;
        long maxEquiFreq;
        List<String> numberList = new ArrayList<>();
        List<String> luckyNumbers = new ArrayList<>();

        Stream<String> lines = Files.lines(Paths.get(fileName));
        allTicketsList = lines.collect(Collectors.toList());

        for (int i = allTicketsList.size() - dataSetSize; i >= 0; i--) {
            validationTicketNumbers = Arrays.asList(allTicketsList.get(i).split(","));

            numberList.clear();
            maxEquiFreqTicketList = allTicketsList.subList(i, i + dataSetSize);
            maxEquiFreqTicketList.forEach(line -> numberList.addAll(Arrays.asList(line.split(","))));
            Map<String, Long> numberFreqMap = numberList.stream().collect(Collectors.groupingBy(item -> item, Collectors.counting()));
            maxEquiFreq = numberFreqMap.entrySet().stream().max((entry1, entry2) -> Math.toIntExact(entry1.getValue() - entry2.getValue())).get().getValue();

            numberList.clear();
            sampleTicketList = allTicketsList.subList(i + 1, i + dataSetSize);
            sampleTicketList.forEach(line -> numberList.addAll(Arrays.asList(line.split(","))));
            numberFreqMap = numberList.stream().collect(Collectors.groupingBy(item -> item, Collectors.counting()));

            logger.debug("sample tickets from {} to {} with maxEquiFreq {}", i, i + dataSetSize, maxEquiFreq);

            for (Long equiFreq = 1L; equiFreq <= maxEquiFreq; equiFreq++) {
                luckyNumbers.clear();

                final Long equiFreqFinal = equiFreq;
                numberFreqMap.forEach((ticketNumber, numberFreq) -> {
                    if (numberFreq.equals(equiFreqFinal)) luckyNumbers.add(ticketNumber);
                });

                //calculating the equi Freq efficiency
                long positiveCounter = 0;
                double efficiency;
                if (luckyNumbers.size() == 0)
                    efficiency = 0;
                else {
                    positiveCounter = luckyNumbers.stream().filter(validationTicketNumbers::contains).count();
                    efficiency = positiveCounter * 100.0 / luckyNumbers.size();
                }

                logger.debug("equiFreq = {}, positiveCounter = {}, luckyNumbers.size() = {}, efficiency = {}", equiFreq, positiveCounter, luckyNumbers.size(), efficiency);
            }
        }
    }
}
