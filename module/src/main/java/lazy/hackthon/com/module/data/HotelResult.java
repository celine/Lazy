package lazy.hackthon.com.module.data;

import java.util.List;

/**
 * Created by wenchihhsieh on 2017/3/12.
 */

public class HotelResult {
    public String hotel_id;
    public List<Block> block;
    public static class Block{
        public String name;
        public List<Price>incremental_price;
    }
    public static class Price{
        public String price;
        public String currency;

    }
}
