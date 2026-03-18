package uk.gov.courtservice.xhibit.business.vos.messagegenerator;

public class MessageVO {
    private final long itemId;

    private final String itemType;

    private final String target;

    public MessageVO(long itemId, String itemType, String target) {
        if (itemType == null) {
            throw new IllegalArgumentException("itemType: null");
        }
        if (target == null) {
            throw new IllegalArgumentException("target: null");
        }
        this.itemId = itemId;
        this.itemType = itemType;
        this.target = target;
    }

    public long getItemId() {
        return itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public String getTarget() {
        return target;
    }

    public String toString() {
        return "Message[itemid=" + itemId + ",itemType=" + itemType + ",target=" + target + "]";
    }
}
