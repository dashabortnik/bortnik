package com.itechart.bortnik.core.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseUtil {

    //create static Logger for current class
    private static Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    private static DataSource dataSource;
    private static final String  JNDI_LOOKUP_SERVICE = "java:/comp/env/jdbc/bortnik_db";
    static{
        try {
            Context context = new InitialContext();
            Object lookup = context.lookup(JNDI_LOOKUP_SERVICE);
            if(lookup != null){
                dataSource =(DataSource)lookup;
            }else{
                new RuntimeException("JNDI look up issue.");
            }
        } catch (NamingException e) {
            logger.error("Error with JNDI lookup: ", e);
        }
    }
    public static DataSource getDataSource(){
        return dataSource;
    }

}
