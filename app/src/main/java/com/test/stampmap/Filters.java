package com.test.stampmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

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
    }
    public enum SearchType implements IFilter{
        NAME("名前"),
        ADDRESS("所在地"),
        ANY("");

        public final String value;

        SearchType(String value){
            this.value = value;
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
    }

    public static ArrayList<StampSet> FilterStamps(JSONArray stampList, SearchType[] searchTypes, Difficulty[] difficulties, Prefecture[] prefectures, String searchTerm) {
        ArrayList<StampSet> results = new ArrayList<>();
        for (int i=0; i<stampList.length(); i++){
            try {
                boolean searchTypeMatch = false, diffMatch = false, prefectureMatch = false;
                JSONObject stampSet = stampList.getJSONObject(i);
                for (SearchType searchType : searchTypes) {
                    if (searchType == SearchType.ANY) {
                        if (stampSet.getString(SearchType.NAME.value).contains(searchTerm) ||
                            stampSet.getString(SearchType.ADDRESS.value).contains(searchTerm)) searchTypeMatch = true;
                    }
                    else if (stampSet.getString(searchType.value).contains(searchTerm)) searchTypeMatch = true;
                }
                for (Difficulty difficulty : difficulties)
                    if (stampSet.getString("難易度").contains(difficulty.value)) diffMatch = true;
                for (Prefecture prefecture : prefectures)
                    if (stampSet.getString(SearchType.ADDRESS.value).contains(prefecture.value)) prefectureMatch = true;
                if (searchTypeMatch && diffMatch && prefectureMatch) results.add(StampSet.StampSetFromJSON(stampSet));
            } catch (JSONException ignored){}
        }
        return results;
    }

    public static ArrayList<StampSet> FilterStamps(JSONArray stampList, SearchType[] searchTypes, String searchTerm) {
        return FilterStamps(stampList, searchTypes, new Difficulty[]{Difficulty.ANY}, new Prefecture[]{Prefecture.ANY}, searchTerm);
    }

    public static ArrayList<StampSet> FilterStamps(JSONArray stampList, Difficulty[] difficulties, String searchTerm) {
        return FilterStamps(stampList, new SearchType[]{SearchType.ANY}, difficulties,new Prefecture[]{Prefecture.ANY}, searchTerm);
    }

    public static ArrayList<StampSet> FilterStamps(JSONArray stampList, String searchTerm) {
        return FilterStamps(stampList, new SearchType[]{SearchType.ANY}, new Difficulty[]{Difficulty.ANY},new Prefecture[]{Prefecture.ANY}, searchTerm);
    }

    public static ArrayList<StampSet> FilterStamps(JSONArray stampList, IFilter[] filters, String searchTerm) {
        Difficulty[] diffs = Arrays.stream(filters).filter(filter -> filter instanceof Difficulty).toArray(Difficulty[]::new);
        SearchType[] searchTypes = Arrays.stream(filters).filter(filter -> filter instanceof SearchType).toArray(SearchType[]::new);
        Prefecture[] prefectures = Arrays.stream(filters).filter(filter -> filter instanceof Prefecture).toArray(Prefecture[]::new);
        searchTypes = searchTypes.length == 0 ? new SearchType[]{SearchType.ANY} : searchTypes;
        diffs = diffs.length == 0 ? new Difficulty[]{Difficulty.ANY} : diffs;
        prefectures = prefectures.length == 0 ? new Prefecture[]{Prefecture.ANY} : prefectures;
        return FilterStamps(stampList, searchTypes, diffs, prefectures, searchTerm);
    }
}
