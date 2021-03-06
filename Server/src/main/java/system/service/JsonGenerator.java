package system.service;

import org.json.JSONArray;
import org.json.JSONObject;
import system.dao.PointsDAOImpl;
import system.dao.WayInfoDAOImpl;
import system.entity.*;

import java.util.Iterator;
import java.util.List;

public class JsonGenerator {
    public JSONObject generateObjectForAllWays(Way way){
        JSONObject viewPoint = new JSONObject();
        Price price = new Price();
        viewPoint.put("id", way.getPosition());
        viewPoint.put("pointA", way.getLine().getStartPoint().getName());
        viewPoint.put("pointB", way.getLine().getEndPoint().getName());
        viewPoint.put("dist", way.getLine().getDistance());
        viewPoint.put("cost", price.wayPrice(way));
        viewPoint.put("time", price.wayTime(way));
        return viewPoint;
    }
    public JSONObject generateObjectForWay(Way way){
        JSONObject viewPoint = new JSONObject();
        viewPoint.put("pos", way.getPosition());
        viewPoint.put("firstpoint", way.getLine().getStartPoint().getName());
        viewPoint.put("secondpoint", way.getLine().getEndPoint().getName());
        viewPoint.put("distance", way.getLine().getDistance());
        viewPoint.put("transport", way.getLine().getTransport().getType());
        return viewPoint;
    }
    public JSONObject generateObjectForAllOrder(Orders order){
        JSONObject viewPoint = new JSONObject();
        viewPoint.put("id", order.getIdOrder());
        viewPoint.put("time", order.getTime());
        viewPoint.put("idWay", order.getWay().getId());
        viewPoint.put("price", order.getPrice());
        viewPoint.put("client", order.getUser().getLogin());
        return viewPoint;
    }
    public JSONObject generateObjectForAllUserOrders(Orders order){
        JSONObject viewPoint = new JSONObject();
        viewPoint.put("number", order.getIdOrder());
        viewPoint.put("pointA", order.getWay().getStart_point().getName());
        viewPoint.put("pointB", order.getWay().getEnd_point().getName());
        viewPoint.put("dist", order.getWay().getDistance());
        viewPoint.put("cost", order.getPrice());
        viewPoint.put("value", order.getVolume());
        viewPoint.put("date", order.getTime());
        viewPoint.put("date_1", order.getTime2());
        viewPoint.put("manager", "отдел транспорта");
        viewPoint.put("status", order.getStatus());
        return viewPoint;
    }
    public String generateSuccessTrueJson() {
        JSONObject response = new JSONObject();
        response.put("success",true);
        return response.toString();
    }
    public String generateSuccessFalseJson() {
        JSONObject response = new JSONObject();
        response.put("success",false);
        response.put("message","Такая точка уже существует! Валера, иди в жопу!");
        return response.toString();
    }
    public String generateAllPoints(List<Points> pointsList){
        JSONArray employees = new JSONArray();
        JSONObject response = new JSONObject();

        for (Iterator<Points> it = pointsList.iterator(); it.hasNext(); ) {
            employees.put(it.next().getName());
        }
        response.put("success",true);
        response.put("data",employees);

        return response.toString();
    }
    public String generateAllUsers(List<User> userList){
        JSONArray employees = new JSONArray();
        JSONObject response = new JSONObject();
        JSONObject object = new JSONObject();

        for (Iterator<User> it = userList.iterator(); it.hasNext(); ) {
            User user = it.next();
            String names = user.getSurname() + " " + user.getName();
            object.put("login", user.getLogin());
            object.put("fio", names);

            employees.put(object);
        }
        response.put("success",true);
        response.put("data",employees);

        return response.toString();
    }
    public String generateStatusBlockManagers(List<User> userList){
        JSONArray employees = new JSONArray();
        JSONObject response = new JSONObject();


        for (Iterator<User> it = userList.iterator(); it.hasNext(); ) {
            JSONObject object = new JSONObject();
            User user = it.next();
            String names = user.getSurname() + " " + user.getName();
            object.put("login", user.getLogin());
            object.put("fio", names);
            object.put("role", user.getRole());

            employees.put(object);
        }
        response.put("success",true);
        response.put("data",employees);

        return response.toString();
    }
    public String generateUser(User user){
        JSONObject response = new JSONObject();

        response.put("success",true);
        response.put("lastName",user.getSurname());
        response.put("firstName",user.getName());
        response.put("secondName","");
        response.put("address",user.getAddress());
        response.put("phone",user.getPhone());
        response.put("email",user.getEmail());

        return response.toString();
    }
    public String generateAllWays(List<Way> wayList){
        JSONArray employees = new JSONArray();
        JSONObject response = new JSONObject();

        for (Iterator<Way> it = wayList.iterator(); it.hasNext(); ) {
            employees.put(generateObjectForAllWays(it.next()));
        }
        response.put("success",true);
        response.put("data",employees);

        return response.toString();
    }
    public String generateWay(List<Way> wayList){
        JSONArray employees = new JSONArray();
        JSONObject response = new JSONObject();

        for (Iterator<Way> it = wayList.iterator(); it.hasNext(); ) {
            employees.put(generateObjectForWay(it.next()));
        }
        response.put("success",true);
        response.put("data",employees);

        return response.toString();
    }
    public String generateAllOrders(List<Orders> orderList){
        JSONArray employees = new JSONArray();
        JSONObject response = new JSONObject();

        for (Iterator<Orders> it = orderList.iterator(); it.hasNext(); ) {
            employees.put(generateObjectForAllOrder(it.next()));
        }
        response.put("success",true);
        response.put("data",employees);

        return response.toString();
    }
    public String generateUserOrders(List<Orders> orderList){
        JSONArray employees = new JSONArray();
        JSONObject response = new JSONObject();

        for (Iterator<Orders> it = orderList.iterator(); it.hasNext(); ) {
            employees.put(generateObjectForAllUserOrders(it.next()));
        }
        response.put("success",true);
        response.put("data",employees);

        return response.toString();
    }
    public String generateSearch(String point1, String point2, double volume){
        WayInfoDAOImpl wayInfo = new WayInfoDAOImpl();
        PointsDAOImpl point = new PointsDAOImpl();
        JSONArray employees = new JSONArray();
        JSONObject response = new JSONObject();
        Points first_point = point.getPointsName(point1);
        Points last_points = point.getPointsName(point2);
        List<WayInfo> wayInfoList = wayInfo.getWayInfo(first_point.getIdPoint(), last_points.getIdPoint());

        for (Iterator<WayInfo> it = wayInfoList.iterator(); it.hasNext(); ) {
            JSONObject object = new JSONObject();
            WayInfo way = it.next();
            double cost = 0;
            double time = 0;
            for(Way w : way.getWays()){
                cost = cost + (w.getLine().getDistance()*w.getLine().getTransport().getKoeff()*volume);
                time = time + (w.getLine().getDistance()/w.getLine().getTransport().getSpeed());
            }
            object.put("number", way.getId());
            object.put("cost", cost);
            object.put("time", time);
            employees.put(object);
        }
        response.put("success",true);
        response.put("data",employees);

        return response.toString();
    }
}