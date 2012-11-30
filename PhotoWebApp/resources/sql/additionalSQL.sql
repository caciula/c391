/*
 *  File name:  additionalSQL.sql
 *  Function:   Includes the additional SQL statements used to setup the database
 */

DROP INDEX description_index;
DROP INDEX subject_index;
DROP INDEX place_index;
DROP TABLE image_views;
DROP SEQUENCE pic_id_sequence;

CREATE SEQUENCE pic_id_sequence;

CREATE INDEX description_index on images(description) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('SYNC ( ON COMMIT)');
CREATE INDEX subject_index on images(subject) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('SYNC ( ON COMMIT)');
CREATE INDEX place_index on images(place) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('SYNC ( ON COMMIT)');

INSERT INTO users values('admin','admin',sysdate);
INSERT INTO persons values('admin',null,null,null,null,null);

CREATE TABLE image_views (
    photo_id   int,
    user_name  varchar(24),
    FOREIGN KEY(photo_id) REFERENCES images,
    FOREIGN KEY(user_name) REFERENCES users,
    UNIQUE (photo_id, user_name)
);