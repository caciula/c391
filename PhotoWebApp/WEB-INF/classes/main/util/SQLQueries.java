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
    
    // Image management queries
    public static final String GET_IMAGES_BY_USER_ID = "select * from images where owner_name=? ORDER BY timing DESC NULLS LAST";
    public static final String GET_IMAGE_BY_ID = "select * from images where photo_id=?";
    public static final String GET_PHOTO_ONLY_BY_ID = "select photo from images where photo_id=?";
    public static final String GET_THUMBNAIL_ONLY_BY_ID = "select thumbnail from images where photo_id=?";
    public static final String UPDATE_IMG_DETAILS_BY_ID = "update images set subject=?, place=?, timing=?, description=?, permitted=? where photo_id=?";
}
