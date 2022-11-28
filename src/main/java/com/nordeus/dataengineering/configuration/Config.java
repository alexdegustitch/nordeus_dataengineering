package com.nordeus.dataengineering.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordeus.dataengineering.model.*;
import com.nordeus.dataengineering.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Configuration
public class Config {

    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private LoginEventRepository loginEventRepository;
    @Autowired
    private RegistrationEventRepository registrationEventRepository;
    @Autowired
    private TransactionEventRepository transactionEventRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Bean
    @Order(1)
    public void deleteALlData(){
        exchangeRateRepository.deleteAll();
        loginEventRepository.deleteAll();
        registrationEventRepository.deleteAll();
        transactionEventRepository.deleteAll();
        currencyRepository.deleteAll();
        userRepository.deleteAll();
        eventRepository.deleteAll();
        System.out.println("All data deleted");
    }
    @Bean
    @Order(2)
    public void readExchangeRates() {

        BufferedReader reader = null;

        try {
            reader = getResourceFileAsString("data/exchange_rates.jsonl");
            String line;
            while ((line = reader.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();

                // convert JSON file to map
                Map<?, ?> map = mapper.readValue(line, Map.class);

                String currency = map.get("currency").toString();
                currencyRepository.save(new CurrencyModel(currency));

                ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
                exchangeRateModel.setCurrencyFrom(currency);
                exchangeRateModel.setCurrencyTo("USD");
                exchangeRateModel.setConversionValue(Double.parseDouble(map.get("rate_to_usd").toString()));
                exchangeRateRepository.save(exchangeRateModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Bean
    @Order(3)
    public void readEvents() {
        BufferedReader reader = null;

        try {
            reader = getResourceFileAsString("data/events.jsonl");

            String line;
            while ((line = reader.readLine()) != null) {
                // System.out.println(line);
                ObjectMapper mapper = new ObjectMapper();
                // convert JSON file to map
                Map<?, ?> map = mapper.readValue(line, Map.class);

                if (!map.containsKey("event_id")) {
                    System.out.println(line);
                    continue;
                }
                long eventId = Long.parseLong(map.get("event_id").toString());

                if (!map.containsKey("event_timestamp")) {
                    System.out.println(line);
                    continue;
                }
                LocalDateTime eventTimestamp = Instant.ofEpochSecond(Long.parseLong(map.get("event_timestamp").toString())).atOffset(
                        ZoneOffset.UTC
                ).toLocalDateTime();

                if (!map.containsKey("event_type")) {
                    System.out.println(line);
                    continue;
                }
                String eventType = map.get("event_type").toString();
                if (!map.containsKey("event_data")) {
                    System.out.println(line);
                    continue;
                }
                map = (Map<?, ?>) map.get("event_data");
                switch (eventType) {
                    case "login":
                        if (!map.containsKey("user_id")) {
                            System.out.println(line);
                            continue;
                        }
                        String userId = map.get("user_id").toString();
                        LoginEventModel loginEventModel = new LoginEventModel(eventId, eventTimestamp, userId);
                        try {
                            loginEventRepository.save(loginEventModel);
                        } catch (Exception e) {
                            System.out.println(line);
                        }
                        break;
                    case "registration":
                        if (!map.containsKey("user_id")) {
                            System.out.println(line);
                            continue;
                        }
                        if (!map.containsKey("country")) {
                            System.out.println(line);
                            continue;
                        }
                        if (!map.containsKey("name")) {
                            System.out.println(line);
                            continue;
                        }
                        if (!map.containsKey("device_os")) {
                            System.out.println(line);
                            continue;
                        }
                        userId = map.get("user_id").toString();
                        String country = map.get("country").toString();
                        String name = map.get("name").toString();
                        String deviceOs = map.get("device_os").toString();
                        try {
                            userRepository.save(new UserModel(userId, name, country, deviceOs));
                        } catch (Exception e) {
                            System.out.println(line);
                        }
                        try {
                            registrationEventRepository.save(new RegistrationEventModel(eventId, eventTimestamp, userId));
                        } catch (Exception e) {
                            System.out.println(line);
                        }
                        break;
                    case "transaction":
                        if (!map.containsKey("user_id")) {
                            System.out.println(line);
                            continue;
                        }
                        if (!map.containsKey("transaction_currency")) {
                            System.out.println(line);
                            continue;
                        }
                        if (!map.containsKey("transaction_amount")) {
                            System.out.println(line);
                            continue;
                        }
                        userId = map.get("user_id").toString();
                        String transactionCurrency = map.get("transaction_currency").toString();
                        double transactionAmount = Double.parseDouble(map.get("transaction_amount").toString());
                        try {
                            transactionEventRepository.save(new TransactionEventModel(eventId, eventTimestamp, userId, transactionCurrency, transactionAmount));
                        } catch (Exception e) {
                            System.out.println(line);
                        }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All data processed");
    }

    @Bean
    @Order(4)
    public void cleanData(){
            loginEventRepository.removeInvalidEventsByDate();
            loginEventRepository.removeDuplicateLogins();
            transactionEventRepository.removeDuplicateTransactions();
            loginEventRepository.removeLoginsForNoRegistratedUsers();
            transactionEventRepository.removeTransactionsForNoRegistratedUsers();
            loginEventRepository.removeLoginsBeforeRegistration();
            transactionEventRepository.removeTransactionsBeforeRegistration();
            transactionEventRepository.removeTransactionsSameLoginTime();

        System.out.println("All data cleaned");
    }
    private static BufferedReader getResourceFileAsString(String fileName) {
        InputStream is = getResourceFileAsInputStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            // return (String)reader.lines().collect(Collectors.joining(System.lineSeparator()));
            return reader;
        } else {
            throw new RuntimeException("resource not found");
        }
    }

    private static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = Config.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }


}
