package org.hyperledger.indy.sdk.pool;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.hyperledger.indy.sdk.ErrorCode;
import org.hyperledger.indy.sdk.ErrorCodeMatcher;
import org.hyperledger.indy.sdk.IndyIntegrationTest;
import org.hyperledger.indy.sdk.utils.PoolUtils;
import org.junit.Test;

public class ClosePoolTest extends IndyIntegrationTest {

	@Test
	public void testClosePoolWorks() throws Exception {
		Pool pool = PoolUtils.createAndOpenPoolLedger();
		assertNotNull(pool);
		openedPools.add(pool);

		pool.closePoolLedger().get();
		openedPools.remove(pool);
	}

	@Test
	public void testClosePoolWorksForTwice() throws Exception {
		thrown.expectCause(new ErrorCodeMatcher(ErrorCode.PoolLedgerInvalidPoolHandle));

		Pool pool = PoolUtils.createAndOpenPoolLedger();
		assertNotNull(pool);
		openedPools.add(pool);

		pool.closePoolLedger().get();
		openedPools.remove(pool);
		pool.closePoolLedger().get();
	}

	@Test
	public void testClosePoolWorksForReopenAfterClose() throws Exception {
		String poolName = PoolUtils.createPoolLedgerConfig();

		Pool pool = Pool.openPoolLedger(poolName, null).get();
		assertNotNull(pool);

		pool.closePoolLedger().get();

		pool = Pool.openPoolLedger(poolName, null).get();
		openedPools.add(pool);
	}

	@Test
	public void testClosePoolWorksForAutoCloseable() throws Exception {
		String poolName = PoolUtils.createPoolLedgerConfig();

		Pool pool = null;

		try (Pool closedPool = Pool.openPoolLedger(poolName, null).get()) {

			pool = closedPool;
			assertFalse(pool.isClosed());
			throw new Exception();
		} catch (Exception e) { }

		assertTrue(pool.isClosed());
	}
}
