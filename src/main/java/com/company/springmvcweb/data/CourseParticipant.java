package com.company.springmvcweb.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Entity(name = "CourseParticipant")
@Table(name="course_participant")

public class CourseParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "course_id")
    private int courseId;
    @Column(name = "participant_id")
    private int participantId;

    public CourseParticipant(int id, int courseId, int participantId) {
        this.id = id;
        this.courseId = courseId;
        this.participantId = participantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public CourseParticipant() {
    }
}
