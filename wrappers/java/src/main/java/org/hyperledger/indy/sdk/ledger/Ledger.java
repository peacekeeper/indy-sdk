package org.hyperledger.indy.sdk.ledger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.hyperledger.indy.sdk.LibSovrin;
import org.hyperledger.indy.sdk.SovrinException;
import org.hyperledger.indy.sdk.SovrinJava;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildAttribRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildClaimDefTxnResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildGetAttribRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildGetClaimDefTxnResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildGetDdoRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildGetNymRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildGetSchemaRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildNodeRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildNymRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.BuildSchemaRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.SignAndSubmitRequestResult;
import org.hyperledger.indy.sdk.ledger.LedgerResults.SubmitRequestResult;
import org.hyperledger.indy.sdk.ledger.PoolJSONParameters.CreatePoolLedgerConfigJSONParameter;
import org.hyperledger.indy.sdk.ledger.PoolJSONParameters.OpenPoolLedgerJSONParameter;
import org.hyperledger.indy.sdk.ledger.PoolResults.ClosePoolLedgerResult;
import org.hyperledger.indy.sdk.ledger.PoolResults.CreatePoolLedgerConfigResult;
import org.hyperledger.indy.sdk.ledger.PoolResults.DeletePoolLedgerConfigResult;
import org.hyperledger.indy.sdk.ledger.PoolResults.OpenPoolLedgerResult;
import org.hyperledger.indy.sdk.ledger.PoolResults.RefreshPoolLedgerResult;
import org.hyperledger.indy.sdk.wallet.Wallet;

import com.sun.jna.Callback;

/**
 * 
 */
public class Ledger extends SovrinJava.API {
	
	private final int poolHandle;

	private Ledger(int poolHandle) {

		this.poolHandle = poolHandle;
	}

	public int getPoolHandle() {

		return this.poolHandle;
	}


	/*
	 * ledger.rs API STATIC METHODS
	 */

	public static Future<SignAndSubmitRequestResult> signAndSubmitRequest(
			Ledger pool,
			Wallet wallet,
			String submitterDid,
			String requestJson) throws SovrinException {

		final CompletableFuture<SignAndSubmitRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_result_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				SignAndSubmitRequestResult result = new SignAndSubmitRequestResult(request_result_json);
				future.complete(result);
			}
		};

		int poolHandle = pool.getPoolHandle();
		int walletHandle = wallet.getWalletHandle();

		int result = LibSovrin.api.sovrin_sign_and_submit_request(
				FIXED_COMMAND_HANDLE, 
				poolHandle,
				walletHandle, 
				submitterDid,
				requestJson,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<SubmitRequestResult> submitRequest(
			Ledger pool,
			String requestJson) throws SovrinException {

		final CompletableFuture<SubmitRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_result_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				SubmitRequestResult result = new SubmitRequestResult(request_result_json);
				future.complete(result);
			}
		};

		int poolHandle = pool.getPoolHandle();

		int result = LibSovrin.api.sovrin_submit_request(
				FIXED_COMMAND_HANDLE, 
				poolHandle,
				requestJson,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildGetDdoRequestResult> buildGetDdoRequest(
			String submitterDid,
			String targetDid,
			String requestJson) throws SovrinException {

		final CompletableFuture<BuildGetDdoRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildGetDdoRequestResult result = new BuildGetDdoRequestResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_get_ddo_request(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				targetDid,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildNymRequestResult> buildNymRequest(
			String submitterDid,
			String targetDid,
			String verkey,
			String alias,
			String role) throws SovrinException {

		final CompletableFuture<BuildNymRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildNymRequestResult result = new BuildNymRequestResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_nym_request(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				targetDid,
				verkey,
				alias,
				role,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildAttribRequestResult> buildAttribRequest(
			String submitterDid,
			String targetDid,
			String hash,
			String raw,
			String enc) throws SovrinException {

		final CompletableFuture<BuildAttribRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildAttribRequestResult result = new BuildAttribRequestResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_attrib_request(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				targetDid,
				hash,
				raw,
				enc,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildGetAttribRequestResult> buildGetAttribRequest(
			String submitterDid,
			String targetDid,
			String data) throws SovrinException {

		final CompletableFuture<BuildGetAttribRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildGetAttribRequestResult result = new BuildGetAttribRequestResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_get_attrib_request(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				targetDid,
				data,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildGetNymRequestResult> buildGetNymRequest(
			String submitterDid,
			String targetDid) throws SovrinException {

		final CompletableFuture<BuildGetNymRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildGetNymRequestResult result = new BuildGetNymRequestResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_get_nym_request(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				targetDid,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildSchemaRequestResult> buildSchemaRequest(
			String submitterDid,
			String data) throws SovrinException {

		final CompletableFuture<BuildSchemaRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildSchemaRequestResult result = new BuildSchemaRequestResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_schema_request(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				data,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildGetSchemaRequestResult> buildGetSchemaRequest(
			String submitterDid,
			String data) throws SovrinException {

		final CompletableFuture<BuildGetSchemaRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildGetSchemaRequestResult result = new BuildGetSchemaRequestResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_get_schema_request(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				data,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildClaimDefTxnResult> buildClaimDefTxn(
			String submitterDid,
			String xref,
			String data) throws SovrinException {

		final CompletableFuture<BuildClaimDefTxnResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildClaimDefTxnResult result = new BuildClaimDefTxnResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_claim_def_txn(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				xref,
				data,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildGetClaimDefTxnResult> buildGetClaimDefTxn(
			String submitterDid,
			String xref) throws SovrinException {

		final CompletableFuture<BuildGetClaimDefTxnResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildGetClaimDefTxnResult result = new BuildGetClaimDefTxnResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_get_claim_def_txn(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				xref,
				cb);

		checkResult(result);

		return future;
	}

	public static Future<BuildNodeRequestResult> buildNodeRequest(
			String submitterDid,
			String targetDid,
			String data) throws SovrinException {

		final CompletableFuture<BuildNodeRequestResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, String request_json) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				BuildNodeRequestResult result = new BuildNodeRequestResult(request_json);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_build_node_request(
				FIXED_COMMAND_HANDLE, 
				submitterDid,
				targetDid,
				data,
				cb);

		checkResult(result);

		return future;
	}
	

	/*
	 * pool.rs API STATIC METHODS
	 */

	public static Future<CreatePoolLedgerConfigResult> createPoolLedgerConfig(
			String configName,
			CreatePoolLedgerConfigJSONParameter config) throws SovrinException {

		final CompletableFuture<CreatePoolLedgerConfigResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				CreatePoolLedgerConfigResult result = new CreatePoolLedgerConfigResult();
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_create_pool_ledger_config(
				FIXED_COMMAND_HANDLE, 
				configName, 
				config == null ? null : config.toJson(), 
				cb);

		checkResult(result);

		return future;
	}

	public static Future<OpenPoolLedgerResult> openPoolLedger(
			String configName,
			OpenPoolLedgerJSONParameter config) throws SovrinException {

		final CompletableFuture<OpenPoolLedgerResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err, int pool_handle) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				Ledger ledger = new Ledger(pool_handle);

				OpenPoolLedgerResult result = new OpenPoolLedgerResult(ledger);
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_open_pool_ledger(
				FIXED_COMMAND_HANDLE, 
				configName, 
				config == null ? null : config.toJson(), 
				cb);

		checkResult(result);

		return future;
	}

	private static Future<RefreshPoolLedgerResult> refreshPoolLedger(
			Ledger ledger) throws SovrinException {

		final CompletableFuture<RefreshPoolLedgerResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				RefreshPoolLedgerResult result = new RefreshPoolLedgerResult();
				future.complete(result);
			}
		};

		int handle = ledger.getPoolHandle();

		int result = LibSovrin.api.sovrin_refresh_pool_ledger(
				FIXED_COMMAND_HANDLE, 
				handle, 
				cb);

		checkResult(result);

		return future;
	}

	private static Future<ClosePoolLedgerResult> closePoolLedger(
			Ledger ledger) throws SovrinException {

		final CompletableFuture<ClosePoolLedgerResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				ClosePoolLedgerResult result = new ClosePoolLedgerResult();
				future.complete(result);
			}
		};

		int handle = ledger.getPoolHandle();

		int result = LibSovrin.api.sovrin_refresh_pool_ledger(
				FIXED_COMMAND_HANDLE, 
				handle, 
				cb);

		checkResult(result);

		return future;
	}

	public static Future<DeletePoolLedgerConfigResult> deletePoolLedgerConfig(
			String configName) throws SovrinException {

		final CompletableFuture<DeletePoolLedgerConfigResult> future = new CompletableFuture<> ();

		Callback cb = new Callback() {

			@SuppressWarnings("unused")
			public void callback(int xcommand_handle, int err) {

				if (! checkCallback(future, xcommand_handle, err)) return;

				DeletePoolLedgerConfigResult result = new DeletePoolLedgerConfigResult();
				future.complete(result);
			}
		};

		int result = LibSovrin.api.sovrin_delete_pool_ledger_config(
				FIXED_COMMAND_HANDLE, 
				configName, 
				cb);

		checkResult(result);

		return future;
	}

	/*
	 * INSTANCE METHODS
	 */

	public Future<RefreshPoolLedgerResult> refresh(
			) throws SovrinException {

		return refreshPoolLedger(this);
	}

	public Future<ClosePoolLedgerResult> close(
			) throws SovrinException {

		return closePoolLedger(this);
	}
}
