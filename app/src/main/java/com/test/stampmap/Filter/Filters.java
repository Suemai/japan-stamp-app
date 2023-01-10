package com.test.stampmap.Filter;

import android.location.Location;
import com.test.stampmap.Activity.MainActivity;
import com.test.stampmap.Fragments.ExploreFragment;
import com.test.stampmap.Interface.IFilter;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;
import org.json.JSONArray;
import org.osmdroid.util.GeoPoint;

import java.util.*;
import java.util.stream.Collectors;

public class Filters {
    public enum Difficulty implements IFilter {
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
        public boolean hasMatch(StampSet stampSet){
            return stampSet.getDifficulty().contains(this.value);
        }
        @Override
        public int filterType(){
            return FilterType.DIFFICULTY.ordinal();
        }
        @Override
        public String getValue() {
            return this.value;
        }
    }
    public enum SearchType implements IFilter{
        NAME("名前"),
        ADDRESS("所在地"),
        ANY("");

        public final String value;
        public String searchTerm;

        SearchType(String value){
            this.value = value;
        }
        @Override
        public boolean hasMatch(StampSet stampSet){
            switch (this.ordinal()){
                case 0:
                    return stampSet.getName().contains(this.searchTerm);
                case 1:
                    return stampSet.getAddress().contains(this.searchTerm);
                case 2:
                    return (stampSet.getName().contains(this.searchTerm) || stampSet.getAddress().contains(this.searchTerm));
                default:
                    return false;
            }
        }
        @Override
        public int filterType(){
            return FilterType.SEARCH.ordinal();
        }
        @Override
        public String getValue() {
            return this.value;
        }
        public SearchType set(String term){
            this.searchTerm = term;
            return this;
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
        public boolean hasMatch(StampSet stampSet){
            return stampSet.getAddress().contains(this.value);
        }
        @Override
        public int filterType(){
            return FilterType.PREFECTURE.ordinal();
        }
        @Override
        public String getValue() {
            return this.value;
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
        public boolean hasMatch(StampSet stampSet) {
            String fee = stampSet.getEntryFee();
            return fee.contains(this.value);
        }
        @Override
        public int filterType() {
            return FilterType.ENTRYFEE.ordinal();
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }
    public enum Distance implements IFilter{
        KILOMETRES("km"),
        MILES("miles");

        public final String value;
        public float distance;

        Distance(String value) {
            this.value = value;
        }

        @Override
        public boolean hasMatch(StampSet stampSet) {
            GeoPoint stampCoords = stampSet.getStamps().get(0).getCoordinates();
            Location myLocation = ExploreFragment.locationProvider.getLastKnownLocation();
            Location stampLocation = new Location("NANI!!!");
            stampLocation.setLatitude(stampCoords.getLatitude());
            stampLocation.setLongitude(stampCoords.getLongitude());
            float dist = myLocation.distanceTo(stampLocation);
            return dist/1000f <= this.distance;
        }

        @Override
        public int filterType() {
            return FilterType.DISTANCE.ordinal();
        }

        @Override
        public String getValue() {
            return null;
        }

        public Distance set(float dist){
            if (ordinal() == 0) this.distance = dist;
            else this.distance = (float)(dist * 1.6);
            return this;
        }
    }

    public static List<StampSet> FilterStamps(String searchTerm) {
        return FilterStamps(new IFilter[]{SearchType.ANY.set(searchTerm)});
    }

    public static List<StampSet> FilterStamps(IFilter[] filters) {
        StampCollection.getInstance().getCurrentFilteredStamps().clear();
        Set<Integer> filterTypes = Arrays.stream(filters).mapToInt(IFilter::filterType).boxed().collect(Collectors.toSet());
        if (filterTypes.isEmpty()) return StampCollection.getInstance().getCurrentFilteredStamps();
        for (StampSet stampSet : StampCollection.getInstance().getAllStamps()) {
            Set<Integer> matchedFilterTypes = new HashSet<>();
            for (IFilter filter : filters) if (filter.hasMatch(stampSet)) matchedFilterTypes.add(filter.filterType());
            if (filterTypes.equals(matchedFilterTypes)) StampCollection.getInstance().getCurrentFilteredStamps().add(stampSet);
        }
        return StampCollection.getInstance().getCurrentFilteredStamps();
    }
}
