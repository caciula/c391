package main.util;

/**
 *  Contains common SQL queries.
 * 
 *  @author Tim Phillips
 */
public class SQLQueries {
    
    // User management queries
    public static final String GET_PERSON_BY_USER_NAME = "select * from persons where user_name=?";
    
    // Group management queries
    public static final String GET_ALL_GROUPS = "select * from groups";
    public static final String GET_GROUP_BY_ID = "select * from groups where group_id=?";
    public static final String GET_MEMBERS_BY_GROUP_ID = "select * from group_lists where group_id=?";
    public static final String GET_GROUPS_BY_USER_ID = "select group_id from group_lists where friend_id=?";
    public static final String GET_USER_GROUPS = "select group_id, group_name from groups where user_name=?";
    
    // Image management queries
    public static final String GET_IMAGES_BY_USER_ID = "select * from images where owner_name=? ORDER BY timing DESC NULLS LAST";
    public static final String GET_IMAGE_VIEWED_BY_USER = "select * from image_views where user_name=? and photo_id=?";
    public static final String GET_IMAGES_SORTED_BY_VIEWS = "select count(user_name) as count, photo_id from image_views group by photo_id order by count DESC";
    public static final String GET_IMAGE_BY_ID = "select * from images where photo_id=?";
    public static final String GET_PHOTO_ONLY_BY_ID = "select photo from images where photo_id=?";
    public static final String GET_THUMBNAIL_ONLY_BY_ID = "select thumbnail from images where photo_id=?";
    public static final String UPDATE_IMG_DETAILS_BY_ID = "update images set subject=?, place=?, timing=?, description=?, permitted=? where photo_id=?";
    public static final String INSERT_IMAGE_VIEW = "insert into image_views values(?,?)";
}
