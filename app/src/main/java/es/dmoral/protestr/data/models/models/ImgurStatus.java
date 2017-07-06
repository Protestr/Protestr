package es.dmoral.protestr.data.models.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by grender on 17/06/17.
 */

@SuppressWarnings("JavaDoc")
public class ImgurStatus {
    @SerializedName("data")
    @Expose
    private ImgurImage data;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("status")
    @Expose
    private long status;

    /**
     * No args constructor for use in serialization
     *
     */
    public ImgurStatus() {
        // empty
    }

    /**
     *
     * @param status
     * @param data
     * @param success
     */
    public ImgurStatus(ImgurImage data, boolean success, long status) {
        this.data = data;
        this.success = success;
        this.status = status;
    }

    public ImgurImage getData() {
        return data;
    }

    public void setData(ImgurImage data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public static class ImgurImage {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private Object title;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("datetime")
        @Expose
        private long datetime;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("animated")
        @Expose
        private boolean animated;
        @SerializedName("width")
        @Expose
        private long width;
        @SerializedName("height")
        @Expose
        private long height;
        @SerializedName("size")
        @Expose
        private long size;
        @SerializedName("views")
        @Expose
        private long views;
        @SerializedName("bandwidth")
        @Expose
        private long bandwidth;
        @SerializedName("vote")
        @Expose
        private Object vote;
        @SerializedName("favorite")
        @Expose
        private boolean favorite;
        @SerializedName("nsfw")
        @Expose
        private Object nsfw;
        @SerializedName("section")
        @Expose
        private Object section;
        @SerializedName("account_url")
        @Expose
        private Object accountUrl;
        @SerializedName("account_id")
        @Expose
        private long accountId;
        @SerializedName("is_ad")
        @Expose
        private boolean isAd;
        @SerializedName("in_most_viral")
        @Expose
        private boolean inMostViral;
        @SerializedName("tags")
        @Expose
        private List<Object> tags = null;
        @SerializedName("ad_type")
        @Expose
        private long adType;
        @SerializedName("ad_url")
        @Expose
        private String adUrl;
        @SerializedName("in_gallery")
        @Expose
        private boolean inGallery;
        @SerializedName("deletehash")
        @Expose
        private String deletehash;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("link")
        @Expose
        private String link;

        /**
         * No args constructor for use in serialization
         *
         */
        public ImgurImage() {
            // empty
        }

        /**
         *
         * @param animated
         * @param accountId
         * @param isAd
         * @param link
         * @param type
         * @param adUrl
         * @param bandwidth
         * @param id
         * @param title
         * @param height
         * @param description
         * @param name
         * @param deletehash
         * @param adType
         * @param datetime
         * @param tags
         * @param inGallery
         * @param vote
         * @param width
         * @param favorite
         * @param section
         * @param size
         * @param nsfw
         * @param views
         * @param accountUrl
         * @param inMostViral
         */
        public ImgurImage(String id, Object title, Object description, long datetime, String type,
                          boolean animated, long width, long height, long size, long views, long bandwidth,
                          Object vote, boolean favorite, Object nsfw, Object section, Object accountUrl,
                          long accountId, boolean isAd, boolean inMostViral, List<Object> tags, long adType,
                          String adUrl, boolean inGallery, String deletehash, String name, String link) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.datetime = datetime;
            this.type = type;
            this.animated = animated;
            this.width = width;
            this.height = height;
            this.size = size;
            this.views = views;
            this.bandwidth = bandwidth;
            this.vote = vote;
            this.favorite = favorite;
            this.nsfw = nsfw;
            this.section = section;
            this.accountUrl = accountUrl;
            this.accountId = accountId;
            this.isAd = isAd;
            this.inMostViral = inMostViral;
            this.tags = tags;
            this.adType = adType;
            this.adUrl = adUrl;
            this.inGallery = inGallery;
            this.deletehash = deletehash;
            this.name = name;
            this.link = link;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public long getDatetime() {
            return datetime;
        }

        public void setDatetime(long datetime) {
            this.datetime = datetime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isAnimated() {
            return animated;
        }

        public void setAnimated(boolean animated) {
            this.animated = animated;
        }

        public long getWidth() {
            return width;
        }

        public void setWidth(long width) {
            this.width = width;
        }

        public long getHeight() {
            return height;
        }

        public void setHeight(long height) {
            this.height = height;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public long getViews() {
            return views;
        }

        public void setViews(long views) {
            this.views = views;
        }

        public long getBandwidth() {
            return bandwidth;
        }

        public void setBandwidth(long bandwidth) {
            this.bandwidth = bandwidth;
        }

        public Object getVote() {
            return vote;
        }

        public void setVote(Object vote) {
            this.vote = vote;
        }

        public boolean isFavorite() {
            return favorite;
        }

        public void setFavorite(boolean favorite) {
            this.favorite = favorite;
        }

        public Object getNsfw() {
            return nsfw;
        }

        public void setNsfw(Object nsfw) {
            this.nsfw = nsfw;
        }

        public Object getSection() {
            return section;
        }

        public void setSection(Object section) {
            this.section = section;
        }

        public Object getAccountUrl() {
            return accountUrl;
        }

        public void setAccountUrl(Object accountUrl) {
            this.accountUrl = accountUrl;
        }

        public long getAccountId() {
            return accountId;
        }

        public void setAccountId(long accountId) {
            this.accountId = accountId;
        }

        public boolean isIsAd() {
            return isAd;
        }

        public void setIsAd(boolean isAd) {
            this.isAd = isAd;
        }

        public boolean isInMostViral() {
            return inMostViral;
        }

        public void setInMostViral(boolean inMostViral) {
            this.inMostViral = inMostViral;
        }

        public List<Object> getTags() {
            return tags;
        }

        public void setTags(List<Object> tags) {
            this.tags = tags;
        }

        public long getAdType() {
            return adType;
        }

        public void setAdType(long adType) {
            this.adType = adType;
        }

        public String getAdUrl() {
            return adUrl;
        }

        public void setAdUrl(String adUrl) {
            this.adUrl = adUrl;
        }

        public boolean isInGallery() {
            return inGallery;
        }

        public void setInGallery(boolean inGallery) {
            this.inGallery = inGallery;
        }

        public String getDeletehash() {
            return deletehash;
        }

        public void setDeletehash(String deletehash) {
            this.deletehash = deletehash;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

    }
}
