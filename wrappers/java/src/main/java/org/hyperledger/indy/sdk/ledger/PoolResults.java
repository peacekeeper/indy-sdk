package org.hyperledger.indy.sdk.ledger;

import org.hyperledger.indy.sdk.SovrinJava;

/**
 * pool.rs results
 */
public final class PoolResults {

	private PoolResults() {

	}

	public static class CreatePoolLedgerConfigResult extends SovrinJava.Result {

		CreatePoolLedgerConfigResult() { }
	}

	public static class OpenPoolLedgerResult extends SovrinJava.Result {

		private Ledger ledger;
		OpenPoolLedgerResult(Ledger ledger) { this.ledger = ledger; }
		public Ledger getLedger() { return this.ledger; }
	}

	public static class RefreshPoolLedgerResult extends SovrinJava.Result {

		RefreshPoolLedgerResult() { }
	}

	public static class ClosePoolLedgerResult extends SovrinJava.Result {

		ClosePoolLedgerResult() { }
	}

	public static class DeletePoolLedgerConfigResult extends SovrinJava.Result {

		DeletePoolLedgerConfigResult() { }
	}
}
