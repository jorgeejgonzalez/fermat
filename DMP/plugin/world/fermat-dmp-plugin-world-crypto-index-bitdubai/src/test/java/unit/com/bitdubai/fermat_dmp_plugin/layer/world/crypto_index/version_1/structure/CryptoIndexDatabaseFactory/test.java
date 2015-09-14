package unit.com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.version_1.structure.CryptoIndexDatabaseFactory;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PlatformDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.database.CryptoIndexDao;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.database.CryptoIndexDatabaseConstants;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.database.CryptoIndexDatabaseFactory;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.interfaces.CryptoIndexInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by francisco on 10/09/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class test {

    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;
    @Mock
    private    Database mockDatabase;
    @Mock
    private DatabaseFactory mockDatabaseFactory;
    @Mock
    private DatabaseTable mockTable;

    @Mock
    private DatabaseTableFactory mockCryptoIndexTableFactory;
    @Mock
    private DatabaseTableRecord mockTableRecord;

    private UUID testOwnerId;

    @Mock
    private CryptoIndexDatabaseFactory cryptoIndexDatabaseFactory;
    @Mock
    private List<DatabaseTableRecord> mockRecords;
    @Mock
    private CryptoIndexDao cryptoIndexDao;

    DatabaseTable mockDatabaseTable = Mockito.mock(DatabaseTable.class);
    DatabaseTableRecord mockDatabaseTableRecord = Mockito.mock(DatabaseTableRecord.class);
    Database mockDatabase1 = Mockito.mock(Database.class);

    private void setUpIds(){
        testOwnerId = UUID.randomUUID();
    }

    private void setUpMockitoGeneralRules() throws Exception{
        when(mockPluginDatabaseSystem.createDatabase(testOwnerId, CryptoIndexDatabaseConstants.CRYPTO_INDEX_DATABASE_NAME)).thenReturn(mockDatabase);
        when(mockDatabase.getDatabaseFactory()).thenReturn(mockDatabaseFactory);
        when(mockDatabaseFactory.newTableFactory(testOwnerId, CryptoIndexDatabaseConstants.CRYPTO_INDEX_TABLE_NAME)).thenReturn(mockCryptoIndexTableFactory);
        when(mockDatabase.getTable(CryptoIndexDatabaseConstants.CRYPTO_INDEX_TABLE_NAME)).thenReturn(mockTable);
        when(mockTable.getEmptyRecord()).thenReturn(mockTableRecord);
        when(mockTable.getRecords()).thenReturn(mockRecords);


    }
    @Before
    public void setUp() throws Exception{
        setUpIds();
        when(mockPluginDatabaseSystem.openDatabase(testOwnerId, CryptoIndexDatabaseConstants.CRYPTO_INDEX_DATABASE_NAME)).thenReturn(mockDatabase);
        cryptoIndexDao = new CryptoIndexDao(mockPluginDatabaseSystem, testOwnerId);
        cryptoIndexDao.initializeDatabase();
        setUpMockitoGeneralRules();

    }
    @Test
    public void test() throws Exception{


       List<CryptoIndexInterface> list = null;
        when(cryptoIndexDao.getLastExchangeRateList("BTC","USD")).thenReturn(list);

        System.out.println(list.get(0));
    }
}
