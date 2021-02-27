package com.javastudio.postgresql;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

class QueryDataTest {

    private Logger logger = LoggerFactory.getLogger(QueryDataTest.class);

    Integer accountId = 21184;
    Date fromDate = null;
    Date toDate = null;
    int maxCount = 30;
    Long lastTranId = 0L;

    @Test
    void name() {
        logger.info("Call postgresql function");

        try (Connection connection = DataSourceProvider.connect()) {
            CallableStatement statement = connection.prepareCall("select * from cms.sp_cms_account_transactions(?,?,?,?,?)");
            statement.setInt(1, accountId);

            statement.setInt(1, accountId);
            if (fromDate != null) statement.setTimestamp(2, new Timestamp(fromDate.getTime()));
            else statement.setNull(2, Types.TIMESTAMP);
            if (fromDate != null) statement.setTimestamp(3, new Timestamp(toDate.getTime()));
            else statement.setNull(3, Types.TIMESTAMP);
            statement.setInt(4, maxCount);
            if (lastTranId != null) statement.setLong(5, lastTranId);
            else statement.setNull(5, Types.BIGINT);

            statement.setQueryTimeout(10);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                logger.info("--------------------------------------------------");
                logger.info("{}", resultSet.getLong("tran_id"));
                logger.info("{}", resultSet.getInt("tran_type_id"));
                logger.info("{}", resultSet.getString("tran_type_name"));
                logger.info("date_time: {}", resultSet.getTimestamp("date_time"));
                logger.info("{}", resultSet.getString("shamsi_date"));
                logger.info("{}", resultSet.getLong("tran_amount"));
                logger.info("{}", resultSet.getByte("operation"));
                logger.info("{}", resultSet.getString("description"));
                logger.info("{}", resultSet.getLong("balance"));
                logger.info("{}", resultSet.getString("card_rrn"));
                logger.info("{}", resultSet.getString("card_pan"));
                logger.info("{}", resultSet.getString("card_req_date_time"));
                logger.info("{}", resultSet.getShort("ref_gateway_id"));
                logger.info("{}", resultSet.getString("gateway_ds"));
                logger.info("{}", resultSet.getInt("host_id"));
                logger.info("{}", resultSet.getInt("ref_tran_type"));
                logger.info("{}", resultSet.getString("ref_tran_type_ds"));
                logger.info("{}", resultSet.getLong("ref_tran_id"));
                logger.info("{}", resultSet.getString("payment_id"));
                logger.info("{}", resultSet.getString("agent_mobile_number"));
                logger.info("{}", resultSet.getLong("terminal_id"));

                break;
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void showTimeZone() {
        try (Connection connection = DataSourceProvider.connect()) {

            PreparedStatement statement = connection.prepareStatement("SHOW TIMEZONE");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                logger.info(resultSet.getString(1));
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void setTimeZone() {
        try (Connection connection = DataSourceProvider.connect()) {

            connection.prepareStatement("SET TIME ZONE 'Europe/Istanbul'").execute();
            PreparedStatement statement = connection.prepareStatement("SHOW TIMEZONE");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                logger.info(resultSet.getString(1));
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void sdasafsdfsd() {
        try (Connection connection = DataSourceProvider.connect()) {

            PreparedStatement statement = connection.prepareStatement("insert ");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                logger.info(resultSet.getString(1));
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Test
    void depositAccount() {

        int accountId = 21184;
        long amount = 100;
        long maxBalance = 0;
        int tranTypeId = 2;
        String description = "Test";
        long acquirerInstitutionIdentificationCode = 581672011;
        String rrn = String.format("%012d", System.currentTimeMillis() / 10);
        String mobileNo = "05520000002";
        int accountProductCode = 101;
        long refTranId = System.currentTimeMillis();
        short gatewayId = 130;
        int refTranType = 2491;
        int mobileId = 0;
        String agentNo = "";
        String paymentId = "";
        Integer hostId = 0;


        try (Connection connection = DataSourceProvider.getConnectionPool().getConnection()) {
            connection.prepareStatement("SET TIME ZONE 'Europe/Istanbul'").execute();

            CallableStatement statement = connection.prepareCall("{call cms.sp_cms_account_deposit(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setInt(1, accountId);
            statement.setLong(2, amount);
            statement.setLong(3, maxBalance);
            statement.setInt(4, tranTypeId);
            statement.setString(5, description);
            statement.setDate(6, new Date(0));
            statement.setInt(7, 0);
            statement.setLong(8, 0);
            statement.setLong(9, acquirerInstitutionIdentificationCode);
            statement.setString(10, rrn);
            statement.setString(11, HelperUtils.format(HelperUtils.now()));
            statement.setString(12, HelperUtils.getSubscriberPointCardNo(mobileNo));
            statement.setInt(13, accountProductCode);

            if (refTranId > 0)
                statement.setLong(14, refTranId);
            else
                statement.setNull(14, Types.BIGINT);

            if (gatewayId >= 0)
                statement.setShort(15, gatewayId);
            else
                statement.setNull(15, Types.SMALLINT);

            if (refTranType >= 0)
                statement.setInt(16, refTranType);
            else
                statement.setNull(16, Types.INTEGER);

            if (mobileId >= 0)
                statement.setInt(17, mobileId);
            else
                statement.setNull(17, Types.INTEGER);

            statement.registerOutParameter(18, Types.NUMERIC);
            statement.registerOutParameter(19, Types.NUMERIC);
            statement.registerOutParameter(20, Types.INTEGER);
            statement.registerOutParameter(21, Types.BIGINT);

            Date reqDateTime = null;
            if (reqDateTime != null)
                statement.setTimestamp(22, new Timestamp(reqDateTime.getTime()));
            else statement.setNull(22, Types.DATE);

            statement.setString(23, agentNo);
            statement.setString(24, paymentId);

            if (hostId != null) statement.setInt(25, hostId);
            else statement.setNull(25, Types.INTEGER);

            logger.info("executing query: {}", statement.toString());
            System.out.println("executing query: " + statement.toString());

            statement.setQueryTimeout(10);
            statement.execute();

            SQLWarning warnings = statement.getWarnings();
            if (warnings != null) {
                System.out.println("---Warning---");
                while (warnings != null) {
                    System.out.println("Message: " + warnings.getMessage());
                    System.out.println("SQLState: " + warnings.getSQLState());
                    System.out.print("Vendor error code: ");
                    System.out.println(warnings.getErrorCode());
                    System.out.println("");
                    warnings = warnings.getNextWarning();
                }
            }

            //-1:(curent_balance + tran_amount > maxBalance) -2: account not found, -9:other errors
            int res = statement.getInt(20);

//            long balance = statement.getBigDecimal(19).longValue();
//            long appliedAmount = statement.getBigDecimal(18).longValue();
//            long resTranId = statement.getLong(21);
            logger.info("response code: {}", res);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}