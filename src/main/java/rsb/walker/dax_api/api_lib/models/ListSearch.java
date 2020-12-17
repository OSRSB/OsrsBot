package rsb.walker.dax_api.api_lib.models;

import com.google.gson.Gson;

import java.util.List;



public class ListSearch {


    private int amount;


    private List<String> list;

    public ListSearch() {
    }

    public ListSearch(int amount, List<String> list) {
        this.amount = amount;
        this.list = list;
    }

    public int getAmount() {
        return amount;
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
