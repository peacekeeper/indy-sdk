package org.hyperledger.indy.sdk.wallet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.hyperledger.indy.sdk.LibSovrin;
import org.hyperledger.indy.sdk.SovrinException;
import org.hyperledger.indy.sdk.SovrinJava;
import org.hyperledger.indy.sdk.ledger.Ledger;
import org.hyperledger.indy.sdk.wallet.AnoncredsResults.IssuerCreateAndStoreClaimDefResult;
import org.hyperledger.indy.sdk.wallet.AnoncredsResults.IssuerCreateAndStoreRevocRegResult;
import org.hyperledger.indy.sdk.wallet.AnoncredsResults.IssuerCreateClaimResult;
import org.hyperledger.indy.sdk.wallet.AnoncredsResults.IssuerRevokeClaimResult;
import org.hyperledger.indy.sdk.wallet.AnoncredsResults.ProverGetClaimOffersResult;
import org.hyperledger.indy.sdk.wallet.AnoncredsResults.ProverStoreClaimOfferResult;
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

	public static Future<CreateWalletResult> create(
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

	public static Future<OpenWalletResult> open(
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

	private static Future<CloseWalletResult> close(
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

	public static Future<DeleteWalletResult> delete(
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

	private static Future<WalletSetSeqNoForValueResult> setSeqNoForValue(
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

	private static Future<SignResult> signMessage(
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

	private static Future<VerifySignatureResult> verifyMessageSignature(
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

	private static Future<EncryptResult> encryptMessage(
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

	private static Future<DecryptResult> decryptMessage(
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
	 * anoncreds.rs API STATIC METHODS
	 */

	private static Future<IssuerCreateAndStoreClaimDefResult> issuerCreateAndStoreClaimDef(
			Wallet wallet,
			String schemaJson, 
			String signatureType, 
			boolean createNonRevoc) throws SovrinException {

		final CompletableFuture<IssuerCreateAndStoreClaimDefResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String claim_def_json, String claim_def_uuid) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				IssuerCreateAndStoreClaimDefResult result = new IssuerCreateAndStoreClaimDefResult(claim_def_json, claim_def_uuid);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_issuer_create_and_store_claim_def(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				schemaJson,
				signatureType,
				createNonRevoc,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<IssuerCreateAndStoreRevocRegResult> issuerCreateAndStoreRevocReg(
			Wallet wallet,
			int claimDefSeqNo, 
			int maxClaimNum) throws SovrinException {

		final CompletableFuture<IssuerCreateAndStoreRevocRegResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String revoc_reg_json, String revoc_reg_uuid) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				IssuerCreateAndStoreRevocRegResult result = new IssuerCreateAndStoreRevocRegResult(revoc_reg_json, revoc_reg_uuid);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_issuer_create_and_store_revoc_reg(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				claimDefSeqNo,
				maxClaimNum,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<IssuerCreateClaimResult> issuerCreateClaim(
			Wallet wallet,
			String claimReqJson, 
			String claimJson,
			int revocRegSeqNo,
			int userRevocIndex) throws SovrinException {

		final CompletableFuture<IssuerCreateClaimResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String revoc_reg_update_json, String xclaim_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				IssuerCreateClaimResult result = new IssuerCreateClaimResult(revoc_reg_update_json, xclaim_json);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_issuer_create_claim(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				claimReqJson,
				claimJson,
				revocRegSeqNo,
				userRevocIndex,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<IssuerRevokeClaimResult> issuerRevokeClaim(
			Wallet wallet,
			int claimDefSeqNo, 
			int revocRegSeqNo, 
			int userRevocIndex) throws SovrinException {

		final CompletableFuture<IssuerRevokeClaimResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String revoc_reg_update_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				IssuerRevokeClaimResult result = new IssuerRevokeClaimResult(revoc_reg_update_json);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_issuer_revoke_claim(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				claimDefSeqNo,
				revocRegSeqNo,
				userRevocIndex,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<ProverStoreClaimOfferResult> proverStoreClaimOffer(
			Wallet wallet,
			String claimOfferJson) throws SovrinException {

		final CompletableFuture<ProverStoreClaimOfferResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				ProverStoreClaimOfferResult result = new ProverStoreClaimOfferResult();
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_prover_store_claim_offer(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				claimOfferJson,
				cb);

		checkResult(result);

		return future;
	}

	private static Future<ProverGetClaimOffersResult> proverGetClaimOffers(
			Wallet wallet,
			String filterJson) throws SovrinException {

		final CompletableFuture<ProverGetClaimOffersResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String claim_offers_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				ProverGetClaimOffersResult result = new ProverGetClaimOffersResult(claim_offers_json);
				future.complete(result);
			}
		};

		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_prover_get_claim_offers(
				FIXED_COMMAND_HANDLE, 
				walletHandle, 
				filterJson,
				cb);

		checkResult(result);

		return future;
	}

	/*
	 * INSTANCE METHODS
	 */

	public Future<CloseWalletResult> closeWallet(
			) throws SovrinException {

		return close(this);
	}

	public Future<WalletSetSeqNoForValueResult> setSeqNoForValue(
			String walletKey,
			String configName) throws SovrinException {

		return setSeqNoForValue(this, walletKey, configName);
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
	
	public Future<SignResult> signMessage(
			String did,
			String msg) throws SovrinException{
		return signMessage(this, did, msg);
	}
	
	//Should a corresponding version of this be available on the Ledger class?
	public Future<VerifySignatureResult> verifyMessageSignature(
			Ledger ledger,
			String did,
			String signedMsg) throws SovrinException{
		return verifyMessageSignature(this, ledger, did, signedMsg);
	}
	
	public Future<EncryptResult> encryptMessage(
			String did,
			String msg) throws SovrinException{
		return encryptMessage(this, did, msg);
	}
	
	public Future<DecryptResult> decryptMessage(
			String did,
			String encryptedMsg) throws SovrinException{
		return decryptMessage(this, did, encryptedMsg);
	}
	
	public Future<IssuerCreateAndStoreClaimDefResult> issuerCreateAndStoreClaimDef(
			String schemaJson, 
			String signatureType, 
			boolean createNonRevoc) throws SovrinException{
		return issuerCreateAndStoreClaimDef(this, schemaJson, signatureType, createNonRevoc);
	}
	
	public Future<IssuerCreateAndStoreRevocRegResult> issuerCreateAndStoreRevocReg(
			int claimDefSeqNo, 
			int maxClaimNum) throws SovrinException{
		return issuerCreateAndStoreRevocReg(this, claimDefSeqNo, maxClaimNum);
	}
	
	public Future<IssuerCreateClaimResult> issuerCreateClaim(
			String claimReqJson, 
			String claimJson,
			int revocRegSeqNo,
			int userRevocIndex) throws SovrinException{
		return issuerCreateClaim(this, claimReqJson, claimJson, revocRegSeqNo, userRevocIndex);
	}
	
	public Future<IssuerRevokeClaimResult> issuerRevokeClaim(
			int claimDefSeqNo, 
			int revocRegSeqNo, 
			int userRevocIndex) throws SovrinException{
		return issuerRevokeClaim(this, claimDefSeqNo, revocRegSeqNo, userRevocIndex);
	}
	
	public Future<ProverStoreClaimOfferResult> proverStoreClaimOffer(
			String claimOfferJson) throws SovrinException {
		return proverStoreClaimOffer(this, claimOfferJson);
	}
	
	public Future<ProverGetClaimOffersResult> proverGetClaimOffers(
			String filterJson) throws SovrinException {
		return proverGetClaimOffers(this, filterJson);
	}
	
	
}
