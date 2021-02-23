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

        Object[] params = new Object[]{
                20, //v_accountid integer
                0, //v_productcode integer,
                BigDecimal.valueOf(1), //v_amount numeric,
                BigDecimal.valueOf(0), //v_minbalance numeric,
                1, //v_trantypeid integer,
                "", //v_description character varying,
                Date.valueOf("2020-07-26"), //v_taxday date,
                0, //v_userid integer,
                1L, //v_card_reqid bigint,
                1L, //v_card_acquireiin bigint,
                "1111", //v_card_rrn character,
                "", //v_card_reqdatetime character varying,
                "", //v_card_pan character varying,
                1L, //v_reftranid bigint,
                (short) 1, //v_refgatewayid smallint,
                1, //v_reftrantype integer,
                1, //v_refmobileid integer,
        };

        int[] out = new int[]{
                Types.NUMERIC, //INOUT v_appliedamount numeric,
                Types.NUMERIC, //INOUT v_balance numeric,
                Types.INTEGER, //INOUT v_result integer,
                Types.BIGINT, //INOUT v_resulttranid bigint DEFAULT NULL::bigint
//v_agentmobilenumber character varying DEFAULT NULL::character varying,
//v_paymentid character varying DEFAULT NULL::character varying,
//v_date timestamp without time zone DEFAULT NULL::timestamp without time zone,
//v_notsettleddepositamount_in numeric DEFAULT NULL::numeric,
//v_hostid integer DEFAULT NULL::integer,

        };

        database.withdraw(params, out);
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

    public void withdraw(Object[] params, int[] out) {
        int n = params.length + out.length;
        String query = "{call sp_ins_cms_account_withdraw" + parameters(n) + "}";
        System.out.println(query);
        try (Connection connection = this.connect()) {
            try (CallableStatement proc = connection.prepareCall(query)) {
                int index = 1;
                for (int i = 0; i < params.length; i++) {
                    System.out.println(index + ") " + params[i].getClass() + ": " + params[i]);
                    proc.setObject(index++, params[i]);
                }

                int outIndex = index;

                for (int i = 0; i < out.length; i++) {
                    proc.registerOutParameter(index++, out[i]);
                }

//                if (applyDefaultParameters) {
//                    for (int i = 0; i < defaultParams.length; i++) {
//                        System.out.println(index + ") " + defaultParams[i].getClass() + ": " + defaultParams[i]);
//                        procedure.setObject(index++, defaultParams[i]);
//                    }
//                }
                proc.execute();

                for (int i = 0; i < out.length; i++) {
                    System.out.println(proc.getObject(outIndex + i));
                }

                if (proc.getWarnings() != null)
                    System.out.println(proc.getWarnings().getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String parameters(int n) {
        StringJoiner joiner = new StringJoiner(",", "(", ")");
        for (int i = 0; i < n; i++) {
            joiner.add("?");
        }
        return joiner.toString();
    }

}