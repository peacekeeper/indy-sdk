package org.hyperledger.indy.sdk;

import java.io.File;

import org.hyperledger.indy.sdk.LibSovrin;
import org.hyperledger.indy.sdk.ledger.Ledger;
import org.hyperledger.indy.sdk.ledger.PoolJSONParameters.OpenPoolLedgerJSONParameter;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.hyperledger.indy.sdk.wallet.WalletResults.CloseWalletResult;
import org.hyperledger.indy.sdk.wallet.WalletResults.CreateWalletResult;
import org.hyperledger.indy.sdk.wallet.WalletResults.DeleteWalletResult;
import org.hyperledger.indy.sdk.wallet.WalletResults.OpenWalletResult;
import org.junit.Assert;

import junit.framework.TestCase;

public class WalletTest extends TestCase {

	private Ledger ledger;
	
	@Override
	protected void setUp() throws Exception {

		if (! LibSovrin.isInitialized()) LibSovrin.init(new File("./lib/libsovrin.so"));

		OpenPoolLedgerJSONParameter openPoolLedgerOptions = new OpenPoolLedgerJSONParameter(null, null, null);
		this.ledger = Ledger.openPoolLedgerAsync("myconfig", openPoolLedgerOptions).get().getLedger();
	}

	@Override
	protected void tearDown() throws Exception {

		this.ledger.close();
	}

	public void testWallet() throws Exception {

		Wallet wallet;
		
		CreateWalletResult result1 = Wallet.createAsync("default", "mywallet", null, null, null).get();
		Assert.assertNotNull(result1);

		OpenWalletResult result2 = Wallet.openAsync("mywallet", null, null).get();
		Assert.assertNotNull(result2);
		wallet = result2.getWallet();

		CloseWalletResult result3 = wallet.closeAsync().get();
		Assert.assertNotNull(result3);

		DeleteWalletResult result4 = Wallet.deleteAsync("mywallet", null).get();
		Assert.assertNotNull(result4);
	}
}
