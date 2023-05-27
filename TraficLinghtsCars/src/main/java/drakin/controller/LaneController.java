package drakin.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import drakin.dao.LaneDao;
import drakin.model.Car;
import drakin.model.Lane;
import drakin.model.Road;
import drakin.reflection.Component;
import drakin.reflection.DependencyInjection;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
@WebServlet("/lane")
public class LaneController extends HttpServlet {

    @DependencyInjection
    private LaneDao dao;

    public LaneDao getDao() {
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Lane lane = dao.get(id).get();
        PrintWriter out = response.getWriter();
        String json = new JSONObject(lane).toString();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            buffer.append(inputLine);
        }
        JSONObject jsonObject = new JSONObject(buffer.toString());
        Map<String, Object> map = jsonObject.toMap();

        List<Car> cars = new ArrayList<>();
        for (Object id: (List<Object>) map.get("cars")) {
            Car car = new Car();
            car.setIdentity(Long.parseLong((String) id));
            cars.add(car);
        }

        Lane lane = new Lane();
        lane.setCars(cars);

        dao.put(lane);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject(lane).toString());

    }
}
