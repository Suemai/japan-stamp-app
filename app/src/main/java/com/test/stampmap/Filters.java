package com.test.stampmap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Filter;
import java.util.stream.Collectors;

public class Filters {
    public enum Difficulty implements IFilter{
        ZERO("☆☆☆☆☆"),
        ONE("★☆☆☆☆"),
        TWO("★★☆☆☆"),
        THREE("★★★☆☆"),
        FOUR("★★★★☆"),
        FIVE("★★★★★"),
        ANY("");

        public final String value;

        Difficulty(String value) {
            this.value = value;
        }
        @Override
        public boolean hasMatch(JSONObject stampSet, String searchTerm){
            return stampSet.optString("難易度").contains(this.value);
        }
        @Override
        public int filterType(){
            return FilterType.DIFFICULTY.ordinal();
        }
    }
    public enum SearchType implements IFilter{
        NAME("名前"),
        ADDRESS("所在地"),
        ANY("");

        public final String value;

        SearchType(String value){
            this.value = value;
        }
        @Override
        public boolean hasMatch(JSONObject stampSet, String searchTerm){
            if (this.value.equals(""))
                return (stampSet.optString(SearchType.NAME.value).contains(searchTerm) || stampSet.optString(SearchType.ADDRESS.value).contains(searchTerm));
            else return (stampSet.optString(this.value).contains(searchTerm));
        }
        @Override
        public int filterType(){
            return FilterType.SEARCH.ordinal();
        }
    }
    public enum Prefecture implements IFilter{
        AICHI("愛知県"),
        AKITA("秋田県"),
        AOMORI("青森県"),
        CHIBA("千葉県"),
        EHIME("愛媛県"),
        FUKUI("福井県"),
        FUKUOKA("福岡県"),
        FUKUSHIMA("福島県"),
        GIFU("岐阜県"),
        GUNMA("群馬県"),
        HIROSHIMA("広島県"),
        HOKKAIDO("北海道"),
        HYOGO("兵庫県"),
        IBARAKI("茨城県"),
        ISHIKAWA("石川県"),
        IWATE("岩手県"),
        KAGAWA("香川県"),
        KAGOSHIMA("鹿児島県"),
        KANAGAWA("神奈川県"),
        KOCHI("高知県"),
        KUMAMOTO("熊本県"),
        KYOTO("京都府"),
        MIE("三重県"),
        MIYAGI("宮城県"),
        MIYAZAKI("宮崎県"),
        NAGANO("長野県"),
        NAGASAKI("長崎県"),
        NARA("奈良県"),
        NIIGATA("新潟県"),
        OITA("大分県"),
        OKAYAMA("岡山県"),
        OKINAWA("沖縄県"),
        OSAKA("大阪府"),
        SAGA("佐賀県"),
        SAITAMA("埼玉県"),
        SHIGA("滋賀県"),
        SHIMANE("島根県"),
        SHIZUOKA("静岡県"),
        TOCHIGI("栃木県"),
        TOKUSHIMA("徳島県"),
        TOKYO("東京都"),
        TOTTORI("鳥取県"),
        TOYAMA("富山県"),
        WAKAYAMA("和歌山県"),
        YAMAGATA("山形県"),
        YAMAGUCHI("山口県"),
        YAMANASHI("山梨県"),
        ANY("");

        public final String value;

        Prefecture(String value){
            this.value = value;
        }
        @Override
        public boolean hasMatch(JSONObject stampSet, String searchTerm){
            return stampSet.optString(SearchType.ADDRESS.value).contains(this.value);
        }
        @Override
        public int filterType(){
            return FilterType.PREFECTURE.ordinal();
        }
    }
    public enum EntryFee implements IFilter{
        FREE("無料"),
        PAID("円"),
        ANY("");

        public final String value;

        EntryFee(String value) {
            this.value = value;
        }
        @Override
        public boolean hasMatch(JSONObject stampSet, String searchTerm) {
            String fee = stampSet.optString("入場料（大人一般）");
            return !fee.equals("") ? fee.contains(this.value) : this != EntryFee.PAID;
        }
        @Override
        public int filterType() {
            return FilterType.ENTRYFEE.ordinal();
        }
    }

    public static ArrayList<StampSet> FilterStamps(JSONArray stampList, String searchTerm) {
        return FilterStamps(stampList, new IFilter[]{SearchType.ANY}, searchTerm);
    }

    public static ArrayList<StampSet> FilterStamps(JSONArray stampList, IFilter[] filters, String searchTerm) {
        ArrayList<StampSet> results = new ArrayList<>();
        Set<Integer> filterTypes = Arrays.stream(filters).mapToInt(IFilter::filterType).boxed().collect(Collectors.toSet());
        if (filterTypes.isEmpty()) return results;
        for (int i=0; i<stampList.length(); i++) {
            JSONObject stampSet = stampList.optJSONObject(i);
            Set<Integer> matchedFilterTypes = new HashSet<>();
            for (IFilter filter : filters) if (filter.hasMatch(stampSet, searchTerm)) matchedFilterTypes.add(filter.filterType());
            if (filterTypes.equals(matchedFilterTypes)) results.add(StampSet.StampSetFromJSON(stampSet));
        }
        return results;
    }
}
