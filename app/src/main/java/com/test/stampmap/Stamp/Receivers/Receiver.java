package com.test.stampmap.Stamp.Receivers;

public class Receiver {

    public interface MyStampsUpdateReceiver {
        void onMyStampsUpdate();
    }

    public interface WishlistUpdateReceiver {
        void onWishlistUpdate();
    }
}
