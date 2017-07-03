package org.hyperledger.indy.sdk.wallet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.hyperledger.indy.sdk.LibSovrin;
import org.hyperledger.indy.sdk.SovrinException;
import org.hyperledger.indy.sdk.SovrinJava;
import org.hyperledger.indy.sdk.ledger.Ledger;
import org.hyperledger.indy.sdk.wallet.SignusJSONParameters.CreateAndStoreMyDidJSONParameter;
import org.hyperledger.indy.sdk.wallet.SignusResults.CreateAndStoreMyDidResult;
import org.hyperledger.indy.sdk.wallet.SignusResults.DecryptResult;
import org.hyperledger.indy.sdk.wallet.SignusResults.EncryptResult;
import org.hyperledger.indy.sdk.wallet.SignusResults.ReplaceKeysResult;
import org.hyperledger.indy.sdk.wallet.SignusResults.SignResult;
import org.hyperledger.indy.sdk.wallet.SignusResults.StoreTheirDidResult;
import org.hyperledger.indy.sdk.wallet.SignusResults.VerifySignatureResult;
import org.hyperledger.indy.sdk.wallet.WalletResults.CloseWalletResult;
import org.hyperledger.indy.sdk.wallet.WalletResults.CreateWalletResult;
import org.hyperledger.indy.sdk.wallet.WalletResults.DeleteWalletResult;
import org.hyperledger.indy.sdk.wallet.WalletResults.OpenWalletResult;
import org.hyperledger.indy.sdk.wallet.WalletResults.WalletSetSeqNoForValueResult;

import com.sun.jna.Callback;

/**
 * wallet.rs API
 */
public class Wallet extends SovrinJava.API {

	private final int walletHandle;

	private Wallet(int walletHandle) {

		this.walletHandle = walletHandle;
	}

	public int getWalletHandle() {

		return this.walletHandle;
	}

	/*
	 * wallet.rs API STATIC METHODS
	 */

	/* IMPLEMENT LATER
	 * public Future<...> registerWalletType(
				...) throws SovrinException;*/

	public static Future<CreateWalletResult> createWallet(
			String poolName,
			String name,
			String xtype,
			String config,
			String credentials) throws SovrinException {

		final CompletableFuture<CreateWalletResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				CreateWalletResult result = new CreateWalletResult();
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_create_wallet(
				FIXED_COMMAND_HANDLE, 
				poolName, 
				name,
				xtype,
				config,
				credentials,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<OpenWalletResult> openWallet(
			String name,
			String runtimeConfig,
			String credentials) throws SovrinException {

		final CompletableFuture<OpenWalletResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, int handle) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				Wallet wallet = new Wallet(handle);

				OpenWalletResult result = new OpenWalletResult(wallet);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_open_wallet(
				FIXED_COMMAND_HANDLE, 
				name,
				runtimeConfig,
				credentials,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<CloseWalletResult> closeWallet(
			Wallet wallet) throws SovrinException {

		final CompletableFuture<CloseWalletResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				CloseWalletResult result = new CloseWalletResult();
				future.complete(result);
			}
		};

		int handle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_close_wallet(
				FIXED_COMMAND_HANDLE, 
				handle, 
				cb);

		checkResult(result);

		return future;
	}

	public static Future<DeleteWalletResult> deleteWallet(
			String name,
			String credentials) throws SovrinException {

		final CompletableFuture<DeleteWalletResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				DeleteWalletResult result = new DeleteWalletResult();
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_delete_wallet(
				FIXED_COMMAND_HANDLE, 
				name,
				credentials,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<WalletSetSeqNoForValueResult> walletSetSeqNoForValue(
			Wallet wallet, 
			String walletKey,
			String configName) throws SovrinException {

		final CompletableFuture<WalletSetSeqNoForValueResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				WalletSetSeqNoForValueResult result = new WalletSetSeqNoForValueResult();
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_wallet_set_seq_no_for_value(
				FIXED_COMMAND_HANDLE, 
				walletHandle,
				walletKey, 
				cb);

		checkResult(result);

		return future;
	}
	
	/*
	 * signus.rs API STATIC METHODS
	 */
	
	private static Future<CreateAndStoreMyDidResult> createAndStoreMyDid(
			Wallet wallet,
			CreateAndStoreMyDidJSONParameter didJson) throws SovrinException {

		final CompletableFuture<CreateAndStoreMyDidResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String did, String verkey, String pk) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				CreateAndStoreMyDidResult result = new CreateAndStoreMyDidResult(did, verkey, pk);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_create_and_store_my_did(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				didJson == null ? null : didJson.toJson(),
				cb);

		checkResult(result);

		return future;
	}

	private static Future<ReplaceKeysResult> replaceKeys(
			Wallet wallet,
			String did,
			String identityJson) throws SovrinException {

		final CompletableFuture<ReplaceKeysResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String verkey, String pk) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				ReplaceKeysResult result = new ReplaceKeysResult(verkey, pk);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_replace_keys(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				did,
				identityJson,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<StoreTheirDidResult> storeTheirDid(
			Wallet wallet,
			String identityJson) throws SovrinException {

		final CompletableFuture<StoreTheirDidResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				StoreTheirDidResult result = new StoreTheirDidResult();
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_store_their_did(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				identityJson,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<SignResult> sign(
			Wallet wallet,
			String did,
			String msg) throws SovrinException {

		final CompletableFuture<SignResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String signature) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				SignResult result = new SignResult(signature);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_sign(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				did,
				msg,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<VerifySignatureResult> verifySignature(
			Wallet wallet,
			Ledger ledger,
			String did,
			String signedMsg) throws SovrinException {

		final CompletableFuture<VerifySignatureResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, boolean valid) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				VerifySignatureResult result = new VerifySignatureResult(valid);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();
		int poolHandle = ledger.getPoolHandle();

		int result = LibSovrin.api.sovrin_verify_signature(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				poolHandle,
				did,
				signedMsg,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<EncryptResult> encrypt(
			Wallet wallet,
			String did,
			String msg) throws SovrinException {

		final CompletableFuture<EncryptResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String encryptedMsg) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				EncryptResult result = new EncryptResult(encryptedMsg);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_encrypt(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				did,
				msg,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<DecryptResult> decrypt(
			Wallet wallet,
			String did,
			String encryptedMsg) throws SovrinException {

		final CompletableFuture<DecryptResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String decryptedMsg) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				DecryptResult result = new DecryptResult(decryptedMsg);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_decrypt(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				did,
				encryptedMsg,
				cb);

		checkResult(result);

		return future;
	}

	/*
	 * INSTANCE METHODS
	 */

	public Future<CloseWalletResult> closeWallet(
			) throws SovrinException {

		return closeWallet(this);
	}

	public Future<WalletSetSeqNoForValueResult> walletSetSeqNoForValue(
			String walletKey,
			String configName) throws SovrinException {

		return walletSetSeqNoForValue(this, walletKey, configName);
	}
	
	public Future<CreateAndStoreMyDidResult> createAndStoreMyDid(
			CreateAndStoreMyDidJSONParameter didJson) throws SovrinException{
		return createAndStoreMyDid(this, didJson);
	}
	
	public Future<ReplaceKeysResult> replaceKeys(
			String did,
			String identityJson) throws SovrinException {
		return replaceKeys(this, did, identityJson);
	}
	
	public Future<StoreTheirDidResult> storeTheirDid(
			String identityJson) throws SovrinException {
		return storeTheirDid(this, identityJson);
	}
	
	public Future<SignResult> sign(
			String did,
			String msg) throws SovrinException{
		return sign(this, did, msg);
	}
	
	//Should a corresponding version of this be available on the Ledger class?
	public Future<VerifySignatureResult> verifySignature(
			Ledger ledger,
			String did,
			String signedMsg) throws SovrinException{
		return verifySignature(this, ledger, did, signedMsg);
	}
	
	public Future<EncryptResult> encrypt(
			String did,
			String msg) throws SovrinException{
		return encrypt(this, did, msg);
	}
	
	public Future<DecryptResult> decrypt(
			String did,
			String encryptedMsg) throws SovrinException{
		return decrypt(this, did, encryptedMsg);
	}
	
}
