package com.nordeus.dataengineering.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**

 * Repository for login_event table

 * @version 1.0

 * @author Aleksandar Paripovic

 */
@Repository
public interface LoginEventRepository extends EventRepository {


    /**
     * Returns total number of logins
     * for a given user
     *
     * @param  userId  id of a user
     * @return      number of logins
     */
    @Query(value = "select count(e.event_timestamp) as numberOfLogins\n" +
            "from login_event le\n" +
            "join event e\n" +
            "on le.event_id = e.event_id\n" +
            "where le.user_id = :user_id", nativeQuery = true)
    int getUserLoginCount(@Param("user_id") String userId);


    /**
     * Returns total number of logins
     * for a given user for a specific date
     *
     * @param  userId  id of a user
     * @param dateFrom specified date
     * @param dateTo day after specified date
     * @return      number of logins
     */
    @Query(value = "select count(e.event_timestamp) as numberOfLogins\n" +
            "from login_event le\n" +
            "join \"event\" e\n" +
            "on le.event_id = e.event_id\n" +
            "where le.user_id = :user_id and e.event_timestamp >= :date_from and e.event_timestamp < :date_to", nativeQuery = true)
    int getUserLoginCountForDate(@Param("user_id") String userId, @Param("date_from") LocalDate dateFrom, @Param("date_to") LocalDate dateTo);


    /**
     * Returns last day user logged in
     *
     * @param  userId  id of a user
     * @return      last login date for a user
     */
    @Query(value = "select max(e.event_timestamp) as lastLoginDate\n" +
            "from login_event le\n" +
            "join \"event\" e\n" +
            "on le.event_id = e.event_id\n" +
            "where le.user_id = :user_id", nativeQuery = true)
    LocalDateTime getUserLastLogin(@Param("user_id") String userId);

    /**
     * Returns last day user logged in before
     * specified day
     *
     * @param  userId  id of a user
     * @return      last login date for a user
     */
    @Query(value = "select max(e.event_timestamp) as lastLoginDate\n" +
            "from login_event le\n" +
            "join \"event\" e\n" +
            "on le.event_id = e.event_id\n" +
            "where le.user_id = :user_id and e.event_timestamp < :date", nativeQuery = true)
    LocalDateTime getUserLastLoginBeforeDate(@Param("user_id") String userId, @Param("date") LocalDate date);


    /**
     * Returns last day user logged in
     *
     * @param  date  spe
     * @return      last login date for a user
     */
    @Query(value = "select ((select max(e.event_timestamp)\\:\\:date from \"event\" e) - :date\\:\\:date) as daysDiff", nativeQuery = true)
    int getDiffDays(@Param("date") LocalDateTime date);


    @Query(value = "" +
            "select tt.numberOfTransactions, tt.totalRevenue, ll.numberOfLogins, ll.numberOfActiveUsers\n" +
            "from (\n" +
            "        select count(te.transaction_amount) as numberOfTransactions, coalesce(sum(te.transaction_amount * er.conversion_value), 0) as totalRevenue\n" +
            "        from transaction_event te\n" +
            "        join exchange_rate er\n" +
            "        on te.currency_iso_code = er.currency_from\n" +
            "        join \"event\" e\n" +
            "        on te.event_id = e.event_id\n" +
            "        where er.currency_to = 'USD') as tt, \n" +
            "    (\n" +
            "        select count(le.user_id) as numberOfLogins, count(distinct(le.user_id)) as numberOfActiveUsers\n" +
            "        from login_event le\n" +
            "        join \"event\" e\n" +
            "        on le.event_id = e.event_id) as ll", nativeQuery = true)
    GameInfo getGameInfo();

    @Query(value = "" +
            "select tt.numberOfTransactions, tt.totalRevenue, ll.numberOfLogins, ll.numberOfActiveUsers\n" +
            "from (\n" +
            "        select count(te.transaction_amount) as numberOfTransactions, coalesce(sum(te.transaction_amount * er.conversion_value), 0) as totalRevenue\n" +
            "        from transaction_event te\n" +
            "        join exchange_rate er\n" +
            "        on te.currency_iso_code = er.currency_from\n" +
            "        join \"event\" e\n" +
            "        on te.event_id = e.event_id\n" +
            "        where er.currency_to = 'USD' and e.event_timestamp >= :date_from and e.event_timestamp < :date_to) as tt, \n" +
            "    (\n" +
            "        select count(le.user_id) as numberOfLogins, count(distinct(le.user_id)) as numberOfActiveUsers\n" +
            "        from login_event le\n" +
            "        join \"event\" e\n" +
            "        on le.event_id = e.event_id\n" +
            "        where e.event_timestamp >= :date_from and e.event_timestamp < :date_to) as ll", nativeQuery = true)
    GameInfo getGameInfoForDate(@Param("date_from") LocalDate dateFrom, @Param("date_to") LocalDate dateTo);

    @Query(value = "" +
            "select cc.country,\n" +
            "       coalesce(tt.numberOfTransactions, 0) as numberOfTransactions,\n" +
            "       coalesce(tt.totalRevenue, 0) as totalRevenue,\n" +
            "       coalesce(ll.numberOfLogins, 0) as numberOfLogins,\n" +
            "       coalesce(ll.numberOfActiveUsers, 0) as numberOfActiveUsers \n" +
            "from \n" +
            "    (\n" +
            "        select u.country as country, count(te.transaction_amount) as numberOfTransactions, sum(te.transaction_amount * er.conversion_value) as totalRevenue\n" +
            "        from transaction_event te\n" +
            "        join exchange_rate er\n" +
            "        on te.currency_iso_code = er.currency_from\n" +
            "        join \"user\" u\n" +
            "        on te.user_id = u.user_id\n" +
            "        join \"event\" e\n" +
            "        on te.event_id = e.event_id\n" +
            "        where er.currency_to = 'USD'\n" +
            "        group by u.country) tt\n" +
            "join\n" +
            "    (\n" +
            "        select u.country as country, count(le.user_id) as numberOfLogins, count(distinct(le.user_id)) as numberOfActiveUsers\n" +
            "        from login_event le\n" +
            "        join \"event\" e\n" +
            "        on le.event_id = e.event_id\n" +
            "        join \"user\" u\n" +
            "        on le.user_id = u.user_id\n" +
            "        group by u.country) as ll\n" +
            "on ll.country = tt.country\n" +
            "right join (select distinct(country) from \"user\" u2) as cc\n" +
            "on cc.country = tt.country", nativeQuery = true)
    List<GameInfoCountries> getGameInfoByCountry();

    @Query(value = "" +
            "select cc.country,\n" +
            "       coalesce(tt.numberOfTransactions, 0) as numberOfTransactions,\n" +
            "       coalesce(tt.totalRevenue, 0) as totalRevenue,\n" +
            "       coalesce(ll.numberOfLogins, 0) as numberOfLogins,\n" +
            "       coalesce(ll.numberOfActiveUsers, 0) as numberOfActiveUsers \n" +
            "from \n" +
            "    (\n" +
            "        select u.country as country, count(te.transaction_amount) as numberOfTransactions, sum(te.transaction_amount * er.conversion_value) as totalRevenue\n" +
            "        from transaction_event te\n" +
            "        join exchange_rate er\n" +
            "        on te.currency_iso_code = er.currency_from\n" +
            "        join \"user\" u\n" +
            "        on te.user_id = u.user_id\n" +
            "        join \"event\" e\n" +
            "        on te.event_id = e.event_id\n" +
            "        where er.currency_to = 'USD' and e.event_timestamp >= :date_from and e.event_timestamp < :date_to\n" +
            "        group by u.country) tt\n" +
            "join\n" +
            "    (\n" +
            "        select u.country as country, count(le.user_id) as numberOfLogins, count(distinct(le.user_id)) as numberOfActiveUsers\n" +
            "        from login_event le\n" +
            "        join \"event\" e\n" +
            "        on le.event_id = e.event_id\n" +
            "        join \"user\" u\n" +
            "        on le.user_id = u.user_id\n" +
            "        where e.event_timestamp >= :date_from and e.event_timestamp < :date_to\n" +
            "        group by u.country) as ll\n" +
            "on ll.country = tt.country\n" +
            "right join (select distinct(country) from \"user\") as cc\n" +
            "on cc.country = tt.country", nativeQuery = true)
    List<GameInfoCountries> getGameInfoByCountryForDate(@Param("date_from") LocalDate dateFrom, @Param("date_to") LocalDate dateTo);

    public static interface GameInfo {
        int getNumberOfLogins();

        int getNumberOfActiveUsers();

        int getNumberOfTransactions();

        double getTotalRevenue();
    }

    public static interface GameInfoCountries {
        String getCountry();

        int getNumberOfLogins();

        int getNumberOfActiveUsers();

        int getNumberOfTransactions();

        double getTotalRevenue();
    }
    @Transactional
    @Modifying
    @Query(value = "delete from \"event\" e\n" +
            "where e.event_timestamp not BETWEEN '2010-05-08 00:00:00' and '2010-05-22 23:59:59'", nativeQuery = true)
    void removeInvalidEventsByDate();
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM \"event\"\n" +
            "WHERE event_id IN (SELECT event_id\n" +
            "FROM (SELECT e1.event_id,\n" +
            "\t\t\tROW_NUMBER() OVER (partition BY e1.event_timestamp, le.user_id ORDER BY e1.event_id) AS rnum\n" +
            "\tFROM \"event\" e1\n" +
            "\tjoin login_event le\n" +
            "\ton le.event_id = e1.event_id) t\n" +
            "WHERE t.rnum > 1)", nativeQuery = true)
    void removeDuplicateLogins();
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM \"event\"\n" +
            "WHERE event_id IN (\n" +
            "\tSELECT \n" +
            "\t\te1.event_id\n" +
            "\tFROM \"event\" e1\n" +
            "\tjoin login_event le\n" +
            "\ton le.event_id = e1.event_id\n" +
            "\tleft join \"user\" u\n" +
            "\ton le.user_id = u.user_id\n" +
            "\twhere u.user_id is null\n" +
            ")", nativeQuery = true)
    void removeLoginsForNoRegistratedUsers();
    @Transactional
    @Modifying
    @Query(value = "delete \n" +
            "from \"event\" e1\n" +
            "using login_event le, registration_event re, \"event\" e2\n" +
            "where e1.event_id = le.event_id and le.user_id = re.user_id and re.event_id = e2.event_id and e1.event_timestamp <= e2.event_timestamp", nativeQuery = true)
    void removeLoginsBeforeRegistration();
}
