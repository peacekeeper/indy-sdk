package org.hyperledger.indy.sdk.wallet;

import org.hyperledger.indy.sdk.ErrorCode;
import org.hyperledger.indy.sdk.ErrorCodeMatcher;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.IndyIntegrationTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;


public class CloseWalletTest extends IndyIntegrationTest {

	@Test
	public void testCloseWalletWorks() throws Exception {

		String walletName = "closeWalletWorks";

		Wallet.createWallet("default", walletName, "default", null, null).get();

		Wallet wallet = Wallet.openWallet(walletName, null, null).get();
		assertNotNull(wallet);

		wallet.closeWallet().get();
	}

	@Test
	public void testCloseWalletWorksForTwice() throws Exception {

		thrown.expect(ExecutionException.class);
		thrown.expectCause(new ErrorCodeMatcher(ErrorCode.WalletInvalidHandle));

		String walletName = "closeWalletWorksForTwice";

		Wallet.createWallet("default", walletName, "default", null, null).get();

		Wallet wallet = Wallet.openWallet(walletName, null, null).get();
		assertNotNull(wallet);

		wallet.closeWallet().get();
		wallet.closeWallet().get();
	}

	@Test
	public void testCloseWalletWorksForPlugged() throws Exception {
		WalletTypeInmem.getInstance().clear();

		String walletName = "testCloseWalletWorksForPlugged";

		Wallet.createWallet("default", walletName, "inmem", null, null).get();

		Wallet wallet = Wallet.openWallet(walletName, null, null).get();
		wallet.closeWallet().get();
		Wallet.openWallet(walletName, null, null).get();

		WalletTypeInmem.getInstance().clear();
	}

	@Test
	public void testCloseWalletWorksForAutoCloseable() throws Exception {

		String walletName = "closeWalletWorksForAutoCloseable";

		Wallet.createWallet("default", walletName, "default", null, null).get();

		Wallet wallet = null;

		try (Wallet closedWallet = Wallet.openWallet(walletName, null, null).get()) {

			wallet = closedWallet;
			assertFalse(wallet.isClosed());
			throw new Exception();
		} catch (Exception e) { if (e instanceof IndyException) throw e; }

		assertTrue(wallet.isClosed());
	}

	@Test
	public void testCloseWalletWorksForFinalize() throws Exception {

		String walletName = "closeWalletWorksForFinalize";

		Wallet.createWallet("default", walletName, "default", null, null).get();

		Wallet wallet = Wallet.openWallet(walletName, null, null).get();
		WeakReference<Wallet> reference = new WeakReference<Wallet> (wallet);
		wallet = null;
		System.gc();

		assertTrue(reference.get() == null);
	}
}
