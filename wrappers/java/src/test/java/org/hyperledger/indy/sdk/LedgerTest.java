package org.hyperledger.indy.sdk;

import java.io.File;

import org.hyperledger.indy.sdk.LibSovrin;
import org.hyperledger.indy.sdk.ledger.Ledger;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildGetDdoRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildGetNymRequestResult;
import org.hyperledger.indy.sdk.ledger.PoolJSONParameters.OpenPoolLedgerJSONParameter;
import org.junit.Assert;

import junit.framework.TestCase;

public class LedgerTest extends TestCase {

	private Ledger ledger;

	@Override
	protected void setUp() throws Exception {

		if (! LibSovrin.isInitialized()) LibSovrin.init(new File("./lib/libsovrin.so"));

		OpenPoolLedgerJSONParameter openPoolLedgerOptions = new OpenPoolLedgerJSONParameter(null, null, null);
		this.ledger = Ledger.openPoolLedger("myconfig", openPoolLedgerOptions).get().getLedger();
	}

	@Override
	protected void tearDown() throws Exception {

		this.ledger.close();
	}

	public void testLedger() throws Exception {

		BuildGetDdoRequestResult result1 = Ledger.buildGetDdoRequest("did:sov:21tDAKCERh95uGgKbJNHYp", "did:sov:1yvXbmgPoUm4dl66D7KhyD", "{}").get();
		Assert.assertNotNull(result1);
		String requestJson1 = result1.getRequestJson();
		Assert.assertNotNull(requestJson1);

		BuildGetNymRequestResult result2 = Ledger.buildGetNymRequest("did:sov:21tDAKCERh95uGgKbJNHYp", "did:sov:1yvXbmgPoUm4dl66D7KhyD").get();
		Assert.assertNotNull(result2);
		String requestJson2 = result2.getRequestJson();
		Assert.assertNotNull(requestJson2);
	}
}
