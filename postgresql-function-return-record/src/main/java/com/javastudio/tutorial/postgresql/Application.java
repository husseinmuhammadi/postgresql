package com.javastudio.tutorial.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.StringJoiner;

public class Application {
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        PostgresqlDatabase database = new PostgresqlDatabase();
        database.withdrawal();
    }
}

class PostgresqlDatabase {

    public static final Logger LOGGER = LoggerFactory.getLogger(PostgresqlDatabase.class);

    final String url = "jdbc:postgresql://172.25.25.117:5432/cmsdb";
    final String username = "cms_usr";
    final String password = "SjIDt3cV3GEaxZse";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     * @throws java.sql.SQLException
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void withdrawal() {
        Object[] params = new Object[]{
                1,
                1,
                1L,
                1L,
                1,
                "A",
                Date.valueOf("2020-07-24"),
                0,
                1L, // v_card_reqid
                1L, // v_card_acquireiin
                "12345678", // v_card_rrn
                "13990423", // v_card_reqdatetime character varying,
                "5022121012345678", // v_card_pan character varying,
                1L, // v_reftranid bigint,
                (short) 1, // v_refgatewayid smallint,
                1, // v_reftrantype integer,
                1, // v_refmobileid integer,
        };
        int[] outTypes = new int[]{Types.NUMERIC, Types.NUMERIC, Types.INTEGER, Types.BIGINT};
        boolean applyDefaultParameters = false;
        Object[] defaultParams = new Object[]{
                "09122113358", // v_agentmobilenumber character varying DEFAULT NULL::character varying
                "1234", // v_paymentid character varying DEFAULT NULL::character varying
                Timestamp.valueOf("2019-04-21 14:17:02.0"), // v_date timestamp without time zone DEFAULT NULL::timestamp without time zone
                BigDecimal.valueOf(1), // v_notsettleddepositamount_in numeric DEFAULT NULL::numeric
                1, // v_hostid integer DEFAULT NULL::integer
        };

        int parameterNo = params.length + outTypes.length + (applyDefaultParameters ? defaultParams.length: 0);
        String sql = "{call sp_ins_cms_account_withdraw" + parameterSequence(parameterNo) + "}";

        System.out.println(sql);
        try (Connection conn = this.connect()) {
            conn.setAutoCommit(false);
            try (CallableStatement func = conn.prepareCall(sql)) {

                int index = 1;
                for (int i = 0; i < params.length; i++) {
                    System.out.println(index + ") " + params[i].getClass() + ": " + params[i]);
                    func.setObject(index++, params[i]);
                }

                int outIndex = index;

                for (int i = 0; i < outTypes.length; i++) {
                    func.registerOutParameter(index++, outTypes[i]);
                }

                if (applyDefaultParameters) {
                    for (int i = 0; i < defaultParams.length; i++) {
                        System.out.println(index + ") " + defaultParams[i].getClass() + ": " + defaultParams[i]);
                        func.setObject(index++, defaultParams[i]);
                    }
                }

                func.execute();

                for (int i = 0; i < outTypes.length; i++) {
                    String value = "";
                    switch (outTypes[i]) {
                        case Types.NUMERIC:
                            value = String.valueOf(func.getBigDecimal(outIndex));
                            break;
                        case Types.INTEGER:
                            value = String.valueOf(func.getInt(outIndex));
                            break;

                    }
                    System.out.println(String.format("out%d: %s", i + 1, value));

                    outIndex++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String parameterSequence(int n) {
        StringJoiner joiner = new StringJoiner(",", "(", ")");
        for (int i = 0; i < n; i++) {
            joiner.add("?");
        }
        return joiner.toString();
    }

}

class AutoRollback implements AutoCloseable {
    private Connection conn;
    private boolean committed;

    public AutoRollback(Connection conn) throws SQLException {
        this.conn = conn;
    }

    public void commit() throws SQLException {
        conn.commit();
        committed = true;
    }

    @Override
    public void close() throws SQLException {
        if (!committed) {
            conn.rollback();
        }
    }
}
