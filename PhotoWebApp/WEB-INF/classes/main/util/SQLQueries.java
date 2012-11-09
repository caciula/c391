package main.util;

/**
 *  Contains common SQL queries.
 * 
 *  @author Tim Phillips
 */
public class SQLQueries {
    
    // Group management queries
    public static final String GET_ALL_GROUPS = "select * from groups";
    public static final String GET_GROUP_BY_ID = "select * from groups where group_id=?";
    
    // Image management queries
    public static final String GET_IMAGE_BY_ID = "select * from images where photo_id=?";
    public static final String GET_PHOTO_ONLY_BY_ID = "select photo from images where photo_id=?";
    public static final String GET_THUMBNAIL_ONLY_BY_ID = "select thumbnail from images where photo_id=?";
    public static final String UPDATE_IMG_DETAILS_BY_ID = "update images set subject=?, place=?, timing=?, description=?, permitted=? where photo_id=?";
}
