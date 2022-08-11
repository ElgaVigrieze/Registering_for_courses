package com.company.springmvcweb.data;

import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

public class ParticipantRepository {
    private static SessionFactory factory;

    public ParticipantRepository() {
        try {
//            factory = new Configuration().configure("com/company/hibernate/hibernate.cfg.xml").buildSessionFactory();
            factory = new Configuration().
                    configure().
                    addAnnotatedClass(Participant.class).
                    addAnnotatedClass(Course.class).
                    addAnnotatedClass(CourseParticipant.class).
                    buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    //register a new participant in DB
    public Integer register(@NonNull Participant participant) {
        var session = factory.openSession();
        Integer participantId = null;

        try {
            participantId = (Integer) session.save(participant);
        } catch (HibernateException ex) {
            System.err.println(ex);
        }
        finally {
            session.close();
        }

        return participantId;
    }


    //check if e-mail is in DB
    public boolean checkIfEmailIsRegistered(String eMail) {
        var session = factory.openSession();

        try {
            var result = (Integer) session.createQuery("SELECT id FROM Participant where e_mail='" + eMail + "'").uniqueResult();
            if (result != null) {
                return true;
            }
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return false;
    }


    // login to the website
    public Participant logIn(String eMail, String password){

        var part1 = getParticipantIdFromEmail(eMail);
        var part2 = getParticipantIdFromPassword(password);

        var session = factory.openSession();

        if (part1 == part2) {

            try {
                var user = session.get(Participant.class, part1);
                return user;
            } catch (HibernateException exception) {
                System.err.println(exception);
            } finally {
                session.close();
            }
        }
        return null;


    }

    //get participant ID based on registered e-mail address
    public Integer getParticipantIdFromEmail(String eMail) {
        var session = factory.openSession();

        try {
            var result = (Integer) session.createQuery("SELECT id FROM Participant where e_mail='" + eMail + "'").uniqueResult();
            if (result != null) {
                return result;
            }
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return 0;
    }

    public Integer getParticipantIdFromPassword(String password) {
        var session = factory.openSession();

        try {
            var result = (Integer) session.createQuery("SELECT id FROM Participant where password ='" + password + "'").uniqueResult();
            if (result != null) {
                return result;
            }
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return 0;
    }


    //get participant by ID
    public Participant getParticipant(int id) {
        var session = factory.openSession();

        try {
            var result = session.get(Participant.class, id);
            if (result != null) {
                return result;
            }
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return null;
    }

    //get participant's password when ID is provided
    private String getSavedPassword(Integer participantId){
        var session = factory.openSession();

        try {
            var result = (String) session.createQuery("SELECT password FROM Participant where id='" + participantId + "'").uniqueResult();
            if (result != null) {
                return result;
            }
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
        return null;
    }


    //register for course, arguments from UI
//    public void registerForCourse(@NonNull CourseParticipant cp){
    public void registerForCourse(int id, int courseId, int participantId){

        CourseParticipant cp = new CourseParticipant(id, courseId,participantId);
        var session = factory.openSession();

        try {

                session.save(cp);
                CourseRepository repo = new CourseRepository();
                repo.decreaseFreeSlots(courseId);


        } catch (HibernateException ex) {
            System.err.println(ex);
        }
        finally {
            session.close();
        }

    }

    public boolean registerForCourseValidation(int id, int courseId, int participantId){
        var session = factory.openSession();
        var result = (Integer) session.createQuery("SELECT id FROM CourseParticipant where participantId='" + participantId + "' and courseId='"+ courseId + "'").uniqueResult();
        if (result == null) {
            return true;
        }else{
            return false;
        }
    }

    public void cancelCourse(int courseId, int participantId){
        var session = factory.openSession();
        var cpId = (Integer) session.createQuery("SELECT id FROM CourseParticipant where courseId='" + courseId + "' and participantId='" + participantId+"'").uniqueResult();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            CourseParticipant cp = session.get(CourseParticipant.class, cpId);
            //session.createQuery("DELETE FROM CourseParticipant WHERE courseId='" + courseId +"' and participantId='"+participantId+"'");
            session.delete(cp);
            tx.commit();

            CourseRepository repo = new CourseRepository();
            repo.increaseFreeSlots(courseId);


        } catch (HibernateException ex) {
            System.err.println(ex);
        }
        finally {
            session.close();
        }

    }


    //view all courses participant has registered for
    public Iterable<Participant> getParticipantsPerCourse(Integer courseId) {
        var session = factory.openSession();

        try {
            return session.createQuery(
                    "SELECT p FROM Participant as p JOIN CourseParticipant as cp ON p.id=cp.participantId where cp.courseId=" + courseId).list();

//            "SELECT c FROM Course as c JOIN CourseParticipant as cp ON c.id=cp.courseId where cp.participantId=" + participantId).list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }

        return new ArrayList<>();
    }

    public void deleteParticipant(int participantId, int courseId) {
        var session = factory.openSession();

        try {
            session.createQuery(
                    "DELETE p FROM Participant as p JOIN CourseParticipant as cp ON p.id=cp.participantId where cp.courseId=" + courseId +" AND p.id=" + participantId);

//            "SELECT c FROM Course as c JOIN CourseParticipant as cp ON c.id=cp.courseId where cp.participantId=" + participantId).list();
        } catch (HibernateException exception) {
            System.err.println(exception);
        } finally {
            session.close();
        }
    }

}