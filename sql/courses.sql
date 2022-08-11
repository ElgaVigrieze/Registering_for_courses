ALTER TABLE `courses`.`course_participant`
DROP PRIMARY KEY;
;


ALTER TABLE `courses`.`course_participant`
    ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
CHANGE COLUMN `course_id` `course_id` INT NOT NULL ,
ADD PRIMARY KEY (`id`);
;
