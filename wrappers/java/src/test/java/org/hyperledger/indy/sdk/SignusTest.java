package org.hyperledger.indy.sdk;

import java.io.File;
import java.util.concurrent.Future;

import org.hyperledger.indy.sdk.LibSovrin;
import org.hyperledger.indy.sdk.ledger.Ledger;
import org.hyperledger.indy.sdk.ledger.PoolJSONParameters.OpenPoolLedgerJSONParameter;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.hyperledger.indy.sdk.wallet.SignusResults.CreateAndStoreMyDidResult;
import org.hyperledger.indy.sdk.wallet.SignusResults.ReplaceKeysResult;
import org.junit.Assert;

import junit.framework.TestCase;

public class SignusTest extends TestCase {

	private Ledger ledger;
	private Wallet wallet;
	
	@Override
	protected void setUp() throws Exception {

		if (! LibSovrin.isInitialized()) LibSovrin.init(new File("./lib/libsovrin.so"));

		OpenPoolLedgerJSONParameter openPoolLedgerOptions = new OpenPoolLedgerJSONParameter(null, null, null);
		this.ledger = Ledger.openPoolLedger("myconfig", openPoolLedgerOptions).get().getLedger();
		this.wallet = Wallet.open("mywallet", null, null).get().getWallet();
	}

	@Override
	protected void tearDown() throws Exception {

		this.wallet.closeWallet();
		this.ledger.close();
		Wallet.delete("mywallet", null);
	}

	public void testSignus() throws Exception {

		Future<CreateAndStoreMyDidResult> future1 = wallet.createAndStoreMyDid(null);
		CreateAndStoreMyDidResult result1 = future1.get();
		Assert.assertNotNull(result1);
		String did1 = result1.getDid();
		String verkey1 = result1.getVerkey();
		String pk1 = result1.getPk();
		Assert.assertNotNull(did1);
		Assert.assertNotNull(verkey1);
		Assert.assertNotNull(pk1);
		System.out.println(did1);
		System.out.println(verkey1);
		System.out.println(pk1);

		Future<ReplaceKeysResult> future2 = wallet.replaceKeys(did1, "{}");
		ReplaceKeysResult result2 = future2.get();
		Assert.assertNotNull(result2);
		String verkey2 = result2.getVerkey();
		String pk2 = result2.getPk();
		Assert.assertNotNull(verkey2);
		Assert.assertNotNull(pk2);
		Assert.assertNotEquals(verkey2, verkey1);
	}
}
