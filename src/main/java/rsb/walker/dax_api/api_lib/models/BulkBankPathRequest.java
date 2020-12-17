package rsb.walker.dax_api.api_lib.models;

import java.util.List;



public class BulkBankPathRequest {

    private PlayerDetails player;

    private List<BankPathRequestPair> requests;

    public BulkBankPathRequest(PlayerDetails player, List<BankPathRequestPair> requests) {
        this.player = player;
        this.requests = requests;
    }

    public PlayerDetails getPlayer() {
        return player;
    }

    public List<BankPathRequestPair> getRequests() {
        return requests;
    }
}
