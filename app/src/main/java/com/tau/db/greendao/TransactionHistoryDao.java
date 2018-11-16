package com.mofei.tau.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mofei.tau.transaction.TransactionHistory;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TRANSACTION_HISTORY".
*/
public class TransactionHistoryDao extends AbstractDao<TransactionHistory, Long> {

    public static final String TABLENAME = "TRANSACTION_HISTORY";

    /**
     * Properties of entity TransactionHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TxId = new Property(1, String.class, "txId", false, "TX_ID");
        public final static Property SentOrReceived = new Property(2, String.class, "sentOrReceived", false, "SENT_OR_RECEIVED");
        public final static Property FromAddress = new Property(3, String.class, "fromAddress", false, "FROM_ADDRESS");
        public final static Property ToAddress = new Property(4, String.class, "toAddress", false, "TO_ADDRESS");
        public final static Property Time = new Property(5, long.class, "time", false, "TIME");
        public final static Property Confirmations = new Property(6, int.class, "confirmations", false, "CONFIRMATIONS");
        public final static Property Value = new Property(7, String.class, "value", false, "VALUE");
        public final static Property Result = new Property(8, boolean.class, "result", false, "RESULT");
        public final static Property Message = new Property(9, String.class, "message", false, "MESSAGE");
    }


    public TransactionHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public TransactionHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TRANSACTION_HISTORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TX_ID\" TEXT," + // 1: txId
                "\"SENT_OR_RECEIVED\" TEXT," + // 2: sentOrReceived
                "\"FROM_ADDRESS\" TEXT," + // 3: fromAddress
                "\"TO_ADDRESS\" TEXT," + // 4: toAddress
                "\"TIME\" INTEGER NOT NULL ," + // 5: time
                "\"CONFIRMATIONS\" INTEGER NOT NULL ," + // 6: confirmations
                "\"VALUE\" TEXT," + // 7: value
                "\"RESULT\" INTEGER NOT NULL ," + // 8: result
                "\"MESSAGE\" TEXT);"); // 9: message
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TRANSACTION_HISTORY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TransactionHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String txId = entity.getTxId();
        if (txId != null) {
            stmt.bindString(2, txId);
        }
 
        String sentOrReceived = entity.getSentOrReceived();
        if (sentOrReceived != null) {
            stmt.bindString(3, sentOrReceived);
        }
 
        String fromAddress = entity.getFromAddress();
        if (fromAddress != null) {
            stmt.bindString(4, fromAddress);
        }
 
        String toAddress = entity.getToAddress();
        if (toAddress != null) {
            stmt.bindString(5, toAddress);
        }
        stmt.bindLong(6, entity.getTime());
        stmt.bindLong(7, entity.getConfirmations());
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(8, value);
        }
        stmt.bindLong(9, entity.getResult() ? 1L: 0L);
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(10, message);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TransactionHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String txId = entity.getTxId();
        if (txId != null) {
            stmt.bindString(2, txId);
        }
 
        String sentOrReceived = entity.getSentOrReceived();
        if (sentOrReceived != null) {
            stmt.bindString(3, sentOrReceived);
        }
 
        String fromAddress = entity.getFromAddress();
        if (fromAddress != null) {
            stmt.bindString(4, fromAddress);
        }
 
        String toAddress = entity.getToAddress();
        if (toAddress != null) {
            stmt.bindString(5, toAddress);
        }
        stmt.bindLong(6, entity.getTime());
        stmt.bindLong(7, entity.getConfirmations());
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(8, value);
        }
        stmt.bindLong(9, entity.getResult() ? 1L: 0L);
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(10, message);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TransactionHistory readEntity(Cursor cursor, int offset) {
        TransactionHistory entity = new TransactionHistory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // txId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sentOrReceived
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // fromAddress
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // toAddress
            cursor.getLong(offset + 5), // time
            cursor.getInt(offset + 6), // confirmations
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // value
            cursor.getShort(offset + 8) != 0, // result
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // message
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TransactionHistory entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTxId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSentOrReceived(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFromAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setToAddress(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTime(cursor.getLong(offset + 5));
        entity.setConfirmations(cursor.getInt(offset + 6));
        entity.setValue(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setResult(cursor.getShort(offset + 8) != 0);
        entity.setMessage(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TransactionHistory entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TransactionHistory entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TransactionHistory entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
