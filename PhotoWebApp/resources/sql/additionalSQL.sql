/*
 *  File name:  additionalSQL.sql
 *  Function:   Includes the additional SQL statements used to setup the database
 */

DROP INDEX description_index;
DROP INDEX subject_index;
DROP INDEX place_index;
DROP TABLE image_views;
DROP SEQUENCE pic_id_sequence;
DROP VIEW fact_table;

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

CREATE VIEW fact_table AS SELECT owner_name, subject, timing as time,
concat(concat(to_char(trunc(timing, 'DAY')), ' ~ '), to_char(trunc(timing, 'DAY')+6)) as week,
concat(concat(to_char(trunc(timing, 'MM')), ' ~ '), to_char(last_day(timing))) as month,
concat(concat(to_char(trunc(timing, 'YYYY')), ' ~ '), to_char(trunc(timing, 'YYYY')+364)) as year,
((to_char(timing, 'W') * 7) + (to_char(timing, 'MM') * 30) + (to_char(timing, 'YY') * 365)) as week_sort,
((to_char(timing, 'MM') * 30) + (to_char(timing, 'YY') * 365)) as month_sort,
((to_char(timing, 'YY') * 365)) as year_sort from images;

