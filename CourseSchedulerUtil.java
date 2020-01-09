import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;


/**
 * Filename:   CourseSchedulerUtil.java
 * Project:    p4
 * Authors:    Debra Deppeler
 * <p>
 * Use this class for implementing Course Planner
 *
 * @param <T> represents type
 */

public class CourseSchedulerUtil<T> {

    // can add private but not public members

    /**
     * Graph object
     */
    private GraphImpl<T> graphImpl;


    /**
     * constructor to initialize a graph object
     */
    public CourseSchedulerUtil() {
        this.graphImpl = new GraphImpl<T>();
    }

    /**
     * createEntity method is for parsing the input json file
     *
     * @return array of Entity object which stores information
     * about a single course including its name and its prerequisites
     * @throws Exception like FileNotFound, JsonParseException
     */
    @SuppressWarnings("rawtypes") public Entity[] createEntity(String fileName) throws Exception {
        // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new FileReader(fileName));
        JSONObject jo = (JSONObject) obj;  // typecasting obj to JSONObject
        JSONArray ja = (JSONArray) jo.get("courses");  // getting all courses
        //        ArrayList<Entity> allCourses = new ArrayList<>();
        Entity[] entities = new Entity[ja.size()];
        boolean name = true;
        Map course;
        int courseNum = 0;

        // iterating through courses to get the number of courses
        Iterator itr1 = ja.iterator();
        //        TODO: might be some problems while iterating
        while (itr1.hasNext()) {
            //            Iterator<Map.Entry> itr2 = ((Map) itr1.next()).entrySet().iterator();

            Entity eachCourse = new Entity();
            // iterate through names and prerequisites
            course = (Map) itr1.next();

            eachCourse.setName(course.get("name"));
            JSONArray prereq = (JSONArray) course.get("prerequisites");
            //            System.out.println(prereq.toString());
            eachCourse.setPrerequisites(prereq.toArray());
            //            System.out.println(prereq.toArray().length);
            //            while (itr2.hasNext()) {
            //                Map.Entry pair = itr2.next();
            //
            //                if (name) {
            //                    eachCourse.setName(pair.getValue());
            //                }
            //                if (!name) {
            //                    // TODO:
            //                    JSONArray prereq = (JSONArray) pair.getValue();
            //                    eachCourse.setPrerequisites(prereq.toArray());
            //                }
            //                name = !name;
            //            }
            entities[courseNum++] = eachCourse;
            //            allCourses.add(eachCourse);
        }
        //        return (Entity[]) (allCourses.toArray());
        return entities;
    }


    /**
     * Construct a directed graph from the created entity object
     *
     * @param entities which has information about a single course
     *                 including its name and its prerequisites
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) public void constructGraph(Entity[] entities) {
        // loop through the entities
        for (int i = 0; i < entities.length; i++) {
            // insert all the courses as a node in graph
            graphImpl.addVertex((T) entities[i].getName());
            //for each course, check its prerequisites
            //and add an edge from prerequisite to the high level course
            for (int j = 0; j < entities[i].getPrerequisites().length; j++) {
                Object sourceVertex = entities[i].getPrerequisites()[j];
                Object targetVertex = entities[i].getName();

                graphImpl.addVertex((T) sourceVertex);
                graphImpl.addEdge((T) sourceVertex, (T) targetVertex);
            }
        }
    }

    /**
     * Returns all the unique available courses
     *
     * @return the sorted list of all available courses
     */
    public Set<T> getAllCourses() {
        return graphImpl.getAllVertices();
    }

    /**
     * To check whether all given courses can be completed or not
     *
     * @return boolean true if all given courses can be completed,
     * otherwise false
     * @throws Exception
     */
    public boolean canCoursesBeCompleted() throws Exception {
        // check cycle to determine whether it is completed or not
        // going through all courses
        for (T vertex : graphImpl.getAllVertices()) {
            for (T successor : graphImpl.getAdjacentVerticesOf(vertex)) {
                if (vertex.equals(successor) || graphImpl.getAdjacentVerticesOf(successor)
                    .contains(vertex)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * The order of courses in which the courses has to be taken
     *
     * @return the list of courses in the order it has to be taken
     * @throws Exception when courses can't be completed in any order
     */
    public List<T> getSubjectOrder() throws Exception {
        if (!canCoursesBeCompleted()) {
            throw new Exception("The courses cannot be completed!");
        }
        //
        //        graphImpl.printGraph();

        Stack<T> stack = new Stack<T>();
        int N = graphImpl.order();  // the length of an array that stores all vertices
        //                ArrayList<T> subjectOrder = new ArrayList<>();
        T[] subjectOrder = (T[]) new Object[N];
        ArrayList<T> visited = new ArrayList<>();

        // loop through all courses
        for (T vertex : graphImpl.getAllVertices()) {
            boolean hasPrereq = false;
            // check the adjacent list of other vertices
            for (T otherVertex : graphImpl.getAllVertices()) {
                // check whether that course has any prerequisite
                if (!vertex.equals(otherVertex) && graphImpl.getAdjacentVerticesOf(otherVertex)
                    .contains(vertex)) {
                    hasPrereq = true;
                    break;
                }
            }
            // push the course to the stack if it does not have any prerequisite
            if (!hasPrereq) {
                visited.add(vertex);
                stack.push(vertex);
            }
        }


        T current;

        ArrayList<T> unvisitedSucc =
            new ArrayList<>();  //list to store all unvisited succ of current

        graphImpl.printGraph();

        while (!stack.isEmpty()) {
//            System.out.println(Arrays.toString(stack.toArray()));
            current = stack.peek();
            boolean succAllVisited = true;

            // check all successor of current course and update unvisited list
            for (T succ : graphImpl.getAdjacentVerticesOf(current)) {
                //                System.out.println("b");
                if (!visited.contains(succ)) {
                    succAllVisited = false;
                    unvisitedSucc.add(succ);
                }
            }

            if (succAllVisited) {
//                System.out.println("if succAllVisited)");
                current = stack.pop();

                subjectOrder[--N] = current;
            } else {
//                System.out.println("else statement");
//                System.out.println(unvisitedSucc.toString());
//                int listSize = unvisitedSucc.size();
//
//                for (int i = 0; i < listSize;i++){
                    T unvisitedVertex = unvisitedSucc.get(0);  // select one unvisited succ

                    visited.add(unvisitedVertex);
   System.out.println(visited.toString());
                    unvisitedSucc.remove(unvisitedVertex);
//                    graphImpl.removeEdge(current,unvisitedVertex);
//                    System.out.println(unvisitedSucc.toString());
                    stack.push(unvisitedVertex);

            }
        }
System.out.println("ok");
        return new ArrayList<T>(Arrays.asList(subjectOrder));
    }


    /**
     * The minimum course required to be taken for a given course
     *
     * @param courseName
     * @return the number of minimum courses needed for a given course
     */
    public int getMinimalCourseCompletion(T courseName) throws Exception {
        if (!canCoursesBeCompleted()) {
            return -1;
        }
        int course_count = 0;

        for (T otherVertex : graphImpl.getAllVertices()) {
            if (!courseName.equals(otherVertex) && graphImpl.getAdjacentVerticesOf(otherVertex)
                .contains(courseName)) {
                course_count++;
            }
        }
        return course_count;
    }
}
