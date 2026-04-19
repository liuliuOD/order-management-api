package io.github.liuliu.ordermanagement.lockkey;

import java.util.UUID;

public class AdvisoryLockKeys {
    public static final int ORDER_NAMESPACE = 1;

    public static int forOrder(UUID orderId) {
        long mixed = orderId.getMostSignificantBits() ^ orderId.getLeastSignificantBits();
        return Long.hashCode(mixed);
    }
}
