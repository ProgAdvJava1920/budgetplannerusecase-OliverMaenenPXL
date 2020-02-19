package be.pxl.student.util;

import be.pxl.student.BudgetPlanner;
import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class to import csv file
 */
public class BudgetPlannerImporter {

    private static final Logger LOGGER = LogManager.getLogger(BudgetPlanner.class);
    private PathMatcher csvMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.csv");

    public void importCsv(Path path) {

        if (!csvMatcher.matches(path)) {
            LOGGER.error("Invalid file: .csv expected. Provided: {}", path);
            return;
        }

        if (!Files.exists(path)) {
            LOGGER.error("File {} does not exists", path);
            return;
        }

        try(BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Payment payment = new Payment(LocalDateTime.parse(data[3]), Double.parseDouble(data[4]), data[5],data[6]);
                List<Payment> payments = new ArrayList<>();
                payments.add(payment);

                Account account = new Account(data[0], data[1], payments);

                LOGGER.debug(account);
            }
        } catch (IOException e) {
            LOGGER.fatal("An error occurred while reading file: {}", path);
        }
    }

}
