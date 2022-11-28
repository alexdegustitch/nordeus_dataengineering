package com.nordeus.dataengineering.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
/**

 * Repository for transaction_event table

 * @version 1.0

 * @author Aleksandar Paripovic

 */
@Repository
public interface TransactionEventRepository extends EventRepository {

    /**
     * Returns total number of transactions and total revenue
     * for a given user
     *
     * @param  userId  id of a user
     * @return      number of transactions and total revenue
     */
    @Query(value = "select count(te.event_id) as numberOfTransactions, coalesce(sum(te.transaction_amount * er.conversion_value), 0) as totalRevenue\n" +
            "from transaction_event te\n" +
            "join \"event\" e\n" +
            "on te.event_id = e.event_id\n" +
            "join exchange_rate er\n" +
            "on te.currency_iso_code = er.currency_from\n" +
            "where te.user_id = :user_id and er.currency_to = 'USD'", nativeQuery = true)
    TransactionInfo getUserTransactionInfo(@Param("user_id") String userId);

    /**
     * Returns total number of transactions and total revenue
     * for a given user for a specific date
     *
     * @param  userId  id of a user
     * @param dateFrom specified date
     * @param dateTo day after specified date
     * @return      number of transactions and total revenue
     */
    @Query(value = "select count(te.event_id) as numberOfTransactions, coalesce(sum(te.transaction_amount * er.conversion_value), 0) as totalRevenue\n" +
            "from transaction_event te\n" +
            "join \"event\" e\n" +
            "on te.event_id = e.event_id\n" +
            "join exchange_rate er\n" +
            "on te.currency_iso_code = er.currency_from\n" +
            "where te.user_id = :user_id and er.currency_to = 'USD' and e.event_timestamp >= :date_from and e.event_timestamp < :date_to", nativeQuery = true)
    TransactionInfo getUserTransactionInfoForDate(@Param("user_id") String userId, @Param("date_from") LocalDate dateFrom, @Param("date_to") LocalDate dateTo);


    public static interface TransactionInfo {

        int getNumberOfTransactions();

        double getTotalRevenue();

    }

    public static interface TransactionInfoForCountries {
        String getCountry();

        int getNumberOfTransactions();

        double getTotalRevenue();
    }

    /**
     * Delete all duplicate transactions. Those are transactions
     * with the equal user id and event_timestamp
     *
     * @return      void
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM \"event\"\n" +
            "WHERE event_id IN (SELECT event_id\n" +
            "FROM (SELECT e1.event_id,\n" +
            "\t\t\tROW_NUMBER() OVER (partition BY e1.event_timestamp, te.user_id ORDER BY e1.event_id) AS rnum\n" +
            "\tFROM \"event\" e1\n" +
            "\tjoin transaction_event te\n" +
            "\ton te.event_id = e1.event_id) t\n" +
            "WHERE t.rnum > 1)", nativeQuery = true)
    void removeDuplicateTransactions();

    /**
     * Delete all transactions where user_id is not
     * registered user
     *
     * @return      void
     */
    @Transactional
    @Modifying
    @Query(value ="DELETE FROM \"event\"\n" +
            "WHERE event_id IN (\n" +
            "\tSELECT \n" +
            "\t\te1.event_id\n" +
            "\tFROM \"event\" e1\n" +
            "\tjoin transaction_event te\n" +
            "\ton te.event_id = e1.event_id\n" +
            "\tleft join \"user\" u\n" +
            "\ton te.user_id = u.user_id\n" +
            "\twhere u.user_id is null\n" +
            ")", nativeQuery = true)
    void removeTransactionsForNoRegistratedUsers();

    /**
     * Delete all transactions which emitted
     * before that user registered
     *
     * @return      void
     */
    @Transactional
    @Modifying
    @Query(value = "delete \n" +
            "from \"event\" e1\n" +
            "using transaction_event te, registration_event re, \"event\" e2\n" +
            "where e1.event_id = te.event_id and te.user_id = re.user_id and re.event_id = e2.event_id and e1.event_timestamp <= e2.event_timestamp", nativeQuery = true)
    void removeTransactionsBeforeRegistration();

    /**
     * Delete all transactions with the same
     * event_timestamp as some login event
     * for the user who made transaction
     *
     * @return      void
     */
    @Transactional
    @Modifying
    @Query(value ="\n" +
            "delete \n" +
            "from \"event\" e1\n" +
            "using \"event\" e2, transaction_event te, login_event le\n" +
            "where \n" +
            "e1.event_id = te.event_id and\n" +
            "e2.event_id = le.event_id and\n" +
            "te.user_id = le.user_id and\n" +
            "e1.event_timestamp = e2.event_timestamp", nativeQuery = true)
    void removeTransactionsSameLoginTime();
}
